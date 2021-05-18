package app.jw.mapable.gm;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.jetbrains.annotations.NotNull;

public class AfterRoadActivity extends AppCompatActivity implements OnMapReadyCallback {

    private LocationRequest locationRequest;
    double startX, startY, endX, endY;
    private static final String TAG = "googlemap_example";
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int UPDATE_INTERVAL_MS = 1000;  // 1초
    private static final int FASTEST_UPDATE_INTERVAL_MS = 500; // 0.5초
    private static final int PERMISSIONS_REQUEST_CODE = 100;

    private FusedLocationProviderClient mFusedLocationClient;
    SharedPreferences sharedPreferences;

    DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_road);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        sharedPreferences = getSharedPreferences("location", 0);
        startX = Double.parseDouble(sharedPreferences.getString("startX", ""));
        startY = Double.parseDouble(sharedPreferences.getString("startY", ""));
        endX = Double.parseDouble(sharedPreferences.getString("endX", ""));
        endY = Double.parseDouble(sharedPreferences.getString("endY", ""));

        drawerLayout = findViewById(R.id.drawerLayout);
        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull @NotNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull @NotNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull @NotNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        }); //TODO


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMapReady(GoogleMap googleMap) {


        String ways = getIntent().getExtras().getString("ways");
        System.out.println(ways);
        Toast.makeText(getApplicationContext(), ways, Toast.LENGTH_LONG).show();


        String[] waysSplit = ways.split("§");
        String[][] waysNewSplit = new String[waysSplit.length][14];

        LatLng[] subwayLatLngs;

        Bitmap departBitmap = ((BitmapDrawable)getResources().getDrawable(R.drawable.marker_start)).getBitmap();
        Bitmap endBitmap = ((BitmapDrawable)getResources().getDrawable(R.drawable.marker_end)).getBitmap();
        MarkerOptions markerDepart = new MarkerOptions();
        markerDepart.position(new LatLng(startX, startY));
        markerDepart.title("출발");
        markerDepart.icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(departBitmap, 45, 64, false)));
        googleMap.addMarker(markerDepart);

        MarkerOptions markerEnd = new MarkerOptions();
        markerEnd.position(new LatLng(endX, endY));
        markerEnd.title("도착");
        markerEnd.icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(endBitmap, 45, 64, false)));
        googleMap.addMarker(markerEnd);



        for(int i = 1; i<waysSplit.length-1; i++)
        {
            waysNewSplit[i] = waysSplit[i].split("※");

            int trafficType = Integer.parseInt(waysNewSplit[i][0]);
            switch (trafficType)
            {
                case 1:
                    String[] waysFinalSplit = waysNewSplit[i][12].split("★");
                    String[][] waysFinalNewSplit = new String[waysFinalSplit.length][];

                    subwayLatLngs = new LatLng[waysFinalSplit.length];
                    for(int j = 0; j<waysFinalSplit.length; j++)
                    {
                        int lineNumber = Integer.parseInt(waysNewSplit[i][5]);
                        waysFinalNewSplit[j] = waysFinalSplit[j].split("☆");
                        subwayLatLngs[j] = new LatLng(Double.parseDouble(waysFinalNewSplit[j][2]), Double.parseDouble(waysFinalNewSplit[j][1]));

                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(subwayLatLngs[j]);
                        markerOptions.title(waysFinalNewSplit[j][0]);
                        //markerOptions.snippet("한국의 수도");
                        markerOptions.snippet(waysNewSplit[i][4]);


                        Bitmap bitmap;

                        switch (lineNumber)
                        {
                            case 1: bitmap = ((BitmapDrawable) (getResources().getDrawable(R.drawable.train_line1))).getBitmap(); break;
                            case 2: bitmap = ((BitmapDrawable) (getResources().getDrawable(R.drawable.train_line2))).getBitmap(); break;
                            case 3: bitmap = ((BitmapDrawable) (getResources().getDrawable(R.drawable.train_line3))).getBitmap(); break;
                            case 4: bitmap = ((BitmapDrawable) (getResources().getDrawable(R.drawable.train_line4))).getBitmap(); break;
                            case 5: bitmap = ((BitmapDrawable) (getResources().getDrawable(R.drawable.train_line5))).getBitmap(); break;
                            case 6: bitmap = ((BitmapDrawable) (getResources().getDrawable(R.drawable.train_line6))).getBitmap(); break;
                            case 7: bitmap = ((BitmapDrawable) (getResources().getDrawable(R.drawable.train_line7))).getBitmap(); break;
                            case 8: bitmap = ((BitmapDrawable) (getResources().getDrawable(R.drawable.train_line8))).getBitmap(); break;
                            case 9: bitmap = ((BitmapDrawable) (getResources().getDrawable(R.drawable.train_line9))).getBitmap(); break;

                            case 101: bitmap = ((BitmapDrawable) (getResources().getDrawable(R.drawable.train_lineairport))).getBitmap(); break;
                            //case 102: bitmap = ((BitmapDrawable) (getResources().getDrawable(R.drawable.train_line9))).getBitmap(); break; //TODO : 자기부상
                            case 104: bitmap = ((BitmapDrawable) (getResources().getDrawable(R.drawable.train_linegyeongui))).getBitmap(); break;
                            case 107: bitmap = ((BitmapDrawable) (getResources().getDrawable(R.drawable.train_lineyongin))).getBitmap(); break;
                            case 108: bitmap = ((BitmapDrawable) (getResources().getDrawable(R.drawable.train_linegyeongchun))).getBitmap(); break;
                            case 109: bitmap = ((BitmapDrawable) (getResources().getDrawable(R.drawable.train_lineshinbundang))).getBitmap(); break;
                           // case 110: bitmap = ((BitmapDrawable) (getResources().getDrawable(R.drawable.train_lineuijeongbu))).getBitmap(); break; //TODO
                            case 112: bitmap = ((BitmapDrawable) (getResources().getDrawable(R.drawable.train_linegyeonggang))).getBitmap(); break;
                            //case 113: bitmap = ((BitmapDrawable) (getResources().getDrawable(R.drawable.train_lineui))).getBitmap(); break; //TODO
                            case 114: bitmap = ((BitmapDrawable) (getResources().getDrawable(R.drawable.train_lineseohae))).getBitmap(); break;
                           // case 115: bitmap = ((BitmapDrawable) (getResources().getDrawable(R.drawable.train_linegimpo))).getBitmap(); break; //TODO
                            case 116: bitmap = ((BitmapDrawable) (getResources().getDrawable(R.drawable.train_linebundang))).getBitmap(); break;


                            case 21: bitmap = ((BitmapDrawable) (getResources().getDrawable(R.drawable.train_lineincheon1))).getBitmap(); break;
                            case 22: bitmap = ((BitmapDrawable) (getResources().getDrawable(R.drawable.train_lineincheon2))).getBitmap(); break;




                            default: bitmap = ((BitmapDrawable) (getResources().getDrawable(R.drawable.train_line1))).getBitmap(); break;
                        }

                        //TODO
//                        switch (lineNumber)
//                        {
//                            case 1: drawable = getDrawable(R.drawable.train_line1);
//                            default: drawable = getDrawable(R.drawable.bus_gbus);
//                        }


                        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(bitmap, 30, 30, false)));




                        googleMap.addMarker(markerOptions);

                        if(j!=0)
                        {

                            System.out.println("★" + lineNumber);
                            switch (lineNumber)
                            {
                                case 1: googleMap.addPolyline(new PolylineOptions().add(subwayLatLngs[j], subwayLatLngs[j-1]).width(10).color(ContextCompat.getColor(AfterRoadActivity.this, R.color.line1))); System.out.println("success");break;
                                case 2: googleMap.addPolyline(new PolylineOptions().add(subwayLatLngs[j], subwayLatLngs[j-1]).width(10).color(ContextCompat.getColor(AfterRoadActivity.this, R.color.line2))); System.out.println("success2");break;
                                case 3: googleMap.addPolyline(new PolylineOptions().add(subwayLatLngs[j], subwayLatLngs[j-1]).width(10).color(ContextCompat.getColor(AfterRoadActivity.this, R.color.line3))); break;
                                case 4: googleMap.addPolyline(new PolylineOptions().add(subwayLatLngs[j], subwayLatLngs[j-1]).width(10).color(ContextCompat.getColor(AfterRoadActivity.this, R.color.line4))); break;
                                case 5: googleMap.addPolyline(new PolylineOptions().add(subwayLatLngs[j], subwayLatLngs[j-1]).width(10).color(ContextCompat.getColor(AfterRoadActivity.this, R.color.line5))); break;
                                case 6: googleMap.addPolyline(new PolylineOptions().add(subwayLatLngs[j], subwayLatLngs[j-1]).width(10).color(ContextCompat.getColor(AfterRoadActivity.this, R.color.line6))); break;
                                case 7: googleMap.addPolyline(new PolylineOptions().add(subwayLatLngs[j], subwayLatLngs[j-1]).width(10).color(ContextCompat.getColor(AfterRoadActivity.this, R.color.line7))); break;
                                case 8: googleMap.addPolyline(new PolylineOptions().add(subwayLatLngs[j], subwayLatLngs[j-1]).width(10).color(ContextCompat.getColor(AfterRoadActivity.this, R.color.line8))); break;
                                case 9: googleMap.addPolyline(new PolylineOptions().add(subwayLatLngs[j], subwayLatLngs[j-1]).width(10).color(ContextCompat.getColor(AfterRoadActivity.this, R.color.line9))); break;

                                case 101: googleMap.addPolyline(new PolylineOptions().add(subwayLatLngs[j], subwayLatLngs[j-1]).width(10).color(ContextCompat.getColor(AfterRoadActivity.this, R.color.lineAirport))); break;
                                case 102: googleMap.addPolyline(new PolylineOptions().add(subwayLatLngs[j], subwayLatLngs[j-1]).width(10).color(ContextCompat.getColor(AfterRoadActivity.this, R.color.lineMaglev))); break;
                                case 104: googleMap.addPolyline(new PolylineOptions().add(subwayLatLngs[j], subwayLatLngs[j-1]).width(10).color(ContextCompat.getColor(AfterRoadActivity.this, R.color.lineGyeongui))); break;
                                case 107: googleMap.addPolyline(new PolylineOptions().add(subwayLatLngs[j], subwayLatLngs[j-1]).width(10).color(ContextCompat.getColor(AfterRoadActivity.this, R.color.lineYongin))); break;
                                case 108: googleMap.addPolyline(new PolylineOptions().add(subwayLatLngs[j], subwayLatLngs[j-1]).width(10).color(ContextCompat.getColor(AfterRoadActivity.this, R.color.lineGyeongchun))); break;
                                case 109: googleMap.addPolyline(new PolylineOptions().add(subwayLatLngs[j], subwayLatLngs[j-1]).width(10).color(ContextCompat.getColor(AfterRoadActivity.this, R.color.lineShinBundang))); break;
                                case 110: googleMap.addPolyline(new PolylineOptions().add(subwayLatLngs[j], subwayLatLngs[j-1]).width(10).color(ContextCompat.getColor(AfterRoadActivity.this, R.color.lineUijeongbu))); break;
                                case 112: googleMap.addPolyline(new PolylineOptions().add(subwayLatLngs[j], subwayLatLngs[j-1]).width(10).color(ContextCompat.getColor(AfterRoadActivity.this, R.color.lineGyeonggang))); break;
                                case 113: googleMap.addPolyline(new PolylineOptions().add(subwayLatLngs[j], subwayLatLngs[j-1]).width(10).color(ContextCompat.getColor(AfterRoadActivity.this, R.color.lineUi))); break;
                                case 114: googleMap.addPolyline(new PolylineOptions().add(subwayLatLngs[j], subwayLatLngs[j-1]).width(10).color(ContextCompat.getColor(AfterRoadActivity.this, R.color.lineSeohae))); break;
                                case 115: googleMap.addPolyline(new PolylineOptions().add(subwayLatLngs[j], subwayLatLngs[j-1]).width(10).color(ContextCompat.getColor(AfterRoadActivity.this, R.color.lineGimpo))); break;
                                case 116: googleMap.addPolyline(new PolylineOptions().add(subwayLatLngs[j], subwayLatLngs[j-1]).width(10).color(ContextCompat.getColor(AfterRoadActivity.this, R.color.lineBundang))); break;


                                case 21: googleMap.addPolyline(new PolylineOptions().add(subwayLatLngs[j], subwayLatLngs[j-1]).width(10).color(ContextCompat.getColor(AfterRoadActivity.this, R.color.lineIncheon1))); break;
                                case 22: googleMap.addPolyline(new PolylineOptions().add(subwayLatLngs[j], subwayLatLngs[j-1]).width(10).color(ContextCompat.getColor(AfterRoadActivity.this, R.color.lineIncheon2))); break;

                                //case 31: mMap.addPolyline(new PolylineOptions().add(subwayLatLngs[j], subwayLatLngs[j-1]).width(10).color(ContextCompat.getColor(AfterRoadActivity.this, R.color.pink01))); break;

                                default: googleMap.addPolyline(new PolylineOptions().add(subwayLatLngs[j], subwayLatLngs[j-1]).width(10).color(ContextCompat.getColor(AfterRoadActivity.this, R.color.pink01)));

                            }


                        }



                        System.out.println(waysFinalNewSplit[j][1]);
                        System.out.println(waysFinalNewSplit[j][2]);
                    }

                    if(i==1)
                    {
                        googleMap.addPolyline(new PolylineOptions().add(new LatLng(startX, startY), subwayLatLngs[0]).width(20).color(Color.MAGENTA));
                    }

                    if(i==waysSplit.length-2)
                    {
                        googleMap.addPolyline(new PolylineOptions().add(new LatLng(endX, endY), subwayLatLngs[subwayLatLngs.length-1]).width(20).color(Color.MAGENTA));
                    }



                    break;

                case 2:
                    String[] waysFinalSplit2 = waysNewSplit[i][13].split("★");
                    String[][] waysFinalNewSplit2 = new String[waysFinalSplit2.length][];

                    subwayLatLngs = new LatLng[waysFinalSplit2.length];

                    for(int j = 0; j<waysFinalSplit2.length; j++)
                    {
                        waysFinalNewSplit2[j] = waysFinalSplit2[j].split("☆");

                        subwayLatLngs[j] = new LatLng(Double.parseDouble(waysFinalNewSplit2[j][2]), Double.parseDouble(waysFinalNewSplit2[j][1]));

                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(subwayLatLngs[j]);
                        markerOptions.title(waysFinalNewSplit2[j][0]);
                        //markerOptions.snippet("한국의 수도");
                        markerOptions.snippet(waysNewSplit[i][4]);
                        googleMap.addMarker(markerOptions);

                        if(j!=0)
                        {
                            googleMap.addPolyline(new PolylineOptions().add(subwayLatLngs[j], subwayLatLngs[j-1]).width(5).color(Color.BLUE));
                        }
                        System.out.println(waysFinalNewSplit2[j][1]);
                        System.out.println(waysFinalNewSplit2[j][2]);

                    }

                    if(i==1)
                    {
                        googleMap.addPolyline(new PolylineOptions().add(new LatLng(startX, startY), subwayLatLngs[0]).width(20).color(Color.MAGENTA));
                    }

                    if(i==waysSplit.length-2)
                    {
                        googleMap.addPolyline(new PolylineOptions().add(new LatLng(endX, endY), subwayLatLngs[subwayLatLngs.length-1]).width(20).color(Color.MAGENTA));
                    }


                    break;

                case 3:


                    break;
            }


        }

        LatLng SEOUL = new LatLng(37.56, 126.97);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 13));


        googleMap.setOnMarkerClickListener(marker -> false);

        googleMap.setOnInfoWindowClickListener(marker -> {
            String markerTitle = marker.getTitle();
            LatLng markerLocation = marker.getPosition();
            String markerSnippet = marker.getSnippet();

            AfterRoadDialog afterRoadDialog = new AfterRoadDialog(AfterRoadActivity.this);

            //TODO
            afterRoadDialog.callFunction();

//                Toast.makeText(AfterRoadActivity.this, markerTitle + markerSnippet, Toast.LENGTH_LONG).show();

        });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
