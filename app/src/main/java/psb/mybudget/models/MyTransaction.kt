package psb.mybudget.models;

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class MyTransaction (
    @PrimaryKey val ID: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "amount") val amount: Double,
    @ColumnInfo(name = "transactionType") val transactionType: TransactionType,
    @ColumnInfo(name = "date") val date: Date = Date()) {

    constructor(name: String, value: Double, transactionType: TransactionType, date: Date = Date()):
            this(UUID.randomUUID().toString(), name, value, transactionType, date)

    //val budgetIdList: MutableSet<String> = mutableSetOf()

    override fun toString(): String{
        return "[ID: $ID - Name: $name - Value: $amount - Date: $date] - "
    }
}
