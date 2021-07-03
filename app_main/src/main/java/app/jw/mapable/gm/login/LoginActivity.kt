package app.jw.mapable.gm.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import app.jw.mapable.gm.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val preferences = getSharedPreferences("preferences", 0)
        val editor = preferences.edit()



        buttonLogin.setOnClickListener {
            val mAuth : FirebaseAuth = FirebaseAuth.getInstance()

            val userID = editLoginID.text.toString()
            val userPW = editLoginPW.text.toString()


            mAuth.signInWithEmailAndPassword(userID, userPW).addOnCompleteListener {
                if(it.isSuccessful)
                {
                    editor.putString("userID", userID)
                    editor.putString("userPW", userPW)
                    editor.putBoolean("isLogin", true)
                    editor.apply()


                    println("SUCCESS")
                }
                else
                {
                    println("FAILED")
                }
            }

        }


        textViewSignUp.setOnClickListener{
            startActivity(Intent(this, SignUpActivity::class.java))
        }




    }
}