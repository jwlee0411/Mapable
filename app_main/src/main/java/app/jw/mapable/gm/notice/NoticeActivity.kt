package app.jw.mapable.gm.notice

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import app.jw.mapable.gm.R
import app.jw.mapable.gm.threadtask.ThreadTask
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_after_search.*
import kotlinx.android.synthetic.main.activity_notice.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class NoticeActivity : AppCompatActivity(){

    lateinit var recyclerAdapter : NoticeRecyclerAdapter

    var notices = ArrayList<NoticeItem>()
    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notice)

        recyclerAdapter = NoticeRecyclerAdapter(this)
        recyclerViewNotice.layoutManager = LinearLayoutManager(applicationContext)
        recyclerViewNotice.adapter = recyclerAdapter


        val db = Firebase.firestore
        db.collection("notice")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val timeStamp = document.data["timestamp"] as com.google.firebase.Timestamp
                    val date: Date = timeStamp.toDate()
                    val dateFormat = android.text.format.DateFormat.getDateFormat(
                        applicationContext
                    )


                    notices.add(NoticeItem(title = document.id, description = document.data["description"] as String, ID = document.data["id"] as String, timestamp = dateFormat.format(date)))
                  }
                setRecyclerView()
            }
            .addOnFailureListener {
                println("Error getting documents.")
            }






        getNotice()








    }

    @SuppressLint("SimpleDateFormat")
    private fun getDateTime(s: String): String? {
        return try {
            val sdf = SimpleDateFormat("MM/dd/yyyy")
            val netDate = Date(s.toLong())
            sdf.format(netDate)
        } catch (e: Exception) {
            e.toString()
        }
    }


    private fun setRecyclerView(){
        notices.apply{
            recyclerAdapter.datas = notices
            recyclerAdapter.notifyDataSetChanged()
        }


    }

    private fun getNotice()
    {
        object : ThreadTask<Void, Int>(){
            override fun onPreExecute() {

            }

            override fun doInBackground(arg: Void?): Int {


                return 0
            }

            override fun onPostExecute(result: Int?) {

            }

        }
    }


}