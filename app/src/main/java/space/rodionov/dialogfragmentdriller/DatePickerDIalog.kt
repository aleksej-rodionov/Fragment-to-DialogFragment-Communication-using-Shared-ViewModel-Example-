package space.rodionov.dialogfragmentdriller

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "DatePickerDIalog LOGS"

class DatePickerDIalog : DialogFragment(), DatePickerDialog.OnDateSetListener {

    companion object {
        const val TAG = "DatePickerDialog"
    }

    private val viewModel: EditTransactionViewModel by viewModels({ requireParentFragment() })
    val sdf = SimpleDateFormat("dd.MM.yy", Locale.getDefault())

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        Log.d(TAG, "onCreateDialog: VIZVAN")

        val dateFormatted = viewModel.dateFormatted.value ?: sdf.format(System.currentTimeMillis()) // to viewModel
        val dateParsed = SimpleDateFormat("dd.MM.yy", Locale.getDefault()).parse(dateFormatted) // to viewModel
        val calendar = Calendar.getInstance() // to viewModel
        calendar.time = dateParsed
        val todayYear = calendar.get(Calendar.YEAR)
        val todayMonth = calendar.get(Calendar.MONTH)
        val todayDay = calendar.get(Calendar.DAY_OF_MONTH)
        return DatePickerDialog(requireContext(), this, todayYear, todayMonth, todayDay)
    }

    override fun onDateSet(datePicker: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val newCalendar = Calendar.getInstance()
        newCalendar.set(Calendar.YEAR, year);
        newCalendar.set(Calendar.MONTH, month);
        newCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        val newDateFormatted = sdf.format(newCalendar.timeInMillis)
        viewModel.dateFormatted.value = newDateFormatted
    }
}









