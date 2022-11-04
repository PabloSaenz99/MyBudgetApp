package psb.mybudget.models;

import java.util.*

class Transaction (val ID: String = UUID.randomUUID().toString(), val name: String,
                   val value: Double, val transactionType: TransactionType,
                   private val date: Date = Date(), val budgetIdList: MutableSet<String> = mutableSetOf()) {

    constructor(name: String, value: Double, transactionType: TransactionType, date: Date = Date(),
                budgetIdList: MutableSet<String> = mutableSetOf()):
            this(UUID.randomUUID().toString(), name, value, transactionType, date, budgetIdList)

    fun getDate(): Date = date

    override fun toString(): String{
        return "[ID: $ID - Name: $name - Value: $value - Date: $date] - $budgetIdList"
    }
}
