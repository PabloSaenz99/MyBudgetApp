package psb.mybudget.ui.home.budgets

import androidx.lifecycle.*
import psb.mybudget.models.Budget
import psb.mybudget.models.sql.AppDatabase

class BudgetListViewModel(private val appDatabase: AppDatabase) : ViewModel() {

    val budgetList: MutableLiveData<List<Budget>> = MutableLiveData()

    init { appDatabase.BudgetTable().getAllIn(budgetList) }
}

class BudgetListViewModelFactory(private val appDatabase: AppDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BudgetListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BudgetListViewModel(appDatabase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
