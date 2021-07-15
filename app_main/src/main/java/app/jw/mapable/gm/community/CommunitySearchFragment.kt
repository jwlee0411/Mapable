package app.jw.mapable.gm.community

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import app.jw.mapable.gm.R
import app.jw.mapable.gm.databinding.FragmentCommunitySearchBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_community_search.*
import kotlinx.android.synthetic.main.fragment_community_search.view.*
import java.util.*
import kotlin.collections.ArrayList


class CommunitySearchFragment : Fragment() {

    private var _binding : FragmentCommunitySearchBinding? = null
    private val binding get() = _binding!!

    var resultCount = 0

    private lateinit var recyclerAdapter: CommunityAdapter
    val datas = ArrayList<ItemCommunityMyMore>()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommunitySearchBinding.inflate(inflater, container, false)
        val root: View = binding.root



        setView(root, root.context)





        return root
    }


    private fun setView(root : View, context: Context)
    {

        recyclerAdapter = CommunityAdapter(context)
        root.recyclerCommunitySearch.adapter = recyclerAdapter
        root.recyclerCommunitySearch.layoutManager = LinearLayoutManager(context)






        root.button.setOnClickListener {
            root.lottieViewCommunitySearch.visibility = View.VISIBLE
            root.viewCommunitySearch.visibility = View.VISIBLE
            datas.clear()
            resultCount = 0

            val db = Firebase.firestore
            val searchString = editCommunitySearch.text.toString()
            db.collection("post").get().addOnSuccessListener {result ->
                for(document in result)
                {
                    val postTitle : String  = document.data["title"] as String

                    if(postTitle.contains(searchString))
                    {
                        val timeStamp = document.data["posttime"] as com.google.firebase.Timestamp
                        val date: Date = timeStamp.toDate()
                        val dateFormat = android.text.format.DateFormat.getDateFormat(context)
                        datas.add(ItemCommunityMyMore(document.data["title"] as String, document.data["content"] as String, document.data["image"] as String, dateFormat.format(date)))
                        resultCount++
                    }

                }



                if(resultCount==0) Toast.makeText(context, "검색결과가 없습니다.", Toast.LENGTH_LONG).show()
                else
                {
                    datas.apply{
                        recyclerAdapter.datas = datas
                        recyclerAdapter.notifyDataSetChanged()
                    }
                }

                fadeOutAnimation(root)

            }








        }

    }
    private fun fadeOutAnimation(root : View)
    {
        val animation = AnimationUtils.loadAnimation(root.context, R.anim.anim_fade_out)

        root.lottieViewCommunitySearch.startAnimation(animation)
        root.viewCommunitySearch.startAnimation(animation)

        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                root.lottieViewCommunitySearch.visibility = View.GONE
                root.viewCommunitySearch.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}