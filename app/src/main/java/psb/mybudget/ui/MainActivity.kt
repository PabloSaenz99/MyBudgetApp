package psb.mybudget.ui

import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import psb.mybudget.R
import psb.mybudget.controllers.BudgetController
import psb.mybudget.databinding.ActivityMainBinding
import psb.mybudget.models.Budget
import psb.mybudget.models.Transaction
import psb.mybudget.models.TransactionType
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val b1 = BudgetController.addBudget(Budget("Budget1", "Description number 1"))
        val b2 = BudgetController.addBudget(Budget("Budget2", "Description 2"))

        BudgetController.addTransactionToBudget(Transaction("T1-1", 3.45, TransactionType.PAID), b1)
        BudgetController.addTransactionToBudget(Transaction("T1-2", -2.6, TransactionType.PAID, budgetIdList =  mutableSetOf(b1)), b1)
        BudgetController.addTransactionToBudget(Transaction("T1-3", 0.69, TransactionType.PAID, budgetIdList = mutableSetOf(b1)), b1)

        BudgetController.addTransactionToBudget(Transaction("T2-1", 1.25, TransactionType.PAID, budgetIdList = mutableSetOf(b2)), b2)
        BudgetController.addTransactionToBudget(Transaction("T2-2", -2.23, TransactionType.PAID, budgetIdList = mutableSetOf(b2)), b2)

        Log.i("B1", BudgetController.getBudgetById(b1).toString())
        Log.i("B2", BudgetController.getBudgetById(b2).toString())

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
}