package app.jw.mapable.gm.community

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import app.jw.mapable.gm.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_community_edit.*
import java.util.*
import kotlin.collections.HashMap

class CommunityEditActivity : AppCompatActivity() {

    lateinit var sharedPreferences : SharedPreferences
    lateinit var editor : SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community_edit)

        sharedPreferences = getSharedPreferences("preferences", 0)
        editor = sharedPreferences.edit()

        setonClick()
    }


    private fun setonClick(){
        communityEditBack.setOnClickListener {
            finish()
        }

        buttonEnroll.setOnClickListener {

            val db = FirebaseFirestore.getInstance()
            val post: MutableMap<String, Any> = HashMap()

            if(editTitle.text == null || editContent.text == null)
            {
                Toast.makeText(this, "빈칸이 있는지 확인해주세요!", Toast.LENGTH_LONG).show()
            }
            else
            {
                val title = editTitle.text.toString()
                var content = editContent.text.toString()
                content = content.replace("\n", "\\n")
                val id = sharedPreferences.getString("id", "userID")!!
                val nickname = sharedPreferences.getString("userName", "")!!




                post["title"] = title
                post["content"] = content
                if(nickname != "") post["username"] = nickname
                else post["username"] = id

                post["like"] = 0
                post["dislike"] = 0
                post["star"] = 0

                post["posttime"] = Date()

                post["image"] = ""


                db.collection("post").document().set(post)
                    .addOnSuccessListener {
                        Toast.makeText(this, "게시물이 등록되었습니다.", Toast.LENGTH_LONG).show()
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "게시물 등록에 실패하였습니다.", Toast.LENGTH_LONG).show()
                    }


            }



        }


    }
}