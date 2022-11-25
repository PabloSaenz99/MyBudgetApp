package psb.mybudget.utils

fun <T> addBooleanToList(list: List<T>, boolean: Boolean): List<Pair<T, Boolean>> {
    val res = mutableListOf<Pair<T, Boolean>>()
    for(element: T in list) {
        res.add(Pair(element, boolean))
    }
    return res
}
