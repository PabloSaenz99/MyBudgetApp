package psb.mybudget.ui.recyclers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class RecyclerAdapter<T: MyViewHolder<DATA>, DATA>(
    private var dataSet: Array<DATA>, @LayoutRes private val layout: Int,
    private val _class: Class<T>) : RecyclerView.Adapter<T>() {

    private val holders: MutableList<T> = mutableListOf()

    fun getItemAt(i: Int): T = holders[i]
    fun setData(data: Array<DATA>) { dataSet = data }

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