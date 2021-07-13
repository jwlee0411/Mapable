package app.jw.mapable.gm.start

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import app.jw.mapable.gm.R
import app.jw.mapable.gm.aftersearch.AfterSearchActivity
import app.jw.mapable.gm.community.CommunityActivity
import app.jw.mapable.gm.firstsetting.FirstSettingEnabledActivity
import app.jw.mapable.gm.info.InfoActivity
import app.jw.mapable.gm.login.LoginActivity
import app.jw.mapable.gm.notice.NoticeActivity
import app.jw.mapable.gm.search.SearchActivity
import app.jw.mapable.gm.setting.SettingActivity
import app.jw.mapable.gm.setting.UserSettingActivity
import app.jw.mapable.gm.star.StarActivity
import com.bumptech.glide.Glide
import com.google.android.gms.location.*
import com.google.android.gms.location.places.ui.PlacePicker
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.navigation.NavigationView
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

   var locationLatitude = 0.0
    var locationLongitude = 0.0

    var layoutInflated = false

    var onTouched = false
    var clicked = false

    var loginType = 0

    var settings = BooleanArray(10)

    var prevLatitude : Double = 37.542035
    var prevLongtitude : Double = 126.966613

    var startX : Double = 0.0
    var startY : Double = 0.0
    var endX : Double = 0.0
    var endY : Double = 0.0

    var start = false
    var end = false

    val update_interval : Long = 1000
    val fastest_update_interval : Long = 500


    lateinit var searchLayoutAnimation : Animation
    lateinit var floatingLayoutAnimation : Animation

    lateinit var recognizer : SpeechRecognizer

    lateinit var mMap : GoogleMap

    lateinit var mCurrentLocation : Location

    lateinit var currentPosition : LatLng

    lateinit var mFusedLocationClient : FusedLocationProviderClient
    lateinit var locationRequest : LocationRequest

    lateinit var sharedPreferences : SharedPreferences
    lateinit var editor : SharedPreferences.Editor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContentView(R.layout.activity_start)



        sharedPreferences = getSharedPreferences("preferences", 0)
        editor = sharedPreferences.edit()


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

    fun getSettings()
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



        val nickname = sharedPreferences.getString("userName", "")
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



    fun setOnClick()
    {
        //debug : 기존 DB를 지우기 위한 디버그용 버튼


        //debug : 기존 DB를 지우기 위한 디버그용 버튼(x) => 그때그때 맞는 용도로 사용
        floatingDebug.setOnClickListener {
            startActivity(Intent(this, StartLocationActivity::class.java))
            //sharedPreferences.edit().clear().apply()
        }



        floatingRoadFound.setOnClickListener {
            startActivity(Intent(this, AfterSearchActivity::class.java))
            finish()
            overridePendingTransition(R.anim.anim_move_bottom_up_full, R.anim.anim_none)
        }


        textViewSearch.setOnClickListener {

           // Toast.makeText(this, "장소 검색 기능은 추후 제공 예정입니다.", Toast.LENGTH_LONG).show()
            val intent = Intent(this, SearchActivity::class.java)
            intent.putExtra("TTS", false)
            startActivity(intent)
            overridePendingTransition(R.anim.anim_move_bottom_up_full, R.anim.anim_none)
        }
        soundButton.setOnClickListener {

            //Toast.makeText(this, "장소 검색 기능은 추후 제공 예정입니다.", Toast.LENGTH_LONG).show()
            val intent = Intent(this, SearchActivity::class.java)
            intent.putExtra("TTS", true)
            startActivity(intent)
            overridePendingTransition(R.anim.anim_move_bottom_up_full, R.anim.anim_none)
        }

        floatingCurrentLocation.setOnClickListener {
            val gpsTracker = GpsTracker(this)
            val latitude : Double = gpsTracker.getLatitude()
            val longtitude : Double = gpsTracker.getLongtitude()

            val prevLatLng = LatLng(latitude, longtitude)

            try{ currentLocationMarker.remove() }
            catch (e : Exception)
            {
                e.printStackTrace()
            }


            val bitmap = (resources.getDrawable(R.drawable.marker_start_ride, null) as BitmapDrawable).bitmap
            currentLocationMarker = mMap.addMarker(MarkerOptions().position(prevLatLng).icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(bitmap, 30, 30, false))))!!






            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(prevLatLng, 15f))
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

            }
            true
        }

