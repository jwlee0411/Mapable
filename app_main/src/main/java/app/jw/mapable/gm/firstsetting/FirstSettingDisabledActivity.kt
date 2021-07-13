package app.jw.mapable.gm.firstsetting

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import app.jw.mapable.gm.R
import app.jw.mapable.gm.start.StartActivity
import kotlinx.android.synthetic.main.activity_first_setting_disabled.*
import java.util.*

class FirstSettingDisabledActivity : AppCompatActivity() {

    lateinit var tts : TextToSpeech

    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    private val params = Bundle()


    private val str = arrayOf(
        """안녕하세요! 지금부터 어플리케이션 설정을 진행하겠습니다. 
            휴대폰의 미디어 음량을 키워 주세요.
            중간에 어플리케이션을 종료하면 설정이 저장되지 않습니다.
            그래도 중간에 설정을 종료하고 싶으면 화면 오른쪽 상단을 눌러 주세요.""",

        "길찾기 설정입니다.\n" +
                "설명이 끝난 후 예를 선택하시려면 화면 상단, 아니오를 선택하시려면 화면 하단을 눌러주세요.\n" +
                "\n" +
                "버스를 탑승하는 경로로 길안내를 진행할까요?",

        "저상버스를 이용 가능한 경로로만 안내할까요?",

        "30분 이상 대기하는 경로를 길찾기에 포함하지 않을까요?",

        "1시간 이상 대기하는 경로를 길찾기에 포함하지 않을까요?",

        "지하철을 탑승하는 경로로 길안내를 진행할까요?",

        "엘리베이터를 이용 가능한 역으로만 안내할까요?",

        "휠체어로 지하철을 탑승할 수 있는 역으로만 안내할까요?",

        "지하철에 휠체어 거치 공간이 있는 노선으로만 안내할까요?",

        "다음은 경로 설정입니다. TODO에요.",

        "이제 모든 설정이 끝났습니다. 지도를 실행합니다.",

    )


