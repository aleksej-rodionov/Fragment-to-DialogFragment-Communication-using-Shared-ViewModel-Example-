package space.rodionov.dialogfragmentdriller

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "ViewModel LOGS"

class EditTransactionViewModel(
    private val state: SavedStateHandle
) : ViewModel() {
    val sdf = SimpleDateFormat("dd.MM.yy", Locale.getDefault())

    //=====================DATE=========================================
    var dateFormatted = state.getLiveData("date", sdf.format(System.currentTimeMillis()))

    //=====================SUM==========================================
    var sum = state.get<Float>("sum") ?: 0f
        set(value) {
            field = value
            state.set("sum", value)
        }

    //=====================CATEGORY=====================================
    var category = state.getLiveData("category", "Food")

    //=====================COMMENT======================================
    var comment = state.get<String>("comment") ?: ""
        set(value) {
            field = value
            state.set("comment", value)
        }

    //=====================DEBTS========================================
    var debtReduced = state.getLiveData("debtReduced", "")

}










