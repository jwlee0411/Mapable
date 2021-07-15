package app.jw.mapable.gm.start

import android.content.SharedPreferences
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.appcompat.app.AppCompatActivity
import app.jw.mapable.gm.R

import kotlinx.android.synthetic.main.activity_start_disabled.*
import java.util.*

class StartDisabledActivity : AppCompatActivity() {

    lateinit var sharedPreferences : SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    private lateinit var tts : TextToSpeech
    private val params = Bundle()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_disabled)

        sharedPreferences = getSharedPreferences("preferences", 0)
        editor = sharedPreferences.edit()


        initAnimation()
        initTTS()



    }


    private fun initAnimation()
    {
        val animationDrawable : AnimationDrawable = layoutDisabled2.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(2000)
        animationDrawable.setExitFadeDuration(4000)
        animationDrawable.start()
    }


    private fun initTTS()
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

                    ttsIdle()
                }
            }
            else
            {

            }
        }
    }

    private fun ttsIdle()
    {
        tts.speak("길찾기를 하시려면 아무 곳이나 클릭해주세요.", TextToSpeech.QUEUE_FLUSH, params, "ttsIdle")
        textViewStartDisabledTitle.text = "대기 중"
        textViewStartDisabledDescription.text = "길찾기를 하시려면 아무 곳이나 클릭해주세요."
    }

    fun ttsStartLocation()
    {
        tts.speak("출발 위치를 검색합니다. 검색할 위치를 말씀해주세요.", TextToSpeech.QUEUE_FLUSH, params, "ttsIdle")
        textViewStartDisabledTitle.text = "출발 위치 검색"
        textViewStartDisabledDescription.text = "출발 위치를 검색합니다."
    }


}