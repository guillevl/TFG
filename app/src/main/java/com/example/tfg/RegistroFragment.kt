package com.example.tfg



import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.view.isVisible
import com.example.tfg.User.MainUsrFragment
import com.example.tfg.api.ApiRest
import com.example.tfg.api.RegisterData
import com.example.tfg.api.RegisterResponse
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class RegistroFragment : Fragment() {
    val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registro, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ApiRest.initService()
        val mainActivity = activity as MainActivity
        mainActivity.setStatusBarColor("#1B2910")
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.isVisible = false
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationViewAdmin)?.isVisible = false
        view?.findViewById<Button>(R.id.btnIrHomeRegistro)?.setOnClickListener {
            var email = view.findViewById<EditText>(R.id.etEmailRegistro).text.toString()
            var password = view.findViewById<EditText>(R.id.etPassRegistro).text.toString()
            var username = view.findViewById<EditText>(R.id.etUsernameRegistro).text.toString()
            var name = view.findViewById<EditText>(R.id.etNombreRegistro).text.toString()
            var apellido = view.findViewById<EditText>(R.id.etApellidoRegistro).text.toString()
            var fecha_nacimiento = view.findViewById<EditText>(R.id.etDate).text.toString()
            register(email, password, username, name, apellido, fecha_nacimiento)
        }
        view?.findViewById<Button>(R.id.btIrLogin_Registro)?.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.container, LoginFragment())?.commit()
        }
        view.findViewById<EditText>(R.id.etDate).setOnClickListener() {
            showDatePickerDialog()
        }
    }
    fun isEmailValid(email: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }
    fun isPasswordValid(password: String): Boolean {
        // Patrón para al menos una letra mayúscula, una minúscula y un caracter especial
        val pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#\$%^&+=])(?=\\S+\$).{7,}$"
        val compiledPattern = Pattern.compile(pattern)
        val matcher = compiledPattern.matcher(password)
        return matcher.matches()
    }
    private fun showDatePickerDialog() {
        val datePicker = DatePickerFragment { day, month, year -> onDateSelected(day, month, year) }
        getActivity()?.let { datePicker.show(it.getSupportFragmentManager(), "datePicker") }
    }

    private fun onDateSelected(day: Int, month: Int, year: Int) {
        view?.findViewById<EditText>(R.id.etDate)
            ?.setText("$day/$month/$year")
    }
    override fun onStop() {
        super.onStop()
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.isVisible = true
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationViewAdmin)?.isVisible = true
    }
    private fun register(email: String, password: String, username: String, name: String, apellido: String, fecha_nacimiento: String) {
        val crearUser = RegisterData(email, password, username,name, apellido, fecha_nacimiento)
        val call = ApiRest.service.meterUser(crearUser)
        call.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                // maneja la respuesta exitosa aquí
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    var registroResponse = response.body()
                    print(registroResponse)
                    activity?.supportFragmentManager?.beginTransaction()
                        ?.replace(R.id.container, MainUsrFragment())?.commit()
                } else {
                    Log.e(TAG, response.errorBody()?.string()?: "Error")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}