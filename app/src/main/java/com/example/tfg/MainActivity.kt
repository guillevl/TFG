package com.example.tfg

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.tfg.Admin.CrearEventoFragment
import com.example.tfg.Admin.ListaUsersFragment
import com.example.tfg.Admin.MainAdminFragment
import com.example.tfg.User.MainUsrFragment
import com.example.tfg.User.MisEventosFragment
import com.example.tfg.User.PerfilFragment
import com.example.tfg.User.RankingFragment
import com.example.tfg.databinding.ActivityMainBinding

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
}