package com.example.tfg

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import com.example.tfg.Admin.CrearEventoFragment
import com.example.tfg.Admin.ListaUsersFragment
import com.example.tfg.Admin.MainAdminFragment
import com.example.tfg.User.MainUsrFragment
import com.example.tfg.User.MisEventosFragment
import com.example.tfg.User.PerfilFragment
import com.example.tfg.User.RankingFragment
import com.example.tfg.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        goToFragment(PantallaInicioFragment())
        //navegacion bottom user
        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.main_user -> replaceFragment(MainUsrFragment())
                R.id.mis_eventos -> replaceFragment(MisEventosFragment())
                R.id.ranking -> replaceFragment(RankingFragment())
                R.id.perfil -> replaceFragment(PerfilFragment())
            }
            true
        }
        //navegacion bottom admin
        binding.bottomNavigationViewAdmin.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.usrs_admin -> replaceFragment(ListaUsersFragment())
                R.id.event_admin -> replaceFragment(MainAdminFragment())
                R.id.add_event -> replaceFragment(CrearEventoFragment())
                R.id.logout -> replaceFragment(LoginFragment())
            }
            true
        }
    }
    fun setStatusBarColor(color: String){
        val colorInt: Int = Color.parseColor(color)
        window.statusBarColor = colorInt
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragment)
        fragmentTransaction.commit()
    }
    fun goToFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }
    //Ocultar bottomnavigation al abrir el teclado
    fun setupKeyboardVisibilityListener() {
        val rootView = findViewById<View>(android.R.id.content)
        val bottomNavigationView=findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        rootView.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            private val windowVisibleDisplayFrame = Rect()
            private var isKeyboardOpen = false

            override fun onGlobalLayout() {
                rootView.getWindowVisibleDisplayFrame(windowVisibleDisplayFrame)
                val screenHeight = rootView.rootView.height

                val heightDiff = screenHeight - windowVisibleDisplayFrame.bottom
                val isOpen =
                    heightDiff > screenHeight * 0.15 // Se considera que el teclado está abierto si la diferencia de altura es superior al 15% de la altura de la pantalla

                if (isOpen != isKeyboardOpen) {
                    isKeyboardOpen = isOpen
                    if (isKeyboardOpen) {
                        // El teclado está abierto, ocultar el BottomNavigationView
                        bottomNavigationView.visibility = View.INVISIBLE
                    } else {
                        // El teclado está cerrado, mostrar el BottomNavigationView
                        bottomNavigationView.visibility = View.VISIBLE
                    }
                }
            }
        })
    }
    //Ocultar bottomnavigation al abrir el teclado
    fun setupKeyboardVisibilityListener2() {
        val rootView = findViewById<View>(android.R.id.content)
        val bottomNavigationView=findViewById<BottomNavigationView>(R.id.bottomNavigationViewAdmin)
        rootView.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            private val windowVisibleDisplayFrame = Rect()
            private var isKeyboardOpen = false

            override fun onGlobalLayout() {
                rootView.getWindowVisibleDisplayFrame(windowVisibleDisplayFrame)
                val screenHeight = rootView.rootView.height

                val heightDiff = screenHeight - windowVisibleDisplayFrame.bottom
                val isOpen =
                    heightDiff > screenHeight * 0.15 // Se considera que el teclado está abierto si la diferencia de altura es superior al 15% de la altura de la pantalla

                if (isOpen != isKeyboardOpen) {
                    isKeyboardOpen = isOpen
                    if (isKeyboardOpen) {
                        // El teclado está abierto, ocultar el BottomNavigationView
                        bottomNavigationView.visibility = View.INVISIBLE
                    } else {
                        // El teclado está cerrado, mostrar el BottomNavigationView
                        bottomNavigationView.visibility = View.VISIBLE
                    }
                }
            }
        })
    }
}