package com.example.reserbuddy.ui.tareas

import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
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

    private val binding get() = _binding!!

    override fun onResume() {
        super.onResume()
        getAllTareas()
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

        traerUsuarios()

        DataHolder.currentFragment = "FragmentTareas"


        swipeRefresh = binding.swipeRefresh

        listaTareas = mutableListOf<Tarea>()

        getAllTareas()
        inicializarAdapters()
        refreshTareas()

        tvMensajeTarea = binding.tvMensajeTarea

        mensajeListaVacia()



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
            getAllTareas()
            swipeRefresh.isRefreshing = false
            resetearContador()

        }
    }

    private fun resetearContador(){
        binding.tvContadorTareas.text = listaTareas.size.toString()
        mensajeListaVacia()
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
            override fun onAsignarClick(position: Int) {

                var tarea = listaTareas[position]
                tarea.asignedDate = FechaGenerator.elegirFecha().Asignada
                tarea.asignedDateCard = FechaGenerator.elegirFecha().AsignadaCard
                DataHolder.currentTarea = tarea
                ListaUsuariosFragment().show(childFragmentManager, "listaUsuariosFragment")

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
}