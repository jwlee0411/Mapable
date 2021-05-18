package app.jw.mapable.gm.FirstSetting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import app.jw.mapable.gm.R;

public class FirstSettingEnabledActivity1 extends AppCompatActivity {

    public static Activity firstSettingEnabledActivity1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_setting_enabled_1);

        firstSettingEnabledActivity1 = FirstSettingEnabledActivity1.this;

        findViewById(R.id.buttonSetting1).setOnClickListener(v -> {
            Intent intent = new Intent(FirstSettingEnabledActivity1.this, FirstSettingEnabledActivity2.class);
            intent.putExtra("defaultSetting", 1);
            startActivity(intent);
        });

        findViewById(R.id.buttonSetting2).setOnClickListener(v -> {
            Intent intent = new Intent(FirstSettingEnabledActivity1.this, FirstSettingEnabledActivity2.class);
            intent.putExtra("defaultSetting", 2);
            startActivity(intent);
        });


        findViewById(R.id.buttonSetting3).setOnClickListener(v -> {
            Intent intent = new Intent(FirstSettingEnabledActivity1.this, FirstSettingEnabledActivity2.class);
            intent.putExtra("defaultSetting", 3);
            startActivity(intent);
        });


        findViewById(R.id.buttonSetting4).setOnClickListener(v -> {
            Intent intent = new Intent(FirstSettingEnabledActivity1.this, FirstSettingEnabledActivity2.class);
            intent.putExtra("defaultSetting", 4);
            startActivity(intent);
        });
    }
}
