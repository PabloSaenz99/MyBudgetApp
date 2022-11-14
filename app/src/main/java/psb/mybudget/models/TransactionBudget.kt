package psb.mybudget.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey

@Entity(primaryKeys = ["transactionId","budgetId"])
data class TransactionBudget(
    val transactionId: String,
    val budgetId: String,
)
