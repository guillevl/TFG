package com.example.tfg.User

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.isVisible
import com.example.tfg.R
import com.example.tfg.RegistroFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class EditarPerfilFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_editar_perfil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.isVisible = false
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationViewAdmin)?.isVisible = false
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Editar usuario")
            .setMessage("¿Desea guardar los cambios de la cuenta?")
            .setPositiveButton("Sí") { dialog, which ->
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.container, PerfilFragment())?.addToBackStack(null)?.commit()
            }
            .setNegativeButton("No") { dialog, which ->
                // Acciones a realizar si el usuario presiona el botón "No"
            }
        val alerta = builder.create()

        view.findViewById<Button>(R.id.btnGuardarDatosUser).setOnClickListener {
            alerta.show()
        }
    }
    override fun onStop() {
        super.onStop()
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.isVisible = true
    }
}