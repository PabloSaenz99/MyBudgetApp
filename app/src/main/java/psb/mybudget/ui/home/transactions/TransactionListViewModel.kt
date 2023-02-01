package psb.mybudget.ui.home.transactions

import androidx.lifecycle.*
import psb.mybudget.models.Budget
import psb.mybudget.models.MyTransaction
import psb.mybudget.models.sql.AppDatabase

class TransactionListViewModel(appDatabase: AppDatabase, budgetId: String) : ViewModel() {

    val transactionList: MutableLiveData<List<MyTransaction>> = MutableLiveData()

    init { appDatabase.TransactionTable().getAllIn(budgetId, transactionList) }
}

class TransactionListViewModelFactory(val appDatabase: AppDatabase, val budgetId: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TransactionListViewModel(appDatabase, budgetId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
