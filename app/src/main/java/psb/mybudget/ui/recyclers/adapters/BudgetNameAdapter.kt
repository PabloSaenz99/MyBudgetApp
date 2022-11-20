package psb.mybudget.ui.recyclers.adapters

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import psb.mybudget.R
import psb.mybudget.models.Budget
import psb.mybudget.ui.TransactionListFragment
import psb.mybudget.ui.recyclers.MyViewHolder
import psb.mybudget.ui.recyclers.getGradientColor
import psb.mybudget.utils.replaceFragment


class BudgetNameAdapter(itemView: View) : MyViewHolder<String>(itemView) {

    private lateinit var name: String
    private var textName: TextView = itemView.findViewById(R.id.textBudgetRecyclerName)

    override fun setData(data: String) {
        name = data
        textName.text = name
    }

    override fun getData(): String = name
}