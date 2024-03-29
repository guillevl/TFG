package com.example.tfg.User

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.tfg.LoginFragment
import com.example.tfg.MainActivity
import com.example.tfg.PantallaInicioFragment
import com.example.tfg.R
import com.example.tfg.api.ApiRest
import com.example.tfg.api.UserData
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PerfilFragment : Fragment() {
    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(context,R.anim.rotate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(context,R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(context,R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(context,R.anim.to_bottom_anim) }
    private var clicked = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_perfil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.isVisible = true
        val mainActivity = activity as MainActivity
        mainActivity.setStatusBarColor("#000000")
        //cogemos id usuario
        val sharedPreferencesGet =
            requireContext().getSharedPreferences("login", Context.MODE_PRIVATE)
        val getID = sharedPreferencesGet.getInt("userID", 0)
        ApiRest.initService()
        getUser(getID.toString(),view)
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Eliminar cuenta")
            .setMessage("¿Seguro que desea eliminar la cuenta?")
            .setPositiveButton("Sí") { dialog, which ->
                // Acciones a realizar si el usuario presiona el botón "Sí"
                deleteUser(getID.toString())

            }
            .setNegativeButton("No") { dialog, which ->
                // Acciones a realizar si el usuario presiona el botón "No"
            }
        val alerta = builder.create()

        view?.findViewById<FloatingActionButton>(R.id.fabPrincipal)?.setOnClickListener {
            onMainButtonClicked()
        }
        view?.findViewById<FloatingActionButton>(R.id.fabEditarPerfil)?.setOnClickListener {
            setVisibility(clicked)
            setClickable(clicked)
            clicked = !clicked
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.container, EditarPerfilFragment())?.addToBackStack(null)?.commit()
        }
        view?.findViewById<FloatingActionButton>(R.id.fabCerrarSesion)?.setOnClickListener {
            setVisibility(clicked)
            setClickable(clicked)
            clicked = !clicked
            val sharedPreferences =
                requireContext().getSharedPreferences("login", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putInt("userID", 0)
            editor.apply()
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.container, LoginFragment())?.commit()
        }
        view?.findViewById<FloatingActionButton>(R.id.fabEliminarUser)?.setOnClickListener {
            setVisibility(clicked)
            setClickable(clicked)
            clicked = !clicked
            alerta.show()
        }

    }

    private fun onMainButtonClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        setClickable(clicked)
        clicked = !clicked
    }

    private fun setAnimation(clicked:Boolean) {
        if (!clicked){
            view?.findViewById<FloatingActionButton>(R.id.fabEditarPerfil)?.startAnimation(fromBottom)
            view?.findViewById<FloatingActionButton>(R.id.fabCerrarSesion)?.startAnimation(fromBottom)
            view?.findViewById<FloatingActionButton>(R.id.fabEliminarUser)?.startAnimation(fromBottom)
            view?.findViewById<FloatingActionButton>(R.id.fabPrincipal)?.startAnimation(rotateOpen)
        }else{
            view?.findViewById<FloatingActionButton>(R.id.fabEditarPerfil)?.startAnimation(toBottom)
            view?.findViewById<FloatingActionButton>(R.id.fabCerrarSesion)?.startAnimation(toBottom)
            view?.findViewById<FloatingActionButton>(R.id.fabEliminarUser)?.startAnimation(toBottom)
            view?.findViewById<FloatingActionButton>(R.id.fabPrincipal)?.startAnimation(rotateClose)
        }
    }

    private fun setVisibility(clicked:Boolean) {
        if (!clicked){
            view?.findViewById<FloatingActionButton>(R.id.fabEditarPerfil)?.visibility = View.VISIBLE
            view?.findViewById<FloatingActionButton>(R.id.fabCerrarSesion)?.visibility = View.VISIBLE
            view?.findViewById<FloatingActionButton>(R.id.fabEliminarUser)?.visibility = View.VISIBLE
        }else{
            view?.findViewById<FloatingActionButton>(R.id.fabEditarPerfil)?.visibility = View.INVISIBLE
            view?.findViewById<FloatingActionButton>(R.id.fabCerrarSesion)?.visibility = View.INVISIBLE
            view?.findViewById<FloatingActionButton>(R.id.fabEliminarUser)?.visibility = View.INVISIBLE
        }
    }
    private fun setClickable(clicked: Boolean){
        if (!clicked){
            view?.findViewById<FloatingActionButton>(R.id.fabEditarPerfil)?.isClickable = true
            view?.findViewById<FloatingActionButton>(R.id.fabCerrarSesion)?.isClickable = true
            view?.findViewById<FloatingActionButton>(R.id.fabEliminarUser)?.isClickable = true
        }else{
            view?.findViewById<FloatingActionButton>(R.id.fabEditarPerfil)?.isClickable = false
            view?.findViewById<FloatingActionButton>(R.id.fabCerrarSesion)?.isClickable = false
            view?.findViewById<FloatingActionButton>(R.id.fabEliminarUser)?.isClickable = false
        }
    }

    private fun getUser(id: String,view: View) {
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
                    Glide.with(view)
                        .load(body.foto_perfil)
                        .into(view.findViewById<ImageView>(R.id.imgPerfil))
                    Glide.with(view)
                        .load(body.foto_poster)
                        .into(view.findViewById<ImageView>(R.id.imgPosterPerfil))
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
    private fun deleteUser(id: String) {
        val call = ApiRest.service.deleteUser(id)
        call.enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    // El usuario ha sido eliminado con éxito
                    activity?.supportFragmentManager?.beginTransaction()
                        ?.replace(R.id.container, PantallaInicioFragment())?.commit()
                    Toast.makeText(context, "Cuenta eliminada", Toast.LENGTH_SHORT).show()
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


