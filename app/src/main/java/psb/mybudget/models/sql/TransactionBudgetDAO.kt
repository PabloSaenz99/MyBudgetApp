package psb.mybudget.models.sql

import androidx.annotation.WorkerThread
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import psb.mybudget.models.Transaction
import psb.mybudget.models.TransactionBudget

@Dao
interface TransactionBudgetDAO {

    @Query("SELECT * FROM TransactionBudget tb WHERE tb.budgetId = :budgetId")
    fun getByBudgetId(budgetId: String): List<TransactionBudget>

    @Query("SELECT * FROM TransactionBudget tb WHERE tb.transactionId = :transactionId")
    fun getByTransactionId(transactionId: String): List<TransactionBudget>

    @Query("SELECT * FROM TransactionBudget tb " +
            "WHERE tb.transactionId = :transactionId AND tb.budgetId = :budgetId")
    fun getByTransactionAndBudgetId(transactionId: String, budgetId: String): TransactionBudget

    @Insert
    suspend fun insert(transactionBudget: TransactionBudget)

    @Delete
    suspend fun delete(transactionBudget: TransactionBudget)

    @Query("DELETE FROM TransactionBudget")
    suspend fun deleteAll()

}