package psb.mybudget.models

import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import psb.mybudget.R
import psb.mybudget.ui.MainActivity
import psb.mybudget.ui.recyclers.getGradientColorFromRes
import java.util.*

@Entity
data class Budget(
    @PrimaryKey val ID: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "color") @ColorInt var color: Int,
    @ColumnInfo(name = "description") var description: String = "") {

    constructor(name1: String, color1: Int, description1: String = ""):
            this(UUID.randomUUID().toString(), name1, color1, description1)

    constructor(): this(UUID.randomUUID().toString(), "", MainActivity.getColorInt(R.color.gradient_blue),"")

    override fun toString(): String{
        return "{ID: $ID - Name: $name - Desc: $description - Color: $color}"
    }
}