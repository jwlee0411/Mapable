package app.jw.mapable.gm.FirstSetting

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import app.jw.mapable.gm.R
import kotlinx.android.synthetic.main.activity_first_setting_enabled_1.*

class FirstSettingEnabledActivity1 : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_setting_enabled_1)
        //TODO : 이게 맞는지 모르겠음
        firstSettingEnabledActivity1 = this@FirstSettingEnabledActivity1

        var intent = Intent(this, FirstSettingEnabledActivity2::class.java)

        buttonSetting1.setOnClickListener {
            intent.putExtra("defaultSetting", 1)
            startActivity(intent)
        }

        buttonSetting2.setOnClickListener {
            intent.putExtra("defaultSetting", 2)
            startActivity(intent)
        }

        buttonSetting3.setOnClickListener {
            intent.putExtra("defaultSetting", 3)
            startActivity(intent)
        }

        buttonSetting4.setOnClickListener {
            intent.putExtra("defaultSetting", 4)
            startActivity(intent)
        }
    }

    companion object {
        lateinit var firstSettingEnabledActivity1: Any
    }


}