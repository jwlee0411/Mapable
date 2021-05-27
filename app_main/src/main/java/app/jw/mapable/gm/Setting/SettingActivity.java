package app.jw.mapable.gm.Setting;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import app.jw.mapable.gm.Explain.ExplainActivity;
import app.jw.mapable.gm.FirstSetting.FirstSettingEnabledActivity2;
import app.jw.mapable.gm.R;
import app.jw.mapable.gm.Start.StartActivity;

public class SettingActivity extends AppCompatActivity {


    Button button;
    CheckBox[] checkBoxes = new CheckBox[10];
    RadioButton[] radioButtons = new RadioButton[5];
    RadioGroup radioSearchWaySet;

    Boolean[] settings = new Boolean[10];
    int searchWay;


    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        preferences = getSharedPreferences("preference", 0);
        editor = preferences.edit();
        
        findView();
    }

    void findView()
    {
        button = findViewById(R.id.buttonSet);

        checkBoxes[0] = findViewById(R.id.checkBus);
        checkBoxes[1] = findViewById(R.id.checkBusLow);
        checkBoxes[2] = findViewById(R.id.checkBusWait30);
        checkBoxes[3] = findViewById(R.id.checkBusWait60);
        checkBoxes[4] = findViewById(R.id.checkSubway);
        checkBoxes[5] = findViewById(R.id.checkSubwayElevator);
        checkBoxes[6] = findViewById(R.id.checkSubwayWheelchairStation);
        checkBoxes[7] = findViewById(R.id.checkSubwayWheelchairOn);
        checkBoxes[8] = findViewById(R.id.checkDisabled);
        checkBoxes[9] = findViewById(R.id.checkNoInfo);

        radioSearchWaySet = findViewById(R.id.radioSearchWayCheck);

        radioButtons[0] = findViewById(R.id.radioBest);
        radioButtons[1] = findViewById(R.id.radioBus);
        radioButtons[2] = findViewById(R.id.radioSubway);
        radioButtons[3] = findViewById(R.id.radioWalk);

        getPreviousSettings();

        setonClick();
    }

    void getPreviousSettings()
    {
        settings[0] = preferences.getBoolean("busRoadFound", false);
        settings[1] = preferences.getBoolean("busLowOnly", false);
        settings[2] = preferences.getBoolean("busWait30", false);
        settings[3] = preferences.getBoolean("busWait60", false);
        settings[4] = preferences.getBoolean("subwayRoadFound", false);
        settings[5] = preferences.getBoolean("subwayElevator", false);
        settings[6] = preferences.getBoolean("subwayWheelchairStation", false);
        settings[7] = preferences.getBoolean("subwayWheelchairOn", false);

        settings[8] = preferences.getBoolean("disabled", false);
        settings[9] = preferences.getBoolean("noInfo", false);

        for(int i = 0; i<10; i++)
        {
            checkBoxes[i].setChecked(settings[i]);
        }

        searchWay = preferences.getInt("searchWay", 0);
        radioButtons[searchWay].setChecked(true);

    }

    void setonClick()
    {
        checkBoxes[0].setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked)
            {
                checkBoxes[1].setEnabled(true);
                checkBoxes[2].setEnabled(true);
                checkBoxes[3].setEnabled(true);
            }
            else
            {
                checkBoxes[1].setEnabled(false);
                checkBoxes[2].setEnabled(false);
                checkBoxes[3].setEnabled(false);
            }
        });


        checkBoxes[4].setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked)
            {
                checkBoxes[5].setEnabled(true);
                checkBoxes[6].setEnabled(true);
                checkBoxes[7].setEnabled(true);
            }
            else
            {
                checkBoxes[5].setEnabled(false);
                checkBoxes[6].setEnabled(false);
                checkBoxes[7].setEnabled(false);
            }
        });




        button.setOnClickListener(v -> {
            if(!checkBoxes[0].isChecked() && radioSearchWaySet.getCheckedRadioButtonId()==R.id.radioBus)
            {
                Toast.makeText(this, "'버스 경로 보기' 를 선택하지 않고 '버스 우선 탐색' 을 선택할 수 없습니다.", Toast.LENGTH_LONG).show();
            }
            else if(!checkBoxes[4].isChecked() && radioSearchWaySet.getCheckedRadioButtonId()==R.id.radioSubway)
            {
                Toast.makeText(this, "'지하철 경로 보기' 를 선택하지 않고 '지하철 우선 탐색' 을 선택할 수 없습니다.", Toast.LENGTH_LONG).show();
            }
            else if(!checkBoxes[0].isChecked() && !checkBoxes[4].isChecked())
            {
                Toast.makeText(this, "버스와 지하철 중 적어도 하나의 옵션을 선택해 주세요.", Toast.LENGTH_LONG).show();
            }
            else if(checkBoxes[8].isChecked())
            {
                //TODO : 정말 이 모드로 설정할 것인지 다시 한 번 확인하는 Dialog 띄우기
            }
            else
            {
                editor.putBoolean("busRoadFound", checkBoxes[0].isChecked());
                editor.putBoolean("busLowOnly", checkBoxes[1].isChecked());
                editor.putBoolean("busWait30", checkBoxes[2].isChecked());
                editor.putBoolean("busWait60", checkBoxes[3].isChecked());
                editor.putBoolean("subwayRoadFound", checkBoxes[4].isChecked());
                editor.putBoolean("subwayElevator", checkBoxes[5].isChecked());
                editor.putBoolean("subwayWheelchairStation", checkBoxes[6].isChecked());
                editor.putBoolean("subwayWheelchairOn", checkBoxes[7].isChecked());
                editor.putBoolean("disabled", checkBoxes[8].isChecked());
                editor.putBoolean("noInfo", checkBoxes[9].isChecked());


                switch (radioSearchWaySet.getCheckedRadioButtonId())
                {
                    case R.id.radioBest : editor.putInt("searchWay", 0); break;
                    case R.id.radioBus : editor.putInt("searchWay", 1); break;
                    case R.id.radioSubway:  editor.putInt("searchWay", 2); break;
                    case R.id.radioWalk:  editor.putInt("searchWay", 3); break;
                }

                editor.apply();
                startActivity(new Intent(this, StartActivity.class));
            }

        });
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SettingActivity.this, StartActivity.class);
        startActivity(intent);
    }



}
