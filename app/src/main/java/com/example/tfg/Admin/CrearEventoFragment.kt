package com.example.tfg.Admin

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import com.example.tfg.*

class CrearEventoFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_crear_evento, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<EditText>(R.id.etFechaEvento).setOnClickListener() {
            showDatePickerDialog()
        }
        view.findViewById<EditText>(R.id.etHoraInicioEvento).setOnClickListener() {
            showTimePickerDialog(1)
        }
        view.findViewById<EditText>(R.id.etHoraFinEvento).setOnClickListener() {
            showTimePickerDialog(2)
        }
        view?.findViewById<Button>(R.id.btnCrearEvento)?.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.container, MainAdminFragment())?.commit()
        }
    }
    private fun showDatePickerDialog() {
        val datePicker = DatePickerEventoFragment { day, month, year -> onDateSelected(day, month, year) }
        getActivity()?.let { datePicker.show(it.getSupportFragmentManager(), "datePicker") }
    }

    private fun onDateSelected(day: Int, month: Int, year: Int) {
        view?.findViewById<EditText>(R.id.etFechaEvento)
            ?.setText("$day/$month/$year")
    }
    private fun showTimePickerDialog(){
        val timePicker = TimePickerFragment {onTimeSelectedInicio(it)}
        getActivity()?.let { timePicker.show(it.getSupportFragmentManager(), "datePicker") }
    }
    private fun showTimePickerDialog(i:Int){
        if (i==1){
            val timePicker = TimePickerFragment {onTimeSelectedInicio(it)}
            getActivity()?.let { timePicker.show(it.getSupportFragmentManager(), "datePicker") }
        }else{
            val timePicker2 = TimePickerFragment {onTimeSelectedFin(it)}
            getActivity()?.let { timePicker2.show(it.getSupportFragmentManager(), "datePicker") }
        }
    }
    private fun onTimeSelectedInicio(time:String){
        view?.findViewById<EditText>(R.id.etHoraInicioEvento)
            ?.setText("$time")
    }
    private fun onTimeSelectedFin(time:String){
        view?.findViewById<EditText>(R.id.etHoraFinEvento)
            ?.setText("$time")
    }
}