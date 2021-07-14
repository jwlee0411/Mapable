package app.jw.mapable.gm.star

import android.content.Intent
import android.content.SharedPreferences
import android.location.Location
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import app.jw.mapable.gm.R
import app.jw.mapable.gm.start.GpsTracker
import app.jw.mapable.gm.start.StartActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_after_search.*
import kotlinx.android.synthetic.main.activity_star.*
import kotlin.math.roundToInt

class StarActivity : AppCompatActivity() {

    lateinit var recyclerAdapter : StarRecyclerAdapter

    val datas = ArrayList<StarItem>()

    lateinit var sharedPreferences : SharedPreferences
    lateinit var editor : SharedPreferences.Editor

    var userName = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_star)

        viewStar.visibility = View.VISIBLE
        lottieViewStar.visibility = View.VISIBLE

        val gpsTracker = GpsTracker(this)
        val locationLatitude : Double = gpsTracker.getLatitude()
        val locationLongitude : Double = gpsTracker.getLongtitude()

        sharedPreferences = getSharedPreferences("preferences", 0)
        editor = sharedPreferences.edit()

        userName = sharedPreferences.getString("userID", "")!!

        buttonBack.setOnClickListener {
            val intent = Intent(this, StartActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.putExtra("reset", false)
            startActivity(intent)
            finish()
        }


        recyclerAdapter = StarRecyclerAdapter(this)
        recyclerStar.adapter = recyclerAdapter
        recyclerStar.layoutManager = LinearLayoutManager(this)
        recyclerStar.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))


        val datas = ArrayList<StarItem>()
        val db = Firebase.firestore
        db.collection("bookmark").get().addOnSuccessListener { result ->

            for (document in result)
            {
                val postID = document.getString("username")
                if(postID == userName)
                {
                    val locationName = document.getString("locationName")!!
                    val latitude = document.getDouble("latitude")!!
                    val longitude = document.getDouble("longitude")!!

                    val distance = getDistance(locationLatitude, locationLongitude, latitude, longitude)

                    datas.add(StarItem(locationName, latitude, longitude, distance))
                }

            }

            datas.apply {
                recyclerAdapter.datas = datas
                recyclerAdapter.notifyDataSetChanged()

            }

            fadeOutAnimation()

        }








    }

    private fun getDistance(lat1 : Double, lon1 : Double, lat2 : Double, lon2 : Double) : String
    {
        val locationStart = Location("start")
        locationStart.latitude = lat1
        locationStart.longitude = lon1

        val locationEnd = Location("end")
        locationEnd.latitude = lat2
        locationEnd.longitude = lon2

        val distance = locationStart.distanceTo(locationEnd)
        if(distance >= 1000){
            return ((distance/10).roundToInt()/100.0).toString() + "km"
        }
        else
        {
            return distance.roundToInt().toString() + "m"
        }
    }

    private fun fadeOutAnimation()
    {
        val animation = AnimationUtils.loadAnimation(this, R.anim.anim_fade_out)

        lottieViewStar.startAnimation(animation)
        viewStar.startAnimation(animation)

        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                lottieViewStar.visibility = View.GONE
                viewStar.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, StartActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("reset", false)
        startActivity(intent)
        finish()
    }
}