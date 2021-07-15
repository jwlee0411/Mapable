package app.jw.mapable.gm.community

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import app.jw.mapable.gm.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_community_my_more.*
import java.util.*
import kotlin.collections.ArrayList


class CommunityMyMoreActivity : AppCompatActivity() {


    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    lateinit var title : String

    private var myPost = ArrayList<String>()
    private var myStar = ArrayList<String>()

    lateinit var recyclerPostAdapter : CommunityAdapter
    lateinit var recyclerStarAdapter: CommunityAdapter

    var userName = ""
    private var userMessage = ""
    private var userPhoto = ""
    var userID = ""

    private lateinit var recyclerAdapter : CommunityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community_my_more)

        sharedPreferences = getSharedPreferences("preferences", 0)
        editor = sharedPreferences.edit()

        userName = sharedPreferences.getString("userName", "이름을 설정해주세요!")!!
        userMessage = sharedPreferences.getString("userMessage", "상태 메시지를 설정해주세요!")!!
        userPhoto = sharedPreferences.getString("userPhoto", "")!!
        userID = sharedPreferences.getString("userID", "")!!

        title = intent.getStringExtra("click")!!
        textViewTitle.text = title

        recyclerAdapter = CommunityAdapter(this)

        recyclerStarAdapter = CommunityAdapter(this)
        recyclerPostAdapter = CommunityAdapter(this)

        recyclerCommunityMyMore.layoutManager = LinearLayoutManager(applicationContext)


        getRequiredPosts()





    }


    private fun getRequiredPosts()
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


                when(title)
                {
                    "찜한 글" -> getMyStar()
                    "내가 쓴 글" -> getMyPost()
                }



            }
            .addOnFailureListener {
                println("Error getting documents.")
            }
    }



    private fun getMyPost()
    {
        recyclerCommunityMyMore.adapter = recyclerPostAdapter
        val datas = ArrayList<ItemCommunityMyMore>()
        val db = Firebase.firestore
        db.collection("post")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val postID = document.id

                    for(getID in myPost)
                    {
                        println("getID : $getID")

                        if(getID == postID)
                        {
                            println("setPost")

                            val timeStamp = document.data["posttime"] as com.google.firebase.Timestamp
                            val date: Date = timeStamp.toDate()
                            val dateFormat = android.text.format.DateFormat.getDateFormat(applicationContext)
                            datas.add(ItemCommunityMyMore(title = document.data["title"] as String, description = document.data["content"] as String, imageLink = document.data["image"] as String, date=dateFormat.format(date)))
                        }
                    }


                }


                datas.apply {
                    recyclerPostAdapter.datas = datas
                    recyclerPostAdapter.notifyDataSetChanged()
                }
                fadeOutAnimation()


            }
            .addOnFailureListener {
                println("Error getting documents.")
                fadeOutAnimation()
            }
    }

    private fun getMyStar()
    {
        recyclerCommunityMyMore.adapter = recyclerStarAdapter
        val datas = ArrayList<ItemCommunityMyMore>()
        val db = Firebase.firestore
        db.collection("post")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {



                    val postID = document.id

                    for(getID in myStar)
                    {
                        println("getID : $getID")
                        if(getID == postID)
                        {
                            val timeStamp = document.data["posttime"] as com.google.firebase.Timestamp
                            val date: Date = timeStamp.toDate()
                            val dateFormat = android.text.format.DateFormat.getDateFormat(applicationContext)
                            datas.add(ItemCommunityMyMore(title = document.data["title"] as String, description = document.data["content"] as String, imageLink = document.data["image"] as String, date=dateFormat.format(date)))
                        }
                    }

                }


                datas.apply {
                    recyclerStarAdapter.datas = datas
                    recyclerStarAdapter.notifyDataSetChanged()
                }
                fadeOutAnimation()


            }
            .addOnFailureListener {
                println("Error getting documents.")
                fadeOutAnimation()
            }
    }

    private fun fadeOutAnimation()
    {
        val animation = AnimationUtils.loadAnimation(this, R.anim.anim_fade_out)

        lottieViewCommunityMyMore.startAnimation(animation)
        viewCommunityMyMore.startAnimation(animation)

        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                lottieViewCommunityMyMore.visibility = View.GONE
                viewCommunityMyMore.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
    }




}