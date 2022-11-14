package psb.mybudget.models.sql

import android.app.usage.NetworkStats
import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import psb.mybudget.models.Budget
import psb.mybudget.models.Transaction
import psb.mybudget.models.TransactionBudget
import psb.mybudget.models.TransactionType

@Database(entities = [Transaction::class, TransactionBudget::class, Budget::class], version = 1)
@TypeConverters(DBConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDAO(): TransactionDAO
    abstract fun transactionBudgetDAO(): TransactionBudgetDAO
    abstract fun budgetDAO(): BudgetDAO

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context, scope: CoroutineScope? = null): AppDatabase {
            return instance ?: synchronized(this) {
                //instance ?: buildDatabase(context).also { instance = it }
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "word_database"
                ).build()
                this.instance = instance
                //Add elements to the database
                scope?.launch { populateDatabase() }
                // return instance
                instance
            }
        }

        private suspend fun populateDatabase() {

            instance?.budgetDAO()?.deleteAll()
            instance?.transactionDAO()?.deleteAll()
            instance?.transactionBudgetDAO()?.deleteAll()


            instance?.budgetDAO()?.getAll()?.collect{
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
