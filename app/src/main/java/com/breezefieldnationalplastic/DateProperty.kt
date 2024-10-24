package com.breezefieldnationalplastic

import androidx.core.util.Pair
import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateProperty {

    fun showDateRangePickerDialog(fm: FragmentManager, calendarConstraints: CalendarConstraints.Builder? = null, callback: (String, String) -> Unit){
        try {
            var dateRangePicker: MaterialDatePicker<Pair<Long, Long>>
            if(calendarConstraints !=null){
                dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
                    .setTitleText("Select dates")
                    //.setSelection(Pair(MaterialDatePicker.thisMonthInUtcMilliseconds(), MaterialDatePicker.todayInUtcMilliseconds()))
                    .setTheme(R.style.ThemeOverlay_App_DatePickerDialog)
                    .setCalendarConstraints(calendarConstraints!!.build())
                    .build()
            }else{
                dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
                    .setTitleText("Select dates")
                    //.setSelection(Pair(MaterialDatePicker.thisMonthInUtcMilliseconds(), MaterialDatePicker.todayInUtcMilliseconds()))
                    .setTheme(R.style.ThemeOverlay_App_DatePickerDialog)
                    .build()
            }



            dateRangePicker.show(fm, "date_range_picker")
            dateRangePicker.addOnPositiveButtonClickListener { selection ->
                val startDate = selection.first
                val endDate = selection.second

                val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val formattedStartDate = dateFormatter.format(Date(startDate!!)).toString()
                val formattedEndDate = dateFormatter.format(Date(endDate!!)).toString()

                callback(formattedStartDate, formattedEndDate)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            callback("", "")
        }
    }

}