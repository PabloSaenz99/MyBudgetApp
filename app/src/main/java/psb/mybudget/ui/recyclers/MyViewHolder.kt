package psb.mybudget.ui.recyclers

import android.app.Activity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

abstract class MyViewHolder<DATA>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun setData(data: DATA)
    abstract fun getData(): DATA
}