package app.jw.mapable.gm.community

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import app.jw.mapable.gm.R
import kotlinx.android.synthetic.main.activity_community_my_more.*


class CommunityMyMoreActivity : AppCompatActivity() {

    lateinit var recyclerAdapter : CommunityMyMoreAdapter
    val datas = ArrayList<ItemCommunityMyMore>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community_my_more)

        val title = intent.getStringExtra("click")
        textViewTitle.text = title

        recyclerAdapter = CommunityMyMoreAdapter(this)
        recyclerCommunityMyMore.adapter = recyclerAdapter

        datas.apply {
            add(ItemCommunityMyMore("제목1", "내용1", "사진1"))
            add(ItemCommunityMyMore("제목2", "내용2", "사진2"))
            add(ItemCommunityMyMore("제목3", "내용3", "사진3"))

            recyclerAdapter.datas = datas
            recyclerAdapter.notifyDataSetChanged()
        }





    }
}