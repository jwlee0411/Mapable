package app.jw.mapable.gm.community

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import app.jw.mapable.gm.R
import app.jw.mapable.gm.databinding.ActivityCommunityBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_community.*


class CommunityActivity : AppCompatActivity() {


    lateinit var binding : ActivityCommunityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCommunityBinding.inflate(layoutInflater)
        setContentView(binding.root) //R.id. 대신 이거 사용!

        val navView : BottomNavigationView = binding.navView

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_community) as NavHostFragment?
        val navController = navHostFragment!!.navController


        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.community_main, R.id.community_search, R.id.community_my
        )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }



}