package app.jw.mapable.gm.AfterRoad

import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import app.jw.mapable.gm.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_start.*

class AfterRoadActivity : AppCompatActivity(), OnMapReadyCallback{

    var startX : Double = 0.0
    var startY : Double = 0.0
    var endX : Double = 0.0
    var endY : Double = 0.0

    lateinit var sharedPreferences: SharedPreferences

    lateinit var bitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_after_road)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)

        sharedPreferences = getSharedPreferences("preferences", 0)
        startX = sharedPreferences.getString("startX", "")!!.toDouble()
        startY = sharedPreferences.getString("startY", "")!!.toDouble()
        endX = sharedPreferences.getString("endX", "")!!.toDouble()
        endY = sharedPreferences.getString("endY", "")!!.toDouble()



        //TODO
        drawerLayout.setDrawerListener(object : DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
            override fun onDrawerOpened(drawerView: View) {}
            override fun onDrawerClosed(drawerView: View) {}
            override fun onDrawerStateChanged(newState: Int) {}
        })





    }

    override fun onMapReady(googleMap: GoogleMap) {
        val ways = intent.extras!!.getString("ways")
        println(ways)
        //   Toast.makeText(getApplicationContext(), ways, Toast.LENGTH_LONG).show();


        //   Toast.makeText(getApplicationContext(), ways, Toast.LENGTH_LONG).show();
        val waysSplit : Array<String> = ways!!.split("§".toRegex()).toTypedArray()
        val waysNewSplit : Array<Array<String?>> = Array(waysSplit.size) { arrayOfNulls<String>(14) }

        var subwayLatLngs: Array<LatLng?>


        val departBitmap = (resources.getDrawable(R.drawable.marker_start) as BitmapDrawable).bitmap
        val endBitmap = (resources.getDrawable(R.drawable.marker_end) as BitmapDrawable).bitmap


        val markerDepart = MarkerOptions()
        markerDepart.position(LatLng(startX, startY))
        markerDepart.title("출발")
        markerDepart.icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(departBitmap, 45, 64, false)))
        googleMap.addMarker(markerDepart)

        val markerEnd = MarkerOptions()
        markerEnd.position(LatLng(endX, endY))
        markerEnd.title("도착")
        markerEnd.icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(endBitmap, 45, 64, false)))
        googleMap.addMarker(markerEnd)


        for(i in waysSplit.indices) // i = 0 ~ waysSplit.size-1까지 for문 돌림
        {
            if(i==0 || i==waysSplit.size-1)
            {

            }
            else
            {
                waysNewSplit[i] = waysSplit[i].split("※".toRegex()).toTypedArray()

                val trafficType = waysNewSplit[i][0]!!.toInt()
                when(trafficType)
                {
                    1->{
                        val waysFinalSplit : Array<String> = waysNewSplit[i][12]!!.split("★".toRegex()).toTypedArray()
                        val waysFinalNewSplit: Array<Array<String>?> = arrayOfNulls(waysFinalSplit.size)

                        subwayLatLngs = arrayOfNulls(waysFinalSplit.size)
                        for (j in waysFinalSplit.indices) {
                            waysFinalNewSplit[j] = waysFinalSplit[j].split("☆".toRegex()).toTypedArray()
                            subwayLatLngs[j] = LatLng(waysFinalNewSplit[j]?.get(2)!!.toDouble(), waysFinalNewSplit[j]?.get(1)!!.toDouble())
                            val markerOptions = MarkerOptions()
                            markerOptions.position(subwayLatLngs[j])
                            markerOptions.title(waysFinalNewSplit[j]?.get(1)!!)
                            //markerOptions.snippet("한국의 수도");
                            markerOptions.snippet(waysNewSplit[i][4])
                            if (j != 0) {
                                val lineNumber = waysNewSplit[i][5]!!.toInt()
                                bitmap = when (lineNumber) {
                                    1 -> (resources.getDrawable(R.drawable.train_line1) as BitmapDrawable).bitmap
                                    2 -> (resources.getDrawable(R.drawable.train_line2) as BitmapDrawable).bitmap
                                    3 -> (resources.getDrawable(R.drawable.train_line3) as BitmapDrawable).bitmap
                                    4 -> (resources.getDrawable(R.drawable.train_line4) as BitmapDrawable).bitmap
                                    5 -> (resources.getDrawable(R.drawable.train_line5) as BitmapDrawable).bitmap
                                    6 -> (resources.getDrawable(R.drawable.train_line6) as BitmapDrawable).bitmap
                                    7 -> (resources.getDrawable(R.drawable.train_line7) as BitmapDrawable).bitmap
                                    8 -> (resources.getDrawable(R.drawable.train_line8) as BitmapDrawable).bitmap
                                    9 -> (resources.getDrawable(R.drawable.train_line9) as BitmapDrawable).bitmap
                                    101 -> (resources.getDrawable(R.drawable.train_lineairport) as BitmapDrawable).bitmap
                                    102 -> (resources.getDrawable(R.drawable.train_linemaglev) as BitmapDrawable).bitmap
                                    104 -> (resources.getDrawable(R.drawable.train_linegyeongui) as BitmapDrawable).bitmap
                                    107 -> (resources.getDrawable(R.drawable.train_lineyongin) as BitmapDrawable).bitmap
                                    108 -> (resources.getDrawable(R.drawable.train_linegyeongchun) as BitmapDrawable).bitmap
                                    109 -> (resources.getDrawable(R.drawable.train_lineshinbundang) as BitmapDrawable).bitmap
                                    110 -> (resources.getDrawable(R.drawable.train_lineuijeongbu) as BitmapDrawable).bitmap
                                    112 -> (resources.getDrawable(R.drawable.train_linegyeonggang) as BitmapDrawable).bitmap
                                    113 -> (resources.getDrawable(R.drawable.train_lineui) as BitmapDrawable).bitmap
                                    114 -> (resources.getDrawable(R.drawable.train_lineseohae) as BitmapDrawable).bitmap
                                    115 -> (resources.getDrawable(R.drawable.train_linegimpo) as BitmapDrawable).bitmap
                                    116 -> (resources.getDrawable(R.drawable.train_linebundang) as BitmapDrawable).bitmap
                                    21 -> (resources.getDrawable(R.drawable.train_lineincheon1) as BitmapDrawable).bitmap
                                    22 -> (resources.getDrawable(R.drawable.train_lineincheon2) as BitmapDrawable).bitmap
                                    else -> (resources.getDrawable(R.drawable.train_line1) as BitmapDrawable).bitmap
                                }
                                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(bitmap, 30, 30, false)))
                                googleMap.addMarker(markerOptions)
                                println("★$lineNumber")
                                when (lineNumber) {
                                    1 -> googleMap.addPolyline(PolylineOptions().add(subwayLatLngs[j], subwayLatLngs[j - 1]).width(10f).color(ContextCompat.getColor(this@AfterRoadActivity, R.color.line1)))
                                    2 -> googleMap.addPolyline(PolylineOptions().add(subwayLatLngs[j], subwayLatLngs[j - 1]).width(10f).color(ContextCompat.getColor(this@AfterRoadActivity, R.color.line2)))
                                    3 -> googleMap.addPolyline(PolylineOptions().add(subwayLatLngs[j], subwayLatLngs[j - 1]).width(10f).color(ContextCompat.getColor(this@AfterRoadActivity, R.color.line3)))
                                    4 -> googleMap.addPolyline(PolylineOptions().add(subwayLatLngs[j], subwayLatLngs[j - 1]).width(10f).color(ContextCompat.getColor(this@AfterRoadActivity, R.color.line4)))
                                    5 -> googleMap.addPolyline(PolylineOptions().add(subwayLatLngs[j], subwayLatLngs[j - 1]).width(10f).color(ContextCompat.getColor(this@AfterRoadActivity, R.color.line5)))
                                    6 -> googleMap.addPolyline(PolylineOptions().add(subwayLatLngs[j], subwayLatLngs[j - 1]).width(10f).color(ContextCompat.getColor(this@AfterRoadActivity, R.color.line6)))
                                    7 -> googleMap.addPolyline(PolylineOptions().add(subwayLatLngs[j], subwayLatLngs[j - 1]).width(10f).color(ContextCompat.getColor(this@AfterRoadActivity, R.color.line7)))
                                    8 -> googleMap.addPolyline(PolylineOptions().add(subwayLatLngs[j], subwayLatLngs[j - 1]).width(10f).color(ContextCompat.getColor(this@AfterRoadActivity, R.color.line8)))
                                    9 -> googleMap.addPolyline(PolylineOptions().add(subwayLatLngs[j], subwayLatLngs[j - 1]).width(10f).color(ContextCompat.getColor(this@AfterRoadActivity, R.color.line9)))
                                    101 -> googleMap.addPolyline(PolylineOptions().add(subwayLatLngs[j], subwayLatLngs[j - 1]).width(10f).color(ContextCompat.getColor(this@AfterRoadActivity, R.color.lineAirport)))
                                    102 -> googleMap.addPolyline(PolylineOptions().add(subwayLatLngs[j], subwayLatLngs[j - 1]).width(10f).color(ContextCompat.getColor(this@AfterRoadActivity, R.color.lineMaglev)))
                                    104 -> googleMap.addPolyline(PolylineOptions().add(subwayLatLngs[j], subwayLatLngs[j - 1]).width(10f).color(ContextCompat.getColor(this@AfterRoadActivity, R.color.lineGyeongui)))
                                    107 -> googleMap.addPolyline(PolylineOptions().add(subwayLatLngs[j], subwayLatLngs[j - 1]).width(10f).color(ContextCompat.getColor(this@AfterRoadActivity, R.color.lineYongin)))
                                    108 -> googleMap.addPolyline(PolylineOptions().add(subwayLatLngs[j], subwayLatLngs[j - 1]).width(10f).color(ContextCompat.getColor(this@AfterRoadActivity, R.color.lineGyeongchun)))
                                    109 -> googleMap.addPolyline(PolylineOptions().add(subwayLatLngs[j], subwayLatLngs[j - 1]).width(10f).color(ContextCompat.getColor(this@AfterRoadActivity, R.color.lineShinBundang)))
                                    110 -> googleMap.addPolyline(PolylineOptions().add(subwayLatLngs[j], subwayLatLngs[j - 1]).width(10f).color(ContextCompat.getColor(this@AfterRoadActivity, R.color.lineUijeongbu)))
                                    112 -> googleMap.addPolyline(PolylineOptions().add(subwayLatLngs[j], subwayLatLngs[j - 1]).width(10f).color(ContextCompat.getColor(this@AfterRoadActivity, R.color.lineGyeonggang)))
                                    113 -> googleMap.addPolyline(PolylineOptions().add(subwayLatLngs[j], subwayLatLngs[j - 1]).width(10f).color(ContextCompat.getColor(this@AfterRoadActivity, R.color.lineUi)))
                                    114 -> googleMap.addPolyline(PolylineOptions().add(subwayLatLngs[j], subwayLatLngs[j - 1]).width(10f).color(ContextCompat.getColor(this@AfterRoadActivity, R.color.lineSeohae)))
                                    115 -> googleMap.addPolyline(PolylineOptions().add(subwayLatLngs[j], subwayLatLngs[j - 1]).width(10f).color(ContextCompat.getColor(this@AfterRoadActivity, R.color.lineGimpo)))
                                    116 -> googleMap.addPolyline(PolylineOptions().add(subwayLatLngs[j], subwayLatLngs[j - 1]).width(10f).color(ContextCompat.getColor(this@AfterRoadActivity, R.color.lineBundang)))
                                    21 -> googleMap.addPolyline(PolylineOptions().add(subwayLatLngs[j], subwayLatLngs[j - 1]).width(10f).color(ContextCompat.getColor(this@AfterRoadActivity, R.color.lineIncheon1)))
                                    22 -> googleMap.addPolyline(PolylineOptions().add(subwayLatLngs[j], subwayLatLngs[j - 1]).width(10f).color(ContextCompat.getColor(this@AfterRoadActivity, R.color.lineIncheon2)))
                                    else -> googleMap.addPolyline(PolylineOptions().add(subwayLatLngs[j], subwayLatLngs[j - 1]).width(10f).color(ContextCompat.getColor(this@AfterRoadActivity, R.color.pink01)))
                                }
                            } else {
                                bitmap = (resources.getDrawable(R.drawable.marker_start_ride) as BitmapDrawable).bitmap
                                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(bitmap, 30, 30, false)))
                                googleMap.addMarker(markerOptions)
                            }
                        }

                        if (i == 1) {
                            googleMap.addPolyline(PolylineOptions().add(LatLng(startX, startY), subwayLatLngs[0]).width(10f).color(Color.MAGENTA))
                        }

                        if (i == waysSplit.size - 2) {
                            googleMap.addPolyline(PolylineOptions().add(LatLng(endX, endY), subwayLatLngs[subwayLatLngs.size - 1]).width(10f).color(Color.MAGENTA))
                        }
                    }

                    2->{

                        val waysFinalSplit2 = waysNewSplit[i][13]!!.split("★".toRegex()).toTypedArray()
                        val waysFinalNewSplit2: Array<Array<String>?> = arrayOfNulls(waysFinalSplit2.size)

                        subwayLatLngs = arrayOfNulls(waysFinalSplit2.size)

                        for (j in waysFinalSplit2.indices) {
                            waysFinalNewSplit2[j] = waysFinalSplit2[j].split("☆".toRegex()).toTypedArray()
                            subwayLatLngs[j] = LatLng(waysFinalNewSplit2[j]?.get(2)!!.toDouble(), waysFinalNewSplit2[j]?.get(1)!!.toDouble())
                            val markerOptions = MarkerOptions()
                            markerOptions.position(subwayLatLngs[j])
                            markerOptions.title(waysFinalNewSplit2[j]?.get(0)!!)
                            markerOptions.snippet(waysNewSplit[i][4])
                            if (j != 0) {
                                val lineNumber = waysNewSplit[i][5]!!.toInt()
                                when (lineNumber) {
                                    1 -> googleMap.addPolyline(PolylineOptions().add(subwayLatLngs[j], subwayLatLngs[j - 1]).width(10f).color(ContextCompat.getColor(this@AfterRoadActivity, R.color.gBus)))
                                    2 -> googleMap.addPolyline(PolylineOptions().add(subwayLatLngs[j], subwayLatLngs[j - 1]).width(10f).color(ContextCompat.getColor(this@AfterRoadActivity, R.color.jwaseokBus)))
                                    3, 12 -> googleMap.addPolyline(PolylineOptions().add(subwayLatLngs[j], subwayLatLngs[j - 1]).width(10f).color(ContextCompat.getColor(this@AfterRoadActivity, R.color.greenBus)))
                                    4, 15, 6, 14, 10 -> googleMap.addPolyline(PolylineOptions().add(subwayLatLngs[j], subwayLatLngs[j - 1]).width(10f).color(ContextCompat.getColor(this@AfterRoadActivity, R.color.redBus)))
                                    5 -> googleMap.addPolyline(PolylineOptions().add(subwayLatLngs[j], subwayLatLngs[j - 1]).width(10f).color(ContextCompat.getColor(this@AfterRoadActivity, R.color.airportBus)))
                                    11 -> googleMap.addPolyline(PolylineOptions().add(subwayLatLngs[j], subwayLatLngs[j - 1]).width(10f).color(ContextCompat.getColor(this@AfterRoadActivity, R.color.blueBus)))
                                    13 -> googleMap.addPolyline(PolylineOptions().add(subwayLatLngs[j], subwayLatLngs[j - 1]).width(10f).color(ContextCompat.getColor(this@AfterRoadActivity, R.color.yellowBus)))
                                    20, 26 -> googleMap.addPolyline(PolylineOptions().add(subwayLatLngs[j], subwayLatLngs[j - 1]).width(10f).color(ContextCompat.getColor(this@AfterRoadActivity, R.color.nongcheonBus)))
                                    21, 22 -> googleMap.addPolyline(PolylineOptions().add(subwayLatLngs[j], subwayLatLngs[j - 1]).width(10f).color(ContextCompat.getColor(this@AfterRoadActivity, R.color.siwaeBus)))
                                }
                                bitmap = when (lineNumber) {
                                    1 -> (resources.getDrawable(R.drawable.bus_gbus_map) as BitmapDrawable).bitmap
                                    2 -> (resources.getDrawable(R.drawable.bus_seat_map) as BitmapDrawable).bitmap
                                    3 -> (resources.getDrawable(R.drawable.bus_maeul_map) as BitmapDrawable).bitmap
                                    4 -> (resources.getDrawable(R.drawable.bus_jichaeng_map) as BitmapDrawable).bitmap
                                    5 -> (resources.getDrawable(R.drawable.bus_airport_map) as BitmapDrawable).bitmap
                                    11 -> (resources.getDrawable(R.drawable.bus_ganseon_map) as BitmapDrawable).bitmap
                                    12 -> (resources.getDrawable(R.drawable.bus_jiseon_map) as BitmapDrawable).bitmap
                                    13 -> (resources.getDrawable(R.drawable.bus_sunhwan_map) as BitmapDrawable).bitmap
                                    15 -> (resources.getDrawable(R.drawable.bus_geuphaeng_map) as BitmapDrawable).bitmap
                                    6, 14, 10 -> (resources.getDrawable(R.drawable.bus_gwangyeok_map) as BitmapDrawable).bitmap
                                    20, 26 -> (resources.getDrawable(R.drawable.bus_neongchon_map) as BitmapDrawable).bitmap
                                    21, 22 -> (resources.getDrawable(R.drawable.bus_siwae_map) as BitmapDrawable).bitmap
                                    else -> (resources.getDrawable(R.drawable.bus_gbus_map) as BitmapDrawable).bitmap
                                }
                                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(bitmap, 30, 30, false)))
                                googleMap.addMarker(markerOptions)
                            } else {
                                bitmap = (resources.getDrawable(R.drawable.marker_start_ride) as BitmapDrawable).bitmap
                                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(bitmap, 30, 30, false)))
                                googleMap.addMarker(markerOptions)
                            }
                            println(waysFinalNewSplit2[j]?.get(1))
                            println(waysFinalNewSplit2[j]?.get(2))
                        }

                        if (i == 1) {
                            googleMap.addPolyline(PolylineOptions().add(LatLng(startX, startY), subwayLatLngs[0]).width(20f).color(Color.MAGENTA))
                        }

                        if (i == waysSplit.size - 2) {
                            googleMap.addPolyline(PolylineOptions().add(LatLng(endX, endY), subwayLatLngs[subwayLatLngs.size - 1]).width(20f).color(Color.MAGENTA))
                        }

                    }

                    3->{

                    }
                }
            }
        }




        val startLatLng = LatLng(sharedPreferences.getFloat("startY", 0.0F).toDouble(), sharedPreferences.getFloat("startX", 0.0F).toDouble())

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLatLng, 13f))


        googleMap.setOnMarkerClickListener { marker: Marker? -> false }

        googleMap.setOnInfoWindowClickListener { marker: Marker ->
            val markerTitle = marker.title
            val markerLocation = marker.position
            val markerSnippet = marker.snippet
            val afterRoadDialog = AfterRoadDialog(this@AfterRoadActivity)

            //TODO
            afterRoadDialog.callFunction()
        }


    }

}