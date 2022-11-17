package psb.mybudget.models.sql

import android.content.Context
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import psb.mybudget.models.Transaction

@Dao
interface TransactionDAO {
    @Query("SELECT * FROM [Transaction]")
    fun getAll(): Flow<List<Transaction>>

    @Query("SELECT * FROM [Transaction] t " +
            "JOIN TransactionBudget tb ON t.ID = tb.transactionId " +
            "JOIN Budget b ON b.ID = :budgetId")
    fun getByBudgetId(budgetId: String): Flow<List<Transaction>>

    @Query("SELECT * FROM [Transaction] t WHERE t.ID = :transactionId")
    suspend fun getById(transactionId: String): Transaction

    @Insert
    suspend fun insert(trans: Transaction)

    @Delete
    suspend fun delete(transaction: Transaction)

    @Query("DELETE FROM [Transaction]")
    suspend fun deleteAll()
}