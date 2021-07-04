package app.jw.mapable.gm.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import app.jw.mapable.gm.R
import kotlinx.android.synthetic.main.activity_search.*
import java.util.*

class SearchActivity : AppCompatActivity() {

    lateinit var tts : TextToSpeech
    private val params = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        editTextSearch.requestFocus()

        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)


        val isTTS = intent.getBooleanExtra("TTS", false)

        if(isTTS) openSTT()

        editTextSearch.addTextChangedListener {
            //TODO : Google API 이용 검색
        }


        backButton.setOnClickListener {
            overridePendingTransition(R.anim.anim_none, R.anim.anim_move_bottom_down_full)
            finish()
        }

        ttsButton.setOnClickListener {
            openSTT()
        }

    }

    private fun openSTT(){
        startActivityForResult(
            Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM).putExtra(
                RecognizerIntent.EXTRA_PROMPT, "Speech Recognition"), 1003)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //resultCode 0 => 강제종료, resultCode 1 => 정상작동

        if(requestCode == 1003) {
            if (data == null) {
                Toast.makeText(this, "입력값이 없습니다.", Toast.LENGTH_LONG).show()
            }
            else {
                val results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                val result = results?.get(0)

                editTextSearch.setText(result) //editText에서는 .text 사용하면 안 됨!



            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.anim_none, R.anim.anim_move_bottom_down_full)
    }
}