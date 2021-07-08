package app.jw.mapable.gm.setting

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.Window
import app.jw.mapable.gm.R
import kotlinx.android.synthetic.main.dialog_user_setting_edit.*

class UserSettingEditDialog(val context : Context) : DialogInterface.OnDismissListener {

    fun callFunction(b : Boolean)
    {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_start)
        dialog.show()

        dialog.setOnDismissListener {

        }




        dialog.buttonOK.setOnClickListener {
            val resultString = dialog.editUserName.text

            if(b)
            {

            }
            else
            {

            }

            dialog.cancel()
        }



    }

    override fun onDismiss(dialog: DialogInterface?) {

    }


}