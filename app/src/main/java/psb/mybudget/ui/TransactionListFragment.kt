package psb.mybudget.ui

import android.R.attr.country
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import psb.mybudget.R
import psb.mybudget.models.Budget
import psb.mybudget.models.MyTransaction
import psb.mybudget.models.sql.AppDatabase
import psb.mybudget.ui.recyclers.adapters.TransactionAdapter
import psb.mybudget.ui.recyclers.createLinearRecycler


/**
 * A simple [Fragment] subclass.
 * Use the [TransactionListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TransactionListFragment(private val budgetId: String) : Fragment() {

    private var _view: View? = null
    private val rootView get() = _view!!

    private lateinit var budget: Budget

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _view = inflater.inflate(R.layout.fragment_transaction_list, container, false)

        val db = AppDatabase.getInstance(rootView.context)

        val budgetView: View = rootView.findViewById(R.id.includeBudget)
        val budgetName: TextView = budgetView.findViewById(R.id.textBudgetName)
        val budgetDesc: TextView = budgetView.findViewById(R.id.textBudgetDescription)
        val budgetAmount: TextView = budgetView.findViewById(R.id.textBudgetTotalAmount)

        val spinner: Spinner = rootView.findViewById(R.id.spinnerFragmentTransactionSort)
        ArrayAdapter.createFromResource(rootView.context, R.array.sort_by, android.R.layout.simple_spinner_item)
            .also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }


        CoroutineScope(SupervisorJob()).launch {
            budget = db.BudgetTable().getById(budgetId)

            budgetName.text = budget.name
            budgetDesc.text = budget.description
            budgetAmount.text = budget.amount.toString() + "€"
        }

        val transactionList: LiveData<List<MyTransaction>> = db.TransactionTable().getAll(budgetId).asLiveData()
        transactionList.observe(viewLifecycleOwner, Observer { transactions ->
            Log.i("Trans", transactions.toString())
            createLinearRecycler(transactions.toTypedArray(), TransactionAdapter::class.java,
                R.id.recyclerTransactionList, R.layout.recycler_transaction, rootView)
        })

        return rootView
    }
}