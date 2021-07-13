package app.jw.mapable.gm.notice

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import app.jw.mapable.gm.R
import kotlinx.android.synthetic.main.activity_notice_detail.*

class NoticeDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notice_detail)


        val extra = intent.extras!!
        val title = extra.getString("title")
        val description = extra.getString("description")
        val timestamp = extra.getString("timestamp")
        val id = extra.getString("id")

        textView5.text = title

        textView6.text = timestamp

        textView8.text = description!!.replace("\\n", System.getProperty("line.separator")!!)

        textView9.text = id


        buttonBack.setOnClickListener {
            finish()
        }










    }
}