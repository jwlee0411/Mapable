package app.jw.mapable.gm.afterroad

import android.app.Dialog
import android.content.Context
import android.view.Window
import app.jw.mapable.gm.R

class AfterRoadDialog (val context: Context){

    fun callFunction() {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_after_road)
        dialog.show()

        //TODO : dialog 속성 추가
    }


}