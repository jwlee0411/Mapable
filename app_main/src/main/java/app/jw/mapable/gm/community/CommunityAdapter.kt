package app.jw.mapable.gm.community

import android.content.Context
import android.content.Intent
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
            var description = itemCommunityMyMore.description
            var descriptionFull = itemCommunityMyMore.description

            if(description.indexOf("\\n") != -1) description = description.substring(0, description.indexOf("\\n"))


            if(description.length > 25)
            {
                description = description.substring(0, 20)
                description = "$description..."
            }

            description = description.replace("\\n", "\n")
            descriptionFull = descriptionFull.replace("\\n", "\n")

            itemView.textSummaryDescription.text = description
            itemView.textSummaryDate.text = itemCommunityMyMore.date

            itemView.itemCommunitySummary.setOnClickListener {
                val intent = Intent(context, CommunityDetailActivity::class.java)
                intent.putExtra("posttime", itemCommunityMyMore.date)
                intent.putExtra("title", itemCommunityMyMore.title)
                intent.putExtra("content", descriptionFull)

                itemView.context.startActivity(intent)


            }

        }
    }
}