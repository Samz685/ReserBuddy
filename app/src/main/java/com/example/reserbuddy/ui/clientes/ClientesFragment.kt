package com.example.reserbuddy.ui.usuarios

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.reserbuddy.DataHolder
import com.example.reserbuddy.FechaGenerator
import com.example.reserbuddy.R
import com.example.reserbuddy.adapters.ClienteAdapter
import com.example.reserbuddy.adapters.OnItemClickListener
import com.example.reserbuddy.adapters.UsuarioAdapter
import com.example.reserbuddy.databinding.FragmentClientesBinding
import com.example.reserbuddy.databinding.FragmentUsuariosBinding
import com.example.reserbuddy.ui.botomSheetListas.ListaTareasFragment
import com.example.reserbuddy.ui.botomSheetListas.ListaUsuariosFragment
import com.example.reservarapp.models.Cliente
import com.example.reservarapp.models.Usuario
import com.example.reservarapp.viewmodels.ClienteViewModel
import com.example.reservarapp.viewmodels.TareaViewModel
import com.example.reservarapp.viewmodels.UsuarioViewModel

class ClientesFragment : Fragment() {

    private var _binding: FragmentClientesBinding? = null
    private lateinit var listaClientes: MutableList<Cliente>
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

        getAllClientes()
        inicializarAdapters()
        refreshUsuarios()




    }

    fun refreshUsuarios() {
        swipeRefresh.setOnRefreshListener {
            getAllClientes()
            swipeRefresh.isRefreshing = false

        }
    }

    private fun resetearContador(){
        binding.tvContadorClientes.text = listaClientes.size.toString()
    }

    private fun inicializarAdapters() {


        mRecyclerView = binding.recyclerClientes
        mLayoutManager = LinearLayoutManager(activity)
        mAdapter = ClienteAdapter(listaClientes, object : OnItemClickListener {
            override fun OnItemClick(vista: View, position: Int) {


            }

            override fun onClick2(position: Int) {

            }

            override fun onClick3(position: Int) {

            }

            override fun onImageClick(position: Int) {


            }


        })
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


            resetearContador()
            mAdapter.notifyDataSetChanged()
        })
    }





    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}