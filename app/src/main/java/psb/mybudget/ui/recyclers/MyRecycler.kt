package psb.mybudget.ui.recyclers

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import psb.mybudget.ui.MainActivity


class MyRecycler<T: MyViewHolder<DATA>, DATA>(
    private var dataSet: Array<DATA>,
    @LayoutRes private val layout: Int,
    private val _class: Class<T>
) : RecyclerView.Adapter<T>() {

    private val holders: MutableList<T> = mutableListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: Array<DATA>) {
        dataSet = data
        this.notifyDataSetChanged()
    }
    fun getItemAt(i: Int): T = holders[i]

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): T {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(layout, viewGroup, false)

        return _class.getConstructor(View::class.java).newInstance(view)
    }

    override fun onBindViewHolder(holder: T, position: Int) {
        holders.add(holder)
        holder.setData(dataSet[position])
    }

    override fun getItemCount(): Int { return dataSet.size }
}