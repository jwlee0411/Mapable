package app.jw.mapable.gm.firstsetting

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import app.jw.mapable.gm.R
import kotlinx.android.synthetic.main.activity_first_setting.*
import java.util.*

class FirstSettingActivity : AppCompatActivity() {


    lateinit var tts : TextToSpeech
    private val params = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_setting)


        initTTS()

        setonClick()

    }

    fun initTTS()
    {
        tts = TextToSpeech(this) { status: Int ->
            if (status == TextToSpeech.SUCCESS) {
                val result: Int = tts.setLanguage(Locale.KOREAN)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
                {
                    Toast.makeText(this, "오류로 인해 TTS가 비활성화되었습니다.", Toast.LENGTH_LONG).show()
                }
                else {
                    tts.setPitch(1.0f)
                    tts.setSpeechRate(1.0f)
                    params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "")


                    tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                        override fun onStart(utteranceId: String) {

                        }
                        override fun onDone(utteranceId: String) {

                        }

                        override fun onError(utteranceId: String) {}
                    })
                    tts.speak(
                        "안녕하세요. 시각장애인이시면 화면 아래쪽을 눌러주세요. ", TextToSpeech.QUEUE_FLUSH, params, "setting00")


                }
            }
            else
            {
                Toast.makeText(this, "오류로 인해 TTS가 비활성화되었습니다.", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun setonClick()
    {
        buttonNormal.setOnClickListener {
            val intent = Intent(this, FirstSettingEnabledActivity::class.java)
            startActivity(intent)
        }

        buttonDisabled.setOnClickListener {
            val intent = Intent(this, FirstSettingDisabledActivity::class.java)
            startActivity(intent)
        }
    }
}