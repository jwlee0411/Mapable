package app.jw.mapable.gm;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

public class AfterRoadDialog {

    private Context context;


    public AfterRoadDialog(Context context)
    {
        this.context = context;
    }

    public void callFunction(String temp)
    {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_after_road);
        dialog.show();

        //TODO : dialog 속성 추가
    }

}
