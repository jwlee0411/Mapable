package app.jw.mapable.gm.start

import android.app.Dialog
import android.content.*
import android.content.Context.CLIPBOARD_SERVICE
import android.net.Uri
import android.view.Window
import android.widget.Toast
import app.jw.mapable.gm.R
import kotlinx.android.synthetic.main.dialog_start_call.*

class StartCallDialog (val context : Context){

    fun callFunction(data : String)
    {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_start_call)
        dialog.show()

        dialog.textPhoneNumber.text = data

        dialog.textCall.setOnClickListener {
            dialog.context.startActivity(Intent("android.intent.action.DIAL", Uri.parse("tel:$data")))
            dialog.dismiss()
        }

        dialog.textCopy.setOnClickListener {
            val clipboardManager = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager

            val clipData = ClipData.newPlainText("", data)
            clipboardManager.setPrimaryClip(clipData)
            Toast.makeText(context, "클립보드에 복사되었습니다.", Toast.LENGTH_LONG).show()
            dialog.dismiss()
        }

        dialog.textDialogCancel.setOnClickListener {
            dialog.dismiss()
        }

    }

}