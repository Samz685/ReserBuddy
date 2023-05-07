package com.example.reserbuddy

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.reserbuddy.databinding.ActivityMainBinding
import com.example.reserbuddy.ui.newReserva.NewReservaFragment
import com.example.reserbuddy.ui.newReserva.NewReservaViewModel
import com.example.reserbuddy.ui.newTarea.NewTareaFragment
import com.example.reserbuddy.ui.newTarea.NewTareaViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var newReservaViewModel : NewReservaViewModel
    private lateinit var newTareaViewModel : NewTareaViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Configura la ActionBar para mostrar el botón de menú

        val toolbar: Toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)


        // Configura el DrawerLayout para que se muestre por encima de la ActionBar
        val toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, binding.toolbar, R.string.open_drawer, R.string.close_drawer)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()







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




    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // Abre el DrawerLayout cuando se toca el botón de menú de la ActionBar
                binding.drawerLayout.openDrawer(GravityCompat.START)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}