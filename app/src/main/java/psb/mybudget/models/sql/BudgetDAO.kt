package psb.mybudget.models.sql

import androidx.annotation.WorkerThread
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import psb.mybudget.models.Budget
import psb.mybudget.models.Transaction

@Dao
interface BudgetDAO {
    @Query("SELECT * FROM Budget")
    fun getAll(): Flow<List<Budget>>

    @Query("SELECT * FROM Budget b WHERE b.ID = :budgetId")
    suspend fun getBudgetById(budgetId: String): Budget

    @Query("SELECT * FROM Budget b WHERE b.name = :budgetName")
    suspend fun getBudgetByName(budgetName: String): List<Budget>

    @Query("UPDATE Budget SET amount = :amount WHERE Budget.ID = :budgetId")
    suspend fun updateAmount(budgetId: String, amount: Double)

    @Insert
    suspend fun insert(budget: Budget)

    @Delete
    suspend fun delete(budget: Budget)

    @Query("DELETE FROM budget")
    suspend fun deleteAll()
}