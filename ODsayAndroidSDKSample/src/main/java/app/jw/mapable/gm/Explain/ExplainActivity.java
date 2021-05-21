package app.jw.mapable.gm.Explain;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import app.jw.mapable.gm.R;
import app.jw.mapable.gm.Start.StartActivity;

public class ExplainActivity extends AppCompatActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explain);

        WebView webView = findViewById(R.id.webView);
       // webView.getSettings().setJavaScriptEnabled(true);


        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getRealSize(point);


        String data = "<iframe src=\"https://docs.google.com/document/d/e/2PACX-1vT6gUBnl_Mg82Vu_dvhzoCXtRCVRmodeKtVgfkfWvL_RnPmGtlQSR3dgV-OtA6b153GoSr8fhkMiJpx/pub?embedded=true\" width=\"" + point.x + "\" height=\"" + point.y + "\" frameborder=0 framespacing=0 ></iframe>";
        webView.setWebViewClient(new SslWebViewConnect());
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        webView.loadData(data, "text/html", null);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ExplainActivity.this, StartActivity.class);
        startActivity(intent);
    }
}
