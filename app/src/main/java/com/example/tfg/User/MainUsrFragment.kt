package com.example.tfg.User

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg.R
import com.example.tfg.api.ApiRest
import com.example.tfg.api.EventsNotFinishedResponse
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainUsrFragment : Fragment() {
    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_usr, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationViewAdmin)?.isVisible =
            false
        var titular = ""
        ApiRest.initService()
        getEventsNotFinished(view, titular)
        searchView = view.findViewById(R.id.svSearch)

        // Configurar el listener de búsqueda
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Realizar la búsqueda aquí
                if (!query.isNullOrEmpty()) {
                    titular = query
                    getEventsNotFinished(view, titular)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Actualizar la búsqueda mientras se escribe
                if (!newText.isNullOrEmpty()) {
                    titular = newText
                    getEventsNotFinished(view, titular)
                }
                return false
            }
        })
    }


    private fun getEventsNotFinished(view: View, titulo: String) {
        val call = ApiRest.service.getEventsNotFinished(false,"*",titulo)
        call.enqueue(object : Callback<EventsNotFinishedResponse> {
            override fun onResponse(
                call: Call<EventsNotFinishedResponse>,
                response: Response<EventsNotFinishedResponse>
            ) {
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    var evtsNot = body
                    var rvUserInfo = view.findViewById<RecyclerView>(R.id.rvEventosPrincipal)
                    rvUserInfo?.layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    rvUserInfo?.adapter = EventsAdapter(evtsNot) { eventId ->
                        activity?.let {
                            val fragment = DetalleEventoFragment()
                            fragment.arguments = Bundle().apply {
                                putString("eventId", eventId.id.toString())
                            }
                            it.supportFragmentManager.beginTransaction()
                                .replace(R.id.container, fragment)?.addToBackStack(null)
                                ?.commit()
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
