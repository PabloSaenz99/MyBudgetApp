package psb.mybudget.ui.recyclers.adapters

import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import psb.mybudget.R
import psb.mybudget.models.MyTransaction
import psb.mybudget.models.sql.AppDatabase
import psb.mybudget.ui.home.transactions.EditTransactionFragment
import psb.mybudget.ui.recyclers.MyViewHolder
import psb.mybudget.ui.recyclers.createGridRecycler
import psb.mybudget.utils.getStroke
import psb.mybudget.utils.replaceFragment


class TransactionAdapter(itemView: View) : MyViewHolder<Pair<MyTransaction, Boolean>>(itemView) {

    private var isEnabled: Boolean = false
    private lateinit var transaction: MyTransaction
    private val textName: TextView = itemView.findViewById(R.id.rt_text_transactionName)
    private val textDate: TextView = itemView.findViewById(R.id.rt_textDate_transactionDate)
    private val textAmount: TextView = itemView.findViewById(R.id.rt_number_transactionAmount)
    private val spinner: Spinner = itemView.findViewById(R.id.rt_spinner_transactionStatus)

    override fun setData(data: Pair<MyTransaction, Boolean>) {
        transaction = data.first
        isEnabled = data.second

        textName.text = transaction.name
        textDate.text = transaction.date.toString()
        textAmount.text = transaction.amount.toString()

        textDate.isEnabled = isEnabled
        textAmount.isEnabled = isEnabled

        val spinnerAdapter = ArrayAdapter. createFromResource(itemView.context, R.array.transaction_status_value, android.R.layout.simple_spinner_item)
            .also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                spinner.adapter = adapter
            }
        spinner.setSelection(spinnerAdapter.getPosition(transaction.transactionType.toString()))

        if(transaction.amount < 0) {
            textAmount.setTextColor(itemView.resources.getColor(R.color.negative_value))
            itemView.background = getStroke(itemView, R.color.negative_value)
        }
        else {
            textAmount.setTextColor(itemView.resources.getColor(R.color.positive_value))
            itemView.background = getStroke(itemView, R.color.positive_value)
        }

        val mutable = MutableLiveData<List<BudgetNameAdapter.Data>>()
        mutable.observe(itemView.context as LifecycleOwner) { budgets ->
            createGridRecycler(budgets.toTypedArray(), BudgetNameAdapter::class.java,
                R.id.rt_recycler_transactionBudgets, R.layout.recycler_budget_id, itemView, 4)
        }
        AppDatabase.getInstance(itemView.context).BudgetTable().getByTransactionIdIn(transaction.ID, mutable)

        if(isEnabled){
            itemView.setOnClickListener {
                //TODO: click button
            }
        }
        itemView.setOnClickListener {
            replaceFragment(EditTransactionFragment(transaction))
        }
    }

    override fun getData(): Pair<MyTransaction, Boolean> { return Pair(transaction, isEnabled) }
}