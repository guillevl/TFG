package com.example.tfg


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.isVisible
import com.example.tfg.User.MainUsrFragment
import com.example.tfg.api.ApiRest
import com.example.tfg.api.RegisterData
import com.example.tfg.api.RegisterResponse
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.json.JSONObject
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
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationViewAdmin)?.isVisible =
            false
        view?.findViewById<Button>(R.id.btnIrHomeRegistro)?.setOnClickListener {
            var email = view.findViewById<EditText>(R.id.etEmailRegistro).text.toString()
            var password = view.findViewById<EditText>(R.id.etPassRegistro).text.toString()
            var username = view.findViewById<EditText>(R.id.etUsernameRegistro).text.toString()
            var name = view.findViewById<EditText>(R.id.etNombreRegistro).text.toString()
            var apellido = view.findViewById<EditText>(R.id.etApellidoRegistro).text.toString()
            var fecha_nacimiento = view.findViewById<EditText>(R.id.etDate).text.toString()
            if (email == "" || password == "" || username == "" || name == "" || apellido == "" || fecha_nacimiento == "") {
                applyShakeEffect()
                view?.findViewById<TextView>(R.id.tvMensageError)?.text = "Rellene todos los campos"
                view?.findViewById<TextView>(R.id.tvMensageError)?.isVisible = true
            } else {
                register(email, password, username, name, apellido, fecha_nacimiento)
            }

        }
        view?.findViewById<Button>(R.id.btIrLogin_Registro)?.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.container, LoginFragment())?.commit()
        }
        view.findViewById<EditText>(R.id.etDate).setOnClickListener() {
            showDatePickerDialog()
        }
    }

    //abre el calendario y se crea una instancia de datePickerFragment
    private fun showDatePickerDialog() {
        val datePicker = DatePickerFragment { day, month, year -> onDateSelected(day, month, year) }
        getActivity()?.let { datePicker.show(it.getSupportFragmentManager(), "datePicker") }
    }

    //recoge los datos que le pasamos en el showdatepikerdialog y los muestra en editText que queremos
    private fun onDateSelected(day: Int, month: Int, year: Int) {
        view?.findViewById<EditText>(R.id.etDate)
            ?.setText("$day/$month/$year")
    }

    override fun onStop() {
        super.onStop()
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.isVisible = true
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationViewAdmin)?.isVisible =
            true
    }

    private fun register(
        email: String,
        password: String,
        username: String,
        name: String,
        apellido: String,
        fecha_nacimiento: String
    ) {
        val crearUser = RegisterData(
            "https://firebasestorage.googleapis.com/v0/b/tfg-gv.appspot.com/o/notes%2Fimages%2Fd550a046-461e-4927-b2b2-b8aeee8e4ced.jpg%2Fimages%2F34?alt=media&token=6ab8923f-4545-446f-8187-214eb2a8d7ed",
            email, password, username, name, apellido, fecha_nacimiento
        )
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
                    val sharedPreferences =
                        requireContext().getSharedPreferences("login", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putInt("userID", response.body()!!.user.id)
                    editor.apply()
                    activity?.supportFragmentManager?.beginTransaction()
                        ?.replace(R.id.container, MainUsrFragment())?.commit()
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorJson = JSONObject(errorBody)
                    val errorObject = errorJson.getJSONObject("error")
                    val errorMessage = errorObject.getString("message")
                    if (errorMessage == "email must be a valid email") {
                        applyShakeEffect()
                        view?.findViewById<TextView>(R.id.tvMensageError)?.text =
                            "Formato de email incorrecto"
                        view?.findViewById<TextView>(R.id.tvMensageError)?.isVisible = true
                    } else if (errorMessage == "Email or Username are already taken") {
                        applyShakeEffect()
                        view?.findViewById<TextView>(R.id.tvMensageError)?.text =
                            "Username o Email ya esta cogido"
                        view?.findViewById<TextView>(R.id.tvMensageError)?.isVisible = true
                    } else if (errorMessage == "password must be at least 6 characters") {
                        applyShakeEffect()
                        view?.findViewById<TextView>(R.id.tvMensageError)?.text =
                            "La contraseña debe tener 6 caracteres"
                        view?.findViewById<TextView>(R.id.tvMensageError)?.isVisible = true
                    }
                    Log.e(TAG, response.errorBody()?.string() ?: "Error")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    //aplica el efecto de shake de pantalla
    private fun applyShakeEffect() {
        val shakeAnimation = AnimationUtils.loadAnimation(context, R.anim.shake)
        view?.startAnimation(shakeAnimation)
    }
}