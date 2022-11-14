package psb.mybudget.ui.home

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import psb.mybudget.models.Budget
import psb.mybudget.models.sql.AppDatabase
import psb.mybudget.models.sql.BudgetDAO

class HomeViewModel(private val budgetDAO: BudgetDAO) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    val budgetList: LiveData<List<Budget>> = budgetDAO.getAll().asLiveData()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(budget: Budget) = viewModelScope.launch {
        budgetDAO.insert(budget)
    }

}

class HomeViewModelFactory(private val budgetDAO: BudgetDAO) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(budgetDAO) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
