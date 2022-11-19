package psb.mybudget.models.sql

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import psb.mybudget.models.Budget

@Dao
interface BudgetDAO {
    @Query("SELECT * FROM Budget")
    fun getAll(): Flow<List<Budget>>

    @Query("SELECT * FROM Budget b WHERE b.name = :budgetName")
    fun getBudgetByName(budgetName: String): Flow<List<Budget>>

    @Query("SELECT * FROM Budget b WHERE b.ID = :budgetId")
    suspend fun getBudgetById(budgetId: String): Budget

    @Query("SELECT name FROM Budget b " +
            "INNER JOIN TransactionBudget tb ON b.ID = tb.budgetId " +
            "WHERE tb.transactionId = :transactionId")
    fun getBudgetNamesByTransactionId(transactionId: String): Flow<List<String>>

    //@Query("SELECT SUM(amount) FROM MyTransaction t INNER JOIN TransactionBudget tb ON t.ID = tb.transactionId AND tb.budgetId = :budgetId")
    //suspend fun getAmount(budgetId: String)

    @Query("UPDATE Budget SET amount = :amount WHERE Budget.ID = :budgetId")
    suspend fun updateAmount(budgetId: String, amount: Double)

    @Insert
    suspend fun insert(budget: Budget)

    @Delete
    suspend fun delete(budget: Budget)

    @Query("DELETE FROM budget")
    suspend fun deleteAll()
}