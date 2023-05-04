package com.example.reserbuddy.ui.newTarea

import android.app.DatePickerDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import com.example.reserbuddy.CurrentFragment
import com.example.reserbuddy.R
import com.example.reserbuddy.adapters.UsuarioAdapter
import com.example.reserbuddy.adapters.UsuarioAdapterSP
import com.example.reserbuddy.databinding.FragmentNewTareaBinding
import com.example.reserbuddy.ui.newReserva.*
import com.example.reservarapp.models.Reserva
import com.example.reservarapp.models.Tarea
import com.example.reservarapp.models.Usuario
import com.example.reservarapp.viewmodels.TareaViewModel
import com.example.reservarapp.viewmodels.UsuarioViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*

private lateinit var datePickerDialog: DatePickerDialog

class NewTareaFragment : BottomSheetDialogFragment() {

    private val tareaViewModel by lazy { ViewModelProvider(this).get(TareaViewModel::class.java) }
    private val usuarioViewModel by lazy { ViewModelProvider(this).get(UsuarioViewModel::class.java) }

    private var _binding: FragmentNewTareaBinding? = null

    private val binding get() = _binding!!

    var calendar = Calendar.getInstance()
    var yearSelected = calendar.get(Calendar.YEAR)
    var monthSelected = calendar.get(Calendar.MONTH)
    var dayOfMonthSelected = calendar.get(Calendar.DAY_OF_MONTH)
    private lateinit var fechaElegida: String
    private lateinit var fechaElegida2: String
    private lateinit var adapter: UsuarioAdapterSP
    private lateinit var listaUsuarios: MutableList<Usuario>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentNewTareaBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        datePickerDialog = DatePickerDialog(requireContext())

        binding.ivDatePicker.setOnClickListener {
            elegirFecha()
        }


        binding.btnCrearTarea.setOnClickListener {
            crearReserva()
        }

        listaUsuarios = CurrentFragment.listaUsuariosTemp



        adapter = UsuarioAdapterSP(listaUsuarios)
        val spinner: Spinner = binding.spUsuarios
        spinner.adapter = adapter


    }

    private fun crearReserva() {
        var nombre = binding.etNombreTarea.text.toString()
        var fecha = fechaElegida
        var comentario = binding.etComentario.text.toString()
        var foto = R.drawable.ic_tarea


        var tarea = Tarea()
        tarea.alias = nombre
        tarea.comentario = comentario
        tarea.asignedDate = fecha
        tarea.foto = foto

        tarea.estado = "Completada"

        tareaViewModel.addTarea(tarea)

        dismiss()
    }

    private fun elegirFecha() {
        datePickerDialog.show()
        datePickerDialog.setOnDateSetListener { _, _year, _monthOfYear, _dayOfMonth ->
            val selectedDate = "$_dayOfMonth/${_monthOfYear + 1}/$_year"
            binding.etFecha.setText(selectedDate)

            fechaElegida = String.format("%04d-%02d-%02d", _year, _monthOfYear + 1, _dayOfMonth)
            fechaElegida2 = "$_dayOfMonth/${_monthOfYear + 1}/$_year"

            dayOfMonthSelected = _dayOfMonth
            monthSelected = _monthOfYear
            yearSelected = _year


        }
    }

}

