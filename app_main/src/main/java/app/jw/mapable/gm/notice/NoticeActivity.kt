package app.jw.mapable.gm.notice

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import app.jw.mapable.gm.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_notice.*
import java.util.*
import kotlin.collections.ArrayList


class NoticeActivity : AppCompatActivity(){

    private lateinit var recyclerAdapter : NoticeRecyclerAdapter

    var notices = ArrayList<NoticeItem>()
    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notice)

        viewNotice.visibility = View.VISIBLE
        lottieViewNotice.visibility = View.VISIBLE
        recyclerViewNotice.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        recyclerAdapter = NoticeRecyclerAdapter(this)
        recyclerViewNotice.layoutManager = LinearLayoutManager(applicationContext)
        recyclerViewNotice.adapter = recyclerAdapter


        val db = Firebase.firestore
        db.collection("notice")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val timeStamp = document.data["timestamp"] as com.google.firebase.Timestamp
                    val date: Date = timeStamp.toDate()
                    val dateFormat = android.text.format.DateFormat.getDateFormat(
                        applicationContext
                    )


                    notices.add(NoticeItem(title = document.id, description = document.data["description"] as String, ID = document.data["id"] as String, timestamp = dateFormat.format(date)))
                  }
                setRecyclerView()
            }
            .addOnFailureListener {
                println("Error getting documents.")
            }














    }



    private fun setRecyclerView(){
        notices.apply{
            recyclerAdapter.datas = notices
            recyclerAdapter.notifyDataSetChanged()
        }

        fadeOutAnimation()


    }

    private fun fadeOutAnimation()
    {
        val animation = AnimationUtils.loadAnimation(this, R.anim.anim_fade_out)

        lottieViewNotice.startAnimation(animation)
        viewNotice.startAnimation(animation)

        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                lottieViewNotice.visibility = View.GONE
                viewNotice.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
    }

}