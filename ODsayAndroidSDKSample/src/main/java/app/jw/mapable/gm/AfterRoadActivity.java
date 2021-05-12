package app.jw.mapable.gm;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class AfterRoadActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationRequest locationRequest;
    double startX, startY, endX, endY;
    private static final String TAG = "googlemap_example";
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int UPDATE_INTERVAL_MS = 1000;  // 1초
    private static final int FASTEST_UPDATE_INTERVAL_MS = 500; // 0.5초
    private static final int PERMISSIONS_REQUEST_CODE = 100;

    private FusedLocationProviderClient mFusedLocationClient;


    SharedPreferences sharedPreferences;


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


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;



        String ways = getIntent().getExtras().getString("ways");
        System.out.println(ways);
        Toast.makeText(getApplicationContext(), ways, Toast.LENGTH_LONG).show();


        String[] waysSplit = ways.split("§");
        String[][] waysNewSplit = new String[waysSplit.length][14];

        LatLng[] subwayLatLngs;

        MarkerOptions markerDepart = new MarkerOptions();
        markerDepart.position(new LatLng(startX, startY));
        markerDepart.title("출발");
        mMap.addMarker(markerDepart);

        MarkerOptions markerEnd = new MarkerOptions();
        markerEnd.position(new LatLng(endX, endY));
        markerEnd.title("도착");
        mMap.addMarker(markerEnd);



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
                        waysFinalNewSplit[j] = waysFinalSplit[j].split("☆");
                        subwayLatLngs[j] = new LatLng(Double.parseDouble(waysFinalNewSplit[j][2]), Double.parseDouble(waysFinalNewSplit[j][1]));

                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(subwayLatLngs[j]);
                        markerOptions.title(waysFinalNewSplit[j][0]);
                        //markerOptions.snippet("한국의 수도");
                        markerOptions.snippet(waysNewSplit[i][4]);
                        mMap.addMarker(markerOptions);

                        if(j!=0)
                        {
                            mMap.addPolyline(new PolylineOptions().add(subwayLatLngs[j], subwayLatLngs[j-1]).width(5).color(Color.RED));
                        }



                        System.out.println(waysFinalNewSplit[j][1]);
                        System.out.println(waysFinalNewSplit[j][2]);
                    }

                    if(i==1)
                    {
                        mMap.addPolyline(new PolylineOptions().add(new LatLng(startX, startY), subwayLatLngs[0]).width(20).color(Color.MAGENTA));
                    }

                    if(i==waysSplit.length-2)
                    {
                        mMap.addPolyline(new PolylineOptions().add(new LatLng(endX, endY), subwayLatLngs[subwayLatLngs.length-1]).width(20).color(Color.MAGENTA));
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
                        mMap.addMarker(markerOptions);

                        if(j!=0)
                        {
                            mMap.addPolyline(new PolylineOptions().add(subwayLatLngs[j], subwayLatLngs[j-1]).width(5).color(Color.BLUE));
                        }
                        System.out.println(waysFinalNewSplit2[j][1]);
                        System.out.println(waysFinalNewSplit2[j][2]);

                    }

                    if(i==1)
                    {
                        mMap.addPolyline(new PolylineOptions().add(new LatLng(startX, startY), subwayLatLngs[0]).width(20).color(Color.MAGENTA));
                    }

                    if(i==waysSplit.length-2)
                    {
                        mMap.addPolyline(new PolylineOptions().add(new LatLng(endX, endY), subwayLatLngs[subwayLatLngs.length-1]).width(20).color(Color.MAGENTA));
                    }


                    break;

                case 3:


                    break;
            }


        }

        LatLng SEOUL = new LatLng(37.56, 126.97);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 13));
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
