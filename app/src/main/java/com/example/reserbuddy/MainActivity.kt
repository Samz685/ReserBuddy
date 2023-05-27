package com.example.reserbuddy


import android.content.ContentValues.TAG
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.reserbuddy.databinding.ActivityMainBinding
import com.example.reserbuddy.interfaces.FragmentListener
import com.example.reserbuddy.ui.login.Login_Activity
import com.example.reserbuddy.ui.newProducto.NewProductoFragment
import com.example.reserbuddy.ui.newReserva.NewReservaFragment
import com.example.reserbuddy.ui.newReserva.NewReservaViewModel
import com.example.reserbuddy.ui.newTarea.NewTareaFragment
import com.example.reserbuddy.ui.newTarea.NewTareaViewModel
import com.example.reserbuddy.ui.reservas.ReservasFragment
import com.example.reserbuddy.ui.usuarios.UsuariosActivity
import com.example.reservarapp.viewmodels.UsuarioViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.navigation.NavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging

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
    private lateinit var tvHola: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        FirebaseApp.initializeApp(this)
        registrarDispositivo()

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
        val headerView: View = navViewComponent.getHeaderView(0)
        tvHola = headerView.findViewById(R.id.tvHolaHeader)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)

        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open_drawer,R.string.close_drawer)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        getCurrentUsuario()

        colorMenuItems(navViewComponent)


        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_reservas, R.id.navigation_lista_compra, R.id.navigation_tareas
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)



        binding.btnAdd.setOnClickListener {


            if (DataHolder.currentFragment == "FragmentReservas"){
                NewReservaFragment().show(supportFragmentManager, "newReserva")
            } else if(DataHolder.currentFragment == "FragmentTareas"){
                NewTareaFragment().show(supportFragmentManager, "newTarea")
            } else if(DataHolder.currentFragment == "FragmentListaCompra") {
                NewProductoFragment().show(supportFragmentManager, "newProducto")
            }

            println("----------------------00000")
            println(DataHolder.currentFragment)
            println("----------------------00000")

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
                R.id.nav_lista_compra -> {
                    navController.navigate(R.id.navigation_lista_compra)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.nav_clientes -> {
                    navController.navigate(R.id.navigation_clientes)
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

    private fun colorMenuItems(navViewComponent: NavigationView) {
        // Cambiar el color del texto de los elementos
        val textColor = Color.parseColor("#686868") // Color rojo
        navViewComponent.itemTextColor = ColorStateList.valueOf(textColor)

        // Cambiar el color del icono de los elementos
        val iconColor = Color.parseColor("#686868") // Color verde
        navViewComponent.itemIconTintList = ColorStateList.valueOf(iconColor)
    }

    fun getCurrentUsuario() {
        usuarioViewModel.getByEmail(auth.currentUser!!.email!!).observe(this, Observer {

            DataHolder.currentUser = it

            tvHola.text = "Hola ${DataHolder.currentUser.alias}!"



        })

    }

    fun registrarDispositivo() {

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                Log.d("FCM Token", token)
                // Aquí puedes enviar el token a tu servidor, guardar en SharedPreferences, etc.
            } else {
                Log.e("FCM Token", "Error al obtener el token")
            }
        }

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