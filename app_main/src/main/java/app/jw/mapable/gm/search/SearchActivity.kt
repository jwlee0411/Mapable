package app.jw.mapable.gm.search

import android.content.Intent
import android.location.Address
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import app.jw.mapable.gm.R
import app.jw.mapable.gm.start.StartLocationActivity
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.OpeningHours
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import kotlinx.android.synthetic.main.activity_search.*


class SearchActivity : AppCompatActivity() {

    lateinit var tts : TextToSpeech

    private lateinit var autocompleteFragment : AutocompleteSupportFragment

    var currentPrevTime = System.currentTimeMillis()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        transparentView.bringToFront()
        backButton.bringToFront()

        val isTTS = intent.getBooleanExtra("TTS", false)

        if(isTTS) openSTT()






        backButton.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.anim_none, R.anim.anim_move_bottom_down_full)
        }

        ttsButton.setOnClickListener {
            openSTT()
        }

        Places.initialize(this, resources.getString(R.string.google_api_key_2))

        autocompleteFragment =
            supportFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                    as AutocompleteSupportFragment

        autocompleteFragment.setCountry("KR")

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(listOf(Place.Field.RATING, Place.Field.WEBSITE_URI, Place.Field.BUSINESS_STATUS, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.PHONE_NUMBER, Place.Field.OPENING_HOURS, Place.Field.ADDRESS))

        autocompleteFragment.setHint("여기서 검색")
        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                val intent = Intent(applicationContext, StartLocationActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.putExtra("name", place.name)
                intent.putExtra("address", place.address)
                intent.putExtra("latitude", place.latLng!!.latitude)
                intent.putExtra("longitude", place.latLng!!.longitude)
                intent.putExtra("phoneNumber", place.phoneNumber)
                intent.putExtra("websiteUri", place.websiteUri)
                intent.putExtra("rating", place.rating)

                val openingHours : OpeningHours? = place.openingHours

                if(openingHours!=null)
                {
                    val list : List<String> = openingHours.weekdayText
                    var open = ""
                    for (str in list)
                    {
                        open = "$open$str★"
                    }
                    println("LOG : $open")
                    intent.putExtra("openingHours", open)
                }


                startActivity(intent)
                finish()
                println("Place: ${place.websiteUri}, ${place.name}, ${place.rating}, ${place.address}, ${place.latLng}, ${place.phoneNumber}, ${place.openingHours}")

            }

            override fun onError(status: Status) {
                println("An error occurred: $status")
            }
        })

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

                autocompleteFragment.setText(result) //editText에서는 .text 사용하면 안 됨!





            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.anim_none, R.anim.anim_move_bottom_down_full)
    }
}