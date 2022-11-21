package psb.mybudget.ui.recyclers.adapters

import android.view.View
import android.widget.TextView
import psb.mybudget.R
import psb.mybudget.ui.recyclers.MyViewHolder


class BudgetNameAdapter(itemView: View) : MyViewHolder<String>(itemView) {

    private lateinit var name: String
    private var textName: TextView = itemView.findViewById(R.id.buttonBudgetRecyclerName)

    override fun setData(data: String) {
        name = data
        textName.text = name
    }

    override fun getData(): String = name
}