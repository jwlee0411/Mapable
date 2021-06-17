package app.jw.mapable.gm.Explain

import android.content.Intent
import android.graphics.Point
import android.net.http.SslError
import android.os.Bundle
import android.webkit.SslErrorHandler
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import app.jw.mapable.gm.R
import app.jw.mapable.gm.Start.StartActivity

class ExplainActivity : AppCompatActivity() {

    lateinit var data : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_explain)
        val webView = findViewById<WebView>(R.id.webView)


        val display = windowManager.defaultDisplay
        val point = Point()
        display.getRealSize(point)

        data = "<iframe src=\"https://docs.google.com/document/d/e/2PACX-1vT6gUBnl_Mg82Vu_dvhzoCXtRCVRmodeKtVgfkfWvL_RnPmGtlQSR3dgV-OtA6b153GoSr8fhkMiJpx/pub?embedded=true\" width=\"" + point.x + "\" height=\"" + point.y + "\" frameborder=0 framespacing=0 ></iframe>"


        webView.webViewClient = SslWebViewConnect()
        webView.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
        webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        webView.loadData(data, "text/html", null)

    }


    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@ExplainActivity, StartActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)

    }


    inner class SslWebViewConnect : WebViewClient() {
        override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
            handler.proceed()
        }

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
         if (url == data) view.loadUrl(url)
            return false
        }
    }
}