package app.jw.mapable.gm.star

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import app.jw.mapable.gm.R
import kotlinx.android.synthetic.main.activity_star.*

class StarActivity : AppCompatActivity() {

    lateinit var recyclerAdapter : StarRecyclerAdapter

    val datas = ArrayList<StarItem>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_star)


        recyclerAdapter = StarRecyclerAdapter(this)
        recyclerStar.adapter = recyclerAdapter
        recyclerStar.layoutManager = LinearLayoutManager(this)
        recyclerStar.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))




        datas.apply {
            recyclerAdapter.datas = datas
            recyclerAdapter.notifyDataSetChanged()
        }

    }
}