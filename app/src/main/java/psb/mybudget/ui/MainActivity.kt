package psb.mybudget.ui

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import psb.mybudget.R
import psb.mybudget.databinding.ActivityMainBinding
import psb.mybudget.models.sql.AppDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var currentBudget: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Layout xml item names: firstLettersOfLayoutName_typeOfWidget_classWhichEditThisWidget
        setMainActivity(this)
        //Init database
        AppDatabase.getInstance(applicationContext, CoroutineScope(SupervisorJob()))

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    companion object {
        private lateinit var activity: MainActivity

        fun setMainActivity(activity: MainActivity) { this.activity = activity }
        fun getMainActivity() : MainActivity = activity
    }
}