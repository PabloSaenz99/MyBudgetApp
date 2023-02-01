package psb.mybudget.ui.home.budgets

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import psb.mybudget.R
import psb.mybudget.models.sql.AppDatabase
import psb.mybudget.ui.home.transactions.EditTransactionActivity
import psb.mybudget.ui.recyclers.adapters.BudgetAdapter
import psb.mybudget.ui.recyclers.createLinearRecycler

class BudgetListFragment : Fragment() {

    // This property is only valid between onCreateView and onDestroyView.
    private var _view: View? = null
    private val rootView get() = _view!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _view = inflater.inflate(R.layout.fragment_budget_list, container, false)

        val root: View = rootView

        val budgetListViewModel = ViewModelProvider(this,
            BudgetListViewModelFactory(AppDatabase.getInstance(rootView.context))
        )[BudgetListViewModel::class.java]

        val adapter = createLinearRecycler(budgetListViewModel.budgetList.value?.toTypedArray()?: arrayOf(),
            BudgetAdapter::class.java, R.id.fh_recycler_budget, R.layout.recycler_budget, root)

        budgetListViewModel.budgetList.observe(viewLifecycleOwner) { budgets ->
            budgets?.let { adapter.setData(it.toTypedArray()) }
        }

        root.findViewById<FloatingActionButton>(R.id.fh_button_add).setOnClickListener {
            view?.findNavController()?.navigate(BudgetListFragmentDirections.actionNavigationBudgetToEditBudgetActivity())
        }

        return root
    }
}