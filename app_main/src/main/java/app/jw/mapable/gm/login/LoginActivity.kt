package app.jw.mapable.gm.login

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import app.jw.mapable.gm.R
import app.jw.mapable.gm.start.StartActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity(){

    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var task : Task<GoogleSignInAccount>
    private lateinit var firebaseAuth : FirebaseAuth

    lateinit var preferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    var userID = ""
    private var userPW = ""

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
            viewLogin.visibility = View.VISIBLE
            lottieViewLogin.visibility = View.VISIBLE

            val signInIntent : Intent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, 99)

        }


        buttonLogin.setOnClickListener {
            viewLogin.visibility = View.VISIBLE
            lottieViewLogin.visibility = View.VISIBLE

            userID = editLoginID.text.toString()
            userPW = editLoginPW.text.toString()



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



                        println("SUCCESS")
                    }
                    else
                    {
                        fadeOutAnimation()
                        Toast.makeText(applicationContext, "로그인에 실패하였습니다. 아이디와 비밀번호를 다시 한 번 확인해 주세요.", Toast.LENGTH_LONG).show()
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
                    loginSuccessGoogle(firebaseAuth.currentUser!!)
                }
                else
                {
                    loginFailed()
                }

            } else {
                loginFailed()
            }
        }
    }

    private fun loginSuccess(user : FirebaseUser)
    {

        fadeOutAnimation()
        val database = Firebase.firestore
        database.collection("users").document(firebaseAuth.currentUser?.uid!!).get().addOnSuccessListener {
            editor.putString("userName", it.getString("userName"))
            editor.putString("userMessage", it.getString("userMessage"))
            editor.putString("message", it.getString("message"))
            editor.apply()


            Toast.makeText(this, "${user.email} 으로 로그인 되었습니다!", Toast.LENGTH_LONG).show()

            val intent = Intent(this, StartActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()


        }


        editor.putString("userID", user.email)
        editor.putString("userUID", user.uid)

        editor.putString("userID", userID)
        editor.putString("userPW", userPW)

        editor.putInt("loginType", 1)
        editor.apply()





    }

    private fun loginSuccessGoogle(user : FirebaseUser)
    {
        val database = Firebase.firestore
        database.collection("users").document(firebaseAuth.currentUser?.uid!!).get().addOnSuccessListener {
            if(it.get("userID") != null)
            {
                fadeOutAnimation()
                editor.putString("userName", it.getString("userName"))
                editor.putString("userMessage", it.getString("userMessage"))
                editor.putString("message", it.getString("message"))
                editor.putString("userID", user.email.toString())
                editor.putString("userPW", "")
                editor.putString("uid", firebaseAuth.currentUser?.uid!!)
                editor.putInt("loginType", 2)
                editor.apply()
                println(firebaseAuth.currentUser?.uid)
                Toast.makeText(this, "${user.email} 으로 로그인 되었습니다!", Toast.LENGTH_LONG).show()
                val intent = Intent(this, StartActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                finish()


            }
            else
            {
                val db = FirebaseFirestore.getInstance()
                val users: MutableMap<String, Any> = HashMap()

                users["userID"] = user.email.toString()
                users["userPW"] = ""
                users["image"] = ""
                users["message"] = ""
                users["usertype"] = true
                users["myPost"] = ArrayList<String>()
                users["myStar"] = ArrayList<String>()
                users["userName"] = ""
                users["userMessage"] = ""


                editor.putString("userID", user.email.toString())
                editor.putString("userPW", "")
                editor.putString("uid", firebaseAuth.currentUser?.uid!!)
                editor.putInt("loginType", 2)
                editor.apply()

                db.collection("users").document(firebaseAuth.currentUser?.uid!!).set(users)
                    .addOnSuccessListener {
                        fadeOutAnimation()
                        Toast.makeText(this, "${user.email} 으로 로그인 되었습니다!", Toast.LENGTH_LONG).show()
                        val intent = Intent(this, StartActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                        finish()
                    }
                    .addOnFailureListener { println("LOG : FAILED") }



            }








        }

    }

    private fun loginFailed()
    {
        fadeOutAnimation()
        Toast.makeText(this, "로그인에 실패하였습니다.", Toast.LENGTH_LONG).show()
    }


    private fun fadeOutAnimation()
    {
        val animation = AnimationUtils.loadAnimation(this, R.anim.anim_fade_out)

        lottieViewLogin.startAnimation(animation)
        viewLogin.startAnimation(animation)

        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                lottieViewLogin.visibility = View.GONE
                viewLogin.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
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