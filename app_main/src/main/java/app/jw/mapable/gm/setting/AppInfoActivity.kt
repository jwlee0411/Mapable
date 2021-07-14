package app.jw.mapable.gm.setting

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import app.jw.mapable.gm.R

import kotlinx.android.synthetic.main.activity_app_info.*

import kotlin.concurrent.timer

class AppInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_info)

        var content = ""

        lottieView.visibility = View.VISIBLE
        view.visibility = View.VISIBLE

        val info = intent.getBooleanExtra("info", false)

        if(info)
        {
            content = "<iframe src=\"https://docs.google.com/document/d/e/2PACX-1vQHA5uC7yEZwQewyoLbcCQBxuvjPemDwEDGuAXEIua9PezSn24s9etB8UhgB8_z9_V9Y-_BonbUhIsj/pub?embedded=true\" width = \"1000px\" height = \"2000px\" frameborder = 0 framespacing = 0></iframe>"

        }
        else
        {
            content = "<iframe src=\"https://docs.google.com/document/d/e/2PACX-1vS6DLgQmcMqY-DLRACL73pXk3MSpJmj66k1-PmitbOXaEPj26i0eLeg5hewKpAhPspLYg6bkHwiqWbP/pub?embedded=true\" width = \"1000px\" height = \"2000px\" frameborder = 0 framespacing = 0></iframe>"

        }


        webView.loadData(content, "text/html; charset=utf-8", "UTF-8")
        webView.webViewClient = MyWebViewClient()

        val handler = object : Handler(){}

        handler.postDelayed({
            fadeOutAnimation()
        }, 3000)






    }


    private fun fadeOutAnimation()
    {
        val animation = AnimationUtils.loadAnimation(this, R.anim.anim_fade_out)

        lottieView.startAnimation(animation)
        view.startAnimation(animation)

        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                lottieView.visibility = View.GONE
                view.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
    }

    open inner class MyWebViewClient : WebViewClient() {

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            return super.shouldOverrideUrlLoading(view, request)
        }

    }
}