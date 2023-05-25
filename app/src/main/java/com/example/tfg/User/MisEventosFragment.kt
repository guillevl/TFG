package com.example.tfg.User

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg.Admin.DetalleEventoAdminFrgment
import com.example.tfg.R
import com.example.tfg.api.ApiRest
import com.example.tfg.api.EventsNotFinishedResponse
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MisEventosFragment : Fragment() {
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
        //var rvUserInfo = view.findViewById<RecyclerView>(R.id.rvMisEventosPrincipal)
        //rvUserInfo.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        //rvUserInfo.adapter = EventsAdapter {
        //activity?.supportFragmentManager?.beginTransaction()
         //?.replace(R.id.container, DetalleEventoAdminFrgment())?.addToBackStack(null)?.commit()
        //}
    }

}