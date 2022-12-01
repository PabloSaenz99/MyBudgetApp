package psb.mybudget.models.sql

import androidx.room.TypeConverter
import psb.mybudget.models.Budget
import java.util.*


class DBConverters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}