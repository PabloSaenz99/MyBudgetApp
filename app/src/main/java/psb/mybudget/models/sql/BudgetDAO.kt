package psb.mybudget.models.sql

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import psb.mybudget.models.Budget
import psb.mybudget.models.MyTransaction
import psb.mybudget.utils.DEFAULT_BUDGET_ID

@Dao
interface BudgetDAO {

    @Query("SELECT * FROM Budget")
    fun getAll(): Flow<List<Budget>>

    @Query("SELECT * FROM Budget b WHERE b.name = :budgetName")
    fun getBudgetByName(budgetName: String): Flow<List<Budget>>

    @Query("SELECT * FROM Budget b " +
            "INNER JOIN TransactionBudget tb ON b.ID = tb.budgetId " +
            "WHERE tb.transactionId = :transactionId")
    fun getBudgetsByTransactionId(transactionId: String): Flow<List<Budget>>

    @Query("SELECT * FROM Budget b WHERE b.ID = '${DEFAULT_BUDGET_ID}'")
    suspend fun getDefaultBudget(): Budget

    @Query("SELECT * FROM Budget b WHERE b.ID = :budgetId")
    suspend fun getBudgetById(budgetId: String): Budget

    @Query("SELECT SUM(amount) FROM MyTransaction t INNER JOIN TransactionBudget tb ON t.ID = tb.transactionId AND tb.budgetId = :budgetId")
    suspend fun getAmountById(budgetId: String): Double?

    @Query("SELECT SUM(amount) FROM MyTransaction")
    suspend fun getDefaultBudgetAmount(): Double

    @Insert
    suspend fun insert(budget: Budget)

    @Update
    suspend fun update(budget: Budget): Int

    @Delete
    suspend fun delete(budget: Budget)

    @Query("DELETE FROM budget")
    suspend fun deleteAll()
}