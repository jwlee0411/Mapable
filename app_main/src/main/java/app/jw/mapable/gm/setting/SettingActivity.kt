package app.jw.mapable.gm.setting

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import app.jw.mapable.gm.R
import app.jw.mapable.gm.start.StartActivity
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity(){

    lateinit var preferences : SharedPreferences
    lateinit var editor : SharedPreferences.Editor
    lateinit var dlg : AlertDialog.Builder

    lateinit var settings : BooleanArray


    override fun onCreate(savedinstancestate : Bundle?)
    {
        super.onCreate(savedinstancestate)
        setContentView(R.layout.activity_setting)
        preferences = getSharedPreferences("preferences", 0)
        editor = preferences.edit()


        settings = BooleanArray(10)

        getPreviousSettings()
        setonClick()
    }

    fun getPreviousSettings()
    {
        settings[0] = preferences.getBoolean("busRoadFound", false)

        settings[4] = preferences.getBoolean("subwayRoadFound", false)



        checkBus.isChecked = settings[0]
        if(settings[0])
        {
            checkBusLow.isEnabled = true
            checkBusWait30.isEnabled = true
            checkBusWait60.isEnabled = true

            settings[1] = preferences.getBoolean("busLowOnly", false)
            settings[2] = preferences.getBoolean("busWait30", false)
            settings[3] = preferences.getBoolean("busWait60", false)


            checkBusLow.isChecked = settings[1]
            checkBusWait30.isChecked = settings[2]
            checkBusWait60.isChecked = settings[3]
        }


        checkSubway.isChecked = settings[4]
        if(settings[4])
        {
            checkSubwayElevator.isEnabled = true
            checkSubwayWheelchairStation.isEnabled = true
            checkSubwayWheelchairOn.isEnabled = true

            settings[5] = preferences.getBoolean("subwayElevator", false)
            settings[6] = preferences.getBoolean("subwayWheelchairStation", false)
            settings[7] = preferences.getBoolean("subwayWheelchairOn", false)

            checkSubwayElevator.isChecked = settings[5]
            checkSubwayWheelchairStation.isChecked = settings[6]
            checkSubwayWheelchairOn.isChecked = settings[7]
        }



        settings[8] = preferences.getBoolean("disabled", false)
        settings[9] = preferences.getBoolean("noInfo", false)

        checkDisabled.isChecked = settings[8]
        checkNoInfo.isChecked = settings[9]

        val searchWay : Int = preferences.getInt("searchWay", 0)

        when(searchWay)
        {
            0 -> radioBest.isChecked = true
            1 -> radioBus.isChecked = true
            2 -> radioSubway.isChecked = true
            3 -> radioWalk.isChecked = true
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

        checkSubway.setOnCheckedChangeListener{ buttonView: CompoundButton?, isChecked: Boolean ->

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
        startActivity(Intent(this, StartActivity::class.java))
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, StartActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}