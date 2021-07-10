package app.jw.mapable.gm.community

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import app.jw.mapable.gm.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_community_my_more.*
import java.util.*
import kotlin.collections.ArrayList


class CommunityMyMoreActivity : AppCompatActivity() {

    private lateinit var recyclerAdapter : CommunityAdapter
    private val datas = ArrayList<ItemCommunityMyMore>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community_my_more)

        val title = intent.getStringExtra("click")
        textViewTitle.text = title

        recyclerAdapter = CommunityAdapter(this)
        recyclerCommunityMyMore.adapter = recyclerAdapter
        recyclerCommunityMyMore.layoutManager = LinearLayoutManager(applicationContext)


        getMyPost()



    }

    private fun getMyPost()
    {
        val db = Firebase.firestore
        db.collection("post")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val timeStamp = document.data["posttime"] as com.google.firebase.Timestamp
                    val date: Date = timeStamp.toDate()
                    val dateFormat = android.text.format.DateFormat.getDateFormat(applicationContext)

                    datas.add(ItemCommunityMyMore(title = document.data["title"] as String, description = document.data["content"] as String, imageLink = document.data["image"] as String))
                }


                datas.apply {
                    recyclerAdapter.datas = datas
                    recyclerAdapter.notifyDataSetChanged()
                }


            }
            .addOnFailureListener {
                println("Error getting documents.")
            }

    }
}