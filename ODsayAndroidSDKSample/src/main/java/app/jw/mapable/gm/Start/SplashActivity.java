package app.jw.mapable.gm.Start;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import app.jw.mapable.gm.FirstSetting.FirstSettingActivity;
import app.jw.mapable.gm.R;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splashAnimation();


//
//        Handler handler = new Handler();
//        handler.postDelayed(() -> {
//
//        }, 2000);
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        finish();
//    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        finish();
//    }

    }

    private void splashAnimation()
    {
        ImageView imageView = findViewById(R.id.imageView);
        TextView textView = findViewById(R.id.textView3);
        Animation textAnim = AnimationUtils.loadAnimation(this, R.anim.anim_splash_textview);
        textView.startAnimation(textAnim);
        Animation imageAnim = AnimationUtils.loadAnimation(this, R.anim.anim_splash_imageview);
        imageView.startAnimation(imageAnim);

        imageAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                overridePendingTransition(R.anim.anim_slide_out_top, R.anim.anim_slide_in_down);

                preferences = getSharedPreferences("preference", 0);
                if(preferences.getBoolean("settingAvailable", false))
                {
                    Intent intent = new Intent(SplashActivity.this, StartActivity.class);
                    startActivity(intent);

                    finish();
                }
                else
                {
                    Intent intent = new Intent(SplashActivity.this, FirstSettingActivity.class);
                    startActivity(intent);
                    finish();
                }




            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private final long backKeyPressedTime = 0;
    // 첫 번째 뒤로 가기 버튼을 누를 때 표시
    private Toast toast;

//    @Override
//    public void onBackPressed() {
//
//        if (System.currentTimeMillis() > backKeyPressedTime + 2500) {
//            backKeyPressedTime = System.currentTimeMillis();
//            toast = Toast.makeText(this, "뒤로 가기 버튼을 한 번 더 누르시면 종료됩니다.", Toast.LENGTH_LONG);
//            toast.show();
//            return;
//        }
//        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간에 2.5초를 더해 현재 시간과 비교 후
//        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간이 2.5초가 지나지 않았으면 종료
//        if (System.currentTimeMillis() <= backKeyPressedTime + 2500) {
//            android.os.Process.killProcess(android.os.Process.myPid());
//            finish();
//        }
//
//
//    }
}
