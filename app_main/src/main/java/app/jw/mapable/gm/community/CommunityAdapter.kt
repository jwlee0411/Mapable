package app.jw.mapable.gm.community

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.jw.mapable.gm.R
import kotlinx.android.synthetic.main.item_community_summary.view.*

class CommunityAdapter(private val context : Context) :
    RecyclerView.Adapter<CommunityAdapter.ItemViewHolder>() {


    var datas = ArrayList<ItemCommunityMyMore>()


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_community_summary, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: CommunityAdapter.ItemViewHolder,
        position: Int
    ) {
        holder.bind(datas[position])
    }

    override fun getItemCount(): Int = datas.size

    inner class ItemViewHolder(view : View) : RecyclerView.ViewHolder(view){
        fun bind(itemCommunityMyMore: ItemCommunityMyMore)
        {
            println("LOG : ${itemCommunityMyMore.title}")
            itemView.textSummaryTitle.text = itemCommunityMyMore.title
            itemView.textSummaryDescription.text = itemCommunityMyMore.description
            itemView.textSummaryDate.text = itemCommunityMyMore.date

        }
    }
}