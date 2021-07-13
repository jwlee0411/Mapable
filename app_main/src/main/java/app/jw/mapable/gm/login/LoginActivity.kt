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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity(){

    lateinit var mGoogleApiClient : GoogleApiClient
    lateinit var googleSignInClient: GoogleSignInClient

    lateinit var task : Task<GoogleSignInAccount>
    lateinit var firebaseAuth : FirebaseAuth

    lateinit var preferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if(account != null){} //TODO : 이미 로그인되어 있는 경우 if문 실행
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        preferences = getSharedPreferences("preferences", 0)
        editor = preferences.edit()

        setLogoAnim()


        val gso : GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

        firebaseAuth = FirebaseAuth.getInstance()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        loginGoogle.setOnClickListener{
            val signInIntent : Intent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, 99)

        }


        buttonLogin.setOnClickListener {


            val userID = editLoginID.text.toString()
            val userPW = editLoginPW.text.toString()



            val emailPattern = android.util.Patterns.EMAIL_ADDRESS

            if(userID == "" ||  !(emailPattern.matcher(userID).matches()))
            {
                Toast.makeText(this, "올바른 이메일을 입력해주세요.", Toast.LENGTH_LONG).show()
            }
            else if(userPW == "" || !Pattern.matches("^[0-9a-zA-Z]*$", userPW)){
                Toast.makeText(this, "올바른 비밀번호를 입력해주세요. (특수문자 입력 불가능)", Toast.LENGTH_LONG).show()
            }
            else if(userPW.length < 8)
            {
                Toast.makeText(this, "비밀번호를 8자 이상으로 입력해주세요.", Toast.LENGTH_LONG).show()
            }
            else
            {
                firebaseAuth.signInWithEmailAndPassword(userID, userPW).addOnCompleteListener {
                    if(it.isSuccessful)
                    {
                        loginSuccess(firebaseAuth.currentUser!!)
                        editor.putString("userID", userID)
                        editor.putString("userPW", userPW)
                        editor.putInt("loginType", 2)
                        editor.apply()


                        println("SUCCESS")
                    }
                    else
                    {
                        println("FAILED")
                    }
                }
            }



        }


        textViewSignUp.setOnClickListener{
            startActivity(Intent(this, SignUpActivity::class.java))
        }




    }

    private fun setLogoAnim()
    {
        val animation = AnimationUtils.loadAnimation(applicationContext, R.anim.anim_rotate_one)
        animation.repeatCount = Animation.INFINITE
        imageViewLogoLogin.startAnimation(animation)
    }

    private fun firebaseAuthWithGoogle(account : GoogleSignInAccount)
    {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this){
            if (task.isSuccessful) {
                if(firebaseAuth.currentUser != null)
                {
                    loginSuccess(firebaseAuth.currentUser!!)
                }
                else
                {
                    loginFailed()
                }


                //TODO : 로그인 성공시 실행
            } else {
                loginFailed()
            }
        }
    }

    fun loginSuccess(user : FirebaseUser)
    {

        val db = FirebaseFirestore.getInstance()
        val users: MutableMap<String, Any> = HashMap()
        users["userID"] = user.email.toString()
        users["userPW"] = ""
        users["image"] = ""
        users["message"] = ""
        users["usertype"] = true

        db.collection("users").document(firebaseAuth.currentUser?.uid!!).set(user)
            .addOnSuccessListener {
                println("LOG : SUCCESS")
            }
            .addOnFailureListener { println("LOG : FAILED") }


        editor.putString("userID", user.email)
        editor.putString("userUID", user.uid)
        editor.putInt("loginType", 1)
        editor.apply()
        Toast.makeText(this, "$user.email 으로 로그인 되었습니다!", Toast.LENGTH_LONG).show()
        finish()
    }

    fun loginFailed()
    {
        Toast.makeText(this, "로그인에 실패하였습니다.", Toast.LENGTH_LONG).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 99)
        {
            task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            }
            catch(e : Exception)
            {
                e.printStackTrace()
            }
        }
    }
}