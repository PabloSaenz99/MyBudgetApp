package psb.mybudget.ui.home.transactions

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.MutableLiveData
import psb.mybudget.R
import psb.mybudget.models.MyTransaction
import psb.mybudget.models.sql.AppDatabase
import psb.mybudget.models.sql.DBConverters
import psb.mybudget.ui.recyclers.MyRecycler
import psb.mybudget.ui.recyclers.adapters.BudgetNameAdapter
import psb.mybudget.ui.recyclers.createGridRecycler
import psb.mybudget.utils.removeFragment

class EditTransactionFragment(private val transaction: MyTransaction) : Fragment() {

    private lateinit var rootView: View
    private lateinit var textName: EditText
    private lateinit var textAmount: EditText
    private lateinit var calendar: CalendarView
    private lateinit var spinner: Spinner
    private lateinit var budgetRecycler: MyRecycler<BudgetNameAdapter, BudgetNameAdapter.Data>
    private var cancel: Boolean = false
    private var delete: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_edit_transaction, container, false)

        textName = rootView.findViewById(R.id.fet_editText_transactionName)
        textAmount = rootView.findViewById(R.id.fet_editNumber_transactionAmount)
        calendar = rootView.findViewById(R.id.fet_calendar_transactionDate)
        spinner = rootView.findViewById(R.id.fet_spinner_transactionStatus)

        rootView.findViewById<Button>(R.id.fet_button_cancel).setOnClickListener {
            cancel = !cancel
            removeFragment(this)
        }
        rootView.findViewById<Button>(R.id.fet_button_delete).setOnClickListener {
            delete = !delete
            removeFragment(this)
        }

        textName.setText(transaction.name)
        textAmount.setText(transaction.amount.toString())
        calendar.date = DBConverters().dateToTimestamp(transaction.date)!!
        ArrayAdapter.createFromResource(rootView.context, R.array.transaction_status_value, android.R.layout.simple_spinner_item)
            .also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                spinner.adapter = adapter
                spinner.setSelection(transaction.transactionType.ordinal)
            }

        val liveData = MutableLiveData<List<BudgetNameAdapter.Data>>()
        liveData.observe(viewLifecycleOwner) { budgets ->
            budgetRecycler = createGridRecycler(budgets.toTypedArray(), BudgetNameAdapter::class.java,
                R.id.fet_recycler_transactionBudgets, R.layout.recycler_budget_id, rootView, 4)
        }
        AppDatabase.getInstance(rootView.context).BudgetTable().getAllAndMatchByTransactionIdIn(transaction.ID, liveData)

        return rootView
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("Status", "Cancel: $cancel. Delete: $delete")
        if(!cancel) {
            if(delete) {
                AppDatabase.getInstance().TransactionTable().remove(transaction)
            }
            else{
                if(textName.text.toString() != "" && textAmount.text.toString().toDouble() != 0.0) {
                    transaction.name = textName.text.toString()
                    transaction.amount = textAmount.text.toString().toDouble()
                    transaction.date = DBConverters().fromTimestamp(calendar.date)!!
                    //transaction.transactionType = spinner.selectedItemPosition

                    //TODO: insert or update
                    AppDatabase.getInstance().TransactionTable().insertOrUpdate(transaction)
                }
                else{
                    Toast.makeText(context, "You must fill name and amount fields", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}