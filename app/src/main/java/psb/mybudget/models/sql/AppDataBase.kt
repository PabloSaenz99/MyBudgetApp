package psb.mybudget.models.sql

import android.content.Context
import android.graphics.Color
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import psb.mybudget.models.*
import psb.mybudget.R
import psb.mybudget.ui.recyclers.adapters.BudgetNameAdapter

@Database(entities = [MyTransaction::class, TransactionBudget::class, Budget::class], version = 1)
@TypeConverters(DBConverters::class)
abstract class AppDatabase : RoomDatabase() {

    protected abstract fun transactionDAO(): TransactionDAO
    protected abstract fun transactionBudgetDAO(): TransactionBudgetDAO
    protected abstract fun budgetDAO(): BudgetDAO

    inner class BudgetTable {
        fun getAll() = budgetDAO().getAll()
        fun getByName(budgetName: String) = budgetDAO().getBudgetByName(budgetName)
        suspend fun getById(budgetId: String) = budgetDAO().getBudgetById(budgetId)
        fun getByTransactionId(transactionId: String) = budgetDAO().getBudgetsByTransactionId(transactionId)
        fun getByTransactionIdIn(transactionId: String, destination: MutableLiveData<List<BudgetNameAdapter.Data>>){
            scope?.launch {
                budgetDAO().getBudgetsByTransactionId(transactionId).collect {
                    val aux = mutableListOf<BudgetNameAdapter.Data>()
                    it.forEach { b -> aux.add(BudgetNameAdapter.Data(transactionId, b.ID, b.name, b.color, true)) }
                    destination.postValue(aux)
                }
            }
        }

        fun getAllAndMatchByTransactionIdIn(transactionId: String, destination: MutableLiveData<List<BudgetNameAdapter.Data>>, editable: Boolean = false) {
            scope?.launch {
                getAll().collect { all ->
                    getByTransactionId(transactionId).collect { some ->
                        val aux = mutableListOf<BudgetNameAdapter.Data>()
                        all.forEach { b -> aux.add(BudgetNameAdapter.Data(transactionId, b.ID, b.name, b.color, some.contains(b))) }
                        destination.postValue(aux)
                    }
                }
            }
        }

        fun add(budget: Budget) { scope?.launch { budgetDAO().insert(budget) } }

        fun remove(budget: Budget) {
            //TODO: more complex, better delete
            scope?.launch {
                transactionBudgetDAO().deleteByBudgetId(budget.ID)
                budgetDAO().delete(budget)
            }
        }
    }

    inner class TransactionTable {
        fun getAllIn(budgetId: String, destination: MutableLiveData<List<MyTransaction>>){
            scope?.launch {
                destination.postValue(transactionDAO().getByBudgetId(budgetId).toMutableList())
            }
        }

        fun add(transaction: MyTransaction, budgetId: String) {
            scope?.launch {
                transactionDAO().insert(transaction)
                transactionBudgetDAO().insert(TransactionBudget(transaction.ID, budgetId))
            }
        }

        fun remove(transaction: MyTransaction) {
            scope?.launch {
                transactionBudgetDAO().deleteByTransactionId(transaction.ID)
                transactionDAO().delete(transaction)
            }
        }

        fun update(transaction: MyTransaction){
            scope?.launch {
                transactionDAO().update(transaction)
            }
        }
    }

    inner class TransactionBudgetTable() {
        fun insert(transactionId: String, budgetId: String){
            scope?.launch {
                transactionBudgetDAO().insert(TransactionBudget(transactionId, budgetId))
            }
        }

        fun remove(transactionId: String, budgetId: String){
            scope?.launch {
                transactionBudgetDAO().delete(TransactionBudget(transactionId, budgetId))
            }
        }
    }

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
                scope?.launch { populateDatabase() }
                // return instance
                instance
            }
        }

        private suspend fun populateDatabase() {
            /*
            instance?.budgetDAO()?.deleteAll()
            instance?.transactionDAO()?.deleteAll()
            instance?.transactionBudgetDAO()?.deleteAll()
            */

            instance?.budgetDAO()?.getAll()?.collect{ it ->
                if(it.isEmpty()) {
                    Color.BLUE
                    val b1 = Budget("Food", 0.0, R.color.gradient_blue, "Money spent in restaurants")
                    val b2 = Budget("Cinema", 0.0, R.color.gradient_yellow, "Money spent in cinema")
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
