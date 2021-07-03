package app.jw.mapable.gm.search

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import app.jw.mapable.gm.R
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)


        editTextSearch.addTextChangedListener {
            //TODO : Google API 이용 검색
        }


        backButton.setOnClickListener {
            finish()
        }

    }
}