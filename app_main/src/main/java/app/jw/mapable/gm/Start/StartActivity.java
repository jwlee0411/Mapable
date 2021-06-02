package app.jw.mapable.gm.Start;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import app.jw.mapable.gm.AfterSearch.AfterSearchActivity;
import app.jw.mapable.gm.Explain.ExplainActivity;
import app.jw.mapable.gm.FirstSetting.FirstSettingEnabledActivity1;
import app.jw.mapable.gm.FirstSetting.FirstSettingEnabledActivity2;
import app.jw.mapable.gm.Info.InfoActivity;
import app.jw.mapable.gm.R;
import app.jw.mapable.gm.Search.SearchActivity;
import app.jw.mapable.gm.Setting.SettingActivity;


public class StartActivity extends AppCompatActivity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {

    boolean mapStatus = false, onTouched = false, clicked = false;
    double prevLatitude = 37.542035, prevLongtitude = 126.966613, startX = 0, startY = 0, endX, endY;
    int UPDATE_INTERVAL_MS = 1000, FASTEST_UPDATE_INTERVAL_MS = 500, PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.RECORD_AUDIO};

    NavigationView navigationView;

    LatLng currentPosition;
    Location mCurrentLocation;
    LinearLayout floatingLinearLayout;
    String locationTmp;
    private GoogleMap mMap;
    ConstraintLayout constraintLayout;
    TextView textSearch;
    Button soundButton, menuButton;
    DrawerLayout drawerLayout;
    SpeechRecognizer recognizer;
    Animation searchLayoutAnimation, floatingLayoutAnimation;
    SharedPreferences sharedPreferences;
    FloatingActionButton floatingLocation, floatingInfo, floatingRoadFound, floatingDebug;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_start);



        //설정화면 => 메인화면인 경우 설정화면 종료
        //다른 액티비티 종료
        try{
            FirstSettingEnabledActivity1 firstSettingEnabledActivity1 = (FirstSettingEnabledActivity1)FirstSettingEnabledActivity1.firstSettingEnabledActivity1;
            FirstSettingEnabledActivity2 firstSettingEnabledActivity2 = (FirstSettingEnabledActivity2)FirstSettingEnabledActivity2.firstSettingEnabledActivity2;
            firstSettingEnabledActivity1.finish();
            firstSettingEnabledActivity2.finish();
        }catch (Exception ignored)
        {

        }




        findView();

        sharedPreferences = getSharedPreferences("preference", 0);
        constraintLayout.bringToFront();

    }

    void findView()
    {
        floatingDebug = findViewById(R.id.floatingDebug);
        floatingInfo = findViewById(R.id.floatingInfo);
        floatingLocation = findViewById(R.id.floatingCurrentLocation);

        textSearch = findViewById(R.id.textViewSearch);
        navigationView = findViewById(R.id.navigationView);
        drawerLayout = findViewById(R.id.drawerLayout);
        menuButton = findViewById(R.id.menuButton);
        floatingRoadFound = findViewById(R.id.floatingRoadFound);
        constraintLayout = findViewById(R.id.searchConstraintLayout);


        floatingLinearLayout = findViewById(R.id.floatingLinearLayout);

        setOnClick();
    }

    void setOnClick()
    {
        //debug : 기존 DB를 지우기 위한 디버그용 버튼

        floatingDebug.setOnClickListener(v -> {
            sharedPreferences.edit().clear().apply();
        });



        floatingRoadFound.setOnClickListener(v -> {
            startActivity(new Intent(this, AfterSearchActivity.class));
            finish();
            overridePendingTransition(R.anim.anim_move_bottom_up_full, R.anim.anim_none);
        });


        textSearch.setOnClickListener(v -> {
            startActivity(new Intent(this, SearchActivity.class));
            finish();
            overridePendingTransition(R.anim.anim_move_bottom_up_full, R.anim.anim_none);
        });

        floatingLocation.setOnClickListener(v -> {
            onTouched = false;
            LatLng prevLatLng = new LatLng(prevLatitude, prevLongtitude);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(prevLatLng, 15));

        });

        floatingInfo.setOnClickListener(v ->
        {
            startActivity(new Intent(StartActivity.this, InfoActivity.class));
            finish();

        });

        //Menu 부분 관련 설정 코드
        menuButton.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(item -> {
            drawerLayout.closeDrawers();
            int id = item.getItemId();

            switch (id)
            {
                case R.id.nav_settings:
                    startActivity(new Intent(StartActivity.this, SettingActivity.class)); break;

                case R.id.nav_explain:
                    startActivity(new Intent(StartActivity.this, ExplainActivity.class)); break;

            }


            return true;
        });

        setMap();
    }

    void setMap()
    {
        ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, 1);
        locationRequest = new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(UPDATE_INTERVAL_MS).setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    void checkPermissionGranted()
    {
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED && hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED   ) {
            startLocationUpdates();
        }
        else
        {
            ActivityCompat.requestPermissions( this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();
        setDefaultLocation();
        checkPermissionGranted();



        LatLng SEOUL = new LatLng(prevLatitude, prevLongtitude);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 13));
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setOnCameraMoveListener(() -> onTouched = true);

        setOnMapClick();



    }

    void setOnMapClick()
    {
        mMap.setOnMapClickListener(latLng -> {


            if(clicked)
            {
                clicked = false;
                searchLayoutAnimation = AnimationUtils.loadAnimation(StartActivity.this, R.anim.anim_move_top_down);
                constraintLayout.startAnimation(searchLayoutAnimation);

                floatingLayoutAnimation = AnimationUtils.loadAnimation(StartActivity.this, R.anim.anim_move_bottom_up);
                floatingLinearLayout.startAnimation(floatingLayoutAnimation);

                searchLayoutAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        constraintLayout.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                floatingLayoutAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        floatingLinearLayout.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
            else
            {
                clicked = true;
                searchLayoutAnimation = AnimationUtils.loadAnimation(StartActivity.this, R.anim.anim_move_top_up);
                constraintLayout.startAnimation(searchLayoutAnimation);

                floatingLayoutAnimation = AnimationUtils.loadAnimation(StartActivity.this, R.anim.anim_move_bottom_down);
                floatingLinearLayout.startAnimation(floatingLayoutAnimation);

                searchLayoutAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        constraintLayout.setVisibility(View.GONE);


                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                floatingLayoutAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        floatingLinearLayout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }




        });

        mMap.setOnMapLongClickListener(latLng -> {

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


            if(!mapStatus)
            {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                Bitmap departBitmap = ((BitmapDrawable)getResources().getDrawable(R.drawable.marker_start)).getBitmap();
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(departBitmap, 45, 64, false)));
                locationTmp = latLng.toString();
                startX = Double.parseDouble(locationTmp.substring(locationTmp.indexOf("(")+1, locationTmp.indexOf(",")));
                startY = Double.parseDouble(locationTmp.substring(locationTmp.indexOf(",")+1, locationTmp.length()-1));
                System.out.println(startX);
                System.out.println(startY);
                markerOptions.title("출발");
                // Toast.makeText(StartActivity.this, "출발위치 :" +  latLng.toString(), Toast.LENGTH_SHORT).show();
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.addMarker(markerOptions);
                mapStatus = true;
            }
            else
            {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                Bitmap endBitmap = ((BitmapDrawable)getResources().getDrawable(R.drawable.marker_end)).getBitmap();
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(endBitmap, 45, 64, false)));
                locationTmp = latLng.toString();
                endX = Double.parseDouble(locationTmp.substring(locationTmp.indexOf("(")+1, locationTmp.indexOf(",")));
                endY = Double.parseDouble(locationTmp.substring(locationTmp.indexOf(",")+1, locationTmp.length()-1));
                System.out.println(latLng);
                markerOptions.title("도착");
                //   Toast.makeText(StartActivity.this, "도착위치 :" +  latLng.toString(), Toast.LENGTH_SHORT).show();
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.addMarker(markerOptions);
                mapStatus = false;

                Intent intent = new Intent(StartActivity.this, AfterSearchActivity.class);
                intent.putExtra("startX", startX);
                intent.putExtra("startY", startY);
                intent.putExtra("endX", endX);
                intent.putExtra("endY", endY);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.anim_move_bottom_up_full, R.anim.anim_none);
            }


        });




        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");

        soundButton = findViewById(R.id.soundButton);
        soundButton.setOnClickListener(v -> {
            recognizer = SpeechRecognizer.createSpeechRecognizer(StartActivity.this);
            recognizer.setRecognitionListener(listener);
            recognizer.startListening(intent);

            //TODO : 작동 안됨
        });
    }



    protected void onStart() {

        super.onStart();
        if(checkPermission())
        {
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
        if(mMap != null) mMap.setMyLocationEnabled(true);
    }

    protected void onStop()
    {
        super.onStop();
        if(mFusedLocationClient!= null)
        {
            mFusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

    private void startLocationUpdates() {

        if(checkLocationServicesStatus())
        {
            int hasFineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

            if(checkPermission())
            {
                mMap.setMyLocationEnabled(true);
            }

        }

    }

    private boolean checkPermission()
    {
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        return hasFineLocationPermission == PackageManager.PERMISSION_GRANTED && hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED;
    }

    public boolean checkLocationServicesStatus()
    {
        LocationManager locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void setDefaultLocation() {

    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    private long backKeyPressedTime = 0;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2500) {
            backKeyPressedTime = System.currentTimeMillis();
            // 첫 번째 뒤로 가기 버튼을 누를 때 표시
            Toast toast = Toast.makeText(this, "뒤로 가기 버튼을 한 번 더 누르시면 종료됩니다.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간에 2.5초를 더해 현재 시간과 비교 후
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간이 2.5초가 지나지 않았으면 종료
        if (System.currentTimeMillis() <= backKeyPressedTime + 2500) {
            finish();
        }
    }

    private final RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {

        }

        @Override
        public void onBeginningOfSpeech() {

        }

        @Override
        public void onRmsChanged(float rmsdB) {

        }

        @Override
        public void onBufferReceived(byte[] buffer) {

        }

        @Override
        public void onEndOfSpeech() {

        }

        @Override
        public void onError(int error) {

        }

        @Override
        public void onResults(Bundle results) {
            //result
        }

        @Override
        public void onPartialResults(Bundle partialResults) {

        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }
    };


    final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NotNull LocationResult locationResult) {
            super.onLocationResult(locationResult);

            List<Location> locationList = locationResult.getLocations();

            if(locationList.size() > 0)
            {
                Location location = locationList.get(locationList.size() - 1);
                prevLatitude = location.getLatitude();
                prevLongtitude = location.getLongitude();
                currentPosition = new LatLng(prevLatitude, prevLongtitude);

                sharedPreferences.edit().putString("latitudeNow", String.valueOf(prevLatitude)).apply();
                sharedPreferences.edit().putString("longtitudeNow", String.valueOf(prevLongtitude)).apply();


                if(!onTouched)
                {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 13));
                }


                mCurrentLocation = location;

            }
        }
    };


}