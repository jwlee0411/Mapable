package app.jw.mapable.gm.Splash

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import app.jw.mapable.gm.FirstSetting.FirstSettingActivity
import app.jw.mapable.gm.R
import app.jw.mapable.gm.Start.StartActivity
import kotlinx.android.synthetic.main.activity_after_search.*
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.android.synthetic.main.dialog_start.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        splashAnimation()

        val preferences : SharedPreferences = getSharedPreferences("preferences", 0)
        preferences.edit().putBoolean("start", false).apply()
        preferences.edit().putBoolean("end", false).apply()


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
}


