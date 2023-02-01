package psb.mybudget.ui.recyclers.adapters

import android.view.View
import android.widget.TextView
import androidx.annotation.ColorInt
import psb.mybudget.R
import psb.mybudget.models.sql.AppDatabase
import psb.mybudget.ui.MainActivity
import psb.mybudget.ui.MainActivity.Companion.getColorInt
import psb.mybudget.ui.MainActivity.Companion.getRoundedBackGround
import psb.mybudget.ui.recyclers.MyViewHolder
import psb.mybudget.utils.DEFAULT_BUDGET_ID


class NameAdapter(itemView: View) : MyViewHolder<NameAdapter.Data>(itemView) {

    private lateinit var data: Data
    private var textName: TextView = itemView.findViewById(R.id.rn_text_name)

    override fun setData(data: Data) {
        this.data = data

        textName.text = this.data.budgetName
        /*
        if(this.data.budgetID == DEFAULT_BUDGET_ID)
            itemView.visibility = View.GONE
        */
        if(!this.data.isSelected || this.data.budgetID == DEFAULT_BUDGET_ID)
            itemView.background = MainActivity.getStroke(R.color.disabled, null)
        else
            itemView.background = MainActivity.getStrokeFromColorIntResolved(this.data.color, this.data.color)

        if(this.data.budgetID != DEFAULT_BUDGET_ID) {
            itemView.setOnClickListener {
                if (this.data.isSelected) {
                    AppDatabase.getInstance().TransactionBudgetTable().remove(this.data.transactionId, this.data.budgetID)
                    itemView.background = MainActivity.getStroke(R.color.disabled, null)
                } else {
                    AppDatabase.getInstance().TransactionBudgetTable().insert(this.data.transactionId, this.data.budgetID)
                    itemView.background = MainActivity.getStrokeFromColorIntResolved(this.data.color, this.data.color)
                }
            }
        }
    }

    override fun getData() = data

    class Data(val transactionId: String, val budgetID: String, val budgetName: String, @ColorInt val color: Int, val isSelected: Boolean){
        override fun toString(): String {
            return "[t $transactionId - b $budgetID - n $budgetName - sel $isSelected]"
        }
    }
}