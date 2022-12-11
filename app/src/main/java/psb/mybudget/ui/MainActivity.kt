package psb.mybudget.ui

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
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

        /**
         * @param colorRes: R.color.red
         */
        fun getColorInt(@ColorRes colorRes: Int) = activity.resources.getColor(colorRes)

        fun getStroke(strokeColor: Int, @ColorInt backgroundColor: Int? = getColorInt(R.color.white)): GradientDrawable {
            val gd = GradientDrawable()
            if(backgroundColor != null)
                gd.setColor(backgroundColor)
            gd.cornerRadius = activity.resources.getDimension(R.dimen.radius)
            gd.setStroke(activity.resources.getDimension(R.dimen.stroke).toInt(), activity.resources.getColor(strokeColor))
            return gd
        }
    }
}