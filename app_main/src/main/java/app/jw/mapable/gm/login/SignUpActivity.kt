package app.jw.mapable.gm.login

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import app.jw.mapable.gm.R
import app.jw.mapable.gm.start.StartActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_signup.*
import java.util.regex.Pattern


class SignUpActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        sharedPreferences = getSharedPreferences("preferences", 0)
        editor = sharedPreferences.edit()

        setLogoAnim()
        
        buttonSignUp.setOnClickListener {
            val mAuth : FirebaseAuth = FirebaseAuth.getInstance()

            val userID = editSignUpID.text.toString()
            val userPW = editSignUpPW.text.toString()


            val emailPattern = android.util.Patterns.EMAIL_ADDRESS

            if(userID == "" || !(emailPattern.matcher(userID).matches()))
            {
                Toast.makeText(this, "올바른 이메일을 입력해주세요.", Toast.LENGTH_LONG).show()
            }
            else if(userPW == "" || !Pattern.matches("^[a-zA-Z0-9]*$", userPW)){
                Toast.makeText(this, "올바른 비밀번호를 입력해주세요. (특수문자 입력 불가능)", Toast.LENGTH_LONG).show()
            }
            else if(userPW.length < 8)
            {
                Toast.makeText(this, "비밀번호를 8자 이상으로 입력해주세요.", Toast.LENGTH_LONG).show()
            }
            else
            {
                mAuth.createUserWithEmailAndPassword(userID, userPW).addOnCompleteListener{
                    if(it.isSuccessful)
                    {
                        val db = FirebaseFirestore.getInstance()
                        val user: MutableMap<String, Any> = HashMap()
                        user["userID"] = userID
                        user["userPW"] = userPW
                        user["image"] = ""
                        user["message"] = ""
                        user["usertype"] = false
                        user["myPost"] = ArrayList<String>()
                        user["myStar"] = ArrayList<String>()
                        user["userName"] = ""
                        user["userMessage"] = ""


                        editor.putString("userID", userID)
                        editor.putString("userPW", userPW)
                        editor.putString("uid", mAuth.currentUser?.uid!!)
                        editor.putInt("loginType", 1)
                        editor.apply()


                        db.collection("users").document(mAuth.currentUser?.uid!!).set(user)
                            .addOnSuccessListener {
                                println("LOG : SUCCESS")
                            }
                            .addOnFailureListener { println("LOG : FAILED") }



                        Toast.makeText(this, "회원가입 및 로그인이 완료되었습니다!", Toast.LENGTH_LONG).show()

                        val intent = Intent(this, StartActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)

                        finish()
                    }
                    else
                    {
                        Toast.makeText(this, "회원가입에 실패하였습니다.", Toast.LENGTH_LONG).show()
                    }
                }
            }

        }

    }

    private fun setLogoAnim()
    {
        val animation = AnimationUtils.loadAnimation(applicationContext, R.anim.anim_rotate_one)
        animation.repeatCount = Animation.INFINITE
        imageViewLogoSignUp.startAnimation(animation)
    }
}


