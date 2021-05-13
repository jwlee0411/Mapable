package app.jw.mapable.gm;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class StartDialog {

    private Context context;

    SharedPreferences preferences;

    Button buttonStart, buttonEnd, buttonCancel;
    TextView textLocationName, textLocationAddress;

    public StartDialog(Context context)
    {
        this.context = context;
    }

    public void callFunction(double x, double y, String locationName, String locationAddress, boolean isStart, boolean isEnd)
    {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_start);
        dialog.show();



        buttonStart = dialog.findViewById(R.id.buttonStart);
        buttonEnd = dialog.findViewById(R.id.buttonEnd);
        buttonCancel = dialog.findViewById(R.id.buttonCancel);

        textLocationName = dialog.findViewById(R.id.textLocationName);
        textLocationAddress = dialog.findViewById(R.id.textLocationAddress);


        preferences = context.getSharedPreferences("preference", 0);


        textLocationName.setText(locationName);
        textLocationAddress.setText(locationAddress);


        if(isStart)
        {
            buttonStart.setVisibility(View.INVISIBLE);
        }
        else if(isEnd)
        {
            buttonEnd.setVisibility(View.INVISIBLE);
        }

        buttonStart.setOnClickListener(v -> {
            preferences.edit().putBoolean("start", true).apply();
            preferences.edit().putFloat("startX", (float) x).putFloat("startY", (float)y).apply();

            Toast.makeText(dialog.getContext(), "출발지가 설정되었습니다.", Toast.LENGTH_LONG).show();


            if(isEnd) //출발지와 도착지가 모두 정해졌으면
            {

            }
            else
            {

            }


        });

        buttonEnd.setOnClickListener(v -> {
            preferences.edit().putBoolean("end", true).apply();
            preferences.edit().putFloat("endX", (float) x).putFloat("endY", (float)y).apply();

            Toast.makeText(dialog.getContext(), "도착지가 설정되었습니다.", Toast.LENGTH_LONG).show();

            if(isStart)//출발지와 도착지가 모두 정해졌으면
            {

            }
            else
            {

            }

        });

        buttonCancel.setOnClickListener(v -> {
            dialog.cancel();
        });


    }
}
