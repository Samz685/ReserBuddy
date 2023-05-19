package com.example.reserbuddy.ui.tareas

import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.ContactsContract.Data
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.reserbuddy.DataHolder
import com.example.reserbuddy.FechaGenerator
import com.example.reserbuddy.R
import com.example.reserbuddy.adapters.OnItemClickListener
import com.example.reserbuddy.adapters.TareaAdapter
import com.example.reserbuddy.databinding.FragmentTareasBinding
import com.example.reserbuddy.ui.botomSheetListas.ListaUsuariosFragment
import com.example.reservarapp.models.Tarea
import com.example.reservarapp.models.Usuario
import com.example.reservarapp.viewmodels.TareaViewModel
import com.example.reservarapp.viewmodels.UsuarioViewModel
import com.google.android.material.tabs.TabLayout

class TareasFragment : Fragment() {

    private var _binding: FragmentTareasBinding? = null
    private lateinit var listaTareas: MutableList<Tarea>
    lateinit var mAdapter: RecyclerView.Adapter<TareaAdapter.ViewHolder>
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private lateinit var mRecyclerView: RecyclerView
    private val tareaViewModel by lazy { ViewModelProvider(this).get(TareaViewModel::class.java) }
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var tvMensajeTarea : TextView
    private val usuarioViewModel by lazy { ViewModelProvider(this).get(UsuarioViewModel::class.java) }
    private lateinit var tabLayout: TabLayout
    var tareaTiming = ""
    private lateinit var user : Usuario

    private val binding get() = _binding!!

    override fun onResume() {
        super.onResume()
        traerTareas()
        true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentTareasBinding.inflate(inflater, container, false)
        val root: View = binding.root




        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        user = DataHolder.currentUser
        tabLayout = binding.tabReservas


        traerUsuarios()

        DataHolder.currentFragment = "FragmentTareas"


        swipeRefresh = binding.swipeRefresh

        listaTareas = mutableListOf<Tarea>()
        tvMensajeTarea = binding.tvMensajeTarea



        traerTareas()
        inicializarAdapters()
        refreshTareas()

        setupTabs()



        mensajeListaVacia()



    }

    private fun traerTareasRol() {
        if(DataHolder.currentUser.rol == "admin"){
            getAllTareas()
        } else{
            getTareasByUsuario()
        }
        tareaTiming = "Todas"
    }

    private fun traerTareasPendientesUsuario() {
        if(DataHolder.currentUser.rol == "admin"){
            getByEstado("Pendiente")
        } else{
            getTareasByUsuarioEstado(DataHolder.currentUser.id,"Pendiente")
        }
        tareaTiming = "Pendiente"
    }

    private fun traerTareasSinAsignar() {
        if(DataHolder.currentUser.rol == "admin"){
            getByEstado("Sin asignar")
        }
        tareaTiming = "Sin asignar"
    }

    private fun mensajeListaVacia() {
        if (listaTareas.size == 0) {
            tvMensajeTarea.visibility = VISIBLE
        } else {
            tvMensajeTarea.visibility = GONE
        }
    }

    fun refreshTareas() {
        swipeRefresh.setOnRefreshListener {
            traerTareas()
            swipeRefresh.isRefreshing = false
            resetearContador()

        }
    }

    private fun resetearContador(){
        binding.tvContadorTareas.text = listaTareas.size.toString()
        mensajeListaVacia()
    }

    private fun traerTareas() {


        when (tareaTiming) {
            "Todas" -> traerTareasRol()
            "Pendiente" -> traerTareasPendientesUsuario()
            "Sin asignar" -> traerTareasSinAsignar()
            else -> traerTareasRol()
        }
        resetearContador()


    }

