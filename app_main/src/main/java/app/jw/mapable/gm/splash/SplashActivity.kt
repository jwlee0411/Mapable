package app.jw.mapable.gm.splash

import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Process
import android.provider.Settings
import android.speech.tts.TextToSpeech
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import app.jw.mapable.gm.firstsetting.FirstSettingActivity
import app.jw.mapable.gm.R
import app.jw.mapable.gm.start.StartActivity
import kotlinx.android.synthetic.main.activity_after_search.*
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.android.synthetic.main.dialog_start.*
import java.util.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val permission = arrayOf(
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION),
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
        )

        if(permission[0] == PackageManager.PERMISSION_DENIED || permission[1] == PackageManager.PERMISSION_DENIED)
        {
            val request_permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION)

            ActivityCompat.requestPermissions(this, request_permissions, 1001)


        }
        else
        {
            splashAnimation()
        }


        val preferences : SharedPreferences = getSharedPreferences("preferences", 0)
        preferences.edit().putBoolean("start", false).apply()
        preferences.edit().putBoolean("end", false).apply()


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
        {
            splashAnimation()
        }
        else
        {
            Toast.makeText(this, "권한 허용을 하지 않으면 어플리케이션을 사용할 수 없습니다. 권한 허용 후 다시 어플리케이션을 실행해주세요. ", Toast.LENGTH_LONG).show()
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri: Uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivityForResult(intent, 0)
            speakPermissionDenied()
            finish()
        }
    }





    fun splashAnimation()
    {

        val textAnim : Animation = AnimationUtils.loadAnimation(this, R.anim.anim_fade_in);
        textView3.startAnimation(textAnim)
        textView10.startAnimation(textAnim)
        val imageAnim = AnimationUtils.loadAnimation(this, R.anim.anim_rotate)
        imageView.startAnimation(imageAnim)
        val newImageAnim = AnimationUtils.loadAnimation(this, R.anim.anim_zoom_in)

        imageView5.startAnimation(newImageAnim)
        imageView4.startAnimation(newImageAnim)

        newImageAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                imageView4.visibility = View.GONE
                imageView5.visibility = View.GONE

                val handler = Handler()

                handler.postDelayed({
                    textView3.visibility = View.GONE
                    textView10.visibility = View.GONE
                    imageView.visibility = View.GONE
                    imageView2.visibility = View.GONE

                    imageView4.visibility = View.VISIBLE
                    imageView5.visibility = View.VISIBLE


                }, 2300)

                val handler2 = Handler()

                //TODO : 애니메이션 오류로 Zoom In 비활성화
                handler2.postDelayed({

                    val preferences : SharedPreferences = getSharedPreferences("preferences", 0)
                    if (preferences.getBoolean("settingAvailable", false)) {
                        val intent = Intent(this@SplashActivity, StartActivity::class.java)
                        startActivity(intent)
                        finish()
                        overridePendingTransition(R.anim.anim_none, R.anim.anim_zoom_in)
                    } else {
                        val intent = Intent(this@SplashActivity, FirstSettingActivity::class.java)
                        startActivity(intent)
                        finish()
                        overridePendingTransition(R.anim.anim_none, R.anim.anim_none)
                    }

                }, 2300)



            }

            override fun onAnimationEnd(animation: Animation) {

            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })

    }

    fun speakPermissionDenied() {
        val params = Bundle()



        lateinit var tts: TextToSpeech
        tts = TextToSpeech(this) { status: Int ->
            if (status == TextToSpeech.SUCCESS) {
                val result: Int = tts.setLanguage(Locale.KOREAN)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(this, "오류로 인해 TTS가 비활성화되었습니다.", Toast.LENGTH_LONG).show()
                } else {
                    tts.setPitch(1.0f)
                    tts.setSpeechRate(1.0f)
                    params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "")
//                    tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
//                        override fun onStart(utteranceId: String?) {
//
//                        }
//
//                        override fun onDone(utteranceId: String?) {
//                            Process.killProcess(Process.myPid())
//                        }
//
//                        override fun onError(utteranceId: String?) {
//
//                        }
//                    })
                    tts.speak(
                        "권한 허용을 하지 않으면 어플리케이션을 사용할 수 없습니다. 권한 허용 후 다시 어플리케이션을 실행해주세요.  ",
                        TextToSpeech.QUEUE_FLUSH,
                        params,
                        ""
                    )
                }
            }
        }
    }



    override fun onBackPressed() {
        super.onBackPressed()
        Process.killProcess(Process.myPid())
    }
}


