package app.jw.mapable.gm.FirstSetting;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import app.jw.mapable.gm.R;

public class FirstSettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_setting);

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent = new Intent(FirstSettingActivity.this, FirstSettingDisabledActivity.class);
            startActivity(intent);
            finish();
        }, 1000);

        Button button = findViewById(R.id.buttonSettingStart);
        button.setOnClickListener(v -> {
            handler.removeMessages(0);
            startActivity(new Intent(FirstSettingActivity.this, FirstSettingEnabledActivity1.class));
            finish();
        });


    }

}
