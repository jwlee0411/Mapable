package app.jw.mapable.gm.setting

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.Window
import app.jw.mapable.gm.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.dialog_user_setting_edit.*

class UserSettingEditDialog(val context : Context) : DialogInterface.OnDismissListener {

    fun callFunction(b : Boolean)
    {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_user_setting_edit)
        dialog.show()

        dialog.setOnDismissListener {

        }


        if(b)
        {
            dialog.textInfo.text = "사용자 이름 설정"

            dialog.editUserName.hint = "사용자 이름을 설정해주세요."

        }
        else
        {
            dialog.textInfo.text = "상태메시지 설정"

            dialog.editUserName.hint = "상태 메시지를 설정해주세요."
        }








        val sharedPreferences = context.getSharedPreferences("preferences", 0)
        val editor = sharedPreferences.edit()

        val uid = sharedPreferences.getString("uid", "")!!


        dialog.buttonOK.setOnClickListener {
            val resultString = dialog.editUserName.text.toString()

            val db = FirebaseFirestore.getInstance()
            val user: MutableMap<String, Any> = HashMap()


            if(b)//유저명 설정
            {

                sharedPreferences.edit().putString("userName", resultString).apply()

                user["userName"] = resultString
                db.collection("users").document(uid).update(user)
                    .addOnSuccessListener {
                        println("LOG : SUCCESS")
                    }
                    .addOnFailureListener { println("LOG : FAILED") }


                editor.putString("userName", resultString)
            }
            else //상태메시지 설정
            {
                user["userMessage"] = resultString

                sharedPreferences.edit().putString("userMessage", resultString).apply()

                db.collection("users").document(uid).update(user)
                    .addOnSuccessListener {
                        println("LOG : SUCCESS")
                    }
                    .addOnFailureListener { println("LOG : FAILED") }


                editor.putString("userMessage", resultString)
            }
            editor.apply()
            dialog.cancel()
        }



    }

    override fun onDismiss(dialog: DialogInterface?) {

    }


}