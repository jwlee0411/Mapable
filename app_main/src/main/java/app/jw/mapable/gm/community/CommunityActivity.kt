package app.jw.mapable.gm.community


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import app.jw.mapable.gm.R
import app.jw.mapable.gm.databinding.ActivityCommunityBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_community.*


class CommunityActivity : AppCompatActivity() {

    //TODO : 도저히 에러가 해결이 안됨


    lateinit var binding : ActivityCommunityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       binding = ActivityCommunityBinding.inflate(layoutInflater)
        setContentView(binding.root) //R.id. 대신 이거 사용!

        val navView : BottomNavigationView = binding.navView


        val navController = findNavController(R.id.nav_host_fragment_activity_community)


        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.community_main, R.id.community_search, R.id.community_my
        )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)


        nav_view.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            var selectedFragment: Fragment? = null

            var transaction : FragmentTransaction? = null
            when (item.itemId) {
                R.id.navigation_community_main -> {
                    println("Click1")
                    selectedFragment = CommunityMainFragment()
                    transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.content, selectedFragment)
                    transaction.addToBackStack(null)
                    transaction.commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_community_search -> {
                    println("Click2")
                    selectedFragment = CommunitySearchFragment()
                    transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.content, selectedFragment)
                    transaction.addToBackStack(null)
                    transaction.commit()
                    return@OnNavigationItemSelectedListener true
                }
            }
            transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.content, selectedFragment!!)
            transaction.commit()
            true
        })

        navView.setupWithNavController(navController)

    }

}