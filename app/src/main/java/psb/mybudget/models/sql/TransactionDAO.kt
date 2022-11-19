package psb.mybudget.models.sql

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import psb.mybudget.models.MyTransaction

@Dao
interface TransactionDAO {
    @Query("SELECT * FROM MyTransaction")
    fun getAll(): Flow<List<MyTransaction>>

    @Query("SELECT * FROM MyTransaction t " +
            "INNER JOIN TransactionBudget tb ON t.ID = tb.transactionId " +
            "WHERE tb.budgetId = :budgetId")
    fun getByBudgetId(budgetId: String): Flow<List<MyTransaction>>

    @Query("SELECT * FROM MyTransaction t WHERE t.ID = :transactionId")
    suspend fun getById(transactionId: String): MyTransaction

    @Insert
    suspend fun insert(transaction: MyTransaction)

    @Delete
    suspend fun delete(transaction: MyTransaction)

    @Query("DELETE FROM MyTransaction")
    suspend fun deleteAll()
}