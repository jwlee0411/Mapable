package app.jw.mapable.gm.Info

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import app.jw.mapable.gm.R

class InfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        infoActivity = this@InfoActivity

        val viewPager: ViewPager = findViewById(R.id.viewPager)
        viewPager.offscreenPageLimit = 16

        val adapter = ViewPagerAdapter(supportFragmentManager, 1)

        adapter.addItem(Fragment1())
        adapter.addItem(Fragment2())
        adapter.addItem(Fragment3())
        adapter.addItem(Fragment4())
        adapter.addItem(Fragment5())
        adapter.addItem(Fragment6())
        adapter.addItem(Fragment7())
        adapter.addItem(Fragment8())
        adapter.addItem(Fragment9())
        adapter.addItem(Fragment10())
        adapter.addItem(Fragment11())
        adapter.addItem(Fragment12())
        adapter.addItem(Fragment13())
        adapter.addItem(Fragment14())
        adapter.addItem(Fragment15())
        adapter.addItem(Fragment16())

        viewPager.adapter = adapter
    }

    companion object{
        lateinit var infoActivity :  Any
    }
}