package psb.mybudget.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
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

fun replaceFragment(newFragment: Fragment, @IdRes oldFragmentId: Int = R.id.nav_host_fragment_activity_main,
                    fragmentManager: FragmentManager = MainActivity.getMainActivity().supportFragmentManager,
                    name: String? = null) {
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