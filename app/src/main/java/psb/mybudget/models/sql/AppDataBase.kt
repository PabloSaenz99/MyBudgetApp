package psb.mybudget.models.sql

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import psb.mybudget.R
import psb.mybudget.models.*
import psb.mybudget.ui.recyclers.adapters.NameAdapter
import psb.mybudget.utils.DEFAULT_BUDGET_ID
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

@Database(entities = [MyTransaction::class, TransactionBudget::class, Budget::class], version = 1)
@TypeConverters(DBConverters::class)
abstract class AppDatabase : RoomDatabase() {

    protected abstract fun transactionDAO(): TransactionDAO
    protected abstract fun transactionBudgetDAO(): TransactionBudgetDAO
    protected abstract fun budgetDAO(): BudgetDAO

    inner class BudgetTable {
        fun getAll() = budgetDAO().getAll()
        fun getAllIn(destination: MutableLiveData<List<Budget>>) {
            scope?.launch { budgetDAO().getAll().collect { destination.postValue(it) } }
        }
        fun getByName(budgetName: String) = budgetDAO().getBudgetByName(budgetName)
        suspend fun getAmount(budgetId: String) : String {
            val df = DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH))
            df.maximumFractionDigits = 2
            return if (isDefaultBudget(budgetId)) {
                df.format(budgetDAO().getDefaultBudgetAmount())
            } else {
                val res = budgetDAO().getAmountById(budgetId)
                if(res == null) "0.0"
                else df.format(res)
            }
        }
        suspend fun getById(budgetId: String) = budgetDAO().getBudgetById(budgetId)
        fun getByTransactionId(transactionId: String) = budgetDAO().getBudgetsByTransactionId(transactionId)
        fun getByTransactionIdIn(transactionId: String, destination: MutableLiveData<List<NameAdapter.Data>>){
            scope?.launch {
                budgetDAO().getBudgetsByTransactionId(transactionId).collect {
                    val aux = mutableListOf<NameAdapter.Data>()
                    it.forEach { b -> aux.add(NameAdapter.Data(transactionId, b.ID, b.name, b.color, true)) }
                    destination.postValue(aux)
                }
            }
        }

        fun getAllAndMatchByTransactionIdIn(transactionId: String, destination: MutableLiveData<List<NameAdapter.Data>>) {
            scope?.launch {
                getAll().collect { all ->
                    getByTransactionId(transactionId).collect { some ->
                        val aux = mutableListOf<NameAdapter.Data>()
                        all.forEach { b -> aux.add(NameAdapter.Data(transactionId, b.ID, b.name, b.color, some.contains(b))) }
                        destination.postValue(aux)
                    }
                }
            }
        }

        fun insertOrUpdate(budget: Budget) {
            if(budget.ID != DEFAULT_BUDGET_ID)
                scope?.launch {
                    if(budgetDAO().update(budget) == 0)
                        budgetDAO().insert(budget)
                }
        }

        fun remove(budget: Budget) {
            if(budget.ID != DEFAULT_BUDGET_ID) {
                scope?.launch {
                    TransactionBudgetTable().removeAllTransactionsFromBudget(budget.ID)
                    budgetDAO().delete(budget)
                }
            }
        }
    }

    inner class TransactionTable {
        suspend fun getById(transactionId: String) = transactionDAO().getById(transactionId)
        fun getAllIn(budgetId: String, destination: MutableLiveData<List<MyTransaction>>){
            scope?.launch {
                if(isDefaultBudget(budgetId))
                    destination.postValue(transactionDAO().getAll().toMutableList())
                else
                    destination.postValue(transactionDAO().getByBudgetId(budgetId).toMutableList())
            }
        }

        fun insertOrUpdate(transaction: MyTransaction, budgets: List<Budget>? = null) {
            scope?.launch {
                if(transactionDAO().update(transaction) == 0)
                    transactionDAO().insert(transaction)
                if(budgets.isNullOrEmpty()) {
                    TransactionBudgetTable().insertDefaultBudgetIfNoBudgets(transaction.ID)
                }
                else {
                    TransactionBudgetTable().insertTransactionIntoManyBudgets(transaction.ID, budgets)
                }
            }
        }

        fun remove(transaction: MyTransaction) {
            scope?.launch {
                transactionBudgetDAO().deleteByTransactionId(transaction.ID)
                transactionDAO().delete(transaction)
            }
        }
    }

    inner class TransactionBudgetTable() {
        fun insertTransactionIntoManyBudgets(transactionId: String, budgets: List<Budget>){
            scope?.launch {
                budgets.forEach { b -> transactionBudgetDAO().insert(TransactionBudget(transactionId, b.ID)) }
                if(hasDefaultBudget(transactionId) && transactionBudgetDAO().getByTransactionId(transactionId).size > 1) {
                    //If it has default budget and more than one budget, remove default
                    transactionBudgetDAO().delete(TransactionBudget(transactionId, DEFAULT_BUDGET_ID))
                }
            }
        }

        fun insertDefaultBudgetIfNoBudgets(transactionId: String) {
            scope?.launch {
                if(!hasBudgets(transactionId)) {
                    transactionBudgetDAO().insert(TransactionBudget(transactionId, DEFAULT_BUDGET_ID))
                }
            }
        }

        fun insert(transactionId: String, budgetId: String){
            if(budgetId != DEFAULT_BUDGET_ID)   //Cannot manually insert default budget
                scope?.launch {
                    transactionBudgetDAO().insert(TransactionBudget(transactionId, budgetId))
                    if(hasDefaultBudget(transactionId)) {   //If it has default budget, remove it (because it already has the above one)
                        transactionBudgetDAO().delete(TransactionBudget(transactionId, DEFAULT_BUDGET_ID))
                    }
                }
        }

        fun removeAllTransactionsFromBudget(budgetId: String) {
            if(budgetId != DEFAULT_BUDGET_ID) {
                scope?.launch {
                    val transactionsSet = mutableSetOf<String>()
                    val transactionBudgets = transactionBudgetDAO().getByBudgetId(budgetId) //Get all transactions with this budget
                    transactionBudgets.forEach { b ->   //Remove this budget from all transactions
                        transactionBudgetDAO().delete(TransactionBudget(b.transactionId, budgetId))
                        transactionsSet.add(b.transactionId)
                    }
                    transactionsSet.forEach { t ->      //Add default budget to this transaction
                        if(!hasBudgets(t))
                            transactionBudgetDAO().insert(TransactionBudget(t, DEFAULT_BUDGET_ID))
                    }
                }
            }
        }

        fun remove(transactionId: String, budgetId: String){
            if(budgetId != DEFAULT_BUDGET_ID)   //Cannot manually remove default budget
                scope?.launch {
                    transactionBudgetDAO().delete(TransactionBudget(transactionId, budgetId))
                    if(!hasBudgets(transactionId)) {    //If it hasn't any budget, add default
                        transactionBudgetDAO().insert(TransactionBudget(transactionId, DEFAULT_BUDGET_ID))
                    }
                }
        }

        private suspend fun hasBudgets(transactionId: String): Boolean =
            transactionBudgetDAO().getBudgetsCountBy(transactionId) > 0

        private suspend fun hasDefaultBudget(transactionId: String): Boolean =
            transactionBudgetDAO().getByTransactionAndBudgetId(transactionId, DEFAULT_BUDGET_ID) != null
    }

    private fun isDefaultBudget(budgetId: String) = budgetId == DEFAULT_BUDGET_ID

    //Singleton
    companion object {
        @Volatile private var instance: AppDatabase? = null
        @Volatile private var scope: CoroutineScope? = null

        fun getInstance(context: Context? = null, scope: CoroutineScope? = null): AppDatabase {
            return instance ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context!!.applicationContext, AppDatabase::class.java, "AppDatabase"
                ).build()
                this.scope = scope
                this.instance = instance

                //Add elements to the database
                scope?.launch { populateDatabase(context) }
                // return instance
                instance
            }
        }

        private suspend fun populateDatabase(context: Context) {
            /*
            instance?.budgetDAO()?.deleteAll()
            instance?.transactionDAO()?.deleteAll()
            instance?.transactionBudgetDAO()?.deleteAll()
            */

            instance?.budgetDAO()?.getAll()?.collect{ it ->
                if(it.isEmpty()) {
                    val b0 = Budget(DEFAULT_BUDGET_ID, "All transactions", context.resources.getColor(R.color.white), "Money spent in all transactions")
                    val b1 = Budget("Food",  context.resources.getColor(R.color.gradient_blue), "Money spent in restaurants")
                    val b2 = Budget("Cinema", context.resources.getColor(R.color.gradient_yellow), "Money spent in cinema")
                    instance?.budgetDAO()?.insert(b0)
                    instance?.budgetDAO()?.insert(b1)
                    instance?.budgetDAO()?.insert(b2)

                    val t1 = MyTransaction("Mc Donalds", 3.45, TransactionType.PAID)
                    val t2 = MyTransaction("The lord of the rings", 0.69, TransactionType.RECEIVED)
                    val t3 = MyTransaction("Star Wars V and popcorns", -2.6, TransactionType.PAID)
                    instance?.transactionDAO()?.insert(t1)
                    instance?.transactionDAO()?.insert(t2)
                    instance?.transactionDAO()?.insert(t3)

                    //Add to common table
                    instance?.transactionBudgetDAO()?.insert(TransactionBudget(t1.ID, b1.ID))
                    instance?.transactionBudgetDAO()?.insert(TransactionBudget(t2.ID, b2.ID))
                    instance?.transactionBudgetDAO()?.insert(TransactionBudget(t3.ID, b1.ID))
                    instance?.transactionBudgetDAO()?.insert(TransactionBudget(t3.ID, b2.ID))
                }
            }
        }

    }
}
