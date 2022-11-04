package psb.mybudget.controllers

import psb.mybudget.models.Budget
import psb.mybudget.models.Transaction

/**
 * Is an "object" and not a class to make it Singleton
 */
object BudgetController {

    private var totalBudget: Double
    private var totalSpent: Double
    private val budgetsList: MutableMap<String, Budget> = mutableMapOf()
    private val transactionsList: MutableSet<Transaction> = mutableSetOf()

    init {
        loadAllTransactions()
        loadAllBudgets()

        totalBudget = 0.0
        totalSpent = calcTotalSpent()
    }

    fun addBudget(budget: Budget): String {
        budgetsList.putIfAbsent(budget.ID, budget)
        saveAll()
        return budget.ID
    }

    fun removeBudget(budgetID: String) {
        for(t in transactionsList)
            t.budgetIdList.remove(budgetID)
        budgetsList.remove(budgetID)
        saveAll()
    }

    fun getBudgetById(budgetID: String): Budget? {
        return budgetsList[budgetID]
    }

    fun getBudgetsByName(name: String): List<Budget> {
        val res = mutableListOf<Budget>()
        for(budget in budgetsList.values){
            if(budget.name == name)
                res.add(budget)
        }
        return res
    }

    fun getAllBudgets(): List<Budget> = budgetsList.values.toList()

    fun addTransactionToBudget(transaction: Transaction, budgetID: String){
        budgetsList[budgetID]?.addTransaction(transaction)
        saveAll()
    }

    fun removeTransactionFromBudget(transaction: Transaction, budgetID: String){
        budgetsList[budgetID]?.removeTransaction(transaction)
        saveAll()
    }

    private fun saveAll() {
        //TODO: Save current budget controller to file
    }

    /*==============================================================================================
    ====================================INIT FUNCTIONS==============================================
    ==============================================================================================*/
    private fun loadAllTransactions() {
        //TODO: Charge transactions from file
    }

    private fun loadAllBudgets() {
        //TODO: Charge budgets from file

        //Add transactions to budget
        for(t in transactionsList) {
            for (s in t.budgetIdList)
                if(budgetsList.containsKey(s))
                    budgetsList[s]!!.addTransaction(t)
        }
    }

    private fun calcTotalSpent(): Double {
        var amount = 0.0
        for(t in transactionsList)
            amount+=t.value
        return amount
    }
}