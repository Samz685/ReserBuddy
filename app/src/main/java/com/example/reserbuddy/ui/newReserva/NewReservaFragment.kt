package com.example.reserbuddy.ui.newReserva

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView

import androidx.lifecycle.ViewModelProvider
import com.example.reserbuddy.AvatarGenerator
import com.example.reserbuddy.R
import com.example.reserbuddy.databinding.FragmentNewReservaBinding
import com.example.reservarapp.models.Cliente
import com.example.reservarapp.models.Reserva
import com.example.reservarapp.viewmodels.ClienteViewModel
import com.example.reservarapp.viewmodels.ReservaViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

import java.util.*

private lateinit var datePickerDialog: DatePickerDialog
private lateinit var timePickerDialog: TimePickerDialog

var calendar = Calendar.getInstance()
var yearSelected = calendar.get(Calendar.YEAR)
var monthSelected = calendar.get(Calendar.MONTH)
var dayOfMonthSelected = calendar.get(Calendar.DAY_OF_MONTH)

private lateinit var fechaElegida: String
private lateinit var fechaElegida2: String
private lateinit var horaElegida: String
var ubiSeleccionada = ""


class NewReservaFragment : BottomSheetDialogFragment() {

    private val reservaViewModel by lazy { ViewModelProvider(this).get(ReservaViewModel::class.java) }
    private val clienteViewModel by lazy { ViewModelProvider(this).get(ClienteViewModel::class.java) }

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

        binding.btnCrearReserva.isEnabled = false
        habilitarBotonAdd()

        datePickerDialog = DatePickerDialog(requireContext())

        timePickerDialog = TimePickerDialog(
            requireContext(),
            TimePickerDialog.OnTimeSetListener { view, _hourOfDay, _minute ->
                val selectedTime = "$_hourOfDay:$_minute"
                binding.etHora.setText(selectedTime)
                val timeSelected = Calendar.getInstance()
                timeSelected.set(Calendar.HOUR_OF_DAY, _hourOfDay)
                timeSelected.set(Calendar.MINUTE, _minute)

                horaElegida = "$_hourOfDay:$_minute"

            }, 12, 0, true
        )




        binding.ivDatePicker.setOnClickListener {
            elegirFecha()
        }

        binding.ivHora.setOnClickListener {
            timePickerDialog.show()

        }

        binding.btnCrearReserva.setOnClickListener {
            crearReserva()
        }

        binding.spUbicacion.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                ubiSeleccionada = parent.getItemAtPosition(position).toString()

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                ubiSeleccionada = "Sin seleccionar"
            }
        }


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


    private fun crearReserva() {

        var nombre = binding.etNombreCliente.text.toString()
        var telefono = binding.etTelefonoCliente.text.toString()
        var fecha = fechaElegida
        var hora = horaElegida
        var numComensales = binding.etNumComensales.text.toString().toInt()
        var ubicacion = ubiSeleccionada
        var foto = R.drawable.ic_terraza
        if (ubicacion.equals("Terraza")) {
            foto = R.drawable.ic_terraza
        } else {
            foto = R.drawable.ic_comedor
        }
        var comentario = binding.etComentario.text.toString()

        var reserva = Reserva()
        reserva.cliente = nombre.toString()
        reserva.telefono = telefono
        reserva.fecha = fecha
        reserva.fechaCard = fechaElegida2
        reserva.hora = hora
        reserva.numComensales = numComensales
        reserva.ubicacion = ubicacion
        reserva.foto = foto
        reserva.comentario = comentario.toString()
        reserva.grupo = "El Pikon"
        reserva.estado = "Pendiente"

        crearCliente(telefono, nombre)

        reservaViewModel.addReserva(reserva)

        dismiss()


    }

    fun habilitarBotonAdd() {

        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Verificar si ambos EditText están vacíos
                binding.btnCrearReserva.isEnabled =
                            !binding.etNombreCliente.text.isNullOrEmpty() &&
                            !binding.etTelefonoCliente.text.isNullOrEmpty() &&
                            !binding.etFecha.text.isNullOrEmpty() &&
                            !binding.etHora.text.isNullOrEmpty() &&
                            !binding.etNumComensales.text.isNullOrEmpty()
            }

            //Estos dos metodos no necesitan ser implementados
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        }

        // Agregar el TextWatcher a ambos EditText
        binding.etNombreCliente.addTextChangedListener(textWatcher)
        binding.etTelefonoCliente.addTextChangedListener(textWatcher)
        binding.etFecha.addTextChangedListener(textWatcher)
        binding.etHora.addTextChangedListener(textWatcher)
        binding.etNumComensales.addTextChangedListener(textWatcher)

    }

    fun crearCliente(telefono: String, nombre: String) {
        clienteViewModel.exists(telefono).observeForever { existe ->
            if (!existe) {
                val cliente = Cliente()
                cliente.nombre = nombre
                cliente.telefono = telefono
                cliente.foto = AvatarGenerator().asignarIcono(nombre.lowercase())
                clienteViewModel.addCliente(cliente)


            }
        }
    }



}