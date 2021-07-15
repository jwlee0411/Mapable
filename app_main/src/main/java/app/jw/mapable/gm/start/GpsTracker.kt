package app.jw.mapable.gm.start

import android.Manifest
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.IBinder
import androidx.core.content.ContextCompat


class GpsTracker(context: Context) : Service(), LocationListener{

    var mContext: Context = context


    private var location:Location? = null
    var locationManager : LocationManager? = null

    private val MIN_DISTANCE_CHANGE_FOR_UPDATES: Float = 10F
    private val MIN_TIME_BW_UPDATES = (1000 * 60 * 1).toLong()

    private var latitude : Double = 0.0
    private var longtitude : Double = 0.0


    init{
        getLocation()
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onLocationChanged(location: Location) {
        getLocation()
    }


    @JvmName("getLocation1")
    fun getLocation(): Location? {






        try {
            val locationManager = mContext.getSystemService(LOCATION_SERVICE) as LocationManager
            val isGPSEnabled: Boolean = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled: Boolean = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if (isGPSEnabled && isNetworkEnabled)
            {
                val hasFineLocationPermission = ContextCompat.checkSelfPermission(
                    mContext,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(
                    mContext,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                if (!(hasFineLocationPermission == PackageManager.PERMISSION_GRANTED && hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED)) return null
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES,
                        this
                    )
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)!!
                        if (location != null) {
                            latitude = location!!.latitude
                            longtitude = location!!.longitude
                        }
                    }
                }
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES,
                            this
                        )
                        if (locationManager != null) {
                            location =
                                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                            if (location != null) {
                                latitude = location!!.latitude
                                longtitude = location!!.longitude
                            }
                        }
                    }
                }
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }



        return location
    }


    @JvmName("getLatitude1")
    fun getLatitude() : Double{
        if(location != null)
        {
            latitude = location!!.latitude
        }
        return latitude
    }


    @JvmName("getLongtitude1")
    fun getLongtitude() : Double{
        if(location != null)
        {
            longtitude = location!!.longitude
        }
        return longtitude
    }



}