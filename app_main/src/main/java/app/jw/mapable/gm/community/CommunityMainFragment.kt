package app.jw.mapable.gm.community

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import app.jw.mapable.gm.R
import app.jw.mapable.gm.databinding.FragmentCommunityMainBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_after_search.*
import kotlinx.android.synthetic.main.fragment_community_main.view.*
import java.util.*
import kotlin.collections.ArrayList

class CommunityMainFragment : Fragment() {

    private var _binding : FragmentCommunityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerAdapter : CommunityMainAdapter

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

    override fun onResume() {
        super.onResume()

    }


    private fun setView(root : View, context : Context)
    {
        root.viewCommunityMain.visibility = View.VISIBLE
        root.lottieViewCommunityMain.visibility = View.VISIBLE


        recyclerAdapter = CommunityMainAdapter(context)
        root.recyclerCommunityMain.adapter = recyclerAdapter
        root.recyclerCommunityMain.layoutManager = LinearLayoutManager(context)


        val db = Firebase.firestore
        db.collection("post")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val timeStamp = document.data["posttime"] as com.google.firebase.Timestamp
                    val date: Date = timeStamp.toDate()
                    val dateFormat = android.text.format.DateFormat.getDateFormat(context)

                    val content = document.data["content"] as String





                    datas.add(ItemCommunityMain(document.data["title"] as String, content, document.data["username"] as String, dateFormat.format(date), document.data["image"] as String, document.data["like"] as Long, document.data["dislike"] as Long, document.data["star"] as Long))
                    swipeRefreshLayout.isRefreshing = false

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


        root.swipeRefreshLayout.setOnRefreshListener {
            refreshFragment(this, requireFragmentManager())
        }
    }

    private fun refreshFragment(fragment: Fragment, fragmentManager: FragmentManager) {
        datas.clear()
        val ft: FragmentTransaction = fragmentManager.beginTransaction()
        ft.detach(fragment).attach(fragment).commit()

    }

    private fun setRecyclerView(root : View, context : Context)
    {
        
        

        datas.apply{
            recyclerAdapter.datas = datas
            recyclerAdapter.notifyDataSetChanged()
        }

        fadeOutAnimation(root)

    }

    private fun fadeOutAnimation(root : View)
    {
        val animation = AnimationUtils.loadAnimation(root.context, R.anim.anim_fade_out)

        root.lottieViewCommunityMain.startAnimation(animation)
        root.viewCommunityMain.startAnimation(animation)

        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                root.lottieViewCommunityMain.visibility = View.GONE
                root.viewCommunityMain.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}