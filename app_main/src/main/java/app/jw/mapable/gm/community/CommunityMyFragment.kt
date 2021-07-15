package app.jw.mapable.gm.community


import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import app.jw.mapable.gm.R
import app.jw.mapable.gm.databinding.FragmentCommunityMyBinding
import app.jw.mapable.gm.setting.UserSettingActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_community_my.view.*
import java.util.*
import kotlin.collections.ArrayList


class CommunityMyFragment : Fragment() {
    private var _binding : FragmentCommunityMyBinding? = null
    private val binding get() = _binding!!

    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    lateinit var recyclerPostAdapter : CommunityAdapter
    lateinit var recyclerStarAdapter: CommunityAdapter

    private var myPost = ArrayList<String>()

    private var myStar = ArrayList<String>()

    var userName = ""
    private var userMessage = ""
    private var userPhoto = ""
    var userID = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommunityMyBinding.inflate(inflater, container, false)
        val root: View = binding.root

        sharedPreferences = requireContext().getSharedPreferences("preferences", 0)
        editor = sharedPreferences.edit()

        root.lottieViewCommunityMy.visibility = View.VISIBLE
        root.viewCommunityMy.visibility = View.VISIBLE


        setonClick(root)

        setView(root)




        return root
    }

    private fun setonClick(root:View)
    {
        var intent: Intent

        root.textLovedTextMore.setOnClickListener{
            intent = Intent(root.context, CommunityMyMoreActivity::class.java)
            intent.putExtra("click", "찜한 글")
            startActivity(intent)
        }

        root.textMyTextMore.setOnClickListener{
            intent = Intent(root.context, CommunityMyMoreActivity::class.java)
            intent.putExtra("click", "내가 쓴 글")
            startActivity(intent)
        }

        root.textMyReplyMore.setOnClickListener{
            intent = Intent(root.context, CommunityMyMoreActivity::class.java)
            intent.putExtra("click", "내가 쓴 댓글")
            startActivity(intent)
        }

        root.textViewMySetting.setOnClickListener{
            intent = Intent(root.context, UserSettingActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setView(root: View)
    {
        userName = sharedPreferences.getString("userName", "이름을 설정해주세요!")!!
        userMessage = sharedPreferences.getString("userMessage", "상태 메시지를 설정해주세요!")!!
        userPhoto = sharedPreferences.getString("userPhoto", "")!!
        userID = sharedPreferences.getString("userID", "")!!
        
        if(userPhoto != "")
        {
            Glide.with(root.context).load(Uri.parse(userPhoto)).into(root.imageMyProfile)
        }




        getRequiredPosts(root)


        root.textMyID.text = userID

        if(userName == "") root.textMyName.text = "이름을 설정해주세요!"
        else root.textMyName.text = userName

        if(userMessage == "") root.textMyMessage.text = "상태메시지를 설정해주세요!"
        else root.textMyMessage.text = userMessage


        recyclerPostAdapter = CommunityAdapter(root.context)
        root.recyclerViewMyPost.adapter = recyclerPostAdapter
        root.recyclerViewMyPost.layoutManager = LinearLayoutManager(root.context)



        recyclerStarAdapter = CommunityAdapter(root.context)
        root.recyclerViewStar.adapter = recyclerStarAdapter
        root.recyclerViewStar.layoutManager = LinearLayoutManager(root.context)








    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getRequiredPosts(root : View)
    {
        val db = Firebase.firestore
        db.collection("users")
            .get()
            .addOnSuccessListener { result ->

                for (document in result) {
                    val getID = document.data["userID"] as String
                    if(userID == getID)
                    {
                        myPost = document.data["myPost"] as ArrayList<String>


                        myStar = document.data["myStar"] as ArrayList<String>
                    println("$myPost + $myStar")

                        break
                    }

                }

                getMyPost(root)
                getMyStar(root)


            }
            .addOnFailureListener {
                println("Error getting documents.")
            }
    }

    private fun getMyPost(root : View)
    {
        val datas = ArrayList<ItemCommunityMyMore>()
        val db = Firebase.firestore
        db.collection("post")
            .get()
            .addOnSuccessListener { result ->
                var i = 0
                for (document in result) {
                    if(i==3) break


                    val postID = document.id

                    for(getID in myPost)
                    {
                        println("getID : $getID / $postID")

                        if(getID == postID)
                        {
                            println("setPost")

                            val timeStamp = document.data["posttime"] as com.google.firebase.Timestamp
                            val date: Date = timeStamp.toDate()
                            val dateFormat = android.text.format.DateFormat.getDateFormat(root.context)
                            datas.add(ItemCommunityMyMore(title = document.data["title"] as String, description = document.data["content"] as String, imageLink = document.data["image"] as String, date=dateFormat.format(date)))
                            i++
                        }
                    }


                }


                datas.apply {
                    recyclerPostAdapter.datas = datas
                    recyclerPostAdapter.notifyDataSetChanged()
                }
                fadeOutAnimation(root)


            }
            .addOnFailureListener {
                println("Error getting documents.")
                fadeOutAnimation(root)
            }
    }

    private fun getMyStar(root : View)
    {
        val datas = ArrayList<ItemCommunityMyMore>()
        val db = Firebase.firestore
        db.collection("post")
            .get()
            .addOnSuccessListener { result ->
                var i = 0
                for (document in result) {
                    if(i==3) break


                    val postID = document.id

                    for(getID in myStar)
                    {
                        println("getID : $getID")
                        if(getID == postID)
                        {
                            val timeStamp = document.data["posttime"] as com.google.firebase.Timestamp
                            val date: Date = timeStamp.toDate()
                            val dateFormat = android.text.format.DateFormat.getDateFormat(root.context)
                            datas.add(ItemCommunityMyMore(title = document.data["title"] as String, description = document.data["content"] as String, imageLink = document.data["image"] as String, date=dateFormat.format(date)))
                            i++
                        }
                    }

                }


                datas.apply {
                    recyclerStarAdapter.datas = datas
                    recyclerStarAdapter.notifyDataSetChanged()
                }
                fadeOutAnimation(root)


            }
            .addOnFailureListener {
                println("Error getting documents.")
                fadeOutAnimation(root)
            }
    }

    private fun fadeOutAnimation(root : View)
    {
        val animation = AnimationUtils.loadAnimation(root.context, R.anim.anim_fade_out)

        root.lottieViewCommunityMy.startAnimation(animation)
        root.viewCommunityMy.startAnimation(animation)

        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                root.lottieViewCommunityMy.visibility = View.GONE
                root.viewCommunityMy.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
    }

}