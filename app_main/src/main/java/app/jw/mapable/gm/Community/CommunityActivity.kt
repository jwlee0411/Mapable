package app.jw.mapable.gm.Community

import android.os.Build
import android.os.Bundle
import android.view.Menu
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavController
import androidx.navigation.Navigation
import app.jw.mapable.gm.R
import kotlinx.android.synthetic.main.activity_community.*


class CommunityActivity : AppCompatActivity() {
    private val mAppBarConfiguration: AppBarConfiguration? = null

    private var currentPage = 0
    private var nextPage = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community)

        button_main.setOnClickListener{
            ChangeFragment(MainFragment.newInstance())
            nextPage = 0

        }

        button_search.setOnClickListener {

        }

        button_my.setOnClickListener {

        }

        // 초기 화면 설정
        ChangeFragment(MainFragment.newInstance())
    }


    private fun ChangeFragment(fragment: Fragment) {
        val tran: FragmentTransaction = supportFragmentManager.beginTransaction()
        //TODO
//        if (nextPage > currentPage) tran.setCustomAnimations(
//            R.anim.slide_left_enter,
//            R.anim.slide_left_exit
//        ) else tran.setCustomAnimations(R.anim.slide_right_enter, R.anim.slide_right_exit)
        tran.replace(R.id.container, fragment)
        tran.commit()
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController: NavController =
            Navigation.findNavController(this, R.id.nav_host_fragment)
        return (NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp())
    }
}