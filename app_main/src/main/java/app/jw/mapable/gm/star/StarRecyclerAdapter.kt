package app.jw.mapable.gm.star

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.jw.mapable.gm.R

class StarRecyclerAdapter(private val context : Context) : RecyclerView.Adapter<StarRecyclerAdapter.ItemViewHolder>() {
    var datas = ArrayList<StarItem>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {

            val view = LayoutInflater.from(context).inflate(R.layout.item_star, parent, false)
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

        }
    }

}