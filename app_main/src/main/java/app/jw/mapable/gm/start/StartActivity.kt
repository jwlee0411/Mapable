package app.jw.mapable.gm.start

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import app.jw.mapable.gm.R
import app.jw.mapable.gm.aftersearch.AfterSearchActivity
import app.jw.mapable.gm.community.CommunityActivity
import app.jw.mapable.gm.info.InfoActivity
import app.jw.mapable.gm.login.LoginActivity
import app.jw.mapable.gm.notice.NoticeActivity
import app.jw.mapable.gm.search.SearchActivity
import app.jw.mapable.gm.setting.AppInfoActivity
import app.jw.mapable.gm.setting.SettingActivity
import app.jw.mapable.gm.setting.UserSettingActivity
import app.jw.mapable.gm.star.StarActivity
import com.bumptech.glide.Glide
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_star.*
import kotlinx.android.synthetic.main.activity_start.*
import kotlinx.android.synthetic.main.activity_user_setting.*
import kotlinx.android.synthetic.main.dialog_user_setting_edit.*
import kotlinx.android.synthetic.main.navi_header_start.*
import kotlinx.android.synthetic.main.navi_header_start.view.*
import java.io.IOException
import java.util.*
import kotlin.math.*


class StartActivity : AppCompatActivity(), OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {


   private lateinit var currentLocationMarker : Marker

   lateinit var selectLocationMarker : Marker

   private var locationLatitude = 0.0
    private var locationLongitude = 0.0

    private var layoutInflated = false

    var onTouched = false
    private var clicked = false

    var loginType = 0

    var settings = BooleanArray(10)

    var prevLatitude : Double = 37.542035
    var prevLongtitude : Double = 126.966613

    var startX : Double = 0.0
    var startY : Double = 0.0

    var start = false
    private var end = false

    private var nickname = ""

    var userID = ""
    private val update_interval : Long = 1000
    private val fastest_update_interval : Long = 500


    private lateinit var searchLayoutAnimation : Animation
    private lateinit var floatingLayoutAnimation : Animation

    lateinit var mMap : GoogleMap

    lateinit var mCurrentLocation : Location

    lateinit var currentPosition : LatLng

    lateinit var mFusedLocationClient : FusedLocationProviderClient
    private lateinit var locationRequest : LocationRequest

    lateinit var sharedPreferences : SharedPreferences
    lateinit var editor : SharedPreferences.Editor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContentView(R.layout.activity_start)

        lottieViewStart.visibility = View.VISIBLE
        viewStart.visibility = View.VISIBLE



        sharedPreferences = getSharedPreferences("preferences", 0)
        editor = sharedPreferences.edit()


        if(!(intent.getBooleanExtra("reset", true)))
        {
            start = sharedPreferences.getBoolean("start", false)
            end = sharedPreferences.getBoolean("end", false)
        }




        loginType = sharedPreferences.getInt("loginType", 0)



        if (sharedPreferences.getBoolean("disabled", false))
        {


           startActivity(Intent(this, StartDisabledActivity::class.java))
            finish()
        }
        else
        {
            searchConstraintLayout.bringToFront()
            setOnClick()
        }

