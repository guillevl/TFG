package com.example.tfg.User

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tfg.MainActivity
import com.example.tfg.R
import com.example.tfg.api.ApiRest
import com.example.tfg.api.EventData
import com.example.tfg.api.EventsNotFinishedResponse
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MisEventosFragment : Fragment() {
    private lateinit var searchView: SearchView
    private var currentOption: TextView? = null
    var isFinished = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mis_eventos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val card = view.findViewById<CardView>(R.id.cadviewEvento)
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationViewAdmin)?.isVisible =
            false
        val mainActivity = activity as MainActivity
        mainActivity.setStatusBarColor("#1B2910")
        val sharedPreferencesGet =
            requireContext().getSharedPreferences("login", Context.MODE_PRIVATE)
        val getID = sharedPreferencesGet.getInt("userID", 0)
        ApiRest.initService()
        var titular = ""
        getMisEventos(view, getID.toString(), titular)
        searchView = view.findViewById(R.id.svSearchMisEventos)

        // Configurar el listener de búsqueda
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Realizar la búsqueda aquí
                if (!query.isNullOrEmpty()) {
                    titular = query
                    getMisEventos(view, getID.toString(), titular)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Actualizar la búsqueda mientras se escribe

                titular = newText!!
                getMisEventos(view, getID.toString(), titular)

                return false
            }
        })
        var OptionActivos = view.findViewById<TextView>(R.id.OptionFinish)//guardas los elementos
        var OptionFinish = view.findViewById<TextView>(R.id.OptionActivos)
        //carga la seleccion por defecto en la variable
        currentOption = if (isFinished) {
            OptionActivos
        } else {
            OptionFinish
        }
        selectOption(currentOption!!) // Inicializar con la primera opción
        OptionActivos.setOnClickListener {
            onOptionClicked(it)
            isFinished = true
            getMisEventos(view, getID.toString(), titular)
        }

        OptionFinish.setOnClickListener {
            onOptionClicked(it)
            isFinished = false
            getMisEventos(view, getID.toString(), titular)
        }
    }

    //gestiona los cambios de ambos textviews
    fun onOptionClicked(view: View) {
        val option = view as TextView
        if (option != currentOption) {
            deselectOption(currentOption!!) //deselecciona
            selectOption(option)//selecciona
            currentOption = option
        }
    }

    //cambia los estilos del textview que has seleccionado
    private fun selectOption(textView: TextView) {
        textView.setTextColor(resources.getColor(R.color.black))
        textView.setBackgroundColor(resources.getColor(R.color.verde_fondo_ssv))
        val fadeInAnimation = AlphaAnimation(0f, 1f)
        fadeInAnimation.duration = 500
        textView.startAnimation(fadeInAnimation)
    }

    //cambia los estilos del textview que has deseleccionado
    private fun deselectOption(textView: TextView) {
        textView.setTextColor(resources.getColor(R.color.griss))
        textView.setBackgroundColor(resources.getColor(R.color.gris_verd))

    }

    private fun getMisEventos(view: View, id: String, titu: String) {
        val call = ApiRest.service.getMisEventos(id, "*", titu, titu, titu, titu, titu, isFinished)
        call.enqueue(object : Callback<EventsNotFinishedResponse> {
            override fun onResponse(
                call: Call<EventsNotFinishedResponse>,
                response: Response<EventsNotFinishedResponse>
            ) {
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    var evtsNot = body
                    var rvUserInfo = view.findViewById<RecyclerView>(R.id.rvMisEventosPrincipal)
                    rvUserInfo?.layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    rvUserInfo?.adapter = MisEventsAdapter(evtsNot) { eventId ->
                        activity?.let {
                            val fragment = DetalleEventoMisEventoFragment()
                            fragment.arguments = Bundle().apply {
                                putString("eventIds", eventId.id.toString())
                            }
                            it.supportFragmentManager.beginTransaction()
                                .replace(R.id.container, fragment)?.addToBackStack(null)?.commit()
                        }
                    }
                    Log.i("getAds", evtsNot.toString())
                } else {
                    Log.e("getAds", response.errorBody()?.string() ?: "Error getting user:")
                }
            }

            override fun onFailure(call: Call<EventsNotFinishedResponse>, t: Throwable) {
                Log.e("getAdsOnFailure", "Error: ${t.message}")
            }
        })
    }
}