//        textView9.setOnClickListener{
//            println("12jladfp")
//        }

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

    fun setMap()
    {

        locationRequest = LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(update_interval).setFastestInterval(fastest_update_interval)

        val builder : LocationSettingsRequest.Builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(locationRequest)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val mapFragment : SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)



    }




    private fun checkPermission(): Boolean {
        val hasFineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        return hasFineLocationPermission == PackageManager.PERMISSION_GRANTED && hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED
    }

    fun checkLocationServicesStatus(): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }


    val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            val locationList = locationResult.locations
            if (locationList.size > 0) {
                val location = locationList[locationList.size - 1]
                prevLatitude = location.latitude
                prevLongtitude = location.longitude
                currentPosition = LatLng(prevLatitude, prevLongtitude)
                sharedPreferences.edit().putString("latitudeNow", prevLatitude.toString()).apply()
                sharedPreferences.edit().putString("longtitudeNow", prevLongtitude.toString())
                    .apply()
                if (!onTouched) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 13f))
                }
                mCurrentLocation = location
            }
        }
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
        //println("LOG0713 : ${addresses[0]} / ${addresses[1]} / ${addresses[2]} / ${addresses[3]} / ${address.url} / ${address.phone} / ${address.adminArea} / ${address.extras}")
        textViewSearch.text = address.getAddressLine(0).toString()
        return address.getAddressLine(0).toString()


    }

    fun setOnMapClick()
    {
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
            //TODO : 기존 코드 갈아엎고 새로운 방식으로 출발 도착 등 결정


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


            locationLatitude = it.latitude
            locationLongitude = it.longitude

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 14f))

            MarkerOptions().position(it)

            val bitmap = (resources.getDrawable(R.drawable.icon_current_location_2) as BitmapDrawable).bitmap
            selectLocationMarker = mMap.addMarker(MarkerOptions().position(it).icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(bitmap, 30, 30, false))))!!

            // mMap.addMarker(MarkerOptions().position(it))
            val locationString : String = getCurrentAddress(locationLatitude, locationLongitude).replace("대한민국 ", "")


            val gpsTracker = GpsTracker(this)
            val latitude : Double = gpsTracker.getLatitude()
            val longitude : Double = gpsTracker.getLongtitude()

            val distance = getDistance(locationLatitude, locationLongitude, latitude, longitude)

            val makeStr = distance

            textDistanceLocation.text = makeStr
            textLocationTitle.text = locationString
            textAddress.text = locationString

//            val intentBuilder = PlacePicker.IntentBuilder()
//            try{
//                val intent = intentBuilder.build(this)
//                startActivityForResult(intent, 1)
//            }
//            catch (e : Exception)
//            {
//                e.printStackTrace()
//            }



            layout_location.setOnClickListener {
                val intent = Intent(this, StartLocationActivity::class.java)
                //TODO : intent.putExtra()
                startActivity(intent)

            }

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
                sharedPreferences.edit().putFloat("endX", latitude.toFloat()).putFloat("endY", longitude.toFloat()).apply()
                sharedPreferences.edit().putString("endNewX", latitude.toString()).putString("endNewY", longitude.toString()).apply()
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



    }

    private fun openAfterSearch()
    {
        start = false
        end = false
        val intent = Intent(this, AfterSearchActivity::class.java)
        startActivity(intent)
    }

    //TODO : getDistance 함수 오류 수정
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



    private val listener: RecognitionListener = object : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle) {}
        override fun onBeginningOfSpeech() {}
        override fun onRmsChanged(rmsdB: Float) {}
        override fun onBufferReceived(buffer: ByteArray) {}
        override fun onEndOfSpeech() {}
        override fun onError(error: Int) {}
        override fun onResults(results: Bundle) {}
        override fun onPartialResults(partialResults: Bundle) {}
        override fun onEvent(eventType: Int, params: Bundle) {}
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 1 && resultCode == Activity.RESULT_OK)
        {
            val place = PlacePicker.getPlace(this, data)
            val name = place.name
            val phonenumber = place.phoneNumber
            val rating = place.rating


            buttonShare.setOnClickListener{
                val intentShare = Intent(Intent.ACTION_SEND)
                intentShare.type = "text/plain"
                intentShare.putExtra(Intent.EXTRA_TEXT, "공유할 텍스트")
                startActivity(Intent.createChooser(intentShare, "공유"))

            }

            buttonCall.setOnClickListener{
                startActivity(Intent("android.intent.action.DIAL", Uri.parse("tel:$phonenumber"))) //TODO : 전화번호 바꾸기
            }

            buttonBookmark.setOnClickListener {

                val uid = sharedPreferences.getString("uid", "")!!

                val bookmarkData = "★" + textLocationTitle.text.toString() + "※" + locationLatitude + "※" + locationLongitude

                val bookmarkString = sharedPreferences.getString("bookmark", "")!!

                editor.putString("bookmark", bookmarkString + bookmarkData).apply()




            }


        }
    }



    var backKeyPressedTime = 0L
    override fun onBackPressed() {

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