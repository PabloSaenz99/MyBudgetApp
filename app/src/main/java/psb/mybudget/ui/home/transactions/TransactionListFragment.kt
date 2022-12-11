package psb.mybudget.ui.home.transactions

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import psb.mybudget.R
import psb.mybudget.models.Budget
import psb.mybudget.models.MyTransaction
import psb.mybudget.models.TransactionType
import psb.mybudget.models.sql.AppDatabase
import psb.mybudget.ui.MainActivity.Companion.getColorInt
import psb.mybudget.ui.MainActivity.Companion.getStroke
import psb.mybudget.ui.recyclers.MyRecycler
import psb.mybudget.ui.recyclers.adapters.TransactionAdapter
import psb.mybudget.ui.recyclers.createLinearRecycler
import psb.mybudget.utils.addBooleanToList


/**
 * A simple [Fragment] subclass.
 * Use the [TransactionListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TransactionListFragment(private val budgetId: String) : Fragment() {

    private var _view: View? = null
    private val rootView get() = _view!!

    private lateinit var budget: Budget
    private lateinit var db: AppDatabase
    private val transactionList: MutableLiveData<List<MyTransaction>> = MutableLiveData(listOf())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _view = inflater.inflate(R.layout.fragment_transaction_list, container, false)

        db = AppDatabase.getInstance(rootView.context)

        val budgetView: View = rootView.findViewById(R.id.includeBudget)
        val budgetName: TextView = budgetView.findViewById(R.id.rb_text_budgetName)
        val budgetDesc: TextView = budgetView.findViewById(R.id.rb_text_budgetDescription)
        val budgetAmount: TextView = budgetView.findViewById(R.id.rb_text_budgetAmount)

        val spinner: Spinner = rootView.findViewById(R.id.spinnerFragmentTransactionSort)
        ArrayAdapter.createFromResource(rootView.context, R.array.sort_by, android.R.layout.simple_spinner_item)
            .also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
        spinner.background = getStroke(R.color.white, null)

        CoroutineScope(SupervisorJob()).launch {
            budget = db.BudgetTable().getById(budgetId)

            budgetName.text = budget.name
            budgetDesc.text = budget.description
            budgetAmount.text = "${db.BudgetTable().getAmount(budgetId)} €"

            val gd = GradientDrawable()
            gd.setColor(budget.color)
            rootView.background = gd
        }

        transactionList.observe(viewLifecycleOwner, Observer { transactions ->
            createLinearRecycler(
                addBooleanToList(transactions, false).toTypedArray(), TransactionAdapter::class.java,
                R.id.recyclerTransactionList, R.layout.recycler_transaction, rootView)
        })

        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                when(position){
                    0 -> transactionList.value = transactionList.value?.sortedBy { it.date }
                    1 -> transactionList.value = transactionList.value?.sortedByDescending { it.date }
                    2 -> transactionList.value = transactionList.value?.sortedBy { it.amount }
                    3 -> transactionList.value = transactionList.value?.sortedByDescending { it.amount }
                    4 -> transactionList.value = transactionList.value?.sortedByDescending { it.transactionType == TransactionType.RECEIVED }
                    5 -> transactionList.value = transactionList.value?.sortedByDescending { it.transactionType == TransactionType.PAID }
                    6 -> transactionList.value = transactionList.value?.sortedByDescending { it.transactionType == TransactionType.WITHHELD }
                    7 -> transactionList.value = transactionList.value?.sortedByDescending { it.transactionType == TransactionType.RECEIVE_PENDING }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        return rootView
    }

    override fun onResume() {
        super.onResume()
        db.TransactionTable().getAllIn(budgetId, transactionList)
    }
}