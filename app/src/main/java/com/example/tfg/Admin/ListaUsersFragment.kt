package com.example.tfg.Admin

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg.PantallaInicioFragment
import com.example.tfg.R
import com.example.tfg.api.ApiRest
import com.example.tfg.api.UserListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListaUsersFragment : Fragment() {
    var idUser: Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lista_users, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ApiRest.initService()
        getUsers()
    }

    private fun getUsers() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Eliminar usuarios")
            .setMessage("¿Seguro que desea eliminar la cuenta de este usuario?")
            .setPositiveButton("Sí") { dialog, which ->
                deleteUser(idUser.toString())
            }
            .setNegativeButton("No") { dialog, which ->
                // Acciones a realizar si el usuario presiona el botón "No"
            }
        val alerta = builder.create()
        val call = ApiRest.service.getUsers()
        call.enqueue(object : Callback<UserListResponse> {
            override fun onResponse(
                call: Call<UserListResponse>,
                response: Response<UserListResponse>
            ) {
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    var usrList = body
                    var rvUserInfo = view?.findViewById<RecyclerView>(R.id.rvListaUsers)
                    rvUserInfo?.layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    rvUserInfo?.adapter = ListaUsersAdapter(usrList) {
                        idUser = it.id
                        alerta.show()
                    }
                    Log.i("getAds", usrList.toString())
                } else {
                    Log.e("getAds", response.errorBody()?.string() ?: "Error getting user:")
                }
            }

            override fun onFailure(call: Call<UserListResponse>, t: Throwable) {
                Log.e("getAdsOnFailure", "Error: ${t.message}")
            }
        })
    }

    private fun deleteUser(id: String) {
        val call = ApiRest.service.deleteUser(id)
        call.enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    // El usuario ha sido eliminado con éxito
                    activity?.supportFragmentManager?.beginTransaction()
                        ?.replace(R.id.container, ListaUsersFragment())?.commit()
                } else {
                    // Ha habido un error en la petición DELETE
                    Log.e("EditProfileFragment", response.errorBody()?.string() ?: "Error")
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                // Ha habido un error en la comunicación con el servidor
                Log.e("EditProfileFragment", "Error: ${t.message}")
            }
        })
    }
}