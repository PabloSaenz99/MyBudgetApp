package psb.mybudget.ui.recyclers.adapters

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import psb.mybudget.R
import psb.mybudget.models.Budget
import psb.mybudget.ui.MainActivity
import psb.mybudget.ui.home.transactions.TransactionListFragment
import psb.mybudget.ui.recyclers.MyViewHolder
import psb.mybudget.utils.getStroke
import psb.mybudget.utils.replaceFragment


class BudgetAdapter(itemView: View) : MyViewHolder<Budget>(itemView) {

    private lateinit var budget: Budget
    private var textName: TextView = itemView.findViewById(R.id.rb_text_budgetName)
    private var textDescription: TextView = itemView.findViewById(R.id.rb_text_budgetDescription)
    private var textAmount: TextView = itemView.findViewById(R.id.rb_text_budgetAmount)

    @SuppressLint("SetTextI18n")
    override fun setData(data: Budget) {
        budget = data

        textName.text = budget.name
        textDescription.text = budget.description
        textAmount.text = budget.amount.toString() + "€"

        if(budget.amount >= 0) {
            itemView.background = getStroke(itemView, R.color.positive_value, budget.color)
            textAmount.setTextColor(ContextCompat.getColor(itemView.context, R.color.positive_value))
        }
        else {
            itemView.background = getStroke(itemView, R.color.negative_value, budget.color)
            //itemView.setBackgroundColor(getGradientColor(itemView.context, R.color.negative_value, 0.15))
            textAmount.setTextColor(ContextCompat.getColor(itemView.context, R.color.negative_value))
        }
        //itemView.setBackgroundColor(getGradientColor(itemView.context, budget.color, 0.15))

        itemView.setOnClickListener {
            MainActivity.getMainActivity().currentBudget = budget.ID
            replaceFragment(TransactionListFragment(budget.ID), R.id.fragmentMain, name = "BudgetFragment")
        }
    }

    override fun getData(): Budget = budget
}