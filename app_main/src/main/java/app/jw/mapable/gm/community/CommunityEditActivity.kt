package app.jw.mapable.gm.community

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import app.jw.mapable.gm.R
import kotlinx.android.synthetic.main.activity_community_edit.*

class CommunityEditActivity : AppCompatActivity() {

    lateinit var sharedPreferences : SharedPreferences
    lateinit var editor : SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community_edit)

        sharedPreferences = getSharedPreferences("preferences", 0)
        editor = sharedPreferences.edit()

        editTitle

        editContent

        setonClick()
    }


    private fun setonClick(){
        communityEditBack.setOnClickListener {
            finish()
        }

        buttonEnroll.setOnClickListener {

        }


    }
}