package com.example.tfg

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.tfg.Admin.MainAdminFragment
import com.example.tfg.User.MainUsrFragment
import com.example.tfg.api.ApiRest
import com.example.tfg.api.LoginCredentials
import com.example.tfg.api.LoginResponse
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginFragment : Fragment() {
    val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.hide()
        ApiRest.initService()
        val mainActivity = activity as MainActivity
        mainActivity.setStatusBarColor("#1B2910")
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.isVisible = false
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationViewAdmin)?.isVisible =
            false
        view.findViewById<Button>(R.id.btnIrHomeLogin).setOnClickListener {
            var email = view.findViewById<EditText>(R.id.etEmailLogin).text.toString()
            var password = view.findViewById<EditText>(R.id.etPassLogin).text.toString()
            login(email, password)
            /**if ((view.findViewById<EditText>(R.id.etEmailLogin).text.toString()) == "hola"){
            activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.container, MainUsrFragment())?.commit()
            }else{
            activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.container, MainAdminFragment())?.commit()
            }**/
        }
        view.findViewById<Button>(R.id.btIrRegistro_Login).setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.container, RegistroFragment())?.addToBackStack(null)?.commit()
        }
    }

    override fun onStop() {
        super.onStop()
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationViewAdmin)?.isVisible =
            true
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.isVisible = true
    }

    private fun login(usernameOrEmail: String, password: String) {

        val credentials = LoginCredentials(usernameOrEmail, password)
        val call = ApiRest.service.login(credentials)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                // maneja la respuesta exitosa aquí
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    if (usernameOrEmail==("admin@gmail.com")){
                        activity?.supportFragmentManager?.beginTransaction()
                            ?.replace(R.id.container, MainAdminFragment())?.commit()
                    }else{
                        val userId = body.user.id
                        //Guardamos el id y el token en local
                        val sharedPreferences =
                            requireContext().getSharedPreferences("login", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putInt("userID", userId)
                        editor.apply()
                        activity?.supportFragmentManager?.beginTransaction()
                            ?.replace(R.id.container, MainUsrFragment())?.commit()
                    }

                } else {

                    Log.e(TAG, response.errorBody()?.string() ?: "Error")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e(TAG, "Error en la solicitud de login: ${t.message}")
            }

        })
    }
}