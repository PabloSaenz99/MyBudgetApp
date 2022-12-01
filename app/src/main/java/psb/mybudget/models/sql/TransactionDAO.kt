package psb.mybudget.models.sql

import androidx.lifecycle.MutableLiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import psb.mybudget.models.MyTransaction

@Dao
interface TransactionDAO {
    @Query("SELECT * FROM MyTransaction")
    fun getAll(): Flow<List<MyTransaction>>

    @Query("SELECT * FROM MyTransaction t " +
            "INNER JOIN TransactionBudget tb ON t.ID = tb.transactionId " +
            "WHERE tb.budgetId = :budgetId")
    suspend fun getByBudgetId(budgetId: String): List<MyTransaction>

    @Query("SELECT * FROM MyTransaction t WHERE t.ID = :transactionId")
    suspend fun getById(transactionId: String): MyTransaction

    @Insert
    suspend fun insert(transaction: MyTransaction)

    @Delete
    suspend fun delete(transaction: MyTransaction)

    @Query("DELETE FROM MyTransaction")
    suspend fun deleteAll()

    @Update
    fun update(transaction: MyTransaction)
}