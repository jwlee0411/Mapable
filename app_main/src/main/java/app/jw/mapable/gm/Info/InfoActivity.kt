package app.jw.mapable.gm.Info

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import app.jw.mapable.gm.R

class InfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        val viewPager: ViewPager = findViewById(R.id.viewPager)
        viewPager.offscreenPageLimit = 3

        val adapter = ViewPagerAdapter(supportFragmentManager, 1)

        val fragment1 = Fragment1()
        val fragment2 = Fragment2()
        val fragment3 = Fragment3()
        adapter.addItem(fragment1)
        adapter.addItem(fragment2)
        adapter.addItem(fragment3)
        viewPager.adapter = adapter
    }
}