package app.jw.mapable.gm.community

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager

import app.jw.mapable.gm.databinding.FragmentCommunityMainBinding
import app.jw.mapable.gm.notice.NoticeItem
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_community_main.view.*
import java.util.*
import kotlin.collections.ArrayList

class CommunityMainFragment : Fragment() {

    private var _binding : FragmentCommunityMainBinding? = null
    private val binding get() = _binding!!

    val datas = ArrayList<ItemCommunityMain>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCommunityMainBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setView(root, root.context)


        return root
    }


    private fun setView(root : View, context : Context)
    {
        val recyclerAdapter = CommunityMainAdapter(context)
        root.recyclerCommunityMain.adapter = recyclerAdapter
        root.recyclerCommunityMain.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        root.recyclerCommunityMain.layoutManager = LinearLayoutManager(context)


        val db = Firebase.firestore
        db.collection("post")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val timeStamp = document.data["posttime"] as com.google.firebase.Timestamp
                    val date: Date = timeStamp.toDate()
                    val dateFormat = android.text.format.DateFormat.getDateFormat(context)


                    datas.add(ItemCommunityMain(document.data["title"] as String, document.data["content"] as String, document.data["username"] as String, dateFormat.format(date), document.data["imageLink"] as String, document.data["like"] as Int, document.data["dislike"] as Int, document.data["star"] as Int))
                }
                setRecyclerView(root, context)
            }
            .addOnFailureListener {
                println("Error getting documents.")
            }

        root.floatingEdit.setOnClickListener{
            val intent = Intent(context, CommunityEditActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setRecyclerView(root : View, context : Context)
    {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}