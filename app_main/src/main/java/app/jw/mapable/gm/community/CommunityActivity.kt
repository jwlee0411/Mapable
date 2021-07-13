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


        navView.setupWithNavController(navController)





    }

}