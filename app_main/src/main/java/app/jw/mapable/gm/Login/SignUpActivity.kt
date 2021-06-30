package app.jw.mapable.gm.Login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import app.jw.mapable.gm.R
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_signup.*

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        
        buttonSignUp.setOnClickListener {
            val mAuth : FirebaseAuth = FirebaseAuth.getInstance()

            mAuth.createUserWithEmailAndPassword(editSignupID.text.toString(), editSignUpPW.text.toString()).addOnCompleteListener{
                if(it.isSuccessful)
                {
                    println("SUCCESS")

                    finish()
                }
                else
                {
                    println("FAILED")
                }
            }
        }

    }
}


