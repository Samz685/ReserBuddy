package com.example.reserbuddy.ui.botomSheetListas

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reserbuddy.DataHolder
import com.example.reserbuddy.adapters.OnItemClickListener
import com.example.reserbuddy.adapters.UsuarioAdapterBasico
import com.example.reserbuddy.databinding.FragmentListaUsuariosBinding
import com.example.reservarapp.models.Usuario
import com.example.reservarapp.viewmodels.TareaViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


lateinit var mAdapter: RecyclerView.Adapter<UsuarioAdapterBasico.ViewHolder>
private lateinit var mLayoutManager: RecyclerView.LayoutManager
private lateinit var mRecyclerView: RecyclerView
private lateinit var listaUsuarios: MutableList<Usuario>

class ListaUsuariosFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentListaUsuariosBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListaUsuariosBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listaUsuarios = DataHolder.listaUsuarios
        inicializarAdapters()


    }



    private fun inicializarAdapters() {


        mRecyclerView = binding.recyclerUsuarios
        mLayoutManager = LinearLayoutManager(activity)
        mAdapter = UsuarioAdapterBasico(listaUsuarios, object : OnItemClickListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun OnItemClick(vista: View, position: Int) {

                var usuario = listaUsuarios[position]
                var tareaViewModel = TareaViewModel()
                var tarea = DataHolder.currentTarea
                tarea.asignedTo = usuario.alias
                tarea.asignedToId= usuario.id

                tareaViewModel.updateAsignacion(tarea)
                dismiss()







            }
            override fun onAsignarClick(position: Int) {
            }

            override fun onImageClick(position: Int) {
                TODO("Not yet implemented")
            }


        })
        mRecyclerView.adapter = mAdapter
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.itemAnimator = DefaultItemAnimator()
        mRecyclerView.layoutManager = mLayoutManager
    }



}