package psb.mybudget.ui.home

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import psb.mybudget.models.Budget
import psb.mybudget.models.sql.AppDatabase
import psb.mybudget.models.sql.BudgetDAO

class HomeViewModel(appDatabase: AppDatabase) : ViewModel() {

    val budgetList: LiveData<List<Budget>> = appDatabase.BudgetTable().getAll().asLiveData()

}

class HomeViewModelFactory(private val appDatabase: AppDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(appDatabase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
