package com.example.reserbuddy.ui.usuarios

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.reserbuddy.DataHolder
import com.example.reserbuddy.R
import com.example.reserbuddy.adapters.OnItemClickListener
import com.example.reserbuddy.adapters.UsuarioAdapter
import com.example.reserbuddy.databinding.ActivityUsuariosBinding
import com.example.reserbuddy.ui.botomSheetListas.ListaTareasFragment
import com.example.reservarapp.models.Usuario
import com.example.reservarapp.viewmodels.TareaViewModel
import com.example.reservarapp.viewmodels.UsuarioViewModel

class UsuariosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUsuariosBinding
    private lateinit var listaUsuarios: MutableList<Usuario>
    private lateinit var mAdapter: UsuarioAdapter
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var usuarioViewModel: UsuarioViewModel
    private lateinit var swipeRefresh: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsuariosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        swipeRefresh = binding.swipeRefresh

        listaUsuarios = mutableListOf<Usuario>()
        usuarioViewModel = ViewModelProvider(this).get(UsuarioViewModel::class.java)

        inicializarAdapters()
        refreshUsuarios()
        getTareasSinAsignar()
    }

    private fun refreshUsuarios() {
        swipeRefresh.setOnRefreshListener {
            getAllUsuarios()
            swipeRefresh.isRefreshing = false
        }
    }

    private fun resetearContador() {
        binding.tvContadorUsuarios.text = listaUsuarios.size.toString()
    }

    private fun inicializarAdapters() {
        mRecyclerView = binding.recyclerUsuarios
        mLayoutManager = LinearLayoutManager(this)
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
                val usuario = listaUsuarios[position]
                DataHolder.currentUser = usuario
                ListaTareasFragment().show(supportFragmentManager, "listaTareasFragment")
            }

            override fun onClick3(position: Int) {
                // TODO: Implementar el método onClick3
            }

            override fun onImageClick(position: Int) {
                // TODO: Implementar el método onImageClick
            }
        }, usuarioViewModel)

        mRecyclerView.adapter = mAdapter
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.itemAnimator = DefaultItemAnimator()
        mRecyclerView.layoutManager = mLayoutManager

        getAllUsuarios()
    }

    private fun getAllUsuarios() {
        usuarioViewModel.getAll().observe(this, Observer { usuarios ->
            listaUsuarios.clear()
            listaUsuarios.addAll(usuarios)

            resetearContador()
            mAdapter.notifyDataSetChanged()
        })
    }

    private fun getTareasSinAsignar() {
        val tareaViewModel = TareaViewModel()
        tareaViewModel.getByEstado("Sin asignar").observe(this, Observer { tareas ->
            DataHolder.listaTareas.clear()
            DataHolder.listaTareas.addAll(tareas)
        })
    }
}
