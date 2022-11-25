package psb.mybudget.models.sql

import android.content.Context
import android.graphics.Color
import androidx.room.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import psb.mybudget.models.*
import psb.mybudget.R

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
        fun getByTransactionId(transactionId: String) = budgetDAO().getBudgetNamesColorsByTransactionId(transactionId)

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
        fun getAllIn(budgetId: String, destination: MutableList<MyTransaction>){
            scope?.launch {
                destination.clear()
                destination.addAll(transactionDAO().getByBudgetId(budgetId))
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
