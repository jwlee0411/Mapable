package app.jw.mapable.gm.Start

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.Window
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import app.jw.mapable.gm.AfterSearch.AfterSearchActivity
import app.jw.mapable.gm.R
import app.jw.mapable.gm.Setting.SettingActivity
import kotlinx.android.synthetic.main.dialog_start.*

class StartDialog(val context : Context) : DialogInterface.OnDismissListener{



    fun callFunction(x:Double, y:Double, locationName:String)
    {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_start)
        dialog.show()

        dialog.setOnDismissListener {

        }


        val preferences = context.getSharedPreferences("preferences", 0)

        val start = preferences.getBoolean("start", false)
        val end = preferences.getBoolean("end", false)




        dialog.textLocationName.text = locationName.replace("대한민국 ", "")

        dialog.buttonStart.setOnClickListener {
            preferences.edit().putBoolean("start", true).apply()

            preferences.edit().putFloat("startX", x.toFloat()).putFloat("startY", y.toFloat()).apply()
            preferences.edit().putString("startLocation", locationName)

            //TODO
            println(locationName)
            println("§" + preferences.getFloat("startX", 0.0F))


            if(end)
            {
                //출발지, 도착지 모두 정해짐
                openAfterSearch()

            }
            else
            {
                Toast.makeText(dialog.context, "출발지가 설정되었습니다.", Toast.LENGTH_LONG).show()
                dialog.cancel()
            }

        }

        dialog.buttonEnd.setOnClickListener {
            preferences.edit().putBoolean("end", true).apply()
            preferences.edit().putFloat("endX", x.toFloat()).putFloat("endY", y.toFloat()).apply()
            preferences.edit().putString("endLocation", locationName)
            if(start)
            {
                //출발지, 도착지 모두 정해짐
                openAfterSearch()
            }
            else
            {
                Toast.makeText(dialog.context, "도착지가 설정되었습니다.", Toast.LENGTH_LONG).show()
                dialog.cancel()
            }

        }

        dialog.buttonCancel.setOnClickListener {
            dialog.cancel()
        }

        dialog.buttonStar.setOnClickListener{
            //TODO : 로그인 기능 구현 후 처리
            Toast.makeText(dialog.context, "준비 중인 기능입니다.", Toast.LENGTH_LONG).show()
        }

        dialog.buttonShare.setOnClickListener{
            val intentShare = Intent(Intent.ACTION_SEND)
            intentShare.type = "text/plain"
            intentShare.putExtra(Intent.EXTRA_TEXT, dialog.textLocationName.text as String + "\n\n스마트한 지도, Mapable에서 공유함")
            context.startActivity(Intent.createChooser(intentShare, "장소 공유"))

        }



    }


    fun openAfterSearch()
    {
        val intent = Intent(context, AfterSearchActivity::class.java)
        context.startActivity(intent)
    }


    override fun onDismiss(dialog: DialogInterface?) {

    }

}