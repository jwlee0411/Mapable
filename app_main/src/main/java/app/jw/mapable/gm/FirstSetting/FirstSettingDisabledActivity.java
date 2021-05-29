package app.jw.mapable.gm.FirstSetting;

import android.Manifest;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import com.vikramezhil.droidspeech.DroidSpeech;

import java.util.ArrayList;
import java.util.Locale;

import app.jw.mapable.gm.R;

public class FirstSettingDisabledActivity extends AppCompatActivity{


    TextToSpeech tts;

    Bundle params = new Bundle();

    int settingProgress = 0;

    ConstraintLayout disabledLayout;

    AnimationDrawable animationDrawable;

    TextView textTitle, textDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_setting_disabled);

        textTitle = findViewById(R.id.textViewSettingDisabledTitle);
        textDescription = findViewById(R.id.textViewSettingDisabledDescription);

        initAnimation();

        initTTS();


    }

    void initAnimation()
    {
        disabledLayout = findViewById(R.id.layoutDisabled);
        animationDrawable = (AnimationDrawable)disabledLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
    }


    void initTTS() {
        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = tts.setLanguage(Locale.KOREAN);

                if (result == TextToSpeech.LANG_MISSING_DATA
                        || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    System.out.println("★ERRORdddd");
                } else {

                    tts.setPitch(1.0f);
                    tts.setSpeechRate(1.0f);

                    params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "");

                    tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                        @Override
                        public void onStart(String utteranceId) {
                        }

                        @Override
                        public void onDone(String utteranceId) {
                            Handler mHandler = new Handler(Looper.getMainLooper());
                            mHandler.postDelayed(() -> {

                                setting_STT();

                            }, 0);

                        }

                        @Override
                        public void onError(String utteranceId) {

                        }
                    });

                    setting01_TTS();

                }
            } else {

            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //resultCode 0 => 강제종료
        //resultCode -1 => 정상작동

        if(requestCode == 1003)
        {
            if(data == null)
            {
                setting_STT();
            }
            else
            {
                ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                Toast.makeText(FirstSettingDisabledActivity.this, results.get(0), Toast.LENGTH_LONG).show();

                String result = results.get(0);

                switch (settingProgress)
                {
                    case 0:

                        break;
                    case 1: //버스 경로 설정
                        if(result.equals("예") || result.equals("네") || result.equals("1") || result.equals("일번") || result.equals("1번")|| result.contains("하나"))
                        {
                            setting02_TTS();
                        }
                        else if(result.equals("아니오") || result.equals("아니요")|| result.equals("2") || result.equals("이번") || result.equals("2번")|| result.contains("둘"))
                        {
                            setting05_TTS();
                        }

                        break;

                    case 2:

                }

            }
        }

    }

    void setting00_TTS() { //처음 설정 소개
        tts.speak("안녕하세요! 지금부터 어플리케이션 설정을 진행하겠습니다.\n" +
                "휴대폰의 미디어 음량을 키워 주시고, 화면을 클릭하지 마세요.\n" +
                "중간에 어플리케이션을 종료하면 설정이 저장되지 않습니다.\n" +
                "그래도 중간에 설정을 종료하고 싶으면 \"종료\" 라고 말해주세요.", TextToSpeech.QUEUE_FLUSH, params, "setting00");
        settingProgress = 1;
    }

    void setting01_TTS() { //처음 설정 소개 + 버스 경로 보기
        tts.speak("안녕하세요! 지금부터 설정을 시작하겠습니다.", TextToSpeech.QUEUE_FLUSH, params, "setting01");
        settingProgress = 1;
    }

    void setting02_TTS() //저상버스만 안내
    {
        tts.speak("안녕하세요! 지금부터 설정을 시작하겠습니다.", TextToSpeech.QUEUE_FLUSH, params, "setting01");
        settingProgress = 1;
    }

    void setting05_TTS() //지하철 경로 보기
    {
        tts.speak("안녕하세요! 지금부터 설정을 시작하겠습니다.", TextToSpeech.QUEUE_FLUSH, params, "setting01");
        settingProgress = 1;
    }



    void setting_STT() {

        startActivityForResult(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM).putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech Recognition"), 1003);

    }




    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (tts != null) {
            tts.stop();
            tts.shutdown();
            tts = null;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}