package app.jw.mapable.gm.FirstSetting;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import app.jw.mapable.gm.R;
import app.jw.mapable.gm.Start.StartActivity;

public class FirstSettingEnabledActivity2 extends AppCompatActivity {

    public static Activity firstSettingEnabledActivity2;

    CheckBox[] checkBoxes;
    RadioGroup radioSearchWaySet;

    int SettingDefault;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    AlertDialog.Builder dlg;

    RadioButton[] radioButtons;

    Button button;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_setting_enabled_2);

        firstSettingEnabledActivity2 = FirstSettingEnabledActivity2.this;

        preferences = getSharedPreferences("preference", 0);
        editor = preferences.edit();

        findView();

        SettingDefault = getIntent().getIntExtra("defaultSetting", 4);

    }


    void findView()
    {
        button = findViewById(R.id.buttonSet);

        checkBoxes = new CheckBox[10];
        radioButtons = new RadioButton[5];

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


        setDefaultSetting();
        setonClick();
    }

    void setDefaultSetting()
    {
        switch (SettingDefault)
        {
            case 1: //휠체어


            case 2: //전동휠체어
                checkBoxes[1].setEnabled(true);
                checkBoxes[2].setEnabled(true);
                checkBoxes[3].setEnabled(true);
                checkBoxes[5].setEnabled(true);
                checkBoxes[6].setEnabled(true);
                checkBoxes[7].setEnabled(true);


                radioButtons[0].setChecked(true);

                checkBoxes[0].setChecked(true);
                checkBoxes[1].setChecked(true);

                checkBoxes[3].setChecked(true);

                checkBoxes[4].setChecked(true);
                checkBoxes[5].setChecked(true);
                checkBoxes[6].setChecked(true);
                checkBoxes[7].setChecked(true);

                break;


            case 3: //거동이 불편함
                checkBoxes[1].setEnabled(true);
                checkBoxes[2].setEnabled(true);
                checkBoxes[3].setEnabled(true);
                checkBoxes[5].setEnabled(true);
                checkBoxes[6].setEnabled(true);
                checkBoxes[7].setEnabled(true);


                radioButtons[3].setChecked(true);

                checkBoxes[0].setChecked(true);
                checkBoxes[1].setChecked(true);

                checkBoxes[3].setChecked(true);

                checkBoxes[4].setChecked(true);
                checkBoxes[5].setChecked(true);


                break;


            case 4: //설정 없음
                radioButtons[0].setChecked(true);
                break;
        }
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
                Toast.makeText(FirstSettingEnabledActivity2.this, "'버스 경로 보기' 를 선택하지 않고 '버스 우선 탐색' 을 선택할 수 없습니다.", Toast.LENGTH_LONG).show();
            }
            else if(!checkBoxes[4].isChecked() && radioSearchWaySet.getCheckedRadioButtonId()==R.id.radioSubway)
            {
                Toast.makeText(FirstSettingEnabledActivity2.this, "'지하철 경로 보기' 를 선택하지 않고 '지하철 우선 탐색' 을 선택할 수 없습니다.", Toast.LENGTH_LONG).show();
            }
            else if(!checkBoxes[0].isChecked() && !checkBoxes[4].isChecked())
            {
                Toast.makeText(FirstSettingEnabledActivity2.this, "버스와 지하철 중 적어도 하나의 옵션을 선택해 주세요.", Toast.LENGTH_LONG).show();
            }
            else if(checkBoxes[8].isChecked())
            {
                dlg = new AlertDialog.Builder(this);
                dlg.setTitle("시각장애인 모드로 설정하시겠어요?");
                dlg.setMessage("시각장애인이 아니라면, 해당 모드를 사용하지 않는 것을 추천합니다.");
                dlg.setPositiveButton("확인", (dialog, which) -> applySetting());
                dlg.setNegativeButton("아니오", (dialog, which) ->{});
                dlg.show();

            }
            else
            {
               applySetting();

            }

        });
    }

    void applySetting()
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

        editor.putBoolean("settingAvailable", true).apply();

        editor.apply();

        startActivity(new Intent(FirstSettingEnabledActivity2.this, StartActivity.class));
        startActivity(new Intent(this, StartActivity.class));
    }


}
