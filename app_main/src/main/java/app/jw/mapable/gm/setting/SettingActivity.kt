package app.jw.mapable.gm.setting

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import app.jw.mapable.gm.R
import app.jw.mapable.gm.start.StartActivity
import kotlinx.android.synthetic.main.activity_setting.*


class SettingActivity : AppCompatActivity() {

    lateinit var sharedPreferences : SharedPreferences
    lateinit var editor : SharedPreferences.Editor
    lateinit var dlg : AlertDialog.Builder

    lateinit var settings : BooleanArray

    var loginType = 0

    private var setting_default : Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        sharedPreferences = getSharedPreferences("preferences", 0)
        editor = sharedPreferences.edit()

        settings = BooleanArray(10)

        setting_default = intent.getIntExtra("defaultSetting", 0)


        loginType = sharedPreferences.getInt("loginType", 0)


        if(loginType==0)
        {
            userSettingView.visibility = View.GONE
            layoutUserSetting.visibility = View.GONE
        }
        else
        {
            userSettingView.visibility = View.VISIBLE
            layoutUserSetting.visibility = View.VISIBLE
        }

        setonClick()

        when(setting_default)
        {
            0 -> {
                getPreviousSettings()
            }
            1,2 ->{
                subwayEnabled()
                busEnabled()

                switchSubway.isChecked = true
                switchSubwayElevator.isChecked = true
                switchSubwayWheelchairStation.isChecked = true
                switchSubwayWheelchairOn.isChecked = true

                switchBus.isChecked = true
                switchBusLow.isChecked = true
                switchBusWait60.isChecked = true

                radioButton1.isChecked = true
            }
            3 ->{
                subwayEnabled()
                busEnabled()
                switchSubway.isChecked = true
                switchSubwayElevator.isChecked = true

                switchBus.isChecked = true
                switchBusLow.isChecked = true
                switchBusWait60.isChecked = true

                radioButton1.isChecked = true
            }
            4 -> {
                subwayDisabled()
                busDisabled()

                radioButton1.isChecked = true
            }
        }





    }

    fun getPreviousSettings()
    {
        settings[0] = sharedPreferences.getBoolean("busRoadFound", false)

        settings[4] = sharedPreferences.getBoolean("subwayRoadFound", false)



        switchBus.isChecked = settings[0]
        if(settings[0])
        {
            busEnabled()

            settings[1] = sharedPreferences.getBoolean("busLowOnly", false)
            settings[2] = sharedPreferences.getBoolean("busWait30", false)
            settings[3] = sharedPreferences.getBoolean("busWait60", false)


            switchBusLow.isChecked = settings[1]
            switchBusWait30.isChecked = settings[2]
            switchBusWait60.isChecked = settings[3]
        }


        switchSubway.isChecked = settings[4]
        if(settings[4])
        {
            subwayEnabled()

            settings[5] = sharedPreferences.getBoolean("subwayElevator", false)
            settings[6] = sharedPreferences.getBoolean("subwayWheelchairStation", false)
            settings[7] = sharedPreferences.getBoolean("subwayWheelchairOn", false)

            switchSubwayElevator.isChecked = settings[5]
            switchSubwayWheelchairStation.isChecked = settings[6]
            switchSubwayWheelchairOn.isChecked = settings[7]
        }



        settings[8] = sharedPreferences.getBoolean("disabled", false)
        settings[9] = sharedPreferences.getBoolean("noInfo", false)


        switchDisabled.isChecked = settings[8]
        switchNoInfo.isChecked = settings[9]

        when(sharedPreferences.getInt("searchWay", 0))
        {
            0 -> radioButton1.isChecked = true
            1 -> radioButton2.isChecked = true
            2 -> radioButton3.isChecked = true
            3 -> radioButton4.isChecked = true
        }
    }

    private fun setonClick(){

        layoutUserSetting.setOnClickListener {
            val intent = Intent(this, UserSettingActivity::class.java)
            startActivity(intent)
        }

        layoutAppInfo.setOnClickListener {
            val intent = Intent(this, AppInfoActivity::class.java)
            intent.putExtra("info", true)
            startActivity(intent)
        }

        layoutAppPrivacy.setOnClickListener {
            val intent = Intent(this, AppInfoActivity::class.java)
            intent.putExtra("info", false)
            startActivity(intent)
        }

        switchBus.setOnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean ->

            if(isChecked)
            {
                busEnabled()

            }
            else
            {
                busDisabled()
            }



        }

        switchSubway.setOnCheckedChangeListener{ buttonView: CompoundButton?, isChecked: Boolean ->

            if(isChecked)
            {
                subwayEnabled()
            }
            else
            {
                subwayDisabled()

            }


        }


        buttonApply.setOnClickListener {
            if (!switchBus.isChecked&&radioWayCheck.checkedRadioButtonId == R.id.radioButton2)
            {
                Toast.makeText(this, "'버스 경로 보기' 를 선택하지 않고 '버스 우선 탐색' 을 선택할 수 없습니다.", Toast.LENGTH_LONG).show()
            }
            else if(!switchSubway.isChecked && radioWayCheck.checkedRadioButtonId == R.id.radioButton3)
            {
                Toast.makeText(this, "'지하철 경로 보기' 를 선택하지 않고 '지하철 우선 탐색' 을 선택할 수 없습니다.", Toast.LENGTH_LONG).show()
            }
            else if(!switchBus.isChecked && !switchSubway.isChecked)
            {
                Toast.makeText(this, "버스와 지하철 중 적어도 하나의 옵션을 선택해 주세요.", Toast.LENGTH_LONG).show()
            }
            else if(switchDisabled.isChecked)
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


    private fun subwayEnabled(){
        switchSubwayElevator.isEnabled = true
        switchSubwayWheelchairStation.isEnabled = true
        switchSubwayWheelchairOn.isEnabled = true

        textSubwayElevator.setTextColor(resources.getColor(R.color.black, null))
        textSubwayElevatorExplain.setTextColor(resources.getColor(R.color.gray1, null))
        textSubwayWheelchairStation.setTextColor(resources.getColor(R.color.black, null))
        textSubwayWheelchairStationExplain.setTextColor(resources.getColor(R.color.gray1, null))
        textSubwayWheelchairOn.setTextColor(resources.getColor(R.color.black, null))
        textSubwayWheelchairOnExplain.setTextColor(resources.getColor(R.color.gray1, null))

    }

    private fun subwayDisabled(){
        switchSubwayElevator.isEnabled = false
        switchSubwayWheelchairStation.isEnabled = false
        switchSubwayWheelchairOn.isEnabled = false

        textSubwayElevator.setTextColor(resources.getColor(R.color.gray2, null))
        textSubwayElevatorExplain.setTextColor(resources.getColor(R.color.gray2, null))
        textSubwayWheelchairStation.setTextColor(resources.getColor(R.color.gray2, null))
        textSubwayWheelchairStationExplain.setTextColor(resources.getColor(R.color.gray2, null))
        textSubwayWheelchairOn.setTextColor(resources.getColor(R.color.gray2, null))
        textSubwayWheelchairOnExplain.setTextColor(resources.getColor(R.color.gray2, null))
    }

    fun busEnabled(){
        switchBusLow.isEnabled = true
        switchBusWait30.isEnabled = true
        switchBusWait60.isEnabled = true

        textBusLow.setTextColor(resources.getColor(R.color.black, null))
        textBusLowExplain.setTextColor(resources.getColor(R.color.gray1, null))
        textBusWait30.setTextColor(resources.getColor(R.color.black, null))
        textBusWait30Explain.setTextColor(resources.getColor(R.color.gray1, null))
        textBusWait60.setTextColor(resources.getColor(R.color.black, null))
        textBusWait60Explain.setTextColor(resources.getColor(R.color.gray1, null))

    }

    private fun busDisabled(){
        switchBusLow.isEnabled = false
        switchBusWait30.isEnabled = false
        switchBusWait60.isEnabled = false

        textBusLow.setTextColor(resources.getColor(R.color.gray2, null))
        textBusLowExplain.setTextColor(resources.getColor(R.color.gray2, null))
        textBusWait30.setTextColor(resources.getColor(R.color.gray2, null))
        textBusWait30Explain.setTextColor(resources.getColor(R.color.gray2, null))
        textBusWait60.setTextColor(resources.getColor(R.color.gray2, null))
        textBusWait60Explain.setTextColor(resources.getColor(R.color.gray2, null))
    }

    private fun applySetting() {
        editor.putBoolean("busRoadFound", switchBus.isChecked)
        editor.putBoolean("busLowOnly", switchBusLow.isChecked)
        editor.putBoolean("busWait30", switchBusWait30.isChecked)
        editor.putBoolean("busWait60", switchBusWait60.isChecked)
        editor.putBoolean("subwayRoadFound", switchSubway.isChecked)
        editor.putBoolean("subwayElevator", switchSubwayElevator.isChecked)
        editor.putBoolean("subwayWheelchairStation", switchSubwayWheelchairStation.isChecked)
        editor.putBoolean("subwayWheelchairOn", switchSubwayWheelchairOn.isChecked)


        editor.putBoolean("disabled", switchDisabled.isChecked)
        editor.putBoolean("noInfo", switchNoInfo.isChecked)
        when (radioWayCheck.checkedRadioButtonId) {
            R.id.radioButton1 -> editor.putInt("searchWay", 0)
            R.id.radioButton2 -> editor.putInt("searchWay", 1)
            R.id.radioButton3 -> editor.putInt("searchWay", 2)
            R.id.radioButton4 -> editor.putInt("searchWay", 3)
        }
        editor.putBoolean("settingAvailable", true)

        editor.apply()
        Toast.makeText(this, "설정이 적용되었습니다.", Toast.LENGTH_LONG).show()

        val intent = Intent(this, StartActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()

        when(setting_default){
            0 -> {
                val intent = Intent(this, StartActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }

            1,2,3,4 -> {
                finish()
            }
        }



    }



}