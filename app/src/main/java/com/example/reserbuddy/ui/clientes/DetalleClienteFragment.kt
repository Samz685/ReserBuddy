package com.example.reserbuddy.ui.usuarios

import android.os.Bundle
import android.provider.ContactsContract.Data
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.reserbuddy.DataHolder
import com.example.reserbuddy.adapters.ReservaAdapter
import com.example.reserbuddy.databinding.FragmentDetalleClienteBinding
import com.example.reservarapp.models.Reserva
import com.example.reservarapp.viewmodels.ClienteViewModel
import com.example.reservarapp.viewmodels.ReservaViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class DetalleClienteFragment : Fragment() {

    private var _binding: FragmentDetalleClienteBinding? = null
    private lateinit var listaReservas: MutableList<Reserva>
    lateinit var mAdapter: RecyclerView.Adapter<ReservaAdapter.ViewHolder>
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private lateinit var mRecyclerView: RecyclerView
    private val clienteViewModel by lazy { ViewModelProvider(this).get(ClienteViewModel::class.java) }
    private val reservaViewModel by lazy { ViewModelProvider(this).get(ReservaViewModel::class.java) }
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var ivFoto : ImageView
    private lateinit var tvNombre : TextView
    private lateinit var tvTelefono : TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvComentario : TextView


    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentDetalleClienteBinding.inflate(inflater, container, false)
        val root: View = binding.root



        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



//        swipeRefresh = binding.swipeRefreshDetalleCliente

        listaReservas = mutableListOf<Reserva>()

        getReservasByCliente(DataHolder.currentCliente.telefono)
        inicializarAdapters()
        refreshUsuarios()




    }


    fun inicializarDetaller(){

        var cliente = DataHolder.currentCliente

        ivFoto = binding.ivDetalleCliente
        tvNombre = binding.tvDetalleClienteNombre
        tvTelefono = binding.tvDetalleClienteTel
        tvEmail = binding.tvDetalleClienteTel
        tvComentario = binding.tvDetalleClienteTel

        ivFoto =
        ivFoto.setImageResource(cliente.foto)
        tvNombre.text = cliente.nombre
        tvTelefono.text = cliente.telefono
        tvEmail.text = cliente.nombre.toString()
        tvComentario.text= cliente.telefono

    }
    fun refreshUsuarios() {
        swipeRefresh.setOnRefreshListener {
            getReservasByCliente(DataHolder.currentCliente.telefono)
            swipeRefresh.isRefreshing = false

        }
    }

    private fun inicializarAdapters() {


//        mRecyclerView = binding.recyclerReservas
//        mLayoutManager = LinearLayoutManager(activity)
//        mAdapter = ClienteAdapter(listaReservas, object : OnItemClickListener {
//            override fun OnItemClick(vista: View, position: Int) {
//
//
//            }
//
//            override fun onClick2(position: Int) {
//
//            }
//
//            override fun onClick3(position: Int) {
//
//            }
//
//            override fun onImageClick(position: Int) {
//
//
//            }
//
//
//        })
//        mRecyclerView.adapter = mAdapter
//        mRecyclerView.setHasFixedSize(true)
//        mRecyclerView.layoutManager = mLayoutManager
    }

    fun getReservasByCliente(numCliente : String) {
        reservaViewModel.getByCliente(numCliente).observe(viewLifecycleOwner, Observer {
            listaReservas.clear()
            for (reserva in it) {
                listaReservas.add(reserva)
            }

            mAdapter.notifyDataSetChanged()
        })
    }





    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}