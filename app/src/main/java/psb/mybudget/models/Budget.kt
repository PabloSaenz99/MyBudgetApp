package psb.mybudget.models

import java.util.*

class Budget(val ID: String = UUID.randomUUID().toString(), var name: String, var description: String = "") {

    constructor(name1: String, description1: String = ""): this(name = name1, description = description1)

    private var transactionsList = mutableListOf<Transaction>()

    fun addTransaction(transaction: Transaction) {
        transaction.budgetIdList.add(ID)
        transactionsList.add(transaction)
    }

    fun removeTransaction(transaction: Transaction) {
        transaction.budgetIdList.remove(ID)
        transactionsList.remove(transaction)
    }

    fun getTransactionTotalAmount(): Double {
        var amount: Double = 0.0
        for (t in transactionsList)
            amount+=t.value
        return amount
    }

    override fun toString(): String{
        return "[ID: $ID - Name: $name - Desc: $description] - $transactionsList"
    }
}