package com.example.reserbuddy.ui.clientes

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
import com.example.reserbuddy.databinding.FragmentClientesBinding
import com.example.reservarapp.models.Cliente
import com.example.reservarapp.viewmodels.ClienteViewModel
import java.util.*


class ClientesFragment : Fragment() {

    private var _binding: FragmentClientesBinding? = null
    private lateinit var listaClientes: MutableList<Cliente>
    private lateinit var listaFiltrada: MutableList<Cliente>
    lateinit var mAdapter: RecyclerView.Adapter<ClienteAdapter.ViewHolder>
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private lateinit var mRecyclerView: RecyclerView
    private val clienteViewModel by lazy { ViewModelProvider(this).get(ClienteViewModel::class.java) }
    private lateinit var swipeRefresh: SwipeRefreshLayout

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentClientesBinding.inflate(inflater, container, false)
        val root: View = binding.root



        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        swipeRefresh = binding.swipeRefreshClientes

        listaClientes = mutableListOf<Cliente>()
        listaFiltrada = mutableListOf<Cliente>()

        getAllClientes()
        inicializarAdapters()
        refreshUsuarios()

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
            getAllClientes()
            swipeRefresh.isRefreshing = false

        }
    }

    private fun resetearContador(){
        binding.tvContadorClientes.text = listaClientes.size.toString()
        mAdapter.notifyDataSetChanged()
    }

    private fun inicializarAdapters() {

        mRecyclerView = binding.recyclerClientes
        mLayoutManager = LinearLayoutManager(activity)
        mAdapter = ClienteAdapter(listaFiltrada, object : OnItemClickListener {
            override fun OnItemClick(vista: View, position: Int) {

                DataHolder.currentCliente = listaFiltrada[position]
                goDetalles()




            }

            override fun onClick2(position: Int) {

            }

            override fun onClick3(position: Int) {

            }

            override fun onImageClick(position: Int) {


            }


        },clienteViewModel)
        mRecyclerView.adapter = mAdapter
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.itemAnimator = DefaultItemAnimator()
        mRecyclerView.layoutManager = mLayoutManager
    }

    fun getAllClientes() {
        clienteViewModel.getAll().observe(viewLifecycleOwner, Observer {
            listaClientes.clear()
            for (cliente in it) {
                listaClientes.add(cliente)
            }


            listaFiltrada.clear()
            listaFiltrada.addAll(listaClientes)
            resetearContador()
            mAdapter.notifyDataSetChanged()
        })
    }

    fun goDetalles(){

        val navController = findNavController(requireParentFragment())
        navController.navigate(R.id.navigation_detalle_cliente)

    }

    private fun filtrarLista(texto: String) {
        listaFiltrada.clear()
        for (item in listaClientes) {
            if (item.nombre.lowercase().contains(texto.lowercase(Locale.getDefault()))) {
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