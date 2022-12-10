package psb.mybudget.models;

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class MyTransaction (
    @PrimaryKey val ID: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "amount") var amount: Double,
    @ColumnInfo(name = "transactionType") var transactionType: TransactionType,
    @ColumnInfo(name = "date") var date: Date = Date(),
    @ColumnInfo(name = "description") var description: String = "") {

    constructor(name: String, value: Double, transactionType: TransactionType, date: Date = Date(), description: String = ""):
            this(UUID.randomUUID().toString(), name, value, transactionType, date, description)
    constructor(): this(UUID.randomUUID().toString(), "", 0.0, TransactionType.RECEIVED, Date(), "")

    override fun toString(): String{
        return "[ID: $ID - Name: $name - Description: $description - Value: $amount - Date: $date]"
    }
}
