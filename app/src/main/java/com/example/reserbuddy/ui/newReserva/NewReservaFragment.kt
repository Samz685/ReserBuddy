package com.example.reserbuddy.ui.newReserva

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.reserbuddy.R
import com.example.reserbuddy.databinding.FragmentNewReservaBinding
import com.example.reservarapp.models.Reserva
import com.example.reservarapp.viewmodels.ReservaViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

private lateinit var datePickerDialog: DatePickerDialog
private lateinit var timePickerDialog: TimePickerDialog

var calendar = Calendar.getInstance()
var yearSelected = calendar.get(Calendar.YEAR)
var monthSelected = calendar.get(Calendar.MONTH)
var dayOfMonthSelected = calendar.get(Calendar.DAY_OF_MONTH)
private lateinit var formattedTime : Timestamp
private lateinit var formattedDate : Date

private lateinit var fechaElegida : String
private lateinit var horaElegida : String



class NewReservaFragment : BottomSheetDialogFragment() {

    private val reservaViewModel by lazy { ViewModelProvider(this).get(ReservaViewModel::class.java) }

    private var _binding: FragmentNewReservaBinding? = null



    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentNewReservaBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return binding.root



    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        datePickerDialog = DatePickerDialog(requireContext())

        timePickerDialog = TimePickerDialog(requireContext(),
            TimePickerDialog.OnTimeSetListener { view, _hourOfDay, _minute ->
                val selectedTime = "$_hourOfDay:$_minute"
                binding.etHora.setText(selectedTime)
                val timeSelected = Calendar.getInstance()
                timeSelected.set(Calendar.HOUR_OF_DAY, _hourOfDay)
                timeSelected.set(Calendar.MINUTE, _minute)

                horaElegida = "$_hourOfDay:$_minute"

            }, 12, 0, true)




        binding.ivDatePicker.setOnClickListener {
            elegirFecha()
        }

        binding.ivHora.setOnClickListener {
            timePickerDialog.show()
        }

        binding.btnCrearReserva.setOnClickListener{
            crearReserva()
        }


    }

    private fun elegirFecha(){
        datePickerDialog.show()
        datePickerDialog.setOnDateSetListener { _, _year, _monthOfYear, _dayOfMonth ->
            val selectedDate = "$_dayOfMonth/${_monthOfYear + 1}/$_year"
            binding.etFecha.setText(selectedDate)

            fechaElegida = "$_dayOfMonth/${_monthOfYear+1}/$_year"

            dayOfMonthSelected = _dayOfMonth
            monthSelected = _monthOfYear
            yearSelected = _year




        }
    }

    private fun elegirhora(){

        timePickerDialog.show()

    }

    private fun crearReserva(){

        var grupo = "El Pikon"
        var nombre = binding.etNombreCliente.text.toString()
        var telefono = binding.etTelefonoCliente.text.toString()
        var fecha = fechaElegida
        var hora = horaElegida
        var numComensales = binding.etNumComensales.text.toString().toInt()
        var ubicacion = binding.spUbicacion.text.toString()
        var foto = R.drawable.ic_terraza
        if (ubicacion.equals("Terraza")){
            foto = R.drawable.ic_terraza
        } else{
            foto = R.drawable.ic_comedor
        }
        var comentario = binding.etComentario

        var reserva = Reserva()
        reserva.cliente = nombre.toString()
        reserva.telefono = telefono
        reserva.fecha = fecha
        reserva.hora = hora
        reserva.numComensales = numComensales
        reserva.ubicacion = ubicacion.toString()
        reserva.foto = foto
        reserva.comentario
        reserva.grupo = "El Pikon"

        reservaViewModel.addReserva(reserva)
        dismiss()








    }





}