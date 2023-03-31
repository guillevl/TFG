package com.example.tfg.Admin

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg.PantallaInicioFragment
import com.example.tfg.R
import com.example.tfg.User.RankingsAdapter

class ListaUsersFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lista_users, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Eliminar usuarios")
            .setMessage("¿Seguro que desea eliminar la cuenta de este usuario?")
            .setPositiveButton("Sí") { dialog, which ->

            }
            .setNegativeButton("No") { dialog, which ->
                // Acciones a realizar si el usuario presiona el botón "No"
            }
        val alerta = builder.create()
        var rvUserInfo = view.findViewById<RecyclerView>(R.id.rvListaUsers)
        rvUserInfo.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvUserInfo.adapter = ListaUsersAdapter {
            alerta.show()
        }
    }
}