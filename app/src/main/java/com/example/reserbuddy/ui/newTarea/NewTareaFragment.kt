package com.example.reserbuddy.ui.newTarea

import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.reserbuddy.DataHolder
import com.example.reserbuddy.FechaGenerator
import com.example.reserbuddy.R
import com.example.reserbuddy.databinding.FragmentNewTareaBinding
import com.example.reserbuddy.ui.tareas.TareasFragment
import com.example.reservarapp.models.Tarea
import com.example.reservarapp.models.Usuario
import com.example.reservarapp.viewmodels.TareaViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*


class NewTareaFragment : BottomSheetDialogFragment() {

    private val tareaViewModel by lazy { ViewModelProvider(this).get(TareaViewModel::class.java) }

    private var _binding: FragmentNewTareaBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ArrayAdapter<Usuario>

    private lateinit var listaUsuarios: MutableList<Usuario>
    private lateinit var usuarioElegido : Usuario


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentNewTareaBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spinner: Spinner = binding.spUsuarios


        binding.btnCrearTarea.setOnClickListener {
            crearReserva()
        }
        listaUsuarios = DataHolder.listaUsuariosSpinner


        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, listaUsuarios)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedUser = parent.getItemAtPosition(position) as Usuario
                usuarioElegido = selectedUser
            }

            override fun onNothingSelected(parent: AdapterView<*>) {



            }
        }



    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun crearReserva() {
        //recogiendo datos del form
        var nombre = binding.etNombreTarea.text.toString()
        var comentario = binding.etComentario.text.toString()
        var foto = R.drawable.ic_tarea
        var asignedTo = usuarioElegido.alias
        var asignedToId = usuarioElegido.id
        var fechaAsignada = FechaGenerator.elegirFecha().Asignada
        var fechaAsignadaCard = FechaGenerator.elegirFecha().AsignadaCard

        //llenando objeto tarea para firebase
        var tarea = Tarea()
        tarea.nombre = nombre
        tarea.comentario = comentario
        tarea.foto = foto

        //si tarea es asignada, estado -> pendiente, añadir fecha asignacion
        if (usuarioElegido.id != ""){
            tarea.estado = "Pendiente"
            tarea.asignedTo = asignedTo
            tarea.asignedToId = asignedToId
            tarea.asignedDate = fechaAsignada
            tarea.asignedDateCard = fechaAsignadaCard
        } else{
            tarea.estado = "Sin asignar"
            tarea.asignedDateCard = "Sin fecha"
        }

        tareaViewModel.addTarea(tarea)
        dismiss()
//        (parentFragment as TareasFragment)?.mAdapter?.notifyDataSetChanged()
    }


}

