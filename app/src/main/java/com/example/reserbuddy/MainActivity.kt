package com.example.reserbuddy

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.reserbuddy.databinding.ActivityMainBinding
import com.example.reserbuddy.ui.newReserva.NewReservaFragment
import com.example.reserbuddy.ui.newReserva.NewReservaViewModel
import com.example.reserbuddy.ui.newTarea.NewTareaFragment
import com.example.reserbuddy.ui.newTarea.NewTareaViewModel
import com.example.reserbuddy.ui.reservas.ReservasFragment
import com.google.android.material.navigation.NavigationView

enum class ProviderType{
    GOOGLE
}
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var newReservaViewModel : NewReservaViewModel
    private lateinit var newTareaViewModel : NewTareaViewModel
    private lateinit var toggle : ActionBarDrawerToggle


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val drawerLayout : DrawerLayout = findViewById(R.id.drawer_layout)
        val navViewComponent : NavigationView = findViewById(R.id.nav_view_component)

        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open_drawer,R.string.close_drawer)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //setup
        val bundle: Bundle? = intent.extras
        val email:String? = bundle?.getString("email")
        val provider:String? = bundle?.getString("provider")
        setup(email?:"", provider?:"")


        //guardando datos
        val prefs: SharedPreferences.Editor? = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs?.putString("eamil", email)
        prefs?.putString("provider",provider)
        prefs?.apply()


//        //borrar datos
//        val prefs: SharedPreferences.Editor? = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
//        prefs?.clear()
//        prefs?.apply()








        // Configura la ActionBar para mostrar el botón de menú

//        val toolbar: Toolbar = findViewById(R.id.toolbar)

//        setSupportActionBar(toolbar)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
//

        // Configura el DrawerLayout para que se muestre por encima de la ActionBar
//        val toggle = ActionBarDrawerToggle(
//            this, binding.drawerLayout, binding.toolbar, R.string.open_drawer, R.string.close_drawer)
//        binding.drawerLayout.addDrawerListener(toggle)
//        toggle.syncState()







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

        newReservaViewModel = ViewModelProvider(this).get(NewReservaViewModel::class.java)
        newTareaViewModel = ViewModelProvider(this).get(NewTareaViewModel::class.java)
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
                    navController.navigate(R.id.navigation_notifications)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                else -> false
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