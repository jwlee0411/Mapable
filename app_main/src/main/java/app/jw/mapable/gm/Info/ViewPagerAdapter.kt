package app.jw.mapable.gm.Info

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import java.util.*

class ViewPagerAdapter(fragmentManager: FragmentManager?, behavior: Int) :
    FragmentStatePagerAdapter(fragmentManager!!, behavior) {
    val items = ArrayList<Fragment>()
    fun addItem(item: Fragment) {
        items.add(item)
    }

    override fun getItem(position: Int): Fragment {
        return items[position]
    }

    override fun getCount(): Int {
        return items.size
    }
}