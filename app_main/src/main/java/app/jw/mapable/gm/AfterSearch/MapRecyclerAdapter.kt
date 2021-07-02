package app.jw.mapable.gm.AfterSearch

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.jw.mapable.gm.R
import com.odsay.odsayandroidsdk.ODsayService

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

        }
    }




}