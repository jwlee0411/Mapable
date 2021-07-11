package app.jw.mapable.gm.setting

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.Window
import app.jw.mapable.gm.R
import kotlinx.android.synthetic.main.dialog_user_setting.*

class UserSettingDialog(val context : Context) : DialogInterface.OnDismissListener {

    fun callFunction()
    {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_user_setting)
        dialog.show()

        dialog.setOnDismissListener {

        }


        dialog.buttonConfirm.setOnClickListener {
            //TODO : 탈퇴처리하기
        }

        dialog.buttonNo.setOnClickListener {
            dialog.cancel()
        }


    }

    override fun onDismiss(dialog: DialogInterface?) {

    }
}