package com.example.tfg

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
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
import kotlin.properties.Delegates

class LoginFragment : Fragment() {
    val TAG = "MainActivity"
    var isChecked by Delegates.notNull<Boolean>()
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
        //Radio Button Recuerdame
        val radioButton = view.findViewById<RadioButton>(R.id.btnRecordarUser)//radio button de recordarme
        val sharedPreferencesGet =
            requireContext().getSharedPreferences("remember", Context.MODE_PRIVATE)//creamos instancia de sharepreference
        isChecked = sharedPreferencesGet.getBoolean("isChecked", false)// Se obtiene el valor booleano almacenado en SharedPreferences
        radioButton.isChecked = isChecked//se asigna el valor al radio button
        if (isChecked) {
            val sharedPreferencesGet =
                requireContext().getSharedPreferences("remember", Context.MODE_PRIVATE)
            var user = sharedPreferencesGet.getString("user", "")
            var password = sharedPreferencesGet.getString("password", "")
            //metemos los valores guardados previamente en los edit texto correspondientes
            view.findViewById<TextView>(R.id.etEmailLogin).text = user
            view.findViewById<TextView>(R.id.etPassLogin).text = password
        }
        radioButton.setOnClickListener {
            isChecked = !isChecked//invertimos valores de la variable
            radioButton.isChecked = isChecked//lo aplicamos a el radio button
            val sharedPreferences =
                requireContext().getSharedPreferences("remember", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()//para modificar los datos del sharepreferences
            editor.putBoolean("isChecked", isChecked)//guardamos nuevos datos en el shareprefence
            editor.apply() // Aplicar los cambios en SharedPreferences
        }
        view.findViewById<Button>(R.id.btnIrHomeLogin).setOnClickListener {
            var email = view.findViewById<EditText>(R.id.etEmailLogin).text.toString()
            var password = view.findViewById<EditText>(R.id.etPassLogin).text.toString()
            login(email, password)
            mainActivity.setBottomNavigationSelectedItemAdmin(0)
            mainActivity.setBottomNavigationSelectedItem(0)

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
                    if (isChecked) {
                        val sharedPreferencesGet =
                            requireContext().getSharedPreferences("remember", Context.MODE_PRIVATE)
                        val user = view?.findViewById<TextView>(R.id.etEmailLogin)?.text.toString()
                        val password = view?.findViewById<TextView>(R.id.etPassLogin)?.text.toString()
                        val editor = sharedPreferencesGet.edit()
                        editor.putBoolean("isChecked", isChecked)
                        //guardamos los datos de inicio de sesion para guardarlos en el sharepreference
                        editor.putString("user", user)
                        editor.putString("password", password)
                        editor.apply()
                    }
                    //distinguimos si es el admin o un usuario corriente
                    if (usernameOrEmail==("admin@gmail.com")){
                        val userId = body.user.id
                        //Guardamos el id y el token en local
                        val sharedPreferences =
                            requireContext().getSharedPreferences("login", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putInt("userID", userId)
                        editor.apply()
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
                    applyShakeEffect()
                    view?.findViewById<TextView>(R.id.tvMensageError)?.isVisible = true
                    Log.e(TAG, response.errorBody()?.string() ?: "Error")

                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e(TAG, "Error en la solicitud de login: ${t.message}")
            }

        })
    }
    private fun applyShakeEffect() {
        val shakeAnimation = AnimationUtils.loadAnimation(context, R.anim.shake)
        view?.startAnimation(shakeAnimation)
    }
}