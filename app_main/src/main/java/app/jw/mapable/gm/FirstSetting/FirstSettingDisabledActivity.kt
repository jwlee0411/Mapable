package app.jw.mapable.gm.FirstSetting

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import app.jw.mapable.gm.R
import kotlinx.android.synthetic.main.activity_first_setting_disabled.*
import java.util.*

class FirstSettingDisabledActivity : AppCompatActivity() {

    lateinit var tts : TextToSpeech

    val params = Bundle()

    var settingProgress = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_setting_disabled)

        initAnimation()
        initTTS()


    }

    fun initAnimation()
    {
        val animationDrawable : AnimationDrawable = layoutDisabled.getBackground() as AnimationDrawable
        animationDrawable.setEnterFadeDuration(2000)
        animationDrawable.setExitFadeDuration(4000)
        animationDrawable.start()
    }

    fun initTTS()
    {
        tts = TextToSpeech(this) { status: Int ->
            if (status == TextToSpeech.SUCCESS) {
                val result: Int = tts.setLanguage(Locale.KOREAN)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
                {
                    println("★TTS ERROR")
                }
                else {
                    tts.setPitch(1.0f)
                    tts.setSpeechRate(1.0f)
                    params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "")
                    tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                        override fun onStart(utteranceId: String) {}
                        override fun onDone(utteranceId: String) {
                            val mHandler = Handler(Looper.getMainLooper())
                            mHandler.postDelayed({ setting_STT() }, 0)
                        }

                        override fun onError(utteranceId: String) {}
                    })
                    setting00_TTS()
                }
            }
            else
            {

            }
        }
    }

    fun setting00_TTS() { //처음 설정 소개
        tts.speak(
            """안녕하세요! 지금부터 어플리케이션 설정을 진행하겠습니다.
휴대폰의 미디어 음량을 키워 주시고, 화면을 클릭하지 마세요.
중간에 어플리케이션을 종료하면 설정이 저장되지 않습니다.
그래도 중간에 설정을 종료하고 싶으면 "종료" 라고 말해주세요.""", TextToSpeech.QUEUE_FLUSH, params, "setting00")
        settingProgress = 1
    }

    fun setting01_TTS() { //처음 설정 소개 + 버스 경로 보기
        tts.speak("길찾기 설정입니다.\n" +
                "예 혹은 아니오로 답해주세요.\n" +
                "\n" +
                "버스를 탑승하는 경로로 길안내를 진행할까요?", TextToSpeech.QUEUE_FLUSH, params, "setting01")

    }

    fun setting02_TTS() //저상버스만 안내
    {
        tts.speak("저상버스를 이용 가능한 경로로만 안내할까요?", TextToSpeech.QUEUE_FLUSH, params, "setting01")
        settingProgress = 1
    }

    fun setting05_TTS() //지하철 경로 보기
    {
        tts.speak("안녕하세요! 지금부터 설정을 시작하겠습니다.", TextToSpeech.QUEUE_FLUSH, params, "setting01")
        settingProgress = 1
    }


    fun setting_STT() {
        startActivityForResult(Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM).putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech Recognition"), 1003)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //resultCode 0 => 강제종료, resultCode 1 => 정상작동

        if(requestCode == 1003)
        {
            if(data==null)
            {
                setting_STT()
            }
            else
            {
                val results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                Toast.makeText(this@FirstSettingDisabledActivity, results!![0], Toast.LENGTH_LONG).show()

                val result = results[0]

                when (settingProgress) {
                    0 -> {

                    }
                    1 -> {
                        if (result == "예" || result == "네" || result == "1" || result == "일번" || result == "1번" || result.contains("하나"))
                        {
                            setting02_TTS()
                        }
                        else if (result == "아니오" || result == "아니요" || result == "2" || result == "이번" || result == "2번" || result.contains("둘"))
                        {
                            setting05_TTS()
                        }
                    }

                    3 -> {
                    }
                }
            }
        }

    }


    override fun onDestroy() {
        super.onDestroy()

        try
        {
            tts.stop()
            tts.shutdown()
        }catch (e : Exception)
        {
            e.printStackTrace()
        }


    }




}