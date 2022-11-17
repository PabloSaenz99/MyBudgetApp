package psb.mybudget.models.sql

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import psb.mybudget.models.Budget
import psb.mybudget.models.Transaction
import psb.mybudget.models.TransactionBudget
import psb.mybudget.models.TransactionType

@Database(entities = [Transaction::class, TransactionBudget::class, Budget::class], version = 1)
@TypeConverters(DBConverters::class)
abstract class AppDatabase : RoomDatabase() {

    protected abstract fun transactionDAO(): TransactionDAO
    protected abstract fun transactionBudgetDAO(): TransactionBudgetDAO
    protected abstract fun budgetDAO(): BudgetDAO

    inner class BudgetTable {
        fun getAll() = budgetDAO().getAll()
        fun getByName(budgetName: String) = budgetDAO().getBudgetByName(budgetName)
        fun getById(budgetId: String) = scope?.launch{ budgetDAO().getBudgetById(budgetId)}

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
        fun getAll(budgetId: String) = transactionDAO().getByBudgetId(budgetId)

        fun add(transaction: Transaction, budgetId: String) {
            scope?.launch {
                transactionDAO().insert(transaction)
                transactionBudgetDAO().insert(TransactionBudget(transaction.ID, budgetId))
            }
        }

        fun remove(transaction: Transaction) {
            scope?.launch {
                transactionBudgetDAO().deleteByTransactionId(transaction.ID)
                transactionDAO().delete(transaction)
            }
        }
    }

    //Singleton
    companion object {
        @Volatile private var instance: AppDatabase? = null
        @Volatile private var scope: CoroutineScope? = null

        fun getInstance(context: Context, scope: CoroutineScope? = null): AppDatabase {
            return instance ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, AppDatabase::class.java, "AppDatabase"
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
                    val b1 = Budget("Food", 0.0, "Money spent in restaurants")
                    val b2 = Budget("Cinema", 0.0, "Money spent in cinema")
                    instance?.budgetDAO()?.insert(b1)
                    instance?.budgetDAO()?.insert(b2)

                    val t1 = Transaction("Mc Donalds", 3.45, TransactionType.PAID)
                    val t2 = Transaction("The lord of the rings", 0.69, TransactionType.RECEIVED)
                    val t3 = Transaction("Star Wars V and popcorns", -2.6, TransactionType.PAID)
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
