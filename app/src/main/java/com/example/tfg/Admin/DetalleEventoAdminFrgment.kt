package com.example.tfg.Admin

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tfg.R
import com.example.tfg.api.ApiRest
import com.example.tfg.api.EventData
import com.example.tfg.api.EventsResponse
import com.example.tfg.api.UserData
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetalleEventoAdminFrgment : Fragment() {
    lateinit var builder: AlertDialog.Builder
    lateinit var user: UserData
    lateinit var event: EventData
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detalle_evento_admin_frgment, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.isVisible = false
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationViewAdmin)?.isVisible = false
        ApiRest.initService()
        val eventIds =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                arguments?.getString("eventIdsAdmin")
            } else {
                arguments?.getString("eventIdsAdmin")
            }
        builder = AlertDialog.Builder(context)
        builder.setTitle("Elige posicion del jugador")
        val opciones = arrayOf("1º Posicion(40ptos)", "2º Posicion(30ptos)", "3º Posicion(20ptos)","4º Posicion(10ptos)")
        builder.setItems(opciones) { dialog, which ->
            when (which) {
                0 -> {
                    user.points = user.points + 4
                    updateUser(user.id.toString(), user)
                    Toast.makeText(context, "Ha recibido 4 puntos", Toast.LENGTH_SHORT).show()
                }
                1 -> {
                    user.points = user.points + 3
                    updateUser(user.id.toString(), user)
                    Toast.makeText(context, "Ha recibido 3 puntos", Toast.LENGTH_SHORT).show()
                }
                2 -> {
                    user.points = user.points + 2
                    updateUser(user.id.toString(), user)
                    Toast.makeText(context, "Ha recibido 2 puntos", Toast.LENGTH_SHORT).show()
                }
                3 -> {
                    user.points = user.points + 1
                    updateUser(eventIds!!, user)
                    Toast.makeText(context, "Ha recibido 1 punto", Toast.LENGTH_SHORT).show()
                }
            }
        }
        val builder2 = AlertDialog.Builder(context)
        builder2.setTitle("Terminar evento")
            .setMessage("¿Seguro que desea terminar el evento?")
            .setPositiveButton("Sí") { dialog, which ->
                // Acciones a realizar si el usuario presiona el botón "Sí"
                event.data.attributes.isFinished = true

                val data = EventsResponse(
                    EventsResponse.Data.Attributes(
                        event.data.attributes.fecha_evento, event.data.attributes.foto_evento, event.data.attributes.hora_fin,
                        event.data.attributes.hora_inicio,event.data.attributes.isFinished,event.data.attributes.nivel,
                        event.data.attributes.sexo,event.data.attributes.titulo_evento
                    )
                )
                println(event)
                updateEvent(eventIds!!,data)

                activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.container, MainAdminFragment())?.commit()
            }
            .setNegativeButton("No") { dialog, which ->
                // Acciones a realizar si el usuario presiona el botón "No"
            }
        val alerta = builder2.create()

        getEventById(eventIds!!,view)
        view?.findViewById<Button>(R.id.btnFinalizar)?.setOnClickListener {
            alerta.show()
        }
    }
    private fun getEventById(id: String,view: View) {
        val call = ApiRest.service.getEventById(id)
        call.enqueue(object: Callback<EventData> {
            override fun onResponse(call: Call<EventData>, response: Response<EventData>) {
                // maneja la respuesta exitosa aquí
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    event = body
                    view?.findViewById<TextView>(R.id.tvFechaEventoDetalleMainAdmin)?.text = body.data.attributes.fecha_evento
                    view?.findViewById<TextView>(R.id.tvTituloEventoDetalleMainAdmin)?.text = body.data.attributes.titulo_evento
                    val hora_inicio = body.data.attributes.hora_inicio.substring(0,5)
                    val hora_fin = body.data.attributes.hora_fin.substring(0,5)
                    view?.findViewById<TextView>(R.id.tvHoraEventoDetalleMainAdmin)?.text = hora_inicio +" - "+hora_fin
                    view?.findViewById<TextView>(R.id.tvNivelEventoDetalleMainAdmin)?.text = body.data.attributes.nivel
                    view?.findViewById<TextView>(R.id.tvSexoEventoDetalleMainAdmin)?.text = ":   "+body.data.attributes.sexo
                    val listaUsersEventosMainAdmin = body.data.attributes.users.data
                    Glide.with(view)
                        .load(body.data.attributes.foto_evento)
                        .into(view.findViewById<ImageView>(R.id.ivFotoEventoDetalleMainAdmin))
                    var rvUserInfo = view.findViewById<RecyclerView>(R.id.rvDetalleEventosMainAdmin)
                    rvUserInfo.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    rvUserInfo.adapter = DetalleEventosAdapterMainAdmin (listaUsersEventosMainAdmin){
                        //it sera el id del usuario que pincho
                        getUser(it,view)
                        builder.show()
                    }
                    Log.i("EditProfileFragment", body.toString())

                } else {
                    Log.e("EditProfileFragment", response.errorBody()?.string() ?: "Error")
                }
            }

            override fun onFailure(call: Call<EventData>, t: Throwable) {
                Log.e("EditProfileFragment", "Error: ${t.message}")
            }
        })
    }
    private fun updateEvent(id: String, updatedEvent: EventsResponse) {
        val call = ApiRest.service.updateEvent(updatedEvent, id)
        call.enqueue(object : Callback<EventData> {
            override fun onResponse(call: Call<EventData>, response: Response<EventData>) {
                // maneja la respuesta exitosa aquí
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    // procesa la respuesta aquí
                } else {
                    Log.e("EditProfileFragment", response.errorBody()?.string() ?: "Error")
                }
            }

            override fun onFailure(call: Call<EventData>, t: Throwable) {
                Log.e("EditProfileFragment", "Error: ${t.message}")
            }
        })
    }
    private fun getUser(id: String,view: View) {
        val call = ApiRest.service.getUserById(id)
        call.enqueue(object : Callback<UserData> {
            override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                // maneja la respuesta exitosa aquí
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    Log.i("EditProfileFragment", body.toString())
                    user = body
                } else {
                    Log.e("EditProfileFragment", response.errorBody()?.string() ?: "Error")
                }
            }

            override fun onFailure(call: Call<UserData>, t: Throwable) {
                Log.e("EditProfileFragment", "Error: ${t.message}")
            }
        })
    }
    private fun updateUser(id: String, updatedUser: UserData) {
        val call = ApiRest.service.updateUser(updatedUser, id)
        call.enqueue(object : Callback<UserData> {
            override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                // maneja la respuesta exitosa aquí
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    // procesa la respuesta aquí
                } else {
                    Log.e("EditProfileFragment", response.errorBody()?.string() ?: "Error")
                }
            }

            override fun onFailure(call: Call<UserData>, t: Throwable) {
                Log.e("EditProfileFragment", "Error: ${t.message}")
            }
        })
    }
    override fun onStop() {
        super.onStop()
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationViewAdmin)?.isVisible = true
    }
}