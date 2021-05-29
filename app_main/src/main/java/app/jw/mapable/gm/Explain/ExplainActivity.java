package app.jw.mapable.gm.Explain;

import android.content.Intent;
import android.graphics.Point;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.Display;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import app.jw.mapable.gm.R;
import app.jw.mapable.gm.Start.StartActivity;

public class ExplainActivity extends AppCompatActivity{

    static String data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explain);

        WebView webView = findViewById(R.id.webView);


        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getRealSize(point);


        data = "<iframe src=\"https://docs.google.com/document/d/e/2PACX-1vT6gUBnl_Mg82Vu_dvhzoCXtRCVRmodeKtVgfkfWvL_RnPmGtlQSR3dgV-OtA6b153GoSr8fhkMiJpx/pub?embedded=true\" width=\"" + point.x + "\" height=\"" + point.y + "\" frameborder=0 framespacing=0 ></iframe>";
        webView.setWebViewClient(new SslWebViewConnect());
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        webView.getSettings().setMixedContentMode( WebSettings.MIXED_CONTENT_ALWAYS_ALLOW );
        webView.loadData(data, "text/html", null);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ExplainActivity.this, StartActivity.class);
        startActivity(intent);
        finish();
    }


    private static class SslWebViewConnect extends WebViewClient
    {
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if(url.equals(data)) view.loadUrl(url);
            return true;
        }
    }
}