        getSettings()


    }

    private fun getSettings()
    {
        settings[0] = sharedPreferences.getBoolean("busRoadFound", false)
        settings[1] = sharedPreferences.getBoolean("busLowOnly", false)
        settings[2] = sharedPreferences.getBoolean("busWait30", false)
        settings[3] = sharedPreferences.getBoolean("busWait60", false)
        settings[4] = sharedPreferences.getBoolean("subwayRoadFound", false)
        settings[5] = sharedPreferences.getBoolean("subwayElevator", false)
        settings[6] = sharedPreferences.getBoolean("subwayWheelchairStation", false)
        settings[7] = sharedPreferences.getBoolean("subwayWheelchairOn", false)
        settings[8] = sharedPreferences.getBoolean("disabled", false)
        settings[9] = sharedPreferences.getBoolean("noInfo", false)

        println("§" + settings[9])

        if(settings[9]) floatingInfo.visibility = View.GONE



        nickname = sharedPreferences.getString("userName", "")!!
        userID = sharedPreferences.getString("userID", "")!!
        val userPhoto = sharedPreferences.getString("userPhoto", "")

        val header = navigationView.getHeaderView(0)

        header.textViewUserName.text = "로그인 해주세요!"

        if(loginType != 0)
        {
            if(nickname != "") header.textViewUserName.text = nickname + "님, 환영합니다!"

            else header.textViewUserName.text = "환영합니다!"

            if(userPhoto != "") Glide.with(this).load(Uri.parse(userPhoto)).into(header.imageViewUserPhoto)

        }

        else header.textViewUserName.text = "로그인 해주세요!"; header.imageViewUserPhoto.setImageResource(R.drawable.profile_default)

    }



    private fun setOnClick()
    {

        layout_location.setOnClickListener {
            //do nothing : 이 코드가 없으면 자꾸 map을 누르는 것으로 인식해 레이아웃이 들어가짐
        }


        buttonShare.setOnClickListener{
            println("LOG210714 : 클릭함")
            val intentShare = Intent(Intent.ACTION_SEND)
            intentShare.type = "text/plain"
            val location = textLocationTitle.text.toString()
            val distance = textDistanceLocation.text.toString()
            intentShare.putExtra(Intent.EXTRA_TEXT,
                "$location\n현재 위치로부터 $distance\n\n스마트한 지도, Mapable에서 공유함"
            )
            startActivity(Intent.createChooser(intentShare, "공유"))

        }
        buttonBookmark.setOnClickListener {

            lottieViewStart.visibility = View.VISIBLE
            viewStart.visibility = View.VISIBLE

            if(loginType == 0) {
                fadeOutAnimation()
                Toast.makeText(this@StartActivity, "북마크 기능을 이용하려면 로그인이 필요합니다.", Toast.LENGTH_LONG).show()
                startActivity(Intent(this@StartActivity, LoginActivity::class.java))}
            else {

                val db = FirebaseFirestore.getInstance()
                val bookmark: MutableMap<String, Any> = HashMap()

                bookmark["locationName"] = textLocationTitle.text.toString()
                bookmark["latitude"] = locationLatitude
                bookmark["longitude"] = locationLongitude
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


        floatingDebug.setOnClickListener {
            //현재 위치를 출발지로 지정함.

            val gpsTracker = GpsTracker(this)
            val latitude : Double = gpsTracker.getLatitude()
            val longitude : Double = gpsTracker.getLongtitude()
            val currentAddressName = getCurrentAddress(latitude, longitude)

            println("$latitude / $longitude / $currentAddressName")

            start = true
            sharedPreferences.edit().putBoolean("start", true).apply()

            sharedPreferences.edit().putFloat("startX", latitude.toFloat()).putFloat("startY", longitude.toFloat()).apply()
            sharedPreferences.edit().putString("startNewX", latitude.toString()).putString("startNewY", longitude.toString()).apply()
            sharedPreferences.edit().putString("startLocation", currentAddressName).apply()



            Toast.makeText(this, "현재 위치가 출발지로 설정되었습니다.", Toast.LENGTH_LONG).show()

            if(end) openAfterSearch()






        }

        floatingCurrentLocation.setOnClickListener {
            val gpsTracker = GpsTracker(this)
            val latitude : Double = gpsTracker.getLatitude()
            val longitude : Double = gpsTracker.getLongtitude()

            val prevLatLng = LatLng(latitude, longitude)

            try{ currentLocationMarker.remove() }
            catch (e : Exception)
            {
                e.printStackTrace()
            }


            val bitmap = (resources.getDrawable(R.drawable.marker_start_ride, null) as BitmapDrawable).bitmap
            currentLocationMarker = mMap.addMarker(MarkerOptions().position(prevLatLng).icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(bitmap, 30, 30, false))))!!


            getCurrentAddress(latitude, longitude)



            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(prevLatLng, 15f))
        }


        floatingRoadFound.setOnClickListener {
            startActivity(Intent(this, AfterSearchActivity::class.java))
            finish()
            overridePendingTransition(R.anim.anim_move_bottom_up_full, R.anim.anim_none)
        }


        textViewSearch.setOnClickListener {


            val intent = Intent(this, SearchActivity::class.java)
            intent.putExtra("TTS", false)
            startActivity(intent)
            overridePendingTransition(R.anim.anim_move_bottom_up_full, R.anim.anim_none)
        }
        soundButton.setOnClickListener {


            val intent = Intent(this, SearchActivity::class.java)
            intent.putExtra("TTS", true)
            startActivity(intent)
            overridePendingTransition(R.anim.anim_move_bottom_up_full, R.anim.anim_none)
        }


        floatingInfo.setOnClickListener { v: View? ->
            startActivity(Intent(this@StartActivity, InfoActivity::class.java))
        }

        //Menu 부분 관련 설정 코드
        menuButton.setOnClickListener {
            drawerLayout.openDrawer(
                GravityCompat.START
            )
        }
        navigationView.itemIconTintList = null
        navigationView.setNavigationItemSelectedListener { item: MenuItem ->
            drawerLayout.closeDrawers()
            when (item.itemId) {
                R.id.nav_settings -> startActivity(Intent(this@StartActivity, SettingActivity::class.java))
                R.id.nav_star -> startActivity(Intent(this@StartActivity, StarActivity::class.java))
                R.id.nav_info -> startActivity(Intent(this@StartActivity, NoticeActivity::class.java))
                R.id.nav_community -> {
                    if(loginType == 0) {
                        Toast.makeText(this@StartActivity, "커뮤니티를 이용하려면 로그인이 필요합니다.", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this@StartActivity, LoginActivity::class.java))}
                    else startActivity(Intent(this@StartActivity, CommunityActivity::class.java))

                }
                R.id.nav_train -> {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://smapp.seoulmetro.co.kr:58443/traininfo/traininfoUserView.do"))
                    startActivity(intent)
                }

                R.id.nav_wheelchair ->
                {
                    val intent = Intent(this, AppInfoActivity::class.java)
                    intent.putExtra("type", 2)
                    startActivity(intent)
                }
                R.id.nav_elevator ->{
                    val intent = Intent(this, AppInfoActivity::class.java)
                    intent.putExtra("type", 3)
                    startActivity(intent)
                }

            }
            true
        }



        setMap()
    }

    fun myViewClick(v : View)
    {
        loginType = sharedPreferences.getInt("loginType", 0)

        when(loginType)
        {
            0 -> startActivity(Intent(this, LoginActivity::class.java))
            1,2 -> {
                val intent = Intent(this, UserSettingActivity::class.java)
//                intent.putExtra("clickView", true)
                startActivity(intent)
            }
        }
    }

    private fun setMap()
    {

        locationRequest = LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(update_interval).setFastestInterval(fastest_update_interval)

        val builder : LocationSettingsRequest.Builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(locationRequest)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val mapFragment : SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)



    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.clear()


        val SEOUL = LatLng(prevLatitude, prevLongtitude)

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 13F))
        mMap.uiSettings.isMyLocationButtonEnabled = true
        mMap.setOnCameraMoveListener { onTouched = true }





        setOnMapClick()

    }


    private fun getCurrentAddress(latitude: Double, longitude: Double): String {

        //지오코더... GPS를 주소로 변환
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses: List<Address>? = try {
            geocoder.getFromLocation(
                latitude,
                longitude,
                7
            )
        } catch (ioException: IOException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show()
            return "지오코더 서비스 사용불가"
        } catch (illegalArgumentException: IllegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show()
            return "잘못된 GPS 좌표"
        }
        if (addresses == null || addresses.isEmpty()) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show()
            return "주소 미발견"
        }
        val address: Address = addresses[0]
        var addressText = address.getAddressLine(0).toString()
        addressText = addressText.replace("대한민국 ", "")

        textViewSearch.text = addressText

        return addressText


    }

    private fun setOnMapClick()
    {

        if(intent.getBooleanExtra("showLocation", false))
        {
            val lat = intent.getDoubleExtra("latitude", 0.0)
            val lon = intent.getDoubleExtra("longitude", 0.0)
            val lng = LatLng(lat, lon)
            setLocationLayout(lat, lon, lng)
        }

        mMap.setOnCameraMoveListener {

            try{ selectLocationMarker.remove() }
        catch (e : Exception)
        {
            e.printStackTrace()
        }}

        mMap.setOnMapClickListener {
            try{ selectLocationMarker.remove() }
            catch (e : Exception)
            {
                e.printStackTrace()
            }

            val gpsTracker = GpsTracker(this)
            val latitude : Double = gpsTracker.getLatitude()
            val longitude : Double = gpsTracker.getLongtitude()

            getCurrentAddress(latitude, longitude)


            if(layoutInflated)
            {
                layoutInflated = false
                val layoutAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_move_bottom_down)
                layout_location.startAnimation(layoutAnimation)

                layoutAnimation.setAnimationListener(object : Animation.AnimationListener{
                    override fun onAnimationStart(animation: Animation?) {

                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        layout_location.visibility = View.GONE
                    }

                    override fun onAnimationRepeat(animation: Animation?) {

                    }

                })

            }


            if(clicked)
            {
                clicked = false
                searchLayoutAnimation = AnimationUtils.loadAnimation(this@StartActivity, R.anim.anim_move_top_down)
                searchConstraintLayout.startAnimation(searchLayoutAnimation)


                searchLayoutAnimation.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation) {
                        searchConstraintLayout.visibility = View.VISIBLE
                    }

                    override fun onAnimationEnd(animation: Animation?) {

                    }

                    override fun onAnimationRepeat(animation: Animation?) {

                    }
                })


                floatingLayoutAnimation = AnimationUtils.loadAnimation(this@StartActivity, R.anim.anim_move_bottom_up)
                floatingLinearLayout.startAnimation(floatingLayoutAnimation)

                floatingLayoutAnimation.setAnimationListener(object:Animation.AnimationListener{
                    override fun onAnimationStart(animation: Animation?) {

                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        floatingLinearLayout.visibility = View.VISIBLE
                    }

                    override fun onAnimationRepeat(animation: Animation?) {

                    }

                })


            }
            else
            {
                clicked = true
                searchLayoutAnimation = AnimationUtils.loadAnimation(this@StartActivity, R.anim.anim_move_top_up)
                searchConstraintLayout.startAnimation(searchLayoutAnimation)


                searchLayoutAnimation.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation) {

                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        searchConstraintLayout.visibility = View.GONE
                    }

                    override fun onAnimationRepeat(animation: Animation?) {

                    }
                })


                floatingLayoutAnimation = AnimationUtils.loadAnimation(this@StartActivity, R.anim.anim_move_bottom_down)
                floatingLinearLayout.startAnimation(floatingLayoutAnimation)

                floatingLayoutAnimation.setAnimationListener(object:Animation.AnimationListener{
                    override fun onAnimationStart(animation: Animation?) {

                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        floatingLinearLayout.visibility = View.GONE
                    }

                    override fun onAnimationRepeat(animation: Animation?) {

                    }

                })

            }

        }



        mMap.setOnMapLongClickListener {

            setLocationLayout(it.latitude, it.longitude, it)

        }

        fadeOutAnimation()

    }

    private fun setLocationLayout(locationLatitude: Double, locationLongitude: Double, it: LatLng)
    {
        //기존 위치 지정 마커가 있다면 지운다.
        try{ selectLocationMarker.remove() }
        catch (e : Exception)
        {
            e.printStackTrace()
        }


        layoutInflated = true

        val layoutAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_move_bottom_up)
        layout_location.startAnimation(layoutAnimation)

        layoutAnimation.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationStart(animation: Animation?) {
                layout_location.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(animation: Animation?) {

            }

            override fun onAnimationRepeat(animation: Animation?) {

            }

        })

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 14f))

        MarkerOptions().position(it)

        val bitmap = (resources.getDrawable(R.drawable.icon_current_location_2) as BitmapDrawable).bitmap
        selectLocationMarker = mMap.addMarker(MarkerOptions().position(it).icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(bitmap, 30, 30, false))))!!

        val locationString : String = getCurrentAddress(locationLatitude, locationLongitude).replace("대한민국 ", "")


        val gpsTracker = GpsTracker(this)
        val latitude : Double = gpsTracker.getLatitude()
        val longitude : Double = gpsTracker.getLongtitude()

        val distance = getDistance(locationLatitude, locationLongitude, latitude, longitude)

        val makeStr = distance

        textDistanceLocation.text = makeStr
        textLocationTitle.text = locationString
        textAddress.text = locationString

        buttonStart.setOnClickListener {
            start = true
            sharedPreferences.edit().putBoolean("start", true).apply()

            sharedPreferences.edit().putFloat("startX", locationLatitude.toFloat()).putFloat("startY", locationLongitude.toFloat()).apply()
            sharedPreferences.edit().putString("startNewX", locationLatitude.toString()).putString("startNewY", locationLongitude.toString()).apply()
            sharedPreferences.edit().putString("startLocation", textLocationTitle.text.toString()).apply()





            if(end)
            {
                //출발지, 도착지 모두 정해짐
                openAfterSearch()

            }
            else
            {
                Toast.makeText(this, "출발지가 설정되었습니다.", Toast.LENGTH_LONG).show()
            }
        }

        buttonEnd.setOnClickListener {
            end = true
            sharedPreferences.edit().putFloat("endX", locationLatitude.toFloat()).putFloat("endY", locationLongitude.toFloat()).apply()
            sharedPreferences.edit().putString("endNewX", locationLatitude.toString()).putString("endNewY", locationLongitude.toString()).apply()
            sharedPreferences.edit().putString("endLocation", textLocationTitle.text.toString()).apply()
            if(start)
            {
                //출발지, 도착지 모두 정해짐
                openAfterSearch()
            }
            else
            {
                Toast.makeText(this, "도착지가 설정되었습니다.", Toast.LENGTH_LONG).show()
            }

        }



    }

    private fun fadeOutAnimation()
    {
        val animation = AnimationUtils.loadAnimation(this, R.anim.anim_fade_out)

        lottieViewStart.startAnimation(animation)
        viewStart.startAnimation(animation)

        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                lottieViewStart.visibility = View.GONE
                viewStart.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
    }

    private fun openAfterSearch()
    {
        start = false
        end = false
        val intent = Intent(this, AfterSearchActivity::class.java)
        startActivity(intent)
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

    private var backKeyPressedTime = 0L
    override fun onBackPressed() {

        if(layout_location.visibility == View.VISIBLE)
        {
            val layoutAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_move_bottom_down)
            layout_location.startAnimation(layoutAnimation)

            layoutAnimation.setAnimationListener(object : Animation.AnimationListener{
                override fun onAnimationStart(animation: Animation?) {

                }

                override fun onAnimationEnd(animation: Animation?) {
                    layout_location.visibility = View.GONE
                }

                override fun onAnimationRepeat(animation: Animation?) {

                }

            })
        }
        else
        {
            if (System.currentTimeMillis() > backKeyPressedTime + 2500) {
                backKeyPressedTime = System.currentTimeMillis()


                val toast = Toast.makeText(this, "뒤로 가기 버튼을 한 번 더 누르시면 종료됩니다.", Toast.LENGTH_LONG)
                toast.show()
                return
            }
            if (System.currentTimeMillis() <= backKeyPressedTime + 2500)
            {
                finish()
            }
        }


    }
}