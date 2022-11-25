package psb.mybudget.utils

import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import psb.mybudget.R

fun getStroke(view: View, @ColorRes strokeColor: Int, @ColorRes backgroundColor: Int? = R.color.white): GradientDrawable {
    val gd = GradientDrawable()
    if(backgroundColor != null)
        gd.setColor(view.context.getColor(backgroundColor))
    gd.cornerRadius = view.resources.getDimension(R.dimen.radius)
    gd.setStroke(view.resources.getDimension(R.dimen.stroke).toInt(), view.resources.getColor(strokeColor))
    return gd
}