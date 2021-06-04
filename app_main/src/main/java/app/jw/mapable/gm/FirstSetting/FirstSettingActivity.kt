package app.jw.mapable.gm.FirstSetting

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import app.jw.mapable.gm.R
import kotlinx.android.synthetic.main.activity_first_setting.*

class FirstSettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_setting)

        val handler = Handler()
        handler.postDelayed({
            val intent = Intent(this@FirstSettingActivity, FirstSettingDisabledActivity::class.java)
            startActivity(intent)
            finish()
        }, 10000)



        buttonSettingStart.setOnClickListener {
            handler.removeMessages(0)
            startActivity(Intent(this@FirstSettingActivity, FirstSettingEnabledActivity1::class.java))
            finish()
        }
    }
}