package com.example.tfg.Admin

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg.R
import com.example.tfg.User.DetalleEventosAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView

class DetalleEventoAdminFrgment : Fragment() {

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
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Elige posicion del jugador")

        val opciones = arrayOf("1º Posicion(40ptos)", "2º Posicion(30ptos)", "3º Posicion(20ptos)","4º Posicion(10ptos)")
        builder.setItems(opciones) { dialog, which ->
            when (which) {
                0 -> {
                    // Acción para la opción 1
                }
                1 -> {
                    // Acción para la opción 2
                }
                2 -> {
                    // Acción para la opción 3
                }
                3 -> {

                }
            }
        }

        var rvUserInfo = view.findViewById<RecyclerView>(R.id.rvDetalleEventos)
        rvUserInfo.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvUserInfo.adapter = DetalleEventosAdapter {
            builder.show()
        }
    }
    override fun onStop() {
        super.onStop()
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationViewAdmin)?.isVisible = true
    }
}