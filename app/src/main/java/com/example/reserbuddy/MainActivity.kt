package com.example.reserbuddy

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.reserbuddy.databinding.ActivityMainBinding
import com.example.reserbuddy.ui.login.Login_Activity
import com.example.reserbuddy.ui.newReserva.NewReservaFragment
import com.example.reserbuddy.ui.newReserva.NewReservaViewModel
import com.example.reserbuddy.ui.newTarea.NewTareaFragment
import com.example.reserbuddy.ui.newTarea.NewTareaViewModel
import com.example.reserbuddy.ui.reservas.ReservasFragment
import com.example.reservarapp.viewmodels.UsuarioViewModel
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

enum class ProviderType{
    BASIC
}
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var usuarioViewModel : UsuarioViewModel
    private lateinit var newReservaViewModel : NewReservaViewModel
    private lateinit var newTareaViewModel : NewTareaViewModel
    private lateinit var toggle : ActionBarDrawerToggle
    private lateinit var auth: FirebaseAuth;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        // Comprobar si el usuario ya ha iniciado sesión
        if (FirebaseAuth.getInstance().currentUser == null) {
            // El usuario ya ha iniciado sesión, llevarlo a la pantalla principal de la aplicación
            val intent = Intent(this, Login_Activity::class.java)
            startActivity(intent)
            finish()
            return
        }

        newReservaViewModel = ViewModelProvider(this).get(NewReservaViewModel::class.java)
        newTareaViewModel = ViewModelProvider(this).get(NewTareaViewModel::class.java)
        usuarioViewModel = ViewModelProvider(this).get(UsuarioViewModel::class.java)


        val drawerLayout : DrawerLayout = findViewById(R.id.drawer_layout)
        val navViewComponent : NavigationView = findViewById(R.id.nav_view_component)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)

        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open_drawer,R.string.close_drawer)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        getCurrentUsuario()



        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_reservas, R.id.navigation_notifications, R.id.navigation_tareas
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)



        binding.btnAdd.setOnClickListener {
            if (DataHolder.currentFragment == "FragmentReservas"){
                NewReservaFragment().show(supportFragmentManager, "newReserva")
            } else if(DataHolder.currentFragment == "FragmentTareas"){
                NewTareaFragment().show(supportFragmentManager, "newTarea")
            }

        }

        navViewComponent.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_home -> {
                    navController.navigate(R.id.navigation_home)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_reservas -> {
                    navController.navigate(R.id.navigation_reservas)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_tareas -> {
                    navController.navigate(R.id.navigation_tareas)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_home -> {
                    navController.navigate(R.id.navigation_home)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.nav_usuarios -> {
                    navController.navigate(R.id.navigation_usuarios)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.nav_cerrar_sesion -> {

                    auth.signOut()
                    val intent = Intent(this, Login_Activity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    true

                }


                else -> false
            }
        }



    }

    fun getCurrentUsuario() {
        usuarioViewModel.getByEmail(auth.currentUser!!.email!!).observe(this, Observer {

            DataHolder.currentUser = it

        })

    }



//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            android.R.id.home -> {
//                // Abre el DrawerLayout cuando se toca el botón de menú de la ActionBar
//                binding.drawerLayout.openDrawer(GravityCompat.START)
//                return true
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }


}