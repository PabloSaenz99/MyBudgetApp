package psb.mybudget.ui.recyclers

import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import psb.mybudget.R
import psb.mybudget.models.Budget

class BudgetAdapter(itemView: View) : MyViewHolder<Budget>(itemView) {

    private lateinit var budget: Budget
    private var textName: TextView = itemView.findViewById(R.id.textBudgetName)
    private var textDescription: TextView = itemView.findViewById(R.id.textBudgetDescription)
    private var textAmount: TextView = itemView.findViewById(R.id.textBudgetTotalAmount)

    override fun setData(data: Budget) {

        budget = data

        textName.text = budget.name
        textDescription.text = budget.description
        textAmount.text = budget.amount.toString() + "€"
        Log.i("Amount", budget.amount.toString())
        if(budget.amount > 0) {
            itemView.setBackgroundColor(getGradientColor(itemView.context, R.color.positive_value, 0.3))
            textAmount.setTextColor(ContextCompat.getColor(itemView.context, R.color.positive_value))
        }
        else {
            itemView.setBackgroundColor(getGradientColor(itemView.context, R.color.negative_value, 0.3))
            textAmount.setTextColor(ContextCompat.getColor(itemView.context, R.color.negative_value))
        }
    }

    override fun getData(): Budget {
        return budget
    }
}