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

        //setting03
        arrayOf<String>("30분 이상 대기하는 경로를 길찾기에 포함하지 않을까요?",

            "버스 길찾기 설정",

            "30분 이상 대기하는 경로를 안내하지 않을까요?"


        ),

        //setting04
        arrayOf<String>("1시간 이상 대기하는 경로를 길찾기에 포함하지 않을까요?",

            "버스 길찾기 설정",

            "1시간 이상 대기하는 경로를 안내하지 않을까요?"


        ),





        //setting05
        arrayOf<String>("지하철을 탑승하는 경로로 길안내를 진행할까요?",
            "지하철 길찾기 설정",

            "지하철 경로로 길안내를 진행할까요?"

        ),

        //setting06
        arrayOf<String>("엘리베이터를 이용 가능한 역으로만 안내할까요?",

            "지하철 길찾기 설정",

            "엘리베이터를 이용 가능한 역으로만 안내할까요?"


        ),

        //setting07
        arrayOf<String>("엘리베이터를 이용 가능한 역으로만 안내할까요?",

            "지하철 길찾기 설정",

            "엘리베이터를 이용 가능한 역으로만 안내할까요?"


        ),
        //setting08
        arrayOf<String>("지하철에 휠체어 거치 공간이 있는 노선으로만 안내할까요?",

            "지하철 길찾기 설정",

            "지하철에 휠체어 거치 공간이 있는 노선으로만 안내할까요?"


        ),

        //setting09
        arrayOf<String>("다음은 경로 설정 모드입니다.\n" +
                "잘 듣고, 설정하고 싶은 번호를 알려주세요.\n" +
                "다시 듣고 싶으시면 아무 말이나 해주세요." +
                "\n\n" +
                "1번, 추천 경로\n" +
                "2번, 버스 경로 우선\n" +
                "3번, 지하철 경로 우선\n" +
                "4번, 도보 적은 경로 우선",

            "길찾기 경로 설정",

            "1. 추천 경로 \n2. 버스 경로 우선 \n3. 지하철 경로 우선 \n4. 도보 적은 경로 우선"


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

    fun setting09_TTS()
    {
        getSettingInput(0)
        tts.speak(settingTTSStr[9][0], TextToSpeech.QUEUE_FLUSH, params, "setting09")
        textViewSettingDisabledTitle.text = settingTTSStr[9][1]
        textViewSettingDisabledDescription.text = settingTTSStr[9][2]
        settingProgress = 9
    }

    private fun settingEnd_TTS()
    {
        getSettingInput(10)
        tts.speak(settingTTSStr[10][0], TextToSpeech.QUEUE_FLUSH, params, "settingEnd")
        textViewSettingDisabledTitle.text = settingTTSStr[10][1]
        textViewSettingDisabledDescription.text = settingTTSStr[10][2]
        settingProgress = 10
    }

    private fun settingNoSet_TTS()
    {
        tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String) {}
            override fun onDone(utteranceId: String) {
                val mHandler = Handler(Looper.getMainLooper())
                mHandler.postDelayed({ setting01_TTS() }, 1000)
            }

            override fun onError(utteranceId: String) {}
        })
        tts.speak("버스와 지하철 길찾기 중 하나 이상을 선택해야 합니다. 설정을 처음부터 다시 진행할게요", TextToSpeech.QUEUE_FLUSH, params, "settingNoSet")

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
                                if(sharedPreferences.getBoolean("busRoadFound", false))
                                {
                                    editor.putBoolean("subwayRoadFound", false)
                                    editor.putBoolean("subwayElevator", false)
                                    editor.putBoolean("subwayWheelchairStation", false)
                                    editor.putBoolean("subwayWheelchairOn", false)
                                    editor.apply()
                                    setting09_TTS()
                                }
                                else
                                {
                                    settingNoSet_TTS()
                                }


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
                                setting09_TTS()
                            }
                            getTTSResultNo(result) -> {
                                editor.putBoolean("subwayWheelchairOn", false)
                                editor.apply()
                                setting09_TTS()
                            }
                            else -> ttsNotRecognized(8)
                        }

                    }

                    9 -> {
                        when {
                            getTTSResultOne(result) -> {
                                editor.putInt("searchWay", 0)
                                editor.apply()
                                settingEnd_TTS()
                            }
                            getTTSResultTwo(result) -> {
                                editor.putInt("searchWay", 1)
                                editor.apply()
                                settingEnd_TTS()

                            }
                            getTTSResultThree(result) -> {
                                editor.putInt("searchWay", 2)
                                editor.apply()
                                settingEnd_TTS()

                            }
                            getTTSResultFour(result) -> {
                                editor.putInt("searchWay", 3)
                                editor.apply()
                                settingEnd_TTS()
                            }
                            else -> ttsNotRecognized(9)
                        }

                    }

                    100 -> {

                        setting01_TTS()

                    }





                }
            }
        }

    }

    fun getTTSResultOne(result : String): Boolean {
        return result == "일" || result == "첫번째" || result == "1" || result == "일번" || result == "1번" || result.contains("하나") || result.contains("추천")
    }

    fun getTTSResultTwo(result : String): Boolean {
        return result == "이" || result == "두번째" || result == "2" || result == "이번" || result == "2번" || result.contains("둘") || result.contains("버스")
    }

    fun getTTSResultThree(result : String): Boolean {
        return result == "삼" || result == "세번째" || result == "3" || result == "삼번" || result == "3번" || result.contains("셋") || result.contains("지하철")
    }
    fun getTTSResultFour(result: String) : Boolean{
        return result == "사" || result == "네번째" || result == "4" || result == "사번" || result == "4번" || result.contains("넷") || result.contains("도보")
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
        tts.speak("무슨 말인지 모르겠어요. 다시 한 번 알려주시겠어요?", TextToSpeech.QUEUE_FLUSH, params, "settingNotRecognized")
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
                        9 -> setting09_TTS()
                        10 -> {
                            val intent = Intent(applicationContext, StartActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(intent)
                        }


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