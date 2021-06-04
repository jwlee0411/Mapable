package app.jw.mapable.gm.Start

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import app.jw.mapable.gm.AfterSearch.AfterSearchActivity
import app.jw.mapable.gm.Explain.ExplainActivity
import app.jw.mapable.gm.FirstSetting.FirstSettingEnabledActivity1
import app.jw.mapable.gm.FirstSetting.FirstSettingEnabledActivity2
import app.jw.mapable.gm.Info.InfoActivity
import app.jw.mapable.gm.R
import app.jw.mapable.gm.Search.SearchActivity
import app.jw.mapable.gm.Setting.SettingActivity
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_start.*

class StartActivity : AppCompatActivity(), OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {


    var mapStatus = false
    var onTouched = false
    var clicked = false


    var prevLatitude : Double = 37.542035
    var prevLongtitude : Double = 126.966613

    var startX : Double = 0.0
    var startY : Double = 0.0
    var endX : Double = 0.0
    var endY : Double = 0.0

    val UPDATE_INTERVAL_MS : Long = 1000
    val FASTEST_UPDATE_INTERVAL_MS : Long = 500
    val PERMISSIONS_REQUEST_CODE : Int = 100

    var REQUIRED_PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.RECORD_AUDIO
    )


    lateinit var searchLayoutAnimation : Animation
    lateinit var floatingLayoutAnimation : Animation

    lateinit var recognizer : SpeechRecognizer

    lateinit var mMap : GoogleMap

    var locationTmp = ""

    lateinit var mCurrentLocation : Location

    lateinit var currentPosition : LatLng

    lateinit var mFusedLocationClient : FusedLocationProviderClient
    lateinit var locationRequest : LocationRequest

    lateinit var sharedPreferences : SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContentView(R.layout.activity_start)

        //설정화면 => 메인화면인 경우 설정화면 종료
        //다른 액티비티 종료


        //설정화면 => 메인화면인 경우 설정화면 종료
        //다른 액티비티 종료
        try {
            val firstSettingEnabledActivity1 = FirstSettingEnabledActivity1.firstSettingEnabledActivity1 as FirstSettingEnabledActivity1
            val firstSettingEnabledActivity2 = FirstSettingEnabledActivity2.firstSettingEnabledActivity2 as FirstSettingEnabledActivity2
            firstSettingEnabledActivity1.finish()
            firstSettingEnabledActivity2.finish()
        } catch (ignored: Exception) {
        }

        sharedPreferences = getSharedPreferences("preferences", 0)
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


    }



    fun setOnClick()
    {
        //debug : 기존 DB를 지우기 위한 디버그용 버튼


        //debug : 기존 DB를 지우기 위한 디버그용 버튼
        floatingDebug.setOnClickListener {
            sharedPreferences.edit().clear().apply()
        }



        floatingRoadFound.setOnClickListener {
            startActivity(Intent(this, AfterSearchActivity::class.java))
            finish()
            overridePendingTransition(R.anim.anim_move_bottom_up_full, R.anim.anim_none)
        }


        textViewSearch.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
            finish()
            overridePendingTransition(R.anim.anim_move_bottom_up_full, R.anim.anim_none)
        }

        floatingCurrentLocation.setOnClickListener {
            onTouched = false
            val prevLatLng = LatLng(prevLatitude, prevLongtitude)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(prevLatLng, 15f))
        }

        floatingInfo.setOnClickListener { v: View? ->
            startActivity(Intent(this@StartActivity, InfoActivity::class.java))
            finish()
        }

        //Menu 부분 관련 설정 코드

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
                R.id.nav_explain -> startActivity(Intent(this@StartActivity, ExplainActivity::class.java))
            }
            true
        }

        setMap()
    }

    fun setMap()
    {
        ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, 1)
        locationRequest = LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(UPDATE_INTERVAL_MS).setFastestInterval(FASTEST_UPDATE_INTERVAL_MS)

        val builder : LocationSettingsRequest.Builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(locationRequest)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val mapFragment : SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)



    }

    fun checkPermissionGranted()
    {
        val hasFineLocationPermission : Int = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val hasCoarseLocationPermission : Int = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)

        if(hasFineLocationPermission == PackageManager.PERMISSION_GRANTED && hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED)
        {

        }
        else
        {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE)
        }
    }

    fun startLocationUpdates()
    {
        if(checkLocationServicesStatus())
        {
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)

            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())

            if(checkPermission())
            {
                mMap.isMyLocationEnabled = true
            }
        }
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
        checkPermissionGranted()

        val SEOUL : LatLng = LatLng(prevLatitude, prevLongtitude)

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 13F))
        mMap.uiSettings.isMyLocationButtonEnabled = true
        mMap.setOnCameraMoveListener { onTouched = true }

        setOnMapClick()

    }


    fun setOnMapClick()
    {
        mMap.setOnMapClickListener {
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
//
//                MarkerOptions markerOptions = new MarkerOptions();
//                markerOptions.position(latLng);
//                locationTmp = latLng.toString();
//                x = Double.parseDouble(locationTmp.substring(locationTmp.indexOf("(")+1, locationTmp.indexOf(",")));
//                y = Double.parseDouble(locationTmp.substring(locationTmp.indexOf(",")+1, locationTmp.length()-1));
//                System.out.println(x);
//                System.out.println(y);
////                markerOptions.title("출발");
////                Toast.makeText(StartActivity.this, "출발위치 :" +  latLng.toString(), Toast.LENGTH_SHORT).show();
//
//
//                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//                mMap.addMarker(markerOptions);
//                mapStatus = true;
//
//            StartDialog startDialog = new StartDialog(StartActivity.this);
//            startDialog.callFunction(x, y, "TODO", "TODO", false, false);


//
//            StartDialog startDialog = new StartDialog(StartActivity.this);
//
//
//            // 0 : 출발, 도착 X
//            // 1: 출발O, 도착X
//            // 2 : 출발X, 도착O
//            // 3 : 모두 정해짐
//
//            startDialog.callFunction(x, y, "TODO");


            val markerOptions = MarkerOptions()

            markerOptions.position(it)
            val departBitmap = (ResourcesCompat.getDrawable(resources, R.drawable.marker_start, null) as BitmapDrawable).bitmap
            val endBitmap = (ResourcesCompat.getDrawable(resources, R.drawable.marker_end, null) as BitmapDrawable).bitmap
            locationTmp = it.toString()


            if (!mapStatus) {
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(departBitmap, 45, 64, false)))
                startX = locationTmp.substring(locationTmp.indexOf("(") + 1, locationTmp.indexOf(",")).toDouble()
                startY = locationTmp.substring(locationTmp.indexOf(",") + 1, locationTmp.length - 1).toDouble()


                markerOptions.title("출발")
                mMap.animateCamera(CameraUpdateFactory.newLatLng(it))
                mMap.addMarker(markerOptions)
                mapStatus = true

            }
            else
            {
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(endBitmap, 45, 64, false)))
                endX = locationTmp.substring(locationTmp.indexOf("(") + 1, locationTmp.indexOf(",")).toDouble()
                endY = locationTmp.substring(locationTmp.indexOf(",") + 1, locationTmp.length - 1).toDouble()
                markerOptions.title("도착")
                mMap.animateCamera(CameraUpdateFactory.newLatLng(it))
                mMap.addMarker(markerOptions)
                mapStatus = false

                val intent = Intent(this@StartActivity, AfterSearchActivity::class.java)
                intent.putExtra("startX", startX)
                intent.putExtra("startY", startY)
                intent.putExtra("endX", endX)
                intent.putExtra("endY", endY)
                startActivity(intent)
                finish()
                overridePendingTransition(R.anim.anim_move_bottom_up_full, R.anim.anim_none)
            }

            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, packageName)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR")

            soundButton.setOnClickListener {
                recognizer = SpeechRecognizer.createSpeechRecognizer(this@StartActivity)
                recognizer.setRecognitionListener(listener)
                recognizer.startListening(intent)
            }


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






    var backKeyPressedTime = 0L
    override fun onBackPressed() {

        if (System.currentTimeMillis() > backKeyPressedTime + 2500) {
            backKeyPressedTime = System.currentTimeMillis()
            // 첫 번째 뒤로 가기 버튼을 누를 때 표시
            val toast = Toast.makeText(this, "뒤로 가기 버튼을 한 번 더 누르시면 종료됩니다.", Toast.LENGTH_LONG)
            toast.show()
            return
        }
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간에 2.5초를 더해 현재 시간과 비교 후
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간이 2.5초가 지나지 않았으면 종료
        if (System.currentTimeMillis() <= backKeyPressedTime + 2500)
        {
            finish()
        }
    }
}