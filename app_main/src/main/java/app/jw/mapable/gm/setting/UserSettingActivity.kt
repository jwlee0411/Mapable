package app.jw.mapable.gm.setting

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import app.jw.mapable.gm.R
import app.jw.mapable.gm.start.StartActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_user_setting.*
import kotlinx.android.synthetic.main.fragment_community_my.view.*

class UserSettingActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_setting)

        sharedPreferences = getSharedPreferences("preferences", 0)

        textEmail.text = sharedPreferences.getString("userID", "")

        val uid = sharedPreferences.getString("uid", "")!!
        val db = Firebase.firestore
        val docRef = db.collection("users").document(uid)
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null)
            {
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val username : String= snapshot.data!!["userName"] as String
                val userMessage : String = snapshot.data!!["userMessage"] as String



                if(username != "")textUserName.text = username
                else textUserName.text = "이름을 설정해주세요!"

                if(userMessage != "") textUserMessage.text = userMessage
                else textUserMessage.text = "상태메시지를 설정해주세요!"


            }

        }


        imageUserSetting.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, 0)
        }

        imageUserNameEdit.setOnClickListener {

            val dialog = UserSettingEditDialog(this)
            dialog.callFunction(true)

        }

        imageUserMessageEdit.setOnClickListener {
            val dialog = UserSettingEditDialog(this)
            dialog.callFunction(false)
        }



        buttonLogout.setOnClickListener {
            Toast.makeText(this, "로그아웃 되었습니다.", Toast.LENGTH_LONG).show()
            sharedPreferences.edit().remove("loginType").commit() //반드시 commit 으로!!


            val intent = Intent(this, StartActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()

        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0 && resultCode == RESULT_OK)
        {
            try{
                val inputStream = contentResolver.openInputStream(data!!.data!!)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream!!.close()

                imageUserSetting.setImageBitmap(bitmap)
                Toast.makeText(this, "이미지는 적용되지 않습니다.", Toast.LENGTH_LONG).show()

            }catch (e : Exception)
            {
                e.printStackTrace()
            }
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, StartActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }
}
