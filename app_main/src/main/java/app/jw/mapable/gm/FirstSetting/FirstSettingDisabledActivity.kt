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
                    tts.setSpeechRate(3.0f)
                    params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "")

                    setting00_TTS()
                }
            }
            else
            {

            }
        }
    }

    fun setting00_TTS() { //처음 설정 소개
        tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String) {}
            override fun onDone(utteranceId: String) {
                val mHandler = Handler(Looper.getMainLooper())
                mHandler.postDelayed({ setting01_TTS() }, 1000)
            }

            override fun onError(utteranceId: String) {}
        })
        tts.speak(
            """안녕하세요! 지금부터 어플리케이션 설정을 진행하겠습니다.
휴대폰의 미디어 음량을 키워 주시고, 화면을 클릭하지 마세요.
중간에 어플리케이션을 종료하면 설정이 저장되지 않습니다.
그래도 중간에 설정을 종료하고 싶으면 "종료" 라고 말해주세요.""", TextToSpeech.QUEUE_FLUSH, params, "setting00")

        textViewSettingDisabledTitle.text = "안녕하세요!"
        textViewSettingDisabledDescription.text = "어플리케이션 설정을 시작합니다.\n" +
                "휴대폰의 미디어 음량을 키워 주시고, 화면을 클릭하지 마세요.\n" +
                "중간에 어플리케이션을 종료하면 설정이 저장되지 않습니다.\n" +
                "그래도 중간에 설정을 종료하고 싶으면 \"종료\" 라고 말해주세요.\n"

    }

    fun setting01_TTS() { //처음 설정 소개 + 버스 경로 보기

        getSettingInput(0)
        tts.speak("길찾기 설정입니다.\n" +
                "예 혹은 아니오로 답해주세요.\n" +
                "\n" +
                "버스를 탑승하는 경로로 길안내를 진행할까요?", TextToSpeech.QUEUE_FLUSH, params, "setting01")


        textViewSettingDisabledTitle.text = "길찾기 설정"
        textViewSettingDisabledDescription.text = "버스 경로로 길안내를 진행할까요?"
        settingProgress = 1

    }

    fun setting02_TTS() //저상버스만 안내
    {
        getSettingInput(0)
        tts.speak("저상버스를 이용 가능한 경로로만 안내할까요?", TextToSpeech.QUEUE_FLUSH, params, "setting02")
        textViewSettingDisabledTitle.text = "버스 길찾기 설정"
        textViewSettingDisabledDescription.text = "저상버스 이용 가능 경로로 안내할까요?"
        settingProgress = 2
    }

    fun setting03_TTS()
    {

    }

    fun setting05_TTS() //지하철 경로 보기
    {
        getSettingInput(0)
        tts.speak("지하철을 탑승하는 경로로 길안내를 진행할까요?", TextToSpeech.QUEUE_FLUSH, params, "setting05")
        settingProgress = 5
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
                    1 -> {
                        if (getTTSResultYes(result)) setting02_TTS()
                        else if (getTTSResultNo(result)) setting05_TTS()
                        else ttsNotRecognized(1)
                    }

                    2 -> {
                        if (getTTSResultYes(result)) {

                            setting03_TTS()
                        }
                        else if (getTTSResultNo(result)) {

                            setting03_TTS()
                        }
                        else ttsNotRecognized(1)
                    }
                    3 -> {
                    }
                }
            }
        }

    }

    fun getTTSResultYes(result : String): Boolean {
        return result == "예" || result == "네" || result == "1" || result == "일번" || result == "1번" || result.contains("하나")
    }

    fun getTTSResultNo(result:String) : Boolean{
        return result == "아니오" || result == "아니요" || result == "2" || result == "이번" || result == "2번" || result.contains("둘")
    }

    fun ttsNotRecognized(settingNum: Int)
    {
        getSettingInput(settingNum)
        tts.speak("무슨 말인지 모르겠어요. 다시 한 번 알려주시겠어요?", TextToSpeech.QUEUE_FLUSH, params, "setting02")
    }

    fun getSettingInput(settingNum : Int)
    {
        tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String) {}
            override fun onDone(utteranceId: String) {
                val mHandler = Handler(Looper.getMainLooper())
                mHandler.postDelayed({
                    when(settingNum)
                    {
                        0 -> setting_STT()
                        1 -> setting01_TTS()
                    }


                                     }, 0)
            }

            override fun onError(utteranceId: String) {}
        })
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