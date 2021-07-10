package app.jw.mapable.gm.setting

import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import app.jw.mapable.gm.R
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_user_setting.*
import kotlinx.android.synthetic.main.fragment_community_my.view.*
import java.io.File

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
            if (e != null) {
                //FAILED
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                textUserName.text = snapshot.data!!["userName"] as CharSequence?
                textUserMessage.text = snapshot.data!!["userMessage"] as CharSequence?

            } else {
                //DATA = NULL
            }

        }

        textUserName.text = sharedPreferences.getString("userName", "이름을 설정해주세요!")
        textUserMessage.text = sharedPreferences.getString("userMessage", "상태메시지를 설정해주세요!")

        val userPhoto = "https://cdn.discordapp.com/attachments/729165233192566847/863209562287243264/app_logo_transparent.png"

        Glide.with(this).load(Uri.parse(userPhoto)).into(imageUserSetting)


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
            //TODO : 로그아웃
            Toast.makeText(this, "로그아웃 되었습니다.", Toast.LENGTH_LONG).show()
        }

        buttonQuit.setOnClickListener {
            val dialog = UserSettingDialog(this)
            dialog.callFunction()

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

//                val file = Uri.fromFile(File(uri))
//                val storageRef : StorageReference = storageRef.child("images/" + )

            }catch (e : Exception)
            {
                e.printStackTrace()
            }
        }

    }
}
