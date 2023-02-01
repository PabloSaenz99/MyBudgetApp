package psb.mybudget.models

enum class TransactionType(val value: Int) {
    WITHHELD(0), PAID(1), RECEIVED(2), RECEIVE_PENDING(3);

    companion object {
        fun fromInt(value: Int) = values().first { it.value == value }
    }
}