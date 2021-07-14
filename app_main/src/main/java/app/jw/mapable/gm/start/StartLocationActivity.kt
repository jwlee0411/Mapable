package app.jw.mapable.gm.start

import android.content.Intent
import android.content.SharedPreferences
import android.location.Location
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import app.jw.mapable.gm.R
import app.jw.mapable.gm.aftersearch.AfterSearchActivity
import app.jw.mapable.gm.login.LoginActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_start.*
import kotlinx.android.synthetic.main.activity_start_location.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class StartLocationActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    var latitude : Double = 0.0
    var longitude : Double = 0.0

    var userID = ""
    var loginType = 0

    var openingHoursAll : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_location)


        sharedPreferences = getSharedPreferences("preferences", 0)
        editor = sharedPreferences.edit()

        userID = sharedPreferences.getString("userID", "")!!

        loginType = sharedPreferences.getInt("loginType", 0)

        val gpsTracker = GpsTracker(this)
        val currentLatitude : Double = gpsTracker.getLatitude()
        val currentLongitude : Double = gpsTracker.getLongtitude()



        val name = intent.getStringExtra("name")

        var address = intent.getStringExtra("address")!!
        address = address.replace("대한민국 ", "").replace(" KR", "")

        latitude = intent.getDoubleExtra("latitude", 0.0)

        longitude = intent.getDoubleExtra("longitude", 0.0)

        var phoneNumber = intent.getStringExtra("phoneNumber")

        val websiteUri = intent.getStringExtra("websiteUri")

        val rating = intent.getStringExtra("rating")

        var openingHours = intent.getStringExtra("openingHours")

         openingHoursAll = openingHours


        if(name != null) textLocationName2.text = name
        else textLocationName2.text = address



        textView23.text = "$address | "

        textView24.text = getDistance(currentLatitude, currentLongitude, latitude, longitude)


        val currentTime: Date = Calendar.getInstance().getTime()
        val date_text: String = SimpleDateFormat("EE", Locale.getDefault()).format(currentTime)


        if(openingHours != null)
        {
            println(date_text)
            println(textOpeningHours.text)

            openingHours = openingHours.substring(openingHours.indexOf(date_text))
            openingHours = openingHours.substring(0, openingHours.indexOf("★"))
            textOpeningHours.text = openingHours
        }
        else
        {
            view8.visibility = View.GONE
            imageButton4.visibility = View.GONE
            textOpeningHours.visibility = View.GONE
        }

        if(websiteUri != null)
        {
            textView25.text = websiteUri
        }
        else
        {
            view9.visibility = View.GONE
            imageButton5.visibility = View.GONE
            textView25.visibility = View.GONE
        }


        if(phoneNumber != null)
        {
            //phoneNumber = phoneNumber.replace("+82 ", "0") : 간혹 오류가 생기는 경우가 있어, 그냥 +82 포함시키는걸로.
            layoutCall.visibility = View.VISIBLE
            layoutCall.setOnClickListener {
                println("log : CALL")
                val startCallDialog = StartCallDialog(this)
                startCallDialog.callFunction(phoneNumber)
            }
        }

        if(rating != null)
        {
            textView26.text = rating
        }
        else
        {
            textView26.text = "?"
        }





        setonClick()

    }

    private fun setonClick(){
        layoutShare.setOnClickListener{
            println("LOG210714 : 클릭함")
            val intentShare = Intent(Intent.ACTION_SEND)
            intentShare.type = "text/plain"
            val location = textLocationName2.text.toString()
            val address = textView23.text.toString().substring(0, textView23.text.toString().length-3)
            val distance = textView24.text.toString()
            intentShare.putExtra(Intent.EXTRA_TEXT,
                "$location : $address\n현재 위치로부터 $distance\n\n스마트한 지도, Mapable에서 공유함"
            )
            startActivity(Intent.createChooser(intentShare, "공유"))


        }

        layoutBookmark.setOnClickListener {
            viewStartLocation.visibility = View.VISIBLE
            lottieViewStartLocation.visibility = View.VISIBLE

            if(loginType == 0) {
                fadeOutAnimation()
                Toast.makeText(this, "북마크 기능을 이용하려면 로그인이 필요합니다.", Toast.LENGTH_LONG).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            else {

                val db = FirebaseFirestore.getInstance()
                val bookmark: MutableMap<String, Any> = HashMap()

                bookmark["locationName"] = textLocationName2.text.toString()
                bookmark["latitude"] = latitude
                bookmark["longitude"] = longitude
                bookmark["username"] = userID

                db.collection("bookmark").document().set(bookmark).addOnSuccessListener {
                    Toast.makeText(this, "북마크를 등록했습니다.", Toast.LENGTH_LONG).show()
                    fadeOutAnimation()
                }
                    .addOnFailureListener{
                        it.printStackTrace()
                        Toast.makeText(this, "북마크 등록에 실패하였습니다.", Toast.LENGTH_LONG).show()
                        fadeOutAnimation()
                    }
            }
        }

        layoutMap.setOnClickListener {

            val intent = Intent(this, StartActivity::class.java)
            intent.putExtra("latitude", latitude)
            intent.putExtra("longitude", longitude)
            intent.putExtra("locationName", textLocationName2.text.toString())
            intent.putExtra("reset", false)
            intent.putExtra("showLocation", true)
            startActivity(intent)
        }

        textOpeningHours.setOnClickListener {
            if(openingHoursAll != null )
            {
                val openingHoursDialog = OpeningHoursDialog(this)
                openingHoursDialog.callFunction(openingHoursAll!!)
            }


        }

        buttonStartLocation.setOnClickListener {
            val end = sharedPreferences.getBoolean("end", false)

            Toast.makeText(this, "출발지가 설정되었습니다.", Toast.LENGTH_LONG).show()
            sharedPreferences.edit().putBoolean("start", true).apply()

            sharedPreferences.edit().putFloat("startX", latitude.toFloat()).putFloat("startY", longitude.toFloat()).apply()
            sharedPreferences.edit().putString("startNewX", latitude.toString()).putString("startNewY", longitude.toString()).apply()
            sharedPreferences.edit().putString("startLocation", textLocationName2.text.toString()).apply()

            if(end)
            {
                sharedPreferences.edit().putBoolean("start", false).apply()
                sharedPreferences.edit().putBoolean("end", false).apply()
                val intent = Intent(this, AfterSearchActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }

            else
            {
                val intent = Intent(this, StartActivity::class.java)
                intent.putExtra("reset", false)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }


        }

        buttonEndLocation.setOnClickListener {
            val start = sharedPreferences.getBoolean("start", false)

            Toast.makeText(this, "도착지가 설정되었습니다.", Toast.LENGTH_LONG).show()
            sharedPreferences.edit().putBoolean("end", true).apply()
            sharedPreferences.edit().putFloat("endX", latitude.toFloat()).putFloat("endY", longitude.toFloat()).apply()
            sharedPreferences.edit().putString("endNewX", latitude.toString()).putString("endNewY", longitude.toString()).apply()
            sharedPreferences.edit().putString("endLocation", textLocationName2.text.toString()).apply()

            if(start)
            {
                sharedPreferences.edit().putBoolean("start", false).apply()
                sharedPreferences.edit().putBoolean("end", false).apply()
                val intent = Intent(this, AfterSearchActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
            else
            {
                val intent = Intent(this, StartActivity::class.java)
                intent.putExtra("reset", false)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }

        }



    }


    private fun fadeOutAnimation()
    {
        val animation = AnimationUtils.loadAnimation(this, R.anim.anim_fade_out)

        lottieViewStartLocation.startAnimation(animation)
        viewStartLocation.startAnimation(animation)

        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                lottieViewStartLocation.visibility = View.GONE
                viewStartLocation.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
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



}