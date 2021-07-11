package app.jw.mapable.gm.star

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import app.jw.mapable.gm.R
import app.jw.mapable.gm.start.GpsTracker
import kotlinx.android.synthetic.main.activity_star.*

class StarActivity : AppCompatActivity() {

    lateinit var recyclerAdapter : StarRecyclerAdapter

    val datas = ArrayList<StarItem>()

    lateinit var sharedPreferences : SharedPreferences
    lateinit var editor : SharedPreferences.Editor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_star)

        val gpsTracker = GpsTracker(this)
        val latitude : Double = gpsTracker.getLatitude()
        val longitude : Double = gpsTracker.getLongtitude()

        sharedPreferences = getSharedPreferences("preferences", 0)
        editor = sharedPreferences.edit()



        recyclerAdapter = StarRecyclerAdapter(this)
        recyclerStar.adapter = recyclerAdapter
        recyclerStar.layoutManager = LinearLayoutManager(this)
        recyclerStar.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        val data : ArrayList<String> = sharedPreferences.getString("bookmark", "")!!.split("★") as ArrayList<String>

        for (value in data)
        {
            val newData = value.split("※") as ArrayList<String>
            val locationName = newData[0]
            val latitude = newData[1].toDouble()
            val longitude = newData[2].toDouble()

            datas.add(StarItem(locationName, latitude, longitude, "0.0km"))



        }






        datas.apply {
            recyclerAdapter.datas = datas
            recyclerAdapter.notifyDataSetChanged()
        }

    }
}