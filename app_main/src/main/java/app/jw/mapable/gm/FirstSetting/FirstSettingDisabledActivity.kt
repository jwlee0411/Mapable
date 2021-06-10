package app.jw.mapable.gm.FirstSetting

import android.content.Intent
import android.content.SharedPreferences
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
import app.jw.mapable.gm.Start.StartDisabledActivity
import kotlinx.android.synthetic.main.activity_first_setting_disabled.*
import java.util.*

class FirstSettingDisabledActivity : AppCompatActivity() {

    lateinit var tts : TextToSpeech

    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    private val params = Bundle()

    private val settingTTSStr = arrayOf(
            //setting00
            arrayOf<String>("""안녕하세요! 지금부터 어플리케이션 설정을 진행하겠습니다. 
            휴대폰의 미디어 음량을 키워 주시고, 화면을 클릭하지 마세요.
            중간에 어플리케이션을 종료하면 설정이 저장되지 않습니다.
            그래도 중간에 설정을 종료하고 싶으면 "종료" 라고 말해주세요.""",

                    "안녕하세요!",

                    "어플리케이션 설정을 시작합니다.\n" +
                    "휴대폰의 미디어 음량을 키워 주시고, 화면을 클릭하지 마세요.\n" +
                    "중간에 어플리케이션을 종료하면 설정이 저장되지 않습니다.\n" +
                    "그래도 중간에 설정을 종료하고 싶으면 \"종료\" 라고 말해주세요.\n"),

            //setting01
            arrayOf<String>("길찾기 설정입니다.\n" +
                    "예 혹은 아니오로 답해주세요.\n" +
                    "\n" +
                    "버스를 탑승하는 경로로 길안내를 진행할까요?",

                    "버스 길찾기 설정",

                    "버스 경로로 길안내를 진행할까요?"
                    ),


            //setting02
            arrayOf<String>("저상버스를 이용 가능한 경로로만 안내할까요?",

                    "버스 길찾기 설정",

                    "저상버스 이용 가능 경로로 안내할까요?"


            ),

            //setting03 : TODO
            arrayOf<String>("저상버스를 이용 가능한 경로로만 안내할까요?",

                    "버스 길찾기 설정",

                    "저상버스 이용 가능 경로로 안내할까요?"


            ),

            //setting04 : TODO
            arrayOf<String>("저상버스를 이용 가능한 경로로만 안내할까요?",

                    "버스 길찾기 설정",

                    "저상버스 이용 가능 경로로 안내할까요?"


            ),





            //setting05
            arrayOf<String>("지하철을 탑승하는 경로로 길안내를 진행할까요?",
                    "지하철 길찾기 설정",

                    "지하철 경로로 길안내를 진행할까요?"

            ),

            //setting06 : TODO
            arrayOf<String>("저상버스를 이용 가능한 경로로만 안내할까요?",

                    "지하철 길찾기 설정",

                    "저상버스 이용 가능 경로로 안내할까요?"


            ),

            //setting07 : TODO
            arrayOf<String>("저상버스를 이용 가능한 경로로만 안내할까요?",

                    "지하철 길찾기 설정",

                    "저상버스 이용 가능 경로로 안내할까요?"


            ),
            //setting08 : TODO
            arrayOf<String>("저상버스를 이용 가능한 경로로만 안내할까요?",

                    "지하철 길찾기 설정",

                    "저상버스 이용 가능 경로로 안내할까요?"


            ),

            //settingEnd
            arrayOf<String>("이제 모든 설정이 끝났습니다. 지도를 실행합니다.",

                    "감사합니다.",

                    "설정이 끝났습니다.\n지도를 실행합니다."


            ),




    )


