package com.example.tfg
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
//subcalse de DialogFragment e implenta la interfaz de DatePickerDialog
//toma paramentros de listener que es de tipo string utilizado para pasar la fecha seleccionada
class DatePickerFragment(val listener: (day: Int, month: Int, year: Int) -> Unit) :
    DialogFragment(), DatePickerDialog.OnDateSetListener {
    //recibe  por paramentros la hora y los minutos que se han seleccionado en el datepicker
    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        listener(day, month, year)
    }
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val picker = DatePickerDialog(activity as Context, this, year, month, day)
        picker.datePicker.maxDate = c.timeInMillis
        return picker
    }
}