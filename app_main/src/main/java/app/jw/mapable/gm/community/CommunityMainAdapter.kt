package app.jw.mapable.gm.community

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import app.jw.mapable.gm.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_user_setting.*
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
            itemView.textSummaryDescription.text = item.content

            itemView.textTime.text = item.posttime
            itemView.textNickname.text = " | " + item.username
            itemView.textView11.text = item.like.toString()

            //TODO : 이미지 적용된 후 사용하기(DB 확인!!)
            //Glide.with(itemView.context).load(Uri.parse(item.imageLink)).into(itemView.imageView7)

        }
    }

}