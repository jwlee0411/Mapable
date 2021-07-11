package app.jw.mapable.gm.start

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import app.jw.mapable.gm.R
import kotlinx.android.synthetic.main.activity_start_location.*

class StartLocationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_location)


        setonClick()

    }

    private fun setonClick(){
        layoutShare.setOnClickListener{

        }

        layoutBookmark.setOnClickListener {

        }

        layoutCall.setOnClickListener {

            val startCallDialog = StartCallDialog(this)
            startCallDialog.callFunction("01081927493")

        }
    }
}