    private fun inicializarAdapters() {


        mRecyclerView = binding.recyclerTareas
        mLayoutManager = LinearLayoutManager(activity)
        mAdapter = TareaAdapter(listaTareas, object : OnItemClickListener {
            override fun OnItemClick(vista: View, position: Int) {

                val expandible_tarea: LinearLayout = vista.findViewById(R.id.expandible_tarea)

                if (expandible_tarea.visibility == View.VISIBLE) {
                    expandible_tarea.visibility = View.GONE
                } else {
                    expandible_tarea.visibility = View.VISIBLE
                }

            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onClick2(position: Int) {

                var tarea = listaTareas[position]
                if(tarea.asignedToId == ""){

                    tarea.asignedDate = FechaGenerator.elegirFecha().Asignada
                    tarea.asignedDateCard = FechaGenerator.elegirFecha().AsignadaCard
                    DataHolder.currentTarea = tarea
                    ListaUsuariosFragment().show(childFragmentManager, "listaUsuariosFragment")


                } else{
                    tareaViewModel.quitarAsignacion(tarea)
                    onResume()



                }

                mAdapter.notifyDataSetChanged()


            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onClick3(position: Int) {
                var tarea = listaTareas[position]
                if(tarea.estado == "Pendiente"){
                    tarea.estado = "Completada"
                    tarea.doneDate = FechaGenerator.elegirFecha().Asignada
                    tarea.doneDateCard = FechaGenerator.elegirFecha().AsignadaCard
                    tareaViewModel.updateEstado(tarea)

                } else{
                    tarea.estado = "Pendiente"
                    tarea.doneDate = ""
                    tarea.doneDateCard = "Sin fecha"
                    tareaViewModel.updateEstado(tarea)
                }
                mAdapter.notifyDataSetChanged()
            }

            override fun onImageClick(position: Int) {
                TODO("Not yet implemented")
            }


        }, tareaViewModel)
        mRecyclerView.adapter = mAdapter
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.itemAnimator = DefaultItemAnimator()
        mRecyclerView.layoutManager = mLayoutManager


    }

    fun getAllTareas() {
        tareaViewModel.getAll().observe(viewLifecycleOwner, Observer {
            listaTareas.clear()
            for (tarea in it) {
                listaTareas.add(tarea)
            }


            resetearContador()
            mAdapter.notifyDataSetChanged()
        })
        tareaTiming = "Todas"
    }

    fun getTareasByUsuario() {
        tareaViewModel.getByUsuario(DataHolder.currentUser.id).observe(viewLifecycleOwner, Observer {
            listaTareas.clear()
            for (tarea in it) {
                listaTareas.add(tarea)
            }


            resetearContador()
            mAdapter.notifyDataSetChanged()
        })
        tareaTiming = "Todas"
    }

    fun getTareasByUsuarioEstado(idUsuario : String, estado : String) {
        tareaViewModel.getByUsuarioEstado(idUsuario, estado).observe(viewLifecycleOwner, Observer {
            listaTareas.clear()
            for (tarea in it) {
                listaTareas.add(tarea)
            }


            resetearContador()
            mAdapter.notifyDataSetChanged()
        })
        if(estado == "Pendiente") tareaTiming = "Pendiente"
        else if (estado == "Sin asignar") tareaTiming = "Sin asignar"
    }

    fun getByEstado(estado : String) {
        tareaViewModel.getByEstado(estado).observe(viewLifecycleOwner, Observer {
            listaTareas.clear()
            for (tarea in it) {
                listaTareas.add(tarea)
            }
            resetearContador()
            mAdapter.notifyDataSetChanged()
        })
        if(estado == "Pendiente") tareaTiming = "Pendiente"
        else if (estado == "Sin asignar") tareaTiming = "Sin asignar"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun getAllUsuariosSP() {
        usuarioViewModel.getAll().observe(viewLifecycleOwner, Observer {
            DataHolder.listaUsuariosSpinner.clear()
            for (usuario in it) {
                DataHolder.listaUsuariosSpinner.add(usuario)
            }
            var ur = Usuario()
            ur.alias = "Sin asignar"
            DataHolder.listaUsuariosSpinner.add(0,ur)
        })

    }

    //Para llenar el bottomsheet de asignacion de usuarios
    fun getAllUsuarios() {
        usuarioViewModel.getAll().observe(viewLifecycleOwner, Observer {
            DataHolder.listaUsuarios.clear()
            for (usuario in it) {
                DataHolder.listaUsuarios.add(usuario)
            }
        })

    }

    fun traerUsuarios(){
        getAllUsuariosSP()
        getAllUsuarios()
    }

    private fun setupTabs() {
        tabLayout.addTab(tabLayout.newTab().setText(R.string.todas))
        tabLayout.addTab(tabLayout.newTab().setText(R.string.pendientes))
//        tabLayout.addTab(tabLayout.newTab().setText(R.string.completadas))
        if(user.rol == "admin"){
            tabLayout.addTab(tabLayout.newTab().setText(R.string.sin_asignar))
        }


        // Cambiar la lista de reservas según la pestaña seleccionada
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> traerTareasRol()
                    1 -> traerTareasPendientesUsuario()
                    2 -> traerTareasSinAsignar()

                    else -> getAllTareas()
                }
                mRecyclerView.adapter?.notifyDataSetChanged()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }
}