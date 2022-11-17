package psb.mybudget.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import psb.mybudget.R
import psb.mybudget.databinding.FragmentHomeBinding
import psb.mybudget.models.Budget
import psb.mybudget.models.Transaction
import psb.mybudget.models.sql.AppDatabase
import psb.mybudget.ui.recyclers.TransactionAdapter
import psb.mybudget.ui.recyclers.createLinearRecycler

/**
 * A simple [Fragment] subclass.
 * Use the [TransactionListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TransactionListFragment(private val budgetId: String) : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var bundleId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            bundleId = it.getString("BundleID")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val db = AppDatabase.getInstance(root.context)

        //TODO:
        val budget = db.BudgetTable().getById(budgetId)
        val transactionList: LiveData<List<Transaction>> = db.TransactionTable().getAll(budget.ID).asLiveData()
        transactionList.observe(viewLifecycleOwner, Observer { transactions ->
            createLinearRecycler(transactions.toTypedArray(), TransactionAdapter::class.java,
                R.id.recyclerTransactionBudgetList, R.layout.recycler_transaction, root)
        })

        return inflater.inflate(R.layout.fragment_transaction_list, container, false)
    }
}