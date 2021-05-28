package app.jw.mapable.gm.Start;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
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

    }

    private void splashAnimation()
    {
        ImageView imageView = findViewById(R.id.imageView);
        ImageView imageView1 = findViewById(R.id.imageView2);
        TextView textView = findViewById(R.id.textView3);
        TextView textView1 = findViewById(R.id.textView10);
        Animation textAnim = AnimationUtils.loadAnimation(this, R.anim.anim_fade_in);
        textView.startAnimation(textAnim);
        textView1.startAnimation(textAnim);
        Animation imageAnim = AnimationUtils.loadAnimation(this, R.anim.anim_rotate);
        imageView.startAnimation(imageAnim);
        Animation newImageAnim = AnimationUtils.loadAnimation(this, R.anim.anim_zoom_in);
        ImageView newImageView = findViewById(R.id.imageView4);
        ImageView newImageView2 = findViewById(R.id.imageView5);
        newImageView2.startAnimation(newImageAnim);
        newImageView.startAnimation(newImageAnim);


        newImageAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                newImageView.setVisibility(View.GONE);
                newImageView2.setVisibility(View.GONE);

                Handler handler = new Handler(); // Handler 사용해 1초가 지나면 MainActivity 열기
                handler.postDelayed(() -> {
                    textView.setVisibility(View.GONE);
                    textView1.setVisibility(View.GONE);
                    imageView1.setVisibility(View.GONE);
                    imageView.setVisibility(View.GONE);
                    newImageView.setVisibility(View.VISIBLE);
                    newImageView2.setVisibility(View.VISIBLE);
                }, 2300);

                Handler handler2 = new Handler(); // Handler 사용해 1초가 지나면 MainActivity 열기
                handler2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        preferences = getSharedPreferences("preference", 0);
                        if(preferences.getBoolean("settingAvailable", false))
                        {
                            Intent intent = new Intent(SplashActivity.this, StartActivity.class);
                            startActivity(intent);

                            finish();
                            overridePendingTransition(R.anim.anim_none, R.anim.anim_zoom_in);
                        }
                        else
                        {
                            Intent intent = new Intent(SplashActivity.this, FirstSettingActivity.class);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.anim_none, R.anim.anim_none);
                        }
                    }
                }, 3700);

            }

            @Override
            public void onAnimationEnd(Animation animation) {

               // overridePendingTransition(R.anim.anim_slide_out_top, R.anim.anim_slide_in_down);






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
