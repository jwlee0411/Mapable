package app.jw.mapable.gm.Start

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.Window
import android.widget.Toast
import app.jw.mapable.gm.R
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


        dialog.textLocationName.text = locationName

        dialog.buttonStart.setOnClickListener {
            preferences.edit().putBoolean("start", true).apply()
            preferences.edit().putFloat("startX", x.toFloat()).putFloat("startY", y.toFloat()).apply()


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

    }


    fun openAfterSearch()
    {
        //GOTO AfterSearch : TODO
    }


    override fun onDismiss(dialog: DialogInterface?) {

    }

}