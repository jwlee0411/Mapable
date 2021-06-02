package app.jw.mapable.gm.Setting

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import app.jw.mapable.gm.R
import app.jw.mapable.gm.Start.StartActivity
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
        preferences = getSharedPreferences("preference", 0)
        editor = preferences.edit()


        settings = BooleanArray(10)

        getPreviousSettings()
        setonClick()
    }

    fun getPreviousSettings()
    {
        settings[0] = preferences.getBoolean("busRoadFound", false)
        settings[1] = preferences.getBoolean("busLowOnly", false)
        settings[2] = preferences.getBoolean("busWait30", false)
        settings[3] = preferences.getBoolean("busWait60", false)
        settings[4] = preferences.getBoolean("subwayRoadFound", false)
        settings[5] = preferences.getBoolean("subwayElevator", false)
        settings[6] = preferences.getBoolean("subwayWheelchairStation", false)
        settings[7] = preferences.getBoolean("subwayWheelchairOn", false)
        settings[8] = preferences.getBoolean("disabled", false)
        settings[9] = preferences.getBoolean("noInfo", false)


        checkBus.isChecked = settings[0]
        checkBusLow.isChecked = settings[1]
        checkBusWait30.isChecked = settings[2]
        checkBusWait60.isChecked = settings[3]

        checkSubway.isChecked = settings[4]
        checkSubwayElevator.isChecked = settings[5]
        checkSubwayWheelchairStation.isChecked = settings[6]
        checkSubwayWheelchairOn.isChecked = settings[7]

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



        }

        checkSubway.setOnCheckedChangeListener{ buttonView: CompoundButton?, isChecked: Boolean ->


        }

        buttonSet.setOnClickListener {

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
        startActivity(Intent(this, StartActivity::class.java))
    }
}