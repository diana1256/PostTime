@file:Suppress("NAME_SHADOWING")

package work.time.worktim.ui.fragment


import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import java.time.LocalDate
import java.util.Calendar

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    interface DateSelectedListener {
        fun onDateSelected(selectedDate: LocalDate, endDate: LocalDate)

    }

    private var isStartDateSelected = true
    private lateinit var startDate: LocalDate
    private lateinit var endDate: LocalDate
    private var dateSelectedListener: DateSelectedListener? = null


    fun setDateSelectedListener(listener: DateSelectedListener) {
        this.dateSelectedListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(requireActivity(), this, year, month, day)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)

        if (isStartDateSelected) {
            startDate = selectedDate
            isStartDateSelected = false
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(requireActivity(), this, year, month, day)
            datePickerDialog.show()
        } else {
            endDate = selectedDate
            isStartDateSelected = true
            dateSelectedListener?.onDateSelected(startDate, endDate)
        }
    }
}


