package psb.mybudget.ui.recyclers.adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import psb.mybudget.R
import psb.mybudget.models.Budget
import psb.mybudget.models.sql.AppDatabase
import psb.mybudget.ui.MainActivity
import psb.mybudget.ui.home.EditBudgetActivity
import psb.mybudget.ui.home.transactions.TransactionListFragment
import psb.mybudget.ui.recyclers.MyViewHolder
import psb.mybudget.utils.getStroke
import psb.mybudget.utils.replaceFragment
import psb.mybudget.utils.startActivity


class BudgetAdapter(itemView: View) : MyViewHolder<Budget>(itemView) {

    private lateinit var budget: Budget
    private var textName: TextView = itemView.findViewById(R.id.rb_text_budgetName)
    private var textDescription: TextView = itemView.findViewById(R.id.rb_text_budgetDescription)
    private var textAmount: TextView = itemView.findViewById(R.id.rb_text_budgetAmount)

    @SuppressLint("SetTextI18n")
    override fun setData(data: Budget) {
        budget = data

        CoroutineScope(SupervisorJob()).launch {
            val amount = AppDatabase.getInstance().BudgetTable().getAmount(budget.ID)
            textAmount.text = amount + "€"

            if(amount.toDouble() >= 0) {
                itemView.background = getStroke(itemView, R.color.positive_value, budget.color)
                textAmount.setTextColor(ContextCompat.getColor(itemView.context, R.color.positive_value))
            }
            else {
                itemView.background = getStroke(itemView, R.color.negative_value, budget.color)
                //itemView.setBackgroundColor(getGradientColor(itemView.context, R.color.negative_value, 0.15))
                textAmount.setTextColor(ContextCompat.getColor(itemView.context, R.color.negative_value))
            }
        }

        textName.text = budget.name
        textDescription.text = budget.description
        //itemView.setBackgroundColor(getGradientColor(itemView.context, budget.color, 0.15))

        itemView.setOnClickListener {
            MainActivity.getMainActivity().currentBudget = budget.ID
            replaceFragment(TransactionListFragment(budget.ID), name = "BudgetFragment")
        }

        itemView.setOnLongClickListener {
            val intent = Intent(itemView.context, EditBudgetActivity::class.java)
            intent.putExtra("ID", data.ID)
            itemView.context.startActivity(intent)
            return@setOnLongClickListener true
        }
    }

    override fun getData(): Budget = budget
}