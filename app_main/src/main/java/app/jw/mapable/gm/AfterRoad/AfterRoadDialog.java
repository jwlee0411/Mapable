package app.jw.mapable.gm.AfterRoad;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import app.jw.mapable.gm.R;

public class AfterRoadDialog {

    private final Context context;


    public AfterRoadDialog(Context context)
    {
        this.context = context;
    }

    public void callFunction()
    {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_after_road);

        dialog.show();

        //TODO : dialog 속성 추가
    }

}
