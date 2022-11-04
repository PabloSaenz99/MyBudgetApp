package psb.mybudget.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import psb.mybudget.R
import psb.mybudget.controllers.BudgetController
import psb.mybudget.databinding.FragmentHomeBinding
import psb.mybudget.ui.recyclers.BudgetAdapter
import psb.mybudget.ui.recyclers.createLinearRecycler
import psb.mybudget.ui.recyclers.getGradientColor

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val bc = BudgetController

        createLinearRecycler(bc.getAllBudgets().toTypedArray(), BudgetAdapter::class.java,
                R.id.recyclerHomeBudgetList, R.layout.recycler_budget, root)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}