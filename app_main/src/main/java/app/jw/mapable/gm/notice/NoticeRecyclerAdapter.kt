package app.jw.mapable.gm.notice

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.jw.mapable.gm.R
import kotlinx.android.synthetic.main.item_notice.view.*

class NoticeRecyclerAdapter(private val context : Context) : RecyclerView.Adapter<NoticeRecyclerAdapter.ItemViewHolder>() {
    var datas = ArrayList<NoticeItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeRecyclerAdapter.ItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_notice, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: NoticeRecyclerAdapter.ItemViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(item: NoticeItem) {
            println("LOG : ${item.title} ${item.timestamp}")

            itemView.layoutItemNotice.setOnClickListener {
                val intent = Intent(context, NoticeDetailActivity::class.java)
                intent.putExtra("id", item.ID)
                intent.putExtra("title", item.title)
                intent.putExtra("description", item.description)
                intent.putExtra("timestamp", item.timestamp)
                context.startActivity(intent)
            }
            itemView.textTitle.text = item.title
            itemView.textUserID.text = " | " + item.ID
            itemView.textTimestamp.text = item.timestamp

            //RecyclerView 관련 처리
            //ex) itemView.textView.text = item.trafficDistance
            //"itemView" 사용

        }
    }


}