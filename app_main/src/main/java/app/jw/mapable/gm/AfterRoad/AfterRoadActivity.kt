package app.jw.mapable.gm.AfterRoad

import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import app.jw.mapable.gm.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class AfterRoadActivity : AppCompatActivity(), OnMapReadyCallback {

    lateinit var sharedPreferences: SharedPreferences
    var startX : Double = 0.0
    var startY : Double = 0.0
    var endX : Double = 0.0
    var endY : Double = 0.0




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_after_road)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this) //TODO : ??


        startX = sharedPreferences.getString("startX", "")?.toDouble()!!
        startY = sharedPreferences.getString("startY", "")?.toDouble()!!

        endX = sharedPreferences.getString("endX", "")?.toDouble()!!

        endY = sharedPreferences.getString("endY", "")?.toDouble()!!


    }

    override fun onMapReady(googleMap: GoogleMap) {

        val ways : String? = intent.extras?.getString("ways")

        //   Toast.makeText(getApplicationContext(), ways, Toast.LENGTH_LONG).show();
        val waysSplit = ways!!.split("§".toRegex()).toTypedArray()
        val waysNewSplit = Array(waysSplit.size) { arrayOfNulls<String>(14) }



        val departBitmap = ResourcesCompat.getDrawable(resources, R.drawable.marker_start, null)?.toBitmap() as Bitmap
        val endBitmap = ResourcesCompat.getDrawable(resources, R.drawable.marker_end, null)?.toBitmap() as Bitmap

        val markerDepart = MarkerOptions()

        markerDepart.position(LatLng(startX, startY))
        markerDepart.title("출발")
        markerDepart.icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(departBitmap, 45, 64, false)))
        googleMap.addMarker(markerDepart)


        val markerEnd = MarkerOptions()

        markerDepart.position(LatLng(endX, endY))
        markerDepart.title("도착")
        markerDepart.icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(endBitmap, 45, 64, false)))
        googleMap.addMarker(markerEnd)


        var i = 1

        for(i in 1 until waysSplit.size-1)
        {
            waysNewSplit[i] = waysSplit[i].split("※".toRegex()).toTypedArray()

            val trafficType : Int = Integer.parseInt(waysNewSplit[i][0]!!)


            when(trafficType)
            {
                1 ->{
                    val waysFinalSplit = waysNewSplit[i][12]!!.split("★".toRegex()).toTypedArray()
                    val waysFinalNewSplit : Array<Array<String>>





                }
            }




        }









    }
}