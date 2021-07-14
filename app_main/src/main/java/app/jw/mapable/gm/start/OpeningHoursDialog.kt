package app.jw.mapable.gm.start

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.Window
import android.widget.Toast
import app.jw.mapable.gm.R
import kotlinx.android.synthetic.main.dialog_opening_hours.*



class OpeningHoursDialog (val context : Context){

    fun callFunction(data : String)
    {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_opening_hours)
        dialog.show()


        dialog.textOpeningHours.text = data.replace("★", "\n\n")


        dialog.textOpeningHoursCancel.setOnClickListener {
            dialog.dismiss()
        }

    }

}