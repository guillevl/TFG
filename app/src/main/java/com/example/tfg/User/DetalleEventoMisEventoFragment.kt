package com.example.tfg.User

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tfg.MainActivity
import com.example.tfg.R
import com.example.tfg.api.ApiRest
import com.example.tfg.api.EventData
import com.example.tfg.api.UsersEventsResponse
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetalleEventoMisEventoFragment : Fragment() {
    lateinit var usuario: UsersEventsResponse
    lateinit var objEvento: EventData
    lateinit var evento: UsersEventsResponse.Event
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detalle_evento_mis_evento, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.isVisible = false
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationViewAdmin)?.isVisible =
            false
        val eventIds =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                arguments?.getString("eventIds")
            } else {
                arguments?.getString("eventIds")
            }
        getEventById(eventIds!!, view)
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Unirte a el evento")
            .setMessage("¿Seguro que deseas salir del evento?")
            .setPositiveButton("Sí") { dialog, which ->
                // Acciones a realizar si el usuario presiona el botón "Sí"
                updateRelacion(usuario.id.toString(), usuario)
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.container, MisEventosFragment())?.commit()
            }
            .setNegativeButton("No") { dialog, which ->
                // Acciones a realizar si el usuario presiona el botón "No"
            }
        val alerta = builder.create()

        view?.findViewById<Button>(R.id.btnSalirEventoMisEventos)?.setOnClickListener {
            alerta.show()
        }
    }

    private fun getEventById(id: String, view: View) {
        val call = ApiRest.service.getEventById(id)
        call.enqueue(object : Callback<EventData> {
            override fun onResponse(call: Call<EventData>, response: Response<EventData>) {
                // maneja la respuesta exitosa aquí
                val body = response.body()
                objEvento = body!!
                if (response.isSuccessful && body != null) {
                    view?.findViewById<TextView>(R.id.tvFechaEventoDetalleMisEventos)?.text =
                        body.data.attributes.fecha_evento
                    view?.findViewById<TextView>(R.id.tvTituloEventoDetalleMisEventos)?.text =
                        body.data.attributes.titulo_evento
                    val hora_inicio = body.data.attributes.hora_inicio.substring(0, 5)
                    val hora_fin = body.data.attributes.hora_fin.substring(0, 5)
                    view?.findViewById<TextView>(R.id.tvHoraEventoDetalleMisEventos)?.text =
                        hora_inicio + " - " + hora_fin
                    view?.findViewById<TextView>(R.id.tvNivelEventoDetalleMisEventos)?.text =
                        body.data.attributes.nivel
                    view?.findViewById<TextView>(R.id.tvSexoEventoDetalleMisEventos)?.text =
                        ":   " + body.data.attributes.sexo
                    val listaUsersEventosMisEventos = body.data.attributes.users.data
                    Glide.with(view)
                        .load(body.data.attributes.foto_evento)
                        .into(view.findViewById<ImageView>(R.id.ivFotoEventoDetalleMisEventos))
                    var rvUserInfo =
                        view.findViewById<RecyclerView>(R.id.rvDetalleEventosMisEventos)
                    rvUserInfo.layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    rvUserInfo.adapter =
                        DetalleEventosAdapterMisEventos(listaUsersEventosMisEventos) {}
                    Log.i("EditProfileFragment", body.toString())
                    val sharedPreferencesGet =
                        requireContext().getSharedPreferences("login", Context.MODE_PRIVATE)
                    val getID = sharedPreferencesGet.getInt("userID", 0)
                    getUsersEvents(getID.toString(), view)
                } else {
                    Log.e("EditProfileFragment", response.errorBody()?.string() ?: "Error")
                }
            }

            override fun onFailure(call: Call<EventData>, t: Throwable) {
                Log.e("EditProfileFragment", "Error: ${t.message}")
            }
        })
    }

    private fun getUsersEvents(id: String, view: View) {
        val call = ApiRest.service.getUsersEvents(id)
        call.enqueue(object : Callback<UsersEventsResponse> {
            override fun onResponse(
                call: Call<UsersEventsResponse>,
                response: Response<UsersEventsResponse>
            ) {
                // maneja la respuesta exitosa aquí
                val body = response.body()
                usuario = body!!
                if (response.isSuccessful && body != null) {
                    var listEventos = body.events
                    evento = UsersEventsResponse.Event(
                        objEvento.data.attributes.fecha_evento,
                        objEvento.data.attributes.foto_evento,
                        objEvento.data.attributes.hora_fin,
                        objEvento.data.attributes.hora_inicio,
                        objEvento.data.id,
                        objEvento.data.attributes.isFinished,
                        objEvento.data.attributes.nivel,
                        objEvento.data.attributes.sexo,
                        objEvento.data.attributes.titulo_evento,
                        objEvento.data.attributes.updatedAt
                    )
                    listEventos.remove(evento)
                    usuario.events = listEventos
                    Log.i("EditProfileFragment", body.toString())
                } else {
                    Log.e("EditProfileFragment", response.errorBody()?.string() ?: "Error")
                }
            }

            override fun onFailure(call: Call<UsersEventsResponse>, t: Throwable) {
                Log.e("EditProfileFragment", "Error: ${t.message}")
            }
        })
    }

    private fun updateRelacion(id: String, updateRelacion: UsersEventsResponse) {
        val call = ApiRest.service.updateRelacion(updateRelacion, id)
        call.enqueue(object : Callback<UsersEventsResponse> {
            override fun onResponse(
                call: Call<UsersEventsResponse>,
                response: Response<UsersEventsResponse>
            ) {
                // maneja la respuesta exitosa aquí
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    // procesa la respuesta aquí
                } else {
                    Log.e("EditProfileFragment", response.errorBody()?.string() ?: "Error")
                }
            }

            override fun onFailure(call: Call<UsersEventsResponse>, t: Throwable) {
                Log.e("EditProfileFragment", "Error: ${t.message}")
            }
        })
    }

    override fun onStop() {
        super.onStop()
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.isVisible = true
    }
}