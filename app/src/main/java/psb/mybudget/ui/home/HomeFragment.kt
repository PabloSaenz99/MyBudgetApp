package psb.mybudget.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import psb.mybudget.R
import psb.mybudget.databinding.FragmentHomeBinding
import psb.mybudget.models.sql.AppDatabase
import psb.mybudget.ui.home.transactions.EditTransactionActivity
import psb.mybudget.ui.recyclers.adapters.BudgetAdapter
import psb.mybudget.ui.recyclers.createLinearRecycler

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val root: View = binding.root

        val homeViewModel = ViewModelProvider(this,
            HomeViewModelFactory(AppDatabase.getInstance(binding.root.context)))[HomeViewModel::class.java]

        val adapter = createLinearRecycler(homeViewModel.budgetList.value?.toTypedArray()?: arrayOf(),
            BudgetAdapter::class.java, R.id.fh_recycler_budget, R.layout.recycler_budget, root)

        homeViewModel.budgetList.observe(viewLifecycleOwner) { budgets ->
            budgets?.let { adapter.setData(it.toTypedArray()) }
        }

        root.findViewById<FloatingActionButton>(R.id.fh_button_add).setOnClickListener {
            if((activity?.supportFragmentManager?.backStackEntryCount ?: 0) > 0)
                context?.startActivity(Intent(context, EditTransactionActivity::class.java))
            else
                context?.startActivity(Intent(context, EditBudgetActivity::class.java))
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}