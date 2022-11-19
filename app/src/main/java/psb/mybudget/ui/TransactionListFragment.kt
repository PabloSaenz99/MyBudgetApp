package psb.mybudget.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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

        val budgetName: TextView = rootView.findViewById(R.id.textBudgetName)
        val budgetDesc: TextView = rootView.findViewById(R.id.textBudgetDescription)
        val budgetAmount: TextView = rootView.findViewById(R.id.textBudgetTotalAmount)

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

        return inflater.inflate(R.layout.fragment_transaction_list, container, false)
    }
}