package com.example.tfg.User

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.tfg.LoginFragment
import com.example.tfg.PantallaInicioFragment
import com.example.tfg.R

class PerfilFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_perfil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Eliminar cuenta")
            .setMessage("¿Seguro que desea eliminar la cuenta?")
            .setPositiveButton("Sí") { dialog, which ->
                // Acciones a realizar si el usuario presiona el botón "Sí"
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.container, PantallaInicioFragment())?.commit()
            }
            .setNegativeButton("No") { dialog, which ->
                // Acciones a realizar si el usuario presiona el botón "No"
            }
        val alerta = builder.create()
        view?.findViewById<Button>(R.id.btnEditarPerfil)?.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.container, EditarPerfilFragment())?.addToBackStack(null)?.commit()
        }
        view?.findViewById<Button>(R.id.btnEliminarCuenta)?.setOnClickListener {
            alerta.show()
        }
        view?.findViewById<Button>(R.id.btnCerrarSesion)?.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.container, LoginFragment())?.commit()
        }
    }
}