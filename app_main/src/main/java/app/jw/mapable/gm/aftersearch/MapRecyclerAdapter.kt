package app.jw.mapable.gm.aftersearch

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.jw.mapable.gm.R
import kotlinx.android.synthetic.main.item_recycler1.view.*
import kotlin.math.round

class MapRecyclerAdapter(private val context : Context) : RecyclerView.Adapter<MapRecyclerAdapter.ItemViewHolder>() {

    var datas = ArrayList<Item>()

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    override fun getItemCount(): Int = datas.size


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_recycler1, parent, false)
        return ItemViewHolder(view)
    }


    inner class ItemViewHolder(view : View) : RecyclerView.ViewHolder(view)
    {
        fun bind(item:Item)
        {

            val totalTime = item.totaltime + "분"
            val payment = "카드 " + item.payment1 + "원"
            val trafficdistance = "이동거리 " + round(item.trafficdistance.toDouble()/100)/10 + "km"
            val totalwalk = "도보 " + round(item.totalwalk.toDouble()/100)/10 + "km"

            itemView.textTotalTime.text = totalTime
            itemView.textPayment.text = payment
            itemView.textTrafficDistance.text = trafficdistance
            itemView.textTotalWalk.text = totalwalk




            val ways : ArrayList<ArrayList<String>> = item.ways1
            //TODO : 2번째 RecyclerView 만들기






        }
    }




}