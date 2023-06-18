package com.example.tfg
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
//subcalse de DialogFragment e implenta la interfaz de timepickerdialog
//toma paramentros de listener que es de tipo string utilizado para pasar la hora seleccionada
class TimePickerFragment(val listener: (String) -> Unit) :
    DialogFragment(), TimePickerDialog.OnTimeSetListener {
    //recibe  por paramentros la hora y los minutos que se han seleccionado en el timepiker
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        listener("$hourOfDay:$minute")
    }
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)
        val picker = TimePickerDialog(activity as Context, this, hour, minute, true)
        return picker
    }
}