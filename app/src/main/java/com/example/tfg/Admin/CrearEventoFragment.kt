package com.example.tfg.Admin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.tfg.*
import com.example.tfg.api.ApiRest
import com.example.tfg.api.EventsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CrearEventoFragment : Fragment() {
    val TAG = "MainActivity"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_crear_evento, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ApiRest.initService()
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
            crearEvento(" ","Fundacion APU","Intermedio","Mixto", "11:00","23:00","01/04/2023")

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
    private fun crearEvento(foto_evento: String, titulo_evento: String, nivel: String, sexo: String, hora_inicio: String, hora_fin: String, fecha_evento: String) {
        val data = EventsResponse(
            EventsResponse.Data.Attributes(fecha_evento,foto_evento,hora_fin,hora_inicio,false,nivel,sexo,titulo_evento
            )
        )
        val call = ApiRest.service.crearEvento(data)
        call.enqueue(object : Callback<EventsResponse> {
            override fun onResponse(
                call: Call<EventsResponse>,
                response: Response<EventsResponse>
            ) {
                // maneja la respuesta exitosa aquí
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    var registroResponse = response.body()
                    print(registroResponse)
                    activity?.supportFragmentManager?.beginTransaction()
                        ?.replace(R.id.container, MainAdminFragment())?.commit()
                    Toast.makeText(context, "Evento creado", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e(TAG, response.errorBody()?.string()?: "Error")
                }
            }

            override fun onFailure(call: Call<EventsResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}