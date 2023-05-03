package com.example.tfg.User

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.tfg.LoginFragment
import com.example.tfg.PantallaInicioFragment
import com.example.tfg.R
import com.example.tfg.api.ApiRest
import com.example.tfg.api.UserData
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
        ApiRest.initService()
        getUser(getID.toString())
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
    private fun getUser(id: String) {
        val call = ApiRest.service.getUserById(id)
        call.enqueue(object: Callback<UserData> {
            override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                // maneja la respuesta exitosa aquí
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    view?.findViewById<TextView>(R.id.txtUsernamePerfil)?.text = "@"+body.username
                    view?.findViewById<TextView>(R.id.txtNombreCompletoPerfil)?.text = body.name+ " " + body.apellido
                    view?.findViewById<TextView>(R.id.txtPuntosPerfil)?.text = body.points.toString()+"pts"
                    if (body.mano_dominante != null) {
                        view?.findViewById<TextView>(R.id.txtManodominantePerfil)?.text =
                            body.mano_dominante
                    }else{
                        view?.findViewById<TextView>(R.id.txtManodominantePerfil)?.text = "Sin indicar"
                    }
                    view?.findViewById<TextView>(R.id.txtFechaPerfil)?.text = body.fecha_nacimiento
                    view?.findViewById<TextView>(R.id.txtCorreoPerfil)?.text = body.email
                    Log.i("EditProfileFragment", body.toString())

                } else {
                    Log.e("EditProfileFragment", response.errorBody()?.string() ?: "Error")
                }
            }

            override fun onFailure(call: Call<UserData>, t: Throwable) {
                Log.e("EditProfileFragment", "Error: ${t.message}")
            }
        })
    }
}


