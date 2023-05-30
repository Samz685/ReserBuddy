package com.example.reserbuddy.ui.newReserva

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import androidx.annotation.RequiresApi

import androidx.lifecycle.ViewModelProvider
import com.example.reserbuddy.*
import com.example.reserbuddy.databinding.FragmentNewReservaBinding
import com.example.reserbuddy.interfaces.FragmentListener
import com.example.reserbuddy.ui.reservas.ReservasFragment
import com.example.reservarapp.models.Cliente
import com.example.reservarapp.models.Reserva
import com.example.reservarapp.viewmodels.ClienteViewModel
import com.example.reservarapp.viewmodels.ReservaViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage

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
var cont = 1
var ubi = true








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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etNumComensales.setText(cont.toString())
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



        binding.btnPlusComensales.setOnClickListener {
            cont++
            binding.etNumComensales.setText(cont.toString())

        }

        binding.btnLessComensales.setOnClickListener {
            if (cont > 1) {
                cont--
                binding.etNumComensales.setText(cont.toString())
            }


        }

        binding.ivDatePicker.setOnClickListener {
            elegirFecha()
        }

        binding.ivHora.setOnClickListener {
            timePickerDialog.show()

        }

        binding.btnCrearReserva.setOnClickListener {
            crearReserva()
            cont = 1
        }



        binding.btnUbi1.setOnClickListener {

            swithUbicacion()
        }

        binding.btnUbi2.setOnClickListener {

            swithUbicacion()
        }

        binding.btnCalendar.setOnClickListener{
            elegirFecha()
        }

        binding.btnHora.setOnClickListener{
            timePickerDialog.show()
        }

    }

    private fun swithUbicacion() {
        ubi = !ubi
        if (ubi) {
            ubiSeleccionada = "Terraza"

        } else {
            ubiSeleccionada = "Salon"

        }

        binding.spUbicacion.setText(ubiSeleccionada)
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


    @RequiresApi(Build.VERSION_CODES.O)
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
        enviarNotificacionGeneral()


        dismiss()
//        (parentFragment as ReservasFragment).onResume()

        // Llama a este método para ejecutar la función en el Fragmento B
        (activity as MainActivity).actualizarReservas()





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

    @RequiresApi(Build.VERSION_CODES.O)
    fun crearCliente(telefono: String, nombre: String) {
        clienteViewModel.exists(telefono).observeForever { existe ->
            if (!existe) {
                val cliente = Cliente()
                cliente.nombre = nombre
                cliente.telefono = telefono
                cliente.foto = AvatarGenerator().asignarIcono(nombre.lowercase())
                cliente.fecha_creacion = FechaGenerator.elegirFecha().Asignada
                clienteViewModel.addCliente(cliente)





            }
        }
    }

    fun enviarNotificacionGeneral() {
        val listaTokens = DataHolder.listaTokens

        for (token in listaTokens) {
            val message = RemoteMessage.Builder(token)
                .setMessageId(UUID.randomUUID().toString())
                .setData(mapOf(
                    "title" to "Nueva reserva",
                    "body" to "Se ha realizado una nueva reserva!"
                ))
                .build()

            FirebaseMessaging.getInstance().send(message)
        }


    }














}