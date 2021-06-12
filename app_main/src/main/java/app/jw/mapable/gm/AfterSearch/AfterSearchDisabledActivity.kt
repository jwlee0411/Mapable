package app.jw.mapable.gm.AfterSearch

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import app.jw.mapable.gm.R
import com.odsay.odsayandroidsdk.API
import com.odsay.odsayandroidsdk.ODsayData
import com.odsay.odsayandroidsdk.ODsayService
import com.odsay.odsayandroidsdk.OnResultCallbackListener
import org.json.JSONObject

class AfterSearchDisabledActivity : AppCompatActivity(){


    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor : SharedPreferences.Editor

    private lateinit var odsayService: ODsayService

    var startX : Double = 0.0
    var startY : Double = 0.0
    var endX : Double = 0.0
    var endY : Double = 0.0

    var startLocation : String = ""
    var endLocation : String = ""




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_disabled)

        sharedPreferences = getSharedPreferences("preferences", 0)
        editor = sharedPreferences.edit()

        startX = sharedPreferences.getFloat("startX", 0.0f).toDouble()
        startY = sharedPreferences.getFloat("startY", 0.0f).toDouble()
        endX = sharedPreferences.getFloat("endX", 0.0f).toDouble()
        endY = sharedPreferences.getFloat("endY", 0.0f).toDouble()

        startLocation = sharedPreferences.getString("startLocation", "")!!
        endLocation = sharedPreferences.getString("endLocation", "")!!
        startLocation = startLocation.replace("대한민국 ", "")
        endLocation = endLocation.replace("대한민국 ", "")

        if (startX != 0.0) {
            odsayService = ODsayService.init(this, getString(R.string.odsay_key))
            odsayService.setReadTimeout(5000)
            odsayService.setConnectionTimeout(5000)


            odsayService.requestSearchPubTransPath(startY.toString(), startX.toString(), endY.toString(), endX.toString(), "0", "0", "0", onRoadFoundResultCallbackListener)



        }

    }

    private val onRoadFoundResultCallbackListener : OnResultCallbackListener = object:OnResultCallbackListener{
    override fun onSuccess(oDsayData: ODsayData, api: API) {
        val jsonObject = oDsayData.json
        roadFoundParse(jsonObject)

    }

    override fun onError(p0: Int, p1: String?, p2: API?) {
    }

    }

    fun roadFoundParse(jsonObject: JSONObject)
    {

    }



    fun getPath()
    {
        editor.remove("prevString").apply()


    }
}