    var settingProgress = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_setting_disabled)

        sharedPreferences = getSharedPreferences("preferences", 0)
        editor = sharedPreferences.edit()


        floatingExit.setOnClickListener{
            finish()
        }

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
                    setting00_TTS()

                }
            }
            else
            {

            }
        }
    }

    fun setting00_TTS()
    {
        tts.setOnUtteranceProgressListener(object : UtteranceProgressListener(){
            override fun onStart(utteranceId: String?) {

            }

            override fun onDone(utteranceId: String?) {
                setting01_TTS()
            }

            override fun onError(utteranceId: String?) {
                TODO("Not yet implemented")
            }

        })
    }

    fun setting01_TTS()
    {
        tts.speak(str[1], TextToSpeech.QUEUE_FLUSH, params, "setting01")
        buttonAffirmative.setOnClickListener {
            tts.stop()
            editor.putBoolean("busRoadFound", true).apply()
            setting02_TTS()
        }

        buttonNegative.setOnClickListener {
            tts.stop()
            editor.putBoolean("busRoadFound", false)
            editor.putBoolean("busLowOnly", false)
            editor.putBoolean("busWait30", false)
            editor.putBoolean("busWait60", false)
            editor.apply()

            setting05_TTS()
        }
    }

    fun setting02_TTS()
    {
        tts.speak(str[2], TextToSpeech.QUEUE_FLUSH, params, "setting02")
        buttonAffirmative.setOnClickListener {
            tts.stop()
            editor.putBoolean("busLowOnly", true).apply()
            setting03_TTS()
        }
        buttonNegative.setOnClickListener {
            tts.stop()
            editor.putBoolean("busLowOnly", false).apply()
            setting03_TTS()
        }
    }

    fun setting03_TTS()
    {
        tts.speak(str[3], TextToSpeech.QUEUE_FLUSH, params, "setting03")
        buttonAffirmative.setOnClickListener {
            tts.stop()
            editor.putBoolean("busWait30", true).apply()
            setting04_TTS()
        }
        buttonNegative.setOnClickListener {
            tts.stop()
            editor.putBoolean("busWait30", false).apply()
            setting04_TTS()
        }
    }

    fun setting04_TTS()
    {
        tts.speak(str[4], TextToSpeech.QUEUE_FLUSH, params, "setting04")
        buttonAffirmative.setOnClickListener {
            tts.stop()
            editor.putBoolean("busWait60", true).apply()
            setting05_TTS()
        }
        buttonNegative.setOnClickListener {
            tts.stop()
            editor.putBoolean("busWait60", false).apply()
            setting05_TTS()
        }
    }

    fun setting05_TTS()
    {
        tts.speak(str[5], TextToSpeech.QUEUE_FLUSH, params, "setting05")
        buttonAffirmative.setOnClickListener {
            tts.stop()
            if(sharedPreferences.getBoolean("busRoadFound", false))
            {
                editor.putBoolean("subwayRoadFound", true).apply()
                setting06_TTS()
            }
            else
            {
                //TODO : finish() 쓸 때 주의!
                tts.speak("버스나 지하철 중 하나의 옵션을 선택해야 합니다. 설정을 처음부터 다시 진행해주세요.", TextToSpeech.QUEUE_FLUSH, params, "setting_finished")
                finish()
            }

        }

        buttonNegative.setOnClickListener {
            tts.stop()
            editor.putBoolean("subwayRoadFound", false)
            editor.putBoolean("subwayElevator", false)
            editor.putBoolean("subwayWheelchairStation", false)
            editor.putBoolean("subwayWheelchairOn", false)
            editor.apply()

            setting09_TTS()
        }
    }

    fun setting06_TTS()
    {
        tts.speak(str[6], TextToSpeech.QUEUE_FLUSH, params, "setting06")
        buttonAffirmative.setOnClickListener {
            tts.stop()
            editor.putBoolean("subwayElevator", true).apply()
            setting07_TTS()
        }
        buttonNegative.setOnClickListener {
            tts.stop()
            editor.putBoolean("subwayElevator", false).apply()
            setting07_TTS()
        }
    }

    fun setting07_TTS()
    {
        tts.speak(str[7], TextToSpeech.QUEUE_FLUSH, params, "setting07")
        buttonAffirmative.setOnClickListener {
            tts.stop()
            editor.putBoolean("busWait60", true).apply()
            setting08_TTS()
        }
        buttonNegative.setOnClickListener {
            tts.stop()
            editor.putBoolean("busWait60", false).apply()
            setting08_TTS()
        }
    }



    fun setting08_TTS()
    {
        tts.speak(str[8], TextToSpeech.QUEUE_FLUSH, params, "setting08")
        buttonAffirmative.setOnClickListener {
            tts.stop()
            editor.putBoolean("subwayWheelchairOn", true).apply()
            setting09_TTS()
        }
        buttonNegative.setOnClickListener {
            tts.stop()
            editor.putBoolean("subwayWheelchairOn", false).apply()
            setting09_TTS()
        }
    }

    //TODO : 여기서부터 ㄱㄱ
    fun setting09_TTS()
    {
        tts.speak(str[9], TextToSpeech.QUEUE_FLUSH, params, "setting09")

        buttonAffirmative.visibility = View.GONE
        buttonNegative.visibility = View.GONE

        layoutFirstSettingWay.visibility = View.VISIBLE

        buttonRecommend.setOnClickListener {
            tts.stop()
            editor.putInt("searchWay", 0).apply()
            settingEnd_TTS()
        }


        buttonBus.setOnClickListener {
            tts.stop()
            editor.putInt("searchWay", 1).apply()
            settingEnd_TTS()
        }

        buttonSubway.setOnClickListener {
            tts.stop()
            editor.putInt("searchWay", 2).apply()
            settingEnd_TTS()
        }


        buttonWalk.setOnClickListener {
            tts.stop()
            editor.putInt("searchWay", 3).apply()
            settingEnd_TTS()
        }


    }

    fun settingEnd_TTS()
    {

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