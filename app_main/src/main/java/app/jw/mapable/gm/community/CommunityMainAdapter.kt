package app.jw.mapable.gm.community

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import app.jw.mapable.gm.R

class CommunityMainAdapter(private val context : Context) : RecyclerView.Adapter<CommunityMainAdapter.ItemViewHolder>() {

    var datas = ArrayList<ItemCommunityMain>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_community_main, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: CommunityMainAdapter.ItemViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(item: ItemCommunityMain) {

            //RecyclerView 관련 처리
            //ex) itemView.textView.text = item.trafficDistance
            //"itemView" 사용

        }
    }

}