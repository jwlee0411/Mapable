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
                            val mHandler = Handler(Looper.getMainLooper())
                            mHandler.postDelayed({  val intent = Intent(this@FirstSettingActivity, FirstSettingDisabledActivity::class.java)
                                startActivity(intent)
                                finish()}, 7000)

                            buttonSettingStart.setOnClickListener {
                                mHandler.removeMessages(0)
                                tts.stop()
                                startActivity(Intent(this@FirstSettingActivity, FirstSettingEnabledActivity::class.java))
                                finish()
                            }
                        }
                        override fun onDone(utteranceId: String) {

                        }

                        override fun onError(utteranceId: String) {}
                    })
                    tts.speak(
                        "안녕하세요. 시각장애인이시면 화면을 누르지 말고 잠시 기다려주세요. ", TextToSpeech.QUEUE_FLUSH, params, "setting00")


                }
            }
            else
            {
                Toast.makeText(this, "오류로 인해 TTS가 비활성화되었습니다.", Toast.LENGTH_LONG).show()
            }
        }
    }
}