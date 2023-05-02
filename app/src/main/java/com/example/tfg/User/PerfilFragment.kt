package com.example.tfg.User

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.service.autofill.UserData
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.tfg.LoginFragment
import com.example.tfg.PantallaInicioFragment
import com.example.tfg.R
import com.example.tfg.api.ApiRest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        //cogemos id usuario
        val sharedPreferencesGet =
            requireContext().getSharedPreferences("login", Context.MODE_PRIVATE)
        val getID = sharedPreferencesGet.getInt("userID", 0)
        getUserById("1")
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

    private fun getUserById(id: String) {
        val call = ApiRest.service.getUserById(id)
        call.enqueue(object : Callback<UserData> {
            override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                // manejar la respuesta exitosa aquí
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    // procesar la respuesta aquí
                    Log.e("TAG", response.body().toString())
                } else {
                    Log.e("TAG", response.errorBody()?.string() ?: "Error")
                }
            }

            override fun onFailure(call: Call<UserData>, t: Throwable) {
                Log.e("TAG", "Error: ${t.message}")
            }
        })
    }
}


