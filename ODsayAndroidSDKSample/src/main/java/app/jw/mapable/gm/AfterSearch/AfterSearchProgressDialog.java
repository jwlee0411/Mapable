package app.jw.mapable.gm.AfterSearch;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import androidx.annotation.NonNull;

import com.airbnb.lottie.Lottie;
import com.airbnb.lottie.LottieAnimationView;

import app.jw.mapable.gm.R;

public class AfterSearchProgressDialog extends Dialog {

    public AfterSearchProgressDialog(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 지저분한(?) 다이얼 로그 제목을 날림
        setContentView(R.layout.dialog_after_search_progress); // 다이얼로그에 박을 레이아웃

        LottieAnimationView lottieAnimationView = findViewById(R.id.lottieView);
        lottieAnimationView.playAnimation();
        lottieAnimationView.bringToFront();

    }
}
