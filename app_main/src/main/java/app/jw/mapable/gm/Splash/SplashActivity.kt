package app.jw.mapable.gm.Splash

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import app.jw.mapable.gm.R
import kotlinx.android.synthetic.main.activity_after_search.*
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.android.synthetic.main.dialog_start.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)



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

                val handler : Handler = Handler()

                handler.postDelayed({
                    //TODO

                }, 2300)

                val handler2 : Handler = Handler()

                handler2.postDelayed({
                                     //TODO
                }, 3700)



            }

            override fun onAnimationEnd(animation: Animation) {

            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })

    }
}


