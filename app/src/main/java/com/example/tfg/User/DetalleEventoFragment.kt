package com.example.tfg.User

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg.Admin.ListaUsersAdapter
import com.example.tfg.PantallaInicioFragment
import com.example.tfg.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class DetalleEventoFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detalle_evento, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.isVisible = false
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationViewAdmin)?.isVisible = false
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Unirte a el evento")
            .setMessage("¿Seguro que deseas unirte al evento?")
            .setPositiveButton("Sí") { dialog, which ->
                // Acciones a realizar si el usuario presiona el botón "Sí"
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.container, MainUsrFragment())?.commit()
            }
            .setNegativeButton("No") { dialog, which ->
                // Acciones a realizar si el usuario presiona el botón "No"
            }
        val alerta = builder.create()
        var rvUserInfo = view.findViewById<RecyclerView>(R.id.rvDetalleEventos)
        rvUserInfo.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvUserInfo.adapter = DetalleEventosAdapter {}
        view?.findViewById<Button>(R.id.btnUnirme)?.setOnClickListener {
            alerta.show()
        }
    }
    override fun onStop() {
        super.onStop()
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.isVisible = true
    }
}