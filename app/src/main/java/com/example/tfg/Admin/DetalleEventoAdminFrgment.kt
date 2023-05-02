package com.example.tfg.Admin

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg.PantallaInicioFragment
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
                    Toast.makeText(context, "Ha recibido 40 puntos", Toast.LENGTH_SHORT).show()
                }
                1 -> {
                    Toast.makeText(context, "Ha recibido 30 puntos", Toast.LENGTH_SHORT).show()
                }
                2 -> {
                    Toast.makeText(context, "Ha recibido 20 puntos", Toast.LENGTH_SHORT).show()
                }
                3 -> {
                    Toast.makeText(context, "Ha recibido 10 puntos", Toast.LENGTH_SHORT).show()
                }
            }
        }
        val builder2 = AlertDialog.Builder(context)
        builder2.setTitle("Terminar evento")
            .setMessage("¿Seguro que desea terminar el evento?")
            .setPositiveButton("Sí") { dialog, which ->
                // Acciones a realizar si el usuario presiona el botón "Sí"
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.container, MainAdminFragment())?.commit()
            }
            .setNegativeButton("No") { dialog, which ->
                // Acciones a realizar si el usuario presiona el botón "No"
            }
        val alerta = builder2.create()
        var rvUserInfo = view.findViewById<RecyclerView>(R.id.rvDetalleEventos)
        rvUserInfo.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvUserInfo.adapter = DetalleEventosAdapter {
            builder.show()
        }
        view?.findViewById<Button>(R.id.btnUnirme)?.setOnClickListener {
            alerta.show()
        }
    }
    override fun onStop() {
        super.onStop()
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationViewAdmin)?.isVisible = true
    }
}