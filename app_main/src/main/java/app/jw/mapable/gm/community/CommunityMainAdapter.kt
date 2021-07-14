package app.jw.mapable.gm.community

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.jw.mapable.gm.R
import kotlinx.android.synthetic.main.item_community_main.view.*

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

            itemView.textSummaryTitle.text = item.title
            var content = item.content

            var contentFull = item.content

            if(content.indexOf("\\n") != -1) content = content.substring(0, content.indexOf("\\n"))


            if(content.length > 25)
            {
                content = content.substring(0, 20)
                content = "$content..."
            }

            content = content.replace("\n", " ")

            itemView.textSummaryDescription.text = content

            itemView.textTime.text = item.posttime
            itemView.textNickname.text = " | ${item.username}"
            itemView.textView11.text = item.like.toString()

            contentFull = contentFull.replace("\\n", "\n")

            itemView.itemCommunityMain.setOnClickListener {
                val intent = Intent(context, CommunityDetailActivity::class.java)
                intent.putExtra("posttime", item.posttime)
                intent.putExtra("username", item.username)
                intent.putExtra("title", item.title)
                intent.putExtra("content", contentFull)

                itemView.context.startActivity(intent)


            }

        }
    }

}