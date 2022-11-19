package psb.mybudget.ui.recyclers.adapters

import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.RecyclerView
import psb.mybudget.R
import psb.mybudget.models.MyTransaction
import psb.mybudget.models.sql.AppDatabase
import psb.mybudget.ui.recyclers.MyViewHolder
import psb.mybudget.ui.recyclers.createGridRecycler
import psb.mybudget.ui.recyclers.createLinearRecycler

class TransactionAdapter(itemView: View) : MyViewHolder<MyTransaction>(itemView) {

    private lateinit var transaction: MyTransaction
    private var textName: TextView = itemView.findViewById(R.id.textTransactionName)
    private var textDate: TextView = itemView.findViewById(R.id.textTransactionDate)
    private var textValue: TextView = itemView.findViewById(R.id.textTransactionValue)
    private var buttonStatus: Button = itemView.findViewById(R.id.buttonTransactionStatus)

    override fun setData(data: MyTransaction) {
        transaction = data

        textName.text = transaction.name
        textDate.text = transaction.date.toString()
        textValue.text = transaction.amount.toString()
        buttonStatus.text = transaction.transactionType.name
        Log.i("T", transaction.toString())

        AppDatabase.getInstance(itemView.context).BudgetTable().getByTransactionId(transaction.ID).asLiveData()
            .observe(itemView.context as LifecycleOwner) { budgets ->
                Log.i("Names", budgets.toString())
                createLinearRecycler(budgets.toTypedArray(), BudgetNameAdapter::class.java,
                    R.id.recyclerTransactionBudgetList, R.layout.recycler_budget_id,
                    itemView, RecyclerView.HORIZONTAL)
        }
    }

    override fun getData(): MyTransaction { return transaction }
}