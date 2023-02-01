package psb.mybudget.models.sql

import androidx.annotation.WorkerThread
import androidx.room.*
import psb.mybudget.models.TransactionBudget

@Dao
interface TransactionBudgetDAO {

    @Query("SELECT * FROM TransactionBudget tb WHERE tb.budgetId = :budgetId")
    suspend fun getByBudgetId(budgetId: String): List<TransactionBudget>

    @Query("SELECT * FROM TransactionBudget tb WHERE tb.transactionId = :transactionId")
    suspend fun getByTransactionId(transactionId: String): List<TransactionBudget>

    @Query("SELECT * FROM TransactionBudget tb " +
            "WHERE tb.transactionId = :transactionId AND tb.budgetId = :budgetId")
    suspend fun getByTransactionAndBudgetId(transactionId: String, budgetId: String): TransactionBudget?

    @Query("SELECT COUNT(*) FROM TransactionBudget tb WHERE tb.transactionId = :transactionId")
    suspend fun getBudgetsCountBy(transactionId: String): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(transactionBudget: TransactionBudget)

    @Delete
    suspend fun delete(transactionBudget: TransactionBudget)

    @Query("DELETE FROM TransactionBudget WHERE TransactionBudget.transactionId = :transactionId")
    suspend fun deleteByTransactionId(transactionId: String)

    @Query("DELETE FROM TransactionBudget WHERE TransactionBudget.budgetId = :budgetId")
    suspend fun deleteByBudgetId(budgetId: String)

    @Query("DELETE FROM TransactionBudget")
    suspend fun deleteAll()

}