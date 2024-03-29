package app.jw.mapable.gm.firstsetting

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import app.jw.mapable.gm.R
import app.jw.mapable.gm.setting.SettingActivity
import kotlinx.android.synthetic.main.activity_first_setting_enabled.*

class FirstSettingEnabledActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_setting_enabled)

        val intent = Intent(this, SettingActivity::class.java)

        lottieSetting1.setOnClickListener {
            intent.putExtra("defaultSetting", 1)
            startActivity(intent)
        }

        lottieSetting2.setOnClickListener {
            intent.putExtra("defaultSetting", 2)
            startActivity(intent)
        }

        lottieSetting3.setOnClickListener {
            intent.putExtra("defaultSetting", 3)
            startActivity(intent)
        }

        lottieSetting4.setOnClickListener {
            intent.putExtra("defaultSetting", 4)
            startActivity(intent)
        }
    }

    companion object {
        lateinit var firstSettingEnabledActivity1: Any
    }


}