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
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import app.jw.mapable.gm.AfterSearch.AfterSearchActivity;
import app.jw.mapable.gm.Explain.ExplainActivity;
import app.jw.mapable.gm.FirstSetting.FirstSettingEnabledActivity1;
import app.jw.mapable.gm.FirstSetting.FirstSettingEnabledActivity2;
import app.jw.mapable.gm.Info.InfoActivity;
import app.jw.mapable.gm.R;
import app.jw.mapable.gm.Setting.SettingActivity;


public class StartActivity extends AppCompatActivity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {

    boolean mapStatus = false;

    boolean onTouched = false;

    double prevLatitude = 37.542035, prevLongtitude = 126.966613;
    LatLng currentPosition;
    double x, y;
    Location mCurrentLocation;
    String locationTmp;
    private GoogleMap mMap;

    TextToSpeech tts;

    boolean isStart = false, isEnd = false;


    //TODO : 테스트용 변수들
    double startX = 0, startY = 0, endX, endY;

    boolean startLocation = false, endLocation = false;

    Button soundButton;
    SpeechRecognizer recognizer;

    SharedPreferences sharedPreferences;
    boolean clicked = false;

    FloatingActionButton floatingLocation, floatingInfo;

    private static final String TAG = "googlemap_example";
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int UPDATE_INTERVAL_MS = 1000;  // 1초
    private static final int FASTEST_UPDATE_INTERVAL_MS = 500; // 0.5초
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    boolean needRequest = false;
    final String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private View mLayout;  // Snackbar 사용하기 위해서는 View가 필요합니다.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_start);


        sharedPreferences = getSharedPreferences("preference", 0);


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



        //debug
        FloatingActionButton floatingDebug = findViewById(R.id.floatingDebug);
        floatingDebug.setOnClickListener(v -> {
            sharedPreferences.edit().clear().apply();
        });







        //DrawerLayout 관련 코드 추가
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        Button menuButton = findViewById(R.id.menuButton);
        menuButton.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
        NavigationView navigationView = findViewById(R.id.navigationView);
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

        floatingInfo = findViewById(R.id.floatingInfo);
        floatingLocation = findViewById(R.id.floatingCurrentLocation);

        floatingLocation.setOnClickListener(v -> {
            onTouched = false;
            LatLng prevLatLng = new LatLng(prevLatitude, prevLongtitude);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(prevLatLng, 15));

        });

        floatingInfo.setOnClickListener(v -> startActivity(new Intent(StartActivity.this, InfoActivity.class)));


//        tts = new TextToSpeech(this, status -> {
//            if(status != ERROR)
//            {
//                tts.setLanguage(Locale.KOREAN);
//            }
//        });

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.INTERNET, Manifest.permission.RECORD_AUDIO}, 1);





        ConstraintLayout constraintLayout = findViewById(R.id.searchConstraintLayout);
        constraintLayout.bringToFront();

        locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL_MS)
                .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);


        LocationSettingsRequest.Builder builder =
                new LocationSettingsRequest.Builder();

        builder.addLocationRequest(locationRequest);


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setDefaultLocation();

        mMap.clear();



        LatLng SEOUL = new LatLng(prevLatitude, prevLongtitude);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 13));



        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);



        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED   ) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            startLocationUpdates(); // 3. 위치 업데이트 시작


        }else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Snackbar.make(mLayout, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.",
                        Snackbar.LENGTH_INDEFINITE).setAction("확인", view -> {

                            // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                            ActivityCompat.requestPermissions( StartActivity.this, REQUIRED_PERMISSIONS,
                                    PERMISSIONS_REQUEST_CODE);
                        }).show();


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions( this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }


        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        // 현재 오동작을 해서 주석처리



        mMap.setOnCameraMoveListener(() -> onTouched = true);

        //mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        mMap.setOnMapClickListener(latLng -> {




            ConstraintLayout searchConstraintLayout = findViewById(R.id.searchConstraintLayout);
            LinearLayout floatingLinearLayout = findViewById(R.id.floatingLinearLayout);
            Animation searchLayoutAnimation, floatingLayoutAnimation;
            if(clicked)
            {
                clicked = false;
                searchLayoutAnimation = AnimationUtils.loadAnimation(StartActivity.this, R.anim.anim_move_top_down);
                searchConstraintLayout.startAnimation(searchLayoutAnimation);

                floatingLayoutAnimation = AnimationUtils.loadAnimation(StartActivity.this, R.anim.anim_move_bottom_up);
                floatingLinearLayout.startAnimation(floatingLayoutAnimation);

                searchLayoutAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        searchConstraintLayout.setVisibility(View.VISIBLE);

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
                searchConstraintLayout.startAnimation(searchLayoutAnimation);

                floatingLayoutAnimation = AnimationUtils.loadAnimation(StartActivity.this, R.anim.anim_move_bottom_down);
                floatingLinearLayout.startAnimation(floatingLayoutAnimation);

                searchLayoutAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        searchConstraintLayout.setVisibility(View.GONE);


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







//        LatLng SEOUL = new LatLng(37.56, 126.97);

//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(SEOUL);
//        markerOptions.title("서울");
//        markerOptions.snippet("한국의 수도");
//        mMap.addMarker(markerOptions);




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