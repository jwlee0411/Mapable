package app.jw.mapable.gm.setting

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import app.jw.mapable.gm.R
import kotlinx.android.synthetic.main.activity_user_setting.*

class UserSettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_setting)



        buttonLogout.setOnClickListener {
            //TODO : 로그아웃
            Toast.makeText(this, "로그아웃 되었습니다.", Toast.LENGTH_LONG).show()
        }

        buttonQuit.setOnClickListener {
            val dialog = UserSettingDialog(this)
            dialog.callFunction()

        }




    }
}