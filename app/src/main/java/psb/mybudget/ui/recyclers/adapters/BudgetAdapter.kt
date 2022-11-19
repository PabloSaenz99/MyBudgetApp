package psb.mybudget.ui.recyclers.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import psb.mybudget.R
import psb.mybudget.models.Budget
import psb.mybudget.ui.TransactionListFragment
import psb.mybudget.ui.recyclers.MyViewHolder
import psb.mybudget.ui.recyclers.getGradientColor
import psb.mybudget.utils.replaceFragment


class BudgetAdapter(itemView: View) : MyViewHolder<Budget>(itemView) {

    private lateinit var budget: Budget
    private var textName: TextView = itemView.findViewById(R.id.textBudgetName)
    private var textDescription: TextView = itemView.findViewById(R.id.textBudgetDescription)
    private var textAmount: TextView = itemView.findViewById(R.id.textBudgetTotalAmount)

    @SuppressLint("SetTextI18n")
    override fun setData(data: Budget) {

        budget = data

        textName.text = budget.name
        textDescription.text = budget.description
        textAmount.text = budget.amount.toString() + "€"

        if(budget.amount >= 0) {
            itemView.setBackgroundColor(getGradientColor(itemView.context, R.color.positive_value, 0.15))
            textAmount.setTextColor(ContextCompat.getColor(itemView.context, R.color.positive_value))
        }
        else {
            itemView.setBackgroundColor(getGradientColor(itemView.context, R.color.negative_value, 0.15))
            textAmount.setTextColor(ContextCompat.getColor(itemView.context, R.color.negative_value))
        }

        itemView.setOnClickListener {
            replaceFragment("BudgetFragment", TransactionListFragment(budget.ID))
        }
    }

    override fun getData(): Budget = budget
}