package app.jw.mapable.gm.AfterRoad

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import app.jw.mapable.gm.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions

class AfterRoadActivity : AppCompatActivity(), OnMapReadyCallback {

    var startX = 0.0f
    var startY = 0.0f
    var endX = 0.0f
    var endY = 0.0f

    private val lineWidth = 10f

    val preferences = getSharedPreferences("preferences", 0)!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_after_road)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)


        startX = preferences.getFloat("startX", 0.0f)
        startY = preferences.getFloat("startY", 0.0f)
        endX = preferences.getFloat("endX", 0.0f)
        endY = preferences.getFloat("endY", 0.0f)




    }

    override fun onMapReady(googleMap: GoogleMap) {
        val ways : String? = intent.extras!!.getString("ways")

        val waysSplit : ArrayList<String> = ways?.split("§") as ArrayList<String>
        val waysNewSplit = ArrayList<ArrayList<String>>()


        val departBitmap = (ResourcesCompat.getDrawable(resources, R.drawable.marker_start, null) as BitmapDrawable).bitmap
        val endBitmap = (ResourcesCompat.getDrawable(resources, R.drawable.marker_end, null) as BitmapDrawable).bitmap

        val markerDepart = MarkerOptions()
        markerDepart.position(LatLng(startX.toDouble(), startY.toDouble()))
        markerDepart.title("출발")
        markerDepart.icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(departBitmap, 45, 64, false)))
        googleMap.addMarker(markerDepart)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(startX.toDouble(), startY.toDouble()), 15f))

        val markerEnd = MarkerOptions()
        markerEnd.position(LatLng(endX.toDouble(), endY.toDouble()))
        markerEnd.title("도착")
        markerEnd.icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(endBitmap, 45, 64, false)))
        googleMap.addMarker(markerEnd)



        for(i in 1..waysSplit.size-2)
        {
            waysNewSplit.add(waysSplit[i].split("") as ArrayList<String>)
            val trafficType : Int = Integer.parseInt(waysNewSplit[i][0])

            if(trafficType != 3)
            {
                val waysFinalSplit = waysNewSplit[i][12].split("★") as ArrayList<String>
                val waysFinalNewSplit = ArrayList<ArrayList<String>>()

                lateinit var currentLatLng : LatLng
                lateinit var prevLatLng : LatLng

                for(j in 0 until waysFinalSplit.size)
                {
                    waysFinalNewSplit[j] = waysFinalSplit[j].split("☆") as ArrayList<String>
                    currentLatLng = LatLng(waysFinalNewSplit[j][2].toDouble(), waysFinalNewSplit[j][1].toDouble())

                    val markerOptions = MarkerOptions()
                    markerOptions.position(currentLatLng)
                    markerOptions.title(waysFinalNewSplit[j][0])
                    markerOptions.snippet(waysNewSplit[i][4])

                    val lineNumber = waysNewSplit[i][5].toInt()
                    lateinit var bitmap: Bitmap
                    lateinit var polylineOptions: PolylineOptions



                    if(j!=0 && trafficType==1)
                    {
                        when(lineNumber)
                        {
                            1 -> {
                                bitmap = (ResourcesCompat.getDrawable(resources, R.drawable.train_line1, null) as BitmapDrawable).bitmap
                                polylineOptions = PolylineOptions().add(currentLatLng, prevLatLng).width(lineWidth).color(ContextCompat.getColor(applicationContext, R.color.line1))
                            }
                            2 -> {
                                bitmap = (ResourcesCompat.getDrawable(resources, R.drawable.train_line2, null) as BitmapDrawable).bitmap
                                polylineOptions = PolylineOptions().add(currentLatLng, prevLatLng).width(lineWidth).color(ContextCompat.getColor(applicationContext, R.color.line2))
                            }
                            3 -> {
                                bitmap = (ResourcesCompat.getDrawable(resources, R.drawable.train_line3, null) as BitmapDrawable).bitmap
                                polylineOptions = PolylineOptions().add(currentLatLng, prevLatLng).width(lineWidth).color(ContextCompat.getColor(applicationContext, R.color.line3))
                            }
                            4 -> {
                                bitmap = (ResourcesCompat.getDrawable(resources, R.drawable.train_line4, null) as BitmapDrawable).bitmap
                                polylineOptions = PolylineOptions().add(currentLatLng, prevLatLng).width(lineWidth).color(ContextCompat.getColor(applicationContext, R.color.line4))
                            }
                            5 -> {
                                bitmap = (ResourcesCompat.getDrawable(resources, R.drawable.train_line5, null) as BitmapDrawable).bitmap
                                polylineOptions = PolylineOptions().add(currentLatLng, prevLatLng).width(lineWidth).color(ContextCompat.getColor(applicationContext, R.color.line5))
                            }
                            6 -> {
                                bitmap = (ResourcesCompat.getDrawable(resources, R.drawable.train_line6, null) as BitmapDrawable).bitmap
                                polylineOptions = PolylineOptions().add(currentLatLng, prevLatLng).width(lineWidth).color(ContextCompat.getColor(applicationContext, R.color.line6))
                            }
                            7 -> {
                                bitmap = (ResourcesCompat.getDrawable(resources, R.drawable.train_line7, null) as BitmapDrawable).bitmap
                                polylineOptions = PolylineOptions().add(currentLatLng, prevLatLng).width(lineWidth).color(ContextCompat.getColor(applicationContext, R.color.line7))
                            }
                            8 -> {
                                bitmap = (ResourcesCompat.getDrawable(resources, R.drawable.train_line8, null) as BitmapDrawable).bitmap
                                polylineOptions = PolylineOptions().add(currentLatLng, prevLatLng).width(lineWidth).color(ContextCompat.getColor(applicationContext, R.color.line8))
                            }
                            9 -> {
                                bitmap = (ResourcesCompat.getDrawable(resources, R.drawable.train_line9, null) as BitmapDrawable).bitmap
                                polylineOptions = PolylineOptions().add(currentLatLng, prevLatLng).width(lineWidth).color(ContextCompat.getColor(applicationContext, R.color.line9))
                            }


                            21 -> {
                                bitmap = (ResourcesCompat.getDrawable(resources, R.drawable.train_lineincheon1, null) as BitmapDrawable).bitmap
                                polylineOptions = PolylineOptions().add(currentLatLng, prevLatLng).width(lineWidth).color(ContextCompat.getColor(applicationContext, R.color.lineIncheon1))
                            }
                            22 -> {
                                bitmap = (ResourcesCompat.getDrawable(resources, R.drawable.train_lineincheon2, null) as BitmapDrawable).bitmap
                                polylineOptions = PolylineOptions().add(currentLatLng, prevLatLng).width(lineWidth).color(ContextCompat.getColor(applicationContext, R.color.lineIncheon2))
                            }

                            101 -> {
                                bitmap = (ResourcesCompat.getDrawable(resources, R.drawable.train_lineairport, null) as BitmapDrawable).bitmap
                                polylineOptions = PolylineOptions().add(currentLatLng, prevLatLng).width(lineWidth).color(ContextCompat.getColor(applicationContext, R.color.lineAirport))
                            }
                            102 -> {
                                bitmap = (ResourcesCompat.getDrawable(resources, R.drawable.train_linemaglev, null) as BitmapDrawable).bitmap
                                polylineOptions = PolylineOptions().add(currentLatLng, prevLatLng).width(lineWidth).color(ContextCompat.getColor(applicationContext, R.color.lineMaglev))
                            }
                            104 -> {
                                bitmap = (ResourcesCompat.getDrawable(resources, R.drawable.train_linegyeongui, null) as BitmapDrawable).bitmap
                                polylineOptions = PolylineOptions().add(currentLatLng, prevLatLng).width(lineWidth).color(ContextCompat.getColor(applicationContext, R.color.lineGyeongui))
                            }
                            107 -> {
                                bitmap = (ResourcesCompat.getDrawable(resources, R.drawable.train_lineyongin, null) as BitmapDrawable).bitmap
                                polylineOptions = PolylineOptions().add(currentLatLng, prevLatLng).width(lineWidth).color(ContextCompat.getColor(applicationContext, R.color.lineYongin))
                            }
                            108 -> {
                                bitmap = (ResourcesCompat.getDrawable(resources, R.drawable.train_linegyeongchun, null) as BitmapDrawable).bitmap
                                polylineOptions = PolylineOptions().add(currentLatLng, prevLatLng).width(lineWidth).color(ContextCompat.getColor(applicationContext, R.color.lineGyeongchun))
                            }
                            109 -> {
                                bitmap = (ResourcesCompat.getDrawable(resources, R.drawable.train_lineshinbundang, null) as BitmapDrawable).bitmap
                                polylineOptions = PolylineOptions().add(currentLatLng, prevLatLng).width(lineWidth).color(ContextCompat.getColor(applicationContext, R.color.lineShinBundang))
                            }

                            110 -> {
                                bitmap = (ResourcesCompat.getDrawable(resources, R.drawable.train_lineuijeongbu, null) as BitmapDrawable).bitmap
                                polylineOptions = PolylineOptions().add(currentLatLng, prevLatLng).width(lineWidth).color(ContextCompat.getColor(applicationContext, R.color.lineUijeongbu))
                            }
                            112 -> {
                                bitmap = (ResourcesCompat.getDrawable(resources, R.drawable.train_linegyeonggang, null) as BitmapDrawable).bitmap
                                polylineOptions = PolylineOptions().add(currentLatLng, prevLatLng).width(lineWidth).color(ContextCompat.getColor(applicationContext, R.color.lineGyeonggang))
                            }
                            113 -> {
                                bitmap = (ResourcesCompat.getDrawable(resources, R.drawable.train_lineui, null) as BitmapDrawable).bitmap
                                polylineOptions = PolylineOptions().add(currentLatLng, prevLatLng).width(lineWidth).color(ContextCompat.getColor(applicationContext, R.color.lineUi))
                            }
                            114 -> {
                                bitmap = (ResourcesCompat.getDrawable(resources, R.drawable.train_lineseohae, null) as BitmapDrawable).bitmap
                                polylineOptions = PolylineOptions().add(currentLatLng, prevLatLng).width(lineWidth).color(ContextCompat.getColor(applicationContext, R.color.lineSeohae))
                            }
                            115 -> {
                                bitmap = (ResourcesCompat.getDrawable(resources, R.drawable.train_linegimpo, null) as BitmapDrawable).bitmap
                                polylineOptions = PolylineOptions().add(currentLatLng, prevLatLng).width(lineWidth).color(ContextCompat.getColor(applicationContext, R.color.lineGimpo))
                            }
                            116 -> {
                                bitmap = (ResourcesCompat.getDrawable(resources, R.drawable.train_linebundang, null) as BitmapDrawable).bitmap
                                polylineOptions = PolylineOptions().add(currentLatLng, prevLatLng).width(lineWidth).color(ContextCompat.getColor(applicationContext, R.color.lineBundang))
                            }


                        }

                        googleMap.addPolyline(polylineOptions)
                    }
                    else if(j!=0 && trafficType == 2)
                    {

                        when(lineNumber) {
                            1 -> {
                                bitmap = (ResourcesCompat.getDrawable(resources, R.drawable.bus_gbus_map, null) as BitmapDrawable).bitmap
                                polylineOptions = PolylineOptions().add(currentLatLng, prevLatLng).width(lineWidth).color(ContextCompat.getColor(applicationContext, R.color.gBus))
                            }
                            2 -> {
                                bitmap = (ResourcesCompat.getDrawable(resources, R.drawable.bus_seat_map, null) as BitmapDrawable).bitmap
                                polylineOptions = PolylineOptions().add(currentLatLng, prevLatLng).width(lineWidth).color(ContextCompat.getColor(applicationContext, R.color.jwaseokBus))
                            }
                            3 -> {
                                bitmap = (ResourcesCompat.getDrawable(resources, R.drawable.bus_maeul_map, null) as BitmapDrawable).bitmap
                                polylineOptions = PolylineOptions().add(currentLatLng, prevLatLng).width(lineWidth).color(ContextCompat.getColor(applicationContext, R.color.greenBus))
                            }
                            4 -> {
                                bitmap = (ResourcesCompat.getDrawable(resources, R.drawable.bus_jichaeng_map, null) as BitmapDrawable).bitmap
                                polylineOptions = PolylineOptions().add(currentLatLng, prevLatLng).width(lineWidth).color(ContextCompat.getColor(applicationContext, R.color.redBus))
                            }
                            5 -> {
                                bitmap = (ResourcesCompat.getDrawable(resources, R.drawable.bus_airport_map, null) as BitmapDrawable).bitmap
                                polylineOptions = PolylineOptions().add(currentLatLng, prevLatLng).width(lineWidth).color(ContextCompat.getColor(applicationContext, R.color.airportBus))
                            }
                            6,10,14 -> {
                                bitmap = (ResourcesCompat.getDrawable(resources, R.drawable.bus_gwangyeok_map, null) as BitmapDrawable).bitmap
                                polylineOptions = PolylineOptions().add(currentLatLng, prevLatLng).width(lineWidth).color(ContextCompat.getColor(applicationContext, R.color.redBus))
                            }
                            11 -> {
                                bitmap = (ResourcesCompat.getDrawable(resources, R.drawable.bus_ganseon_map, null) as BitmapDrawable).bitmap
                                polylineOptions = PolylineOptions().add(currentLatLng, prevLatLng).width(lineWidth).color(ContextCompat.getColor(applicationContext, R.color.blueBus))
                            }
                            12 -> {
                                bitmap = (ResourcesCompat.getDrawable(resources, R.drawable.bus_jiseon_map, null) as BitmapDrawable).bitmap
                                polylineOptions = PolylineOptions().add(currentLatLng, prevLatLng).width(lineWidth).color(ContextCompat.getColor(applicationContext, R.color.greenBus))
                            }
                            13 -> {
                                bitmap = (ResourcesCompat.getDrawable(resources, R.drawable.bus_sunhwan_map, null) as BitmapDrawable).bitmap
                                polylineOptions = PolylineOptions().add(currentLatLng, prevLatLng).width(lineWidth).color(ContextCompat.getColor(applicationContext, R.color.yellowBus))
                            }
                            15 -> {
                                bitmap = (ResourcesCompat.getDrawable(resources, R.drawable.bus_geuphaeng_map, null) as BitmapDrawable).bitmap
                                polylineOptions = PolylineOptions().add(currentLatLng, prevLatLng).width(lineWidth).color(ContextCompat.getColor(applicationContext, R.color.redBus))
                            }
                            20,26 -> {
                                bitmap = (ResourcesCompat.getDrawable(resources, R.drawable.bus_nongcheon_map, null) as BitmapDrawable).bitmap
                                polylineOptions = PolylineOptions().add(currentLatLng, prevLatLng).width(lineWidth).color(ContextCompat.getColor(applicationContext, R.color.nongcheonBus))
                            }
                            21,22 -> {
                                bitmap = (ResourcesCompat.getDrawable(resources, R.drawable.bus_siwae_map, null) as BitmapDrawable).bitmap
                                polylineOptions = PolylineOptions().add(currentLatLng, prevLatLng).width(lineWidth).color(ContextCompat.getColor(applicationContext, R.color.siwaeBus))
                            }

                        }
                        googleMap.addPolyline(polylineOptions)
                    }
                    else
                    {
                        bitmap = (resources.getDrawable(R.drawable.marker_start_ride) as BitmapDrawable).bitmap
                    }

                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(bitmap, 30, 30, false)))
                    googleMap.addMarker(markerOptions)








                }


//                if (i == 1) {
//                    googleMap.addPolyline(
//                        PolylineOptions().add(
//                            LatLng(startX.toDouble(), startY.toDouble()),
//                            subwayLatLngs.get(0)
//                        ).width(20f).color(
//                            Color.MAGENTA
//                        )
//                    )
//                }
//
//                if (i == waysSplit.size - 2) { googleMap.addPolyline(PolylineOptions().add(LatLng(endX.toDouble(), endY.toDouble()), subwayLatLngs.get(subwayLatLngs.size - 1)).width(20f).color(Color.MAGENTA))
                }



            }

        }



}
