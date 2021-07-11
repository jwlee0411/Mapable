package app.jw.mapable.gm.star

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.jw.mapable.gm.R
import app.jw.mapable.gm.aftersearch.AfterSearchActivity
import app.jw.mapable.gm.start.StartActivity
import kotlinx.android.synthetic.main.activity_start.*
import kotlinx.android.synthetic.main.item_star.view.*

class StarRecyclerAdapter(private val context : Context) : RecyclerView.Adapter<StarRecyclerAdapter.ItemViewHolder>() {
    var datas = ArrayList<StarItem>()

    lateinit var sharedPreferences : SharedPreferences
    lateinit var editor : SharedPreferences.Editor

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_star, parent, false)
        sharedPreferences = context.getSharedPreferences("preferences", 0)
        editor = sharedPreferences.edit()
        return ItemViewHolder(view)

    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    inner class ItemViewHolder(view : View) : RecyclerView.ViewHolder(view){
        fun bind(item : StarItem)
        {
            //TODO : ㅇㅇ



            itemView.buttonDepart.setOnClickListener {
                val latitude = item.latitude
                val longitude = item.longitude

                val end = sharedPreferences.getBoolean("end", false)

                sharedPreferences.edit().putBoolean("start", true).apply()

                sharedPreferences.edit().putFloat("startX", latitude.toFloat()).putFloat("startY", longitude.toFloat()).apply()
                sharedPreferences.edit().putString("startNewX", latitude.toString()).putString("startNewY", longitude.toString()).apply()
                sharedPreferences.edit().putString("startLocation", item.locationName).apply()


                if(end)
                {
                    sharedPreferences.edit().putBoolean("start", false).apply()
                    sharedPreferences.edit().putBoolean("end", false).apply()
                    val intent = Intent(context, AfterSearchActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    context.startActivity(intent)
                }
                else
                {
                    val intent = Intent(context, StartActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    context.startActivity(intent)
                }

            }

            itemView.buttonArrive.setOnClickListener {
                val latitude = item.latitude
                val longitude = item.longitude

                val start = sharedPreferences.getBoolean("start", false)

                sharedPreferences.edit().putFloat("endX", latitude.toFloat()).putFloat("endY", longitude.toFloat()).apply()
                sharedPreferences.edit().putString("endNewX", latitude.toString()).putString("endNewY", longitude.toString()).apply()
                sharedPreferences.edit().putString("endLocation", item.locationName).apply()

                if(start)
                {

                }
                else
                {
                    val intent = Intent(context, StartActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    context.startActivity(intent)
                    //TODO : finish()
                }

            }

            itemView.textLocationTitle2.text = item.locationName

            itemView.textLocationDescription2.text

            itemView.textLocationDistance2.text = item.distance
        }
    }

}