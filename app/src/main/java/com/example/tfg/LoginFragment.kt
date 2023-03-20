package com.example.tfg

import android.os.Bundle
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
import com.google.android.material.bottomnavigation.BottomNavigationView

class LoginFragment : Fragment() {

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
        val mainActivity = activity as MainActivity
        mainActivity.setStatusBarColor("#1B2910")
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.isVisible = false
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationViewAdmin)?.isVisible = false
        view.findViewById<Button>(R.id.btnIrHomeLogin).setOnClickListener {
            if ((view.findViewById<EditText>(R.id.etEmailLogin).text.toString()) == "hola"){
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.container, MainUsrFragment())?.commit()
            }else{
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.container, MainAdminFragment())?.commit()
            }
        }
        view.findViewById<Button>(R.id.btIrRegistro_Login).setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.container, RegistroFragment())?.addToBackStack(null)?.commit()
        }
    }
    override fun onStop() {
        super.onStop()
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationViewAdmin)?.isVisible = true
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.isVisible = true
    }
}