package app.jw.mapable.gm.FirstSetting

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_first_setting_enabled_1.*

class FirstSettingEnabledActivity1 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
}