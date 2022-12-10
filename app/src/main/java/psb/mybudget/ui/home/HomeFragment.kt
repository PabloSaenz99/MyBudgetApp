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
import psb.mybudget.models.MyTransaction
import psb.mybudget.models.sql.AppDatabase
import psb.mybudget.ui.home.transactions.EditTransactionFragment
import psb.mybudget.ui.recyclers.adapters.BudgetAdapter
import psb.mybudget.ui.recyclers.createLinearRecycler
import psb.mybudget.utils.replaceFragment

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

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()

        val root: View = binding.root

        val homeViewModel = ViewModelProvider(this,
            HomeViewModelFactory(AppDatabase.getInstance(root.context)))[HomeViewModel::class.java]

        val adapter = createLinearRecycler(homeViewModel.budgetList.value?.toTypedArray()?: arrayOf(),
            BudgetAdapter::class.java, R.id.fh_recycler_budget, R.layout.recycler_budget, root)

        homeViewModel.budgetList.observe(viewLifecycleOwner) { budgets ->
            budgets?.let { adapter.setData(it.toTypedArray()) }
        }

        val button: FloatingActionButton = root.findViewById(R.id.fh_button_add)
        button.setOnClickListener {
            if((activity?.supportFragmentManager?.backStackEntryCount ?: 0) > 0)
                replaceFragment(EditTransactionFragment(MyTransaction()))
            else
                context?.startActivity(Intent(context, EditBudgetActivity::class.java))
        }
    }
}