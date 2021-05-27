package app.jw.mapable.gm.Setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import app.jw.mapable.gm.Explain.ExplainActivity;
import app.jw.mapable.gm.R;
import app.jw.mapable.gm.Start.StartActivity;

public class SettingActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SettingActivity.this, StartActivity.class);
        startActivity(intent);
    }
}