    var settingProgress = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_setting_disabled)

        sharedPreferences = getSharedPreferences("preferences", 0)
        editor = sharedPreferences.edit()

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
            settingTTSStr[0][0], TextToSpeech.QUEUE_FLUSH, params, "setting00")

        textViewSettingDisabledTitle.text = settingTTSStr[0][1]
        textViewSettingDisabledDescription.text = settingTTSStr[0][2]

    }

    fun setting01_TTS() { //처음 설정 소개 + 버스 경로 보기

        getSettingInput(0)
        tts.speak(settingTTSStr[1][0], TextToSpeech.QUEUE_FLUSH, params, "setting01")


        textViewSettingDisabledTitle.text = settingTTSStr[1][1]
        textViewSettingDisabledDescription.text = settingTTSStr[1][2]
        settingProgress = 1

    }

    fun setting02_TTS() //저상버스만 안내
    {
        getSettingInput(0)
        tts.speak(settingTTSStr[2][0], TextToSpeech.QUEUE_FLUSH, params, "setting02")
        textViewSettingDisabledTitle.text = settingTTSStr[2][1]
        textViewSettingDisabledDescription.text = settingTTSStr[2][2]
        settingProgress = 2
    }

    fun setting03_TTS() //TODO
    {
        getSettingInput(0)
        tts.speak(settingTTSStr[3][0], TextToSpeech.QUEUE_FLUSH, params, "setting03")
        textViewSettingDisabledTitle.text = settingTTSStr[3][1]
        textViewSettingDisabledDescription.text = settingTTSStr[3][2]
        settingProgress = 3
    }

    fun setting04_TTS() //TODO
    {
        getSettingInput(0)
        tts.speak(settingTTSStr[4][0], TextToSpeech.QUEUE_FLUSH, params, "setting02")
        textViewSettingDisabledTitle.text = settingTTSStr[4][1]
        textViewSettingDisabledDescription.text = settingTTSStr[4][2]
        settingProgress = 4
    }

    fun setting05_TTS() //지하철 경로 보기
    {
        getSettingInput(0)
        tts.speak(settingTTSStr[5][0], TextToSpeech.QUEUE_FLUSH, params, "setting05")
        textViewSettingDisabledTitle.text = settingTTSStr[5][1]
        textViewSettingDisabledDescription.text = settingTTSStr[5][2]
        settingProgress = 5
    }

    fun setting06_TTS()
    {
        getSettingInput(0)
        tts.speak(settingTTSStr[6][0], TextToSpeech.QUEUE_FLUSH, params, "setting06")
        textViewSettingDisabledTitle.text = settingTTSStr[6][1]
        textViewSettingDisabledDescription.text = settingTTSStr[6][2]
        settingProgress = 6
    }

    fun setting07_TTS()
    {
        getSettingInput(0)
        tts.speak(settingTTSStr[7][0], TextToSpeech.QUEUE_FLUSH, params, "setting07")
        textViewSettingDisabledTitle.text = settingTTSStr[7][1]
        textViewSettingDisabledDescription.text = settingTTSStr[7][2]
        settingProgress = 7
    }

    fun setting08_TTS()
    {
        getSettingInput(0)
        tts.speak(settingTTSStr[8][0], TextToSpeech.QUEUE_FLUSH, params, "setting08")
        textViewSettingDisabledTitle.text = settingTTSStr[8][1]
        textViewSettingDisabledDescription.text = settingTTSStr[8][2]
        settingProgress = 8
    }

    private fun settingEnd_TTS()
    {
        getSettingInput(0)
        tts.speak(settingTTSStr[9][0], TextToSpeech.QUEUE_FLUSH, params, "settingEnd")
        textViewSettingDisabledTitle.text = settingTTSStr[9][1]
        textViewSettingDisabledDescription.text = settingTTSStr[9][2]
        settingProgress = 9
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
                        when {
                            getTTSResultYes(result) -> {
                                editor.putBoolean("busRoadFound", true)
                                editor.apply()
                                setting02_TTS()
                            }
                            getTTSResultNo(result) -> {
                                editor.putBoolean("busRoadFound", false)
                                editor.putBoolean("busLowOnly", false)
                                editor.putBoolean("busWait30", false)
                                editor.putBoolean("busWait60", false)
                                editor.apply()
                                setting05_TTS()
                            }
                            else -> ttsNotRecognized(1)
                        }
                    }

                    2 -> {
                        when {
                            getTTSResultYes(result) -> {
                                editor.putBoolean("busLowOnly", true)
                                editor.apply()
                                setting03_TTS()
                            }
                            getTTSResultNo(result) -> {
                                editor.putBoolean("busLowOnly", false)
                                editor.apply()

                                setting03_TTS()
                            }
                            else -> ttsNotRecognized(2)
                        }
                    }
                    3 -> {
                        when {
                            getTTSResultYes(result) -> {
                                editor.putBoolean("busWait30", true)
                                editor.apply()
                                setting04_TTS()
                            }
                            getTTSResultNo(result) -> {
                                editor.putBoolean("busWait30", false)
                                editor.apply()
                                setting04_TTS()
                            }
                            else -> ttsNotRecognized(3)
                        }

                    }

                    4 -> {
                        when {
                            getTTSResultYes(result) -> {
                                editor.putBoolean("busWait60", true)
                                editor.apply()
                                setting05_TTS()
                            }
                            getTTSResultNo(result) -> {
                                editor.putBoolean("busWait60", false)
                                editor.apply()
                                setting05_TTS()
                            }
                            else -> ttsNotRecognized(4)
                        }

                    }

                    5 -> {
                        when {
                            getTTSResultYes(result) -> {
                                editor.putBoolean("subwayRoadFound", true)
                                editor.apply()
                                setting06_TTS()
                            }
                            getTTSResultNo(result) -> {
                                editor.putBoolean("subwayRoadFound", false)
                                editor.putBoolean("subwayElevator", false)
                                editor.putBoolean("subwayWheelchairStation", false)
                                editor.putBoolean("subwayWheelchairOn", false)
                                editor.apply()
                                settingEnd_TTS()

                            }
                            else -> ttsNotRecognized(5)
                        }
                    }

                    6 -> {
                        when {
                            getTTSResultYes(result) -> {
                                editor.putBoolean("subwayElevator", true)
                                editor.apply()
                                setting07_TTS()
                            }
                            getTTSResultNo(result) -> {
                                editor.putBoolean("subwayElevator", false)
                                editor.apply()
                                setting07_TTS()
                            }
                            else -> ttsNotRecognized(6)
                        }

                    }

                    7 -> {
                        when {
                            getTTSResultYes(result) -> {
                                editor.putBoolean("subwayWheelchairStation", true)
                                editor.apply()
                                setting08_TTS()
                            }
                            getTTSResultNo(result) -> {
                                editor.putBoolean("subwayWheelchairStation", false)
                                editor.apply()
                                setting08_TTS()
                            }
                            else -> ttsNotRecognized(7)
                        }

                    }


                    8 -> {
                        when {
                            getTTSResultYes(result) -> {
                                editor.putBoolean("subwayWheelchairOn", true)
                                editor.apply()
                                settingEnd_TTS()
                            }
                            getTTSResultNo(result) -> {
                                editor.putBoolean("subwayWheelchairOn", false)
                                editor.apply()
                                settingEnd_TTS()
                            }
                            else -> ttsNotRecognized(8)
                        }

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
                        2 -> setting02_TTS()
                        3 -> setting03_TTS()
                        4 -> setting04_TTS()
                        5 -> setting05_TTS()
                        6 -> setting06_TTS()
                        7 -> setting07_TTS()
                        8 -> setting08_TTS()
                        9 -> startActivity(Intent(applicationContext, StartDisabledActivity::class.java))


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