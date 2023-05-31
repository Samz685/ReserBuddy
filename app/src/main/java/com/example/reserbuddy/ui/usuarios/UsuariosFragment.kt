package com.example.reserbuddy.ui.usuarios

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.reserbuddy.DataHolder
import com.example.reserbuddy.R
import com.example.reserbuddy.adapters.ClienteAdapter
import com.example.reserbuddy.adapters.OnItemClickListener
import com.example.reserbuddy.adapters.UsuarioAdapter
import com.example.reserbuddy.databinding.FragmentClientesBinding
import com.example.reserbuddy.databinding.FragmentUsuariosBinding
import com.example.reservarapp.models.Cliente
import com.example.reservarapp.models.Usuario
import com.example.reservarapp.viewmodels.ClienteViewModel
import com.example.reservarapp.viewmodels.TareaViewModel
import com.example.reservarapp.viewmodels.UsuarioViewModel
import java.util.*


class UsuariosFragment : Fragment() {

    private var _binding: FragmentUsuariosBinding? = null
    private lateinit var listaUsuarios: MutableList<Usuario>
    private lateinit var listaFiltrada: MutableList<Usuario>
    lateinit var mAdapter: RecyclerView.Adapter<UsuarioAdapter.ViewHolder>
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private lateinit var mRecyclerView: RecyclerView
    private val usuarioViewModel by lazy { ViewModelProvider(this).get(UsuarioViewModel::class.java) }
    private lateinit var swipeRefresh: SwipeRefreshLayout

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentUsuariosBinding.inflate(inflater, container, false)
        val root: View = binding.root



        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        swipeRefresh = binding.swipeRefreshUsuarios

        listaUsuarios = mutableListOf<Usuario>()
        listaFiltrada = mutableListOf<Usuario>()

        getAllUsuarios()
        inicializarAdapters()
        refreshUsuarios()
        getTareasSinAsignar()

        binding.etBuscar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // No se necesita implementar nada aquí
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Filtra la lista cada vez que el texto cambia
                filtrarLista(s.toString())
            }

            override fun afterTextChanged(s: Editable) {
                // No se necesita implementar nada aquí
            }
        })


    }

    fun refreshUsuarios() {
        swipeRefresh.setOnRefreshListener {
            getAllUsuarios()
            swipeRefresh.isRefreshing = false

        }
    }

    private fun resetearContador(){
        binding.tvContadorUsuarios.text = listaUsuarios.size.toString()
        mAdapter.notifyDataSetChanged()
    }

    private fun inicializarAdapters() {

        mRecyclerView = binding.recyclerUsuarios
        mLayoutManager = LinearLayoutManager(activity)
        mAdapter = UsuarioAdapter(listaFiltrada, object : OnItemClickListener {
            override fun OnItemClick(vista: View, position: Int) {

                DataHolder.currentUser = listaFiltrada[position]
                goDetalles()




            }

            override fun onClick2(position: Int) {

            }

            override fun onClick3(position: Int) {

            }

            override fun onImageClick(position: Int) {


            }


        },usuarioViewModel)
        mRecyclerView.adapter = mAdapter
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.itemAnimator = DefaultItemAnimator()
        mRecyclerView.layoutManager = mLayoutManager
    }

    fun getAllUsuarios() {
        usuarioViewModel.getAll().observe(viewLifecycleOwner, Observer {
            listaUsuarios.clear()
            for (usuario in it) {
                listaUsuarios.add(usuario)
            }


            listaFiltrada.clear()
            listaFiltrada.addAll(listaUsuarios)

            resetearContador()
            mAdapter.notifyDataSetChanged()
        })
    }



    fun getTareasSinAsignar() {
        val tareaViewModel = TareaViewModel()
        tareaViewModel.getByEstado("Sin asignar").observe(viewLifecycleOwner, Observer {
            DataHolder.listaTareas.clear()
            for (tarea in it) {
                DataHolder.listaTareas.add(tarea)
            }

        })
    }

    fun goDetalles(){

        val navController = findNavController(requireParentFragment())
        navController.navigate(R.id.navigation_detalle_cliente)

    }

    private fun filtrarLista(texto: String) {
        listaFiltrada.clear()
        for (item in listaUsuarios) {
            if (item.alias.lowercase().contains(texto.lowercase(Locale.getDefault()))) {
                listaFiltrada.add(item)
            }
        }
        mAdapter.notifyDataSetChanged()
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}