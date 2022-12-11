package psb.mybudget.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import psb.mybudget.R
import psb.mybudget.ui.MainActivity

fun startActivity(activity: Class<Activity>, context: Context) {
    val intent = Intent(context, activity)
    context.startActivity(intent)
}

fun replaceFragment(newFragment: Fragment, name: String, @IdRes oldFragmentId: Int = R.id.nav_host_fragment_activity_main,
                    fragmentManager: FragmentManager = MainActivity.getMainActivity().supportFragmentManager, ) {
    fragmentManager.commit {
        replace(oldFragmentId, newFragment)
        //disallowAddToBackStack()
        addToBackStack(name)
    }
}

fun removeFragment(fragment: Fragment, fragmentManager: FragmentManager = MainActivity.getMainActivity().supportFragmentManager) {
    fragmentManager.commit {
        remove(fragment)
    }
}

const val DEFAULT_BUDGET_ID = "DefaultBudgetId"
const val INTENT_BUDGET_ID = "budgetId"
const val FRAGMENT_BUDGET_LIST_ID = "budgetListFragment"

const val INTENT_TRANSACTION_ID = "transactionId"
const val FRAGMENT_TRANSACTION_LIST_ID = "transactionListFragment"