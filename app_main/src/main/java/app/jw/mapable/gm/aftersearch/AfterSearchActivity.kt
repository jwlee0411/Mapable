package app.jw.mapable.gm.aftersearch

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import app.jw.mapable.gm.R
import app.jw.mapable.gm.search.SearchActivity
import app.jw.mapable.gm.start.StartActivity
import com.odsay.odsayandroidsdk.API
import com.odsay.odsayandroidsdk.ODsayData
import com.odsay.odsayandroidsdk.ODsayService
import com.odsay.odsayandroidsdk.OnResultCallbackListener
import kotlinx.android.synthetic.main.activity_after_search.*
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class AfterSearchActivity : AppCompatActivity() {



    private val recyclerAdapter = MapRecyclerAdapter(this)


    lateinit var preferences : SharedPreferences
    lateinit var editor : SharedPreferences.Editor

    var datas = ArrayList<Item>()

    lateinit var odsayService : ODsayService

    var startLocation = ""
    var endLocation = ""

    var startX = 0.0f
    var startY = 0.0f
    var endX = 0.0f
    var endY = 0.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_after_search)




        firstSetting()
        setonClick()
        getPath()

    }

    private fun firstSetting(){

        preferences = getSharedPreferences("preferences", 0)!!
        editor = preferences.edit()!!

        odsayService = ODsayService.init(this, getString(R.string.odsay_key))


        lottieView2.visibility = View.VISIBLE
        view.visibility = View.VISIBLE

        recyclerView.addItemDecoration(DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL))
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.adapter = recyclerAdapter

        startX = preferences.getFloat("startX", 0.0f)
        startY = preferences.getFloat("startY", 0.0f)
        endX = preferences.getFloat("endX", 0.0f)
        endY = preferences.getFloat("endY", 0.0f)

        editor.putBoolean("start", false)
        editor.putBoolean("end", false)
        editor.remove("prevString")
        editor.apply()

        startLocation = preferences.getString("startLocation", "")!!
        endLocation = preferences.getString("endLocation", "")!!
        startLocation = startLocation.replace("대한민국 ", "")
        endLocation = endLocation.replace("대한민국 ", "")

        textViewStart.text = startLocation
        textViewEnd.text = endLocation

    }

    private fun setonClick() {
        buttonClose.setOnClickListener {
            //TODO : 왜 Intent 선언을 안 해도 에러가 안 날까
            intent = Intent(this@AfterSearchActivity, StartActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }


        textViewStart.setOnClickListener {
            intent = Intent(this@AfterSearchActivity, SearchActivity::class.java)
            startActivity(intent)
        }

        textViewEnd.setOnClickListener {
            intent = Intent(this@AfterSearchActivity, SearchActivity::class.java)
            startActivity(intent)
        }

        buttonOpposite.setOnClickListener {
            editor.putFloat("endX", startX).putFloat("endY", startY)
            editor.putFloat("startX", endX).putFloat("startY", endY)
            editor.putString("startLocation", endLocation).putString("endLocation", startLocation)
            editor.apply()

            intent = Intent(this, AfterSearchActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }

    }

    private fun getPath()
    {
        if(startX != 0.0F)
        {
            odsayService.setReadTimeout(5000)
            odsayService.setConnectionTimeout(5000)
            println("LOG : getPath $startY / $startX")


            val startNewX = preferences.getString("startNewX", "")!!
            val startNewY = preferences.getString("startNewY", "")!!
            val endNewX = preferences.getString("endNewX", "")!!
            val endNewY = preferences.getString("endNewY", "")!!
            odsayService.requestSearchPubTransPath(startNewY, startNewX, endNewY, endNewX , "0", "0", "0", onRoadFoundResultCallbackListener)

            swipeRefreshLayout.setOnRefreshListener {
                editor.putFloat("endX", endX).putFloat("endY", endY)
                editor.putFloat("startX", startX).putFloat("startY", startY)
                editor.putString("startLocation", startLocation).putString("endLocation", endLocation)
                editor.apply()

                intent = Intent(this, AfterSearchActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                finish()
            }

        }
    }

    private val onRoadFoundResultCallbackListener: OnResultCallbackListener = object : OnResultCallbackListener {
            override fun onSuccess(oDsayData: ODsayData, api: API) {
                val jsonObject : JSONObject = oDsayData.json
                RoadFoundParse(jsonObject)
            }
            override fun onError(i: Int, errorMessage: String, api: API) {


                //TODO : 에러(Json object undefined) 뜨는 건 정상임. 그동안 확인을 안해서 몰랐을 뿐 ㅠㅠ
//                println("ERROR + $errorMessage")
//                Toast.makeText(applicationContext, "알 수 없는 오류입니다. \n$errorMessage", Toast.LENGTH_LONG).show()
//                fadeOutAnimation()


            }
    }

    private fun RoadFoundParse(jsonObject: JSONObject){

        //초기 변수 선언 실시
        var pathAll = ArrayList<ArrayList<ArrayList<String>>>()

        try {
            println("LOG : RoadFoundParse")
            val newObject = jsonObject.getJSONObject("result")
            val pathArray = newObject.getJSONArray("path")



            for(i in 0 until pathArray.length())
            {
                println("First for $i")
                println(pathArray.length())
                var pathAllOne = ArrayList<ArrayList<String>>()
                var pathAllTwo = ArrayList<String>()
                val pathObject = pathArray.getJSONObject(i)
                val infoObject = pathObject.getJSONObject("info")

                val trafficdistance = infoObject.getString("trafficDistance")
                val totalwalk = infoObject.getString("totalWalk")
                val totaltime = infoObject.getString("totalTime")
                val payment = infoObject.getString("payment")

                pathAllTwo.add(trafficdistance)
                pathAllTwo.add(totalwalk)
                pathAllTwo.add(totaltime)
                pathAllTwo.add(payment)



                pathAllOne.add(pathAllTwo)

                var subPathArray = pathObject.getJSONArray("subPath")

                for(j in 0 until subPathArray.length()) {
                    println("Second for $j")
                    println(subPathArray.length())
                    val pathAllThree = ArrayList<String>()
                    val subPathObject = subPathArray.getJSONObject(j)
                    val trafficType = subPathObject.getString("trafficType")
                    pathAllThree.add(trafficType)

                    pathAllThree.add(subPathObject.getString("distance"))
                    pathAllThree.add(subPathObject.getString("sectionTime"))

                    if(trafficType.equals("1") || trafficType.equals("2"))
                    {
                        pathAllThree.add(subPathObject.getString("stationCount"))
                        pathAllThree.add(subPathObject.getString("startName"))
                        pathAllThree.add(subPathObject.getString("startX"))
                        pathAllThree.add(subPathObject.getString("startY"))
                        pathAllThree.add(subPathObject.getString("endName"))
                        pathAllThree.add(subPathObject.getString("endX"))
                        pathAllThree.add(subPathObject.getString("endY"))
                        pathAllThree.add(subPathObject.getString("startID"))
                        pathAllThree.add(subPathObject.getString("endID"))

                        val stopObject = subPathObject.getJSONObject("passStopList")
                        val stopArray = stopObject.getJSONArray("stations")
                        var stopList = ""

                        for(k in 0 until stopArray.length())
                        {
                            val stopNewObject = stopArray.getJSONObject(k)
                            stopList = stopList + stopNewObject.getString("stationName")+ "☆" + stopNewObject.getString("x") + "☆" + stopNewObject.getString("y") + "★"
                        }
                        pathAllThree.add(stopList)
                        println(stopList)


                    }

                    if(trafficType.equals("1"))
                    {
                        pathAllThree.add(subPathObject.getString("name"))
                        pathAllThree.add(subPathObject.getString("subwayCode"))
                        pathAllThree.add(subPathObject.getString("wayCode"))
                        pathAllThree.add(subPathObject.getString("door"))

                    }
                    else if(trafficType.equals("2"))
                    {
                        pathAllThree.add(subPathObject.getString("busNo"))
                        pathAllThree.add(subPathObject.getString("type"))
                    }
                    else
                    {


                    }




                    pathAllOne.add(pathAllThree)


                }



                pathAll.add(pathAllOne)
                datas.add(Item(trafficdistance = trafficdistance, totalwalk = totalwalk, totaltime = totaltime, payment1 = payment, ways1 = pathAllOne))



            }

            println(datas)
            println("LOG : $datas.size")
            //마지막에 넣기
            datas.apply{
                recyclerAdapter.datas = datas
                recyclerAdapter.notifyDataSetChanged()
            }



        }
        catch (e : Exception)
        {
            val errorObject = jsonObject.getJSONObject("error")
            Toast.makeText(this, """${errorObject.getString("msg")}출발지와 도착지를 올바른 곳으로 설정했는지 다시 한 번 확인해 주세요.""".trimIndent(), Toast.LENGTH_LONG).show()
            fadeOutAnimation()
        }
        


    }



    override fun onBackPressed() {
        val intent = Intent(applicationContext, StartActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }


    private fun fadeOutAnimation()
    {
        val animation = AnimationUtils.loadAnimation(this, R.anim.anim_fade_out)

        lottieView2.startAnimation(animation)
        view.startAnimation(animation)

        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                lottieView2.visibility = View.GONE
                view.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
    }

}