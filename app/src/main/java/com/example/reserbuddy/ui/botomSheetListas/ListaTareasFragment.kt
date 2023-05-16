package com.example.reserbuddy.ui.botomSheetListas

import android.annotation.SuppressLint
import android.content.Context
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
import com.example.reserbuddy.FechaGenerator
import com.example.reserbuddy.adapters.OnItemClickListener
import com.example.reserbuddy.adapters.TareaAdapterBasico
import com.example.reserbuddy.adapters.UsuarioAdapterBasico
import com.example.reserbuddy.databinding.FragmentListaTareasBinding
import com.example.reserbuddy.ui.tareas.TareasFragment
import com.example.reserbuddy.ui.usuarios.UsuariosFragment
import com.example.reservarapp.models.Tarea
import com.example.reservarapp.models.Usuario
import com.example.reservarapp.viewmodels.TareaViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


lateinit var mAdapter2: RecyclerView.Adapter<TareaAdapterBasico.ViewHolder>
private lateinit var mLayoutManager: RecyclerView.LayoutManager
private lateinit var mRecyclerView: RecyclerView
private lateinit var listaTareas: MutableList<Tarea>


class ListaTareasFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentListaTareasBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListaTareasBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listaTareas = DataHolder.listaTareas
        inicializarAdapters()


    }



    private fun inicializarAdapters() {


        mRecyclerView = binding.recyclerTareas2
        mLayoutManager = LinearLayoutManager(activity)
        mAdapter2 = TareaAdapterBasico(listaTareas, object : OnItemClickListener {
            @SuppressLint("NotifyDataSetChanged")
            @RequiresApi(Build.VERSION_CODES.O)
            override fun OnItemClick(vista: View, position: Int) {

                var tarea = listaTareas[position]
                var tareaViewModel = TareaViewModel()
                var usuario = DataHolder.currentUser
                tarea.asignedTo = usuario.alias
                tarea.asignedToId= usuario.id
                tarea.estado = "Pendiente"
                tarea.asignedDate = FechaGenerator.elegirFecha().Asignada
                tarea.asignedDateCard = FechaGenerator.elegirFecha().AsignadaCard

                tareaViewModel.updateAsignacion(tarea)
                dismiss()
                (parentFragment as UsuariosFragment).mAdapter.notifyDataSetChanged()



            }
            override fun onClick2(position: Int) {
            }

            override fun onClick3(position: Int) {
                TODO("Not yet implemented")
            }

            override fun onImageClick(position: Int) {
                TODO("Not yet implemented")
            }


        })
        mRecyclerView.adapter = mAdapter2
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.itemAnimator = DefaultItemAnimator()
        mRecyclerView.layoutManager = mLayoutManager
    }






}