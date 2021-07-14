package app.jw.mapable.gm.community

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import app.jw.mapable.gm.R
import kotlinx.android.synthetic.main.activity_community_detail.*

class CommunityDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community_detail)

        //제목
        textView5.text = intent.getStringExtra("title")

        //타임스탬프
        textView6.text = intent.getStringExtra("posttime")

        //내용
        textView8.text = intent.getStringExtra("content")

        //닉네임
        textView9.text = intent.getStringExtra("username")

        buttonCommunityBack.setOnClickListener { finish() }

//        communityLike.setOnClickListener{
//
//        }
//
//        communityStar.setOnClickListener{
//
//        }
    }
}