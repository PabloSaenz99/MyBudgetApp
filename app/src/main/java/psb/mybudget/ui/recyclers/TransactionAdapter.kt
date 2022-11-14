package psb.mybudget.ui.recyclers

import android.view.View
import android.widget.Button
import android.widget.TextView
import psb.mybudget.R
import psb.mybudget.models.Transaction

class TransactionAdapter(itemView: View) : MyViewHolder<Transaction>(itemView) {

    lateinit var transaction: Transaction
    var textName: TextView = itemView.findViewById(R.id.textTransactionName)
    var textDate: TextView = itemView.findViewById(R.id.textTransactionDate)
    var textValue: TextView = itemView.findViewById(R.id.textTransactionValue)
    var buttonStatus: Button = itemView.findViewById(R.id.buttonTransactionStatus)

    override fun setData(data: Transaction) {
        transaction = data

        textName.text = transaction.name
        textDate.text = transaction.date.toString()
        textValue.text = transaction.amount.toString()
        buttonStatus.text = transaction.transactionType.name

        //TODO: add recycler
        //createGridRecycler(transaction.budgetIdList.toTypedArray(), )
    }

    override fun getData(): Transaction { return transaction }
}