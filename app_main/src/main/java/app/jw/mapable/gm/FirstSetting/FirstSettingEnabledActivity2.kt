package app.jw.mapable.gm.FirstSetting

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import app.jw.mapable.gm.R
import app.jw.mapable.gm.Start.StartActivity
import kotlinx.android.synthetic.main.activity_setting.*

class FirstSettingEnabledActivity2 : AppCompatActivity(){


    lateinit var preferences : SharedPreferences
    lateinit var editor : SharedPreferences.Editor

    var SettingDefault : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_setting_enabled_2)

        firstSettingEnabledActivity2 = this

        preferences = getSharedPreferences("preferences", 0)
        editor = preferences.edit()


        SettingDefault = intent.getIntExtra("defaultSetting", 4)

        setDefaultSetting()
        setonClick()

    }


    fun setDefaultSetting()
    {
        when(SettingDefault)
        {
            1, 2 -> {
                checkBusLow.isEnabled = true
                checkBusWait30.isEnabled = true
                checkBusWait60.isEnabled = true

                checkSubwayElevator.isEnabled = true
                checkSubwayWheelchairStation.isEnabled = true
                checkSubwayWheelchairOn.isEnabled = true

                radioBest.isChecked = true

                checkBus.isChecked = true
                checkBusLow.isChecked = true
                checkBusWait60.isChecked = true

                checkSubway.isChecked = true
                checkSubwayElevator.isChecked = true
                checkSubwayWheelchairStation.isChecked = true
                checkSubwayWheelchairOn.isChecked = true

            }

            3 ->{
                checkBusLow.isEnabled = true
                checkBusWait30.isEnabled = true
                checkBusWait60.isEnabled = true

                checkSubwayElevator.isEnabled = true
                checkSubwayWheelchairStation.isEnabled = true
                checkSubwayWheelchairOn.isEnabled = true

                radioWalk.isEnabled = true

                checkBus.isChecked = true
                checkBusLow.isChecked = true
                checkBusWait60.isChecked = true

                checkSubway.isChecked = true
                checkSubwayElevator.isChecked = true

            }

            4 -> {
                radioBest.isChecked = true
            }


        }
    }

    fun setonClick()
    {
        checkBus.setOnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean ->

            if(isChecked)
            {
                checkBusLow.isEnabled = true
                checkBusWait30.isEnabled = true
                checkBusWait60.isEnabled = true
            }
            else
            {
                checkBusLow.isEnabled = false
                checkBusWait30.isEnabled = false
                checkBusWait60.isEnabled = false
            }



        }

        checkSubway.setOnCheckedChangeListener{ _, isChecked->

            if(isChecked)
            {
                checkSubwayElevator.isEnabled = true
                checkSubwayWheelchairStation.isEnabled = true
                checkSubwayWheelchairOn.isEnabled = true
            }
            else
            {
                checkSubwayElevator.isEnabled = false
                checkSubwayWheelchairStation.isEnabled = false
                checkSubwayWheelchairOn.isEnabled = false
            }


        }

        buttonSet.setOnClickListener {
            if (!checkBus.isChecked&&radioSearchWayCheck.checkedRadioButtonId == R.id.radioBus)
            {
                Toast.makeText(this, "'버스 경로 보기' 를 선택하지 않고 '버스 우선 탐색' 을 선택할 수 없습니다.", Toast.LENGTH_LONG).show()
            }
            else if(!checkSubway.isChecked && radioSearchWayCheck.checkedRadioButtonId == R.id.radioSubway)
            {
                Toast.makeText(this, "'지하철 경로 보기' 를 선택하지 않고 '지하철 우선 탐색' 을 선택할 수 없습니다.", Toast.LENGTH_LONG).show()
            }
            else if(!checkBus.isChecked && !checkSubway.isChecked)
            {
                Toast.makeText(this, "버스와 지하철 중 적어도 하나의 옵션을 선택해 주세요.", Toast.LENGTH_LONG).show()
            }
            else if(checkDisabled.isChecked)
            {
                val dlg = AlertDialog.Builder(this)
                dlg.setTitle("시각장애인 모드로 설정하시겠어요?")
                dlg.setMessage("시각장애인이 아니라면, 해당 모드를 사용하지 않는 것을 추천합니다.")
                dlg.setPositiveButton("확인") { _, _ -> applySetting() }
                dlg.setNegativeButton("아니오") { _, _-> }
                dlg.show()
            }
            else applySetting()
        }

    }

    fun applySetting() {
        editor.putBoolean("busRoadFound", checkBus.isChecked)
        editor.putBoolean("busLowOnly", checkBusLow.isChecked)
        editor.putBoolean("busWait30", checkBusWait30.isChecked)
        editor.putBoolean("busWait60", checkBusWait60.isChecked)
        editor.putBoolean("subwayRoadFound", checkSubway.isChecked)
        editor.putBoolean("subwayElevator", checkSubwayElevator.isChecked)
        editor.putBoolean("subwayWheelchairStation", checkSubwayWheelchairStation.isChecked)
        editor.putBoolean("subwayWheelchairOn", checkSubwayWheelchairOn.isChecked)
        editor.putBoolean("disabled", checkDisabled.isChecked)
        editor.putBoolean("noInfo", checkNoInfo.isChecked)
        when (radioSearchWayCheck.checkedRadioButtonId) {
            R.id.radioBest -> editor.putInt("searchWay", 0)
            R.id.radioBus -> editor.putInt("searchWay", 1)
            R.id.radioSubway -> editor.putInt("searchWay", 2)
            R.id.radioWalk -> editor.putInt("searchWay", 3)
        }
        editor.apply()
        Toast.makeText(this, "설정이 적용되었습니다.", Toast.LENGTH_LONG).show()
        startActivity(Intent(this, StartActivity::class.java))
    }

    companion object {
        lateinit var firstSettingEnabledActivity2: Any
    }

}
