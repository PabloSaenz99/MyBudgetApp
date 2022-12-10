package psb.mybudget.ui.recyclers.adapters

import android.view.View
import android.widget.TextView
import androidx.annotation.ColorRes
import psb.mybudget.R
import psb.mybudget.models.sql.AppDatabase
import psb.mybudget.ui.recyclers.MyViewHolder


class BudgetNameAdapter(itemView: View) : MyViewHolder<BudgetNameAdapter.Data>(itemView) {

    private lateinit var data: Data
    private var textName: TextView = itemView.findViewById(R.id.rbi_text_budgetName)

    override fun setData(data: Data) {
        this.data = data

        textName.text = this.data.budgetName

        if(this.data.isSelected)
            itemView.setBackgroundColor(itemView.context.getColor(this.data.color))
        else
            itemView.setBackgroundColor(itemView.context.getColor(R.color.disabled))

        itemView.setOnClickListener {
            if(this.data.isSelected) {
                AppDatabase.getInstance().TransactionBudgetTable().remove(this.data.transactionId, this.data.budgetID)
                itemView.setBackgroundColor(itemView.context.getColor(R.color.disabled))
            }
            else {
                AppDatabase.getInstance().TransactionBudgetTable().insert(this.data.transactionId, this.data.budgetID)
                itemView.setBackgroundColor(itemView.context.getColor(this.data.color))
            }
        }
    }

    override fun getData() = data

    class Data(val transactionId: String, val budgetID: String, val budgetName: String, @ColorRes val color: Int, val isSelected: Boolean){
        override fun toString(): String {
            return "[t $transactionId - b $budgetID - n $budgetName - sel $isSelected]"
        }
    }
}