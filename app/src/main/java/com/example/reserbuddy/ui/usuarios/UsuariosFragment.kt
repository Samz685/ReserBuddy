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
import com.example.reserbuddy.adapters.OnItemClickListener
import com.example.reserbuddy.adapters.UsuarioAdapter
import com.example.reserbuddy.databinding.FragmentUsuariosBinding
import com.example.reserbuddy.ui.botomSheetListas.ListaTareasFragment
import com.example.reserbuddy.ui.botomSheetListas.ListaUsuariosFragment
import com.example.reservarapp.models.Usuario
import com.example.reservarapp.viewmodels.TareaViewModel
import com.example.reservarapp.viewmodels.UsuarioViewModel

class UsuariosFragment : Fragment() {

    private var _binding: FragmentUsuariosBinding? = null
    private lateinit var listaUsuarios: MutableList<Usuario>
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


        swipeRefresh = binding.swipeRefresh

        listaUsuarios = mutableListOf<Usuario>()

        getAllUsuarios()
        inicializarAdapters()
        refreshUsuarios()
        getTareasPendientes()



    }

    fun refreshUsuarios() {
        swipeRefresh.setOnRefreshListener {
            getAllUsuarios()
            swipeRefresh.isRefreshing = false

        }
    }

    private fun resetearContador(){
        binding.tvContadorUsuarios.text = listaUsuarios.size.toString()
    }

    private fun inicializarAdapters() {


        mRecyclerView = binding.recyclerUsuarios
        mLayoutManager = LinearLayoutManager(activity)
        mAdapter = UsuarioAdapter(listaUsuarios, object : OnItemClickListener {
            override fun OnItemClick(vista: View, position: Int) {

                val expandible_usuario: LinearLayout = vista.findViewById(R.id.expandible_usuario)

                if (expandible_usuario.visibility == View.VISIBLE) {
                    expandible_usuario.visibility = View.GONE
                } else {
                    expandible_usuario.visibility = View.VISIBLE
                }

            }

            override fun onClick2(position: Int) {
                var usuario = listaUsuarios[position]
                    DataHolder.currentUser = usuario
                    ListaTareasFragment().show(childFragmentManager, "listaTareasFragment")
            }

            override fun onClick3(position: Int) {
                TODO("Not yet implemented")
            }

            override fun onImageClick(position: Int) {


            }


        }, usuarioViewModel)
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


            resetearContador()
            mAdapter.notifyDataSetChanged()
        })
    }

    fun getTareasPendientes() {
        val tareaViewModel = TareaViewModel()
        tareaViewModel.getByEstado("Pendiente").observe(viewLifecycleOwner, Observer {
            DataHolder.listaTareas.clear()
            for (tarea in it) {
                DataHolder.listaTareas.add(tarea)
            }

        })
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}