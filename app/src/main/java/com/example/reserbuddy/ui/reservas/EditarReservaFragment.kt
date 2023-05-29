package com.example.reserbuddy.ui.reservas

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.example.reserbuddy.BotonHelper
import com.example.reserbuddy.DataHolder
import com.example.reserbuddy.R
import com.example.reserbuddy.databinding.FragmentEditarClienteBinding
import com.example.reserbuddy.databinding.FragmentEditarReservaBinding
import com.example.reserbuddy.ui.newReserva.*
import com.example.reserbuddy.ui.usuarios.UsuariosFragment
import com.example.reservarapp.models.Cliente
import com.example.reservarapp.models.Reserva
import com.example.reservarapp.viewmodels.ClienteViewModel
import com.example.reservarapp.viewmodels.ReservaViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*


class EditarReservaFragment : BottomSheetDialogFragment() {
    private val reservaViewModel by lazy { ViewModelProvider(this).get(ReservaViewModel::class.java) }

    private var _binding: FragmentEditarReservaBinding? = null
    private val binding get() = _binding!!
    private lateinit var datePickerDialog: DatePickerDialog
    private lateinit var timePickerDialog: TimePickerDialog
    private lateinit var btnAplicar: Button
    private lateinit var etHora: EditText
    private lateinit var etFecha: EditText
    private lateinit var etNumComensales: EditText
    private lateinit var etUbicacion: EditText
    private lateinit var etComentario: EditText
    private lateinit var reservaActual: Reserva
    private lateinit var fechaElegida: String
    private lateinit var fechaElegida2: String
    private lateinit var horaElegida: String
    var ubi = true



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentEditarReservaBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        inicializarComponentes()
        traerDatosReserva()

        datePickerDialog = DatePickerDialog(requireContext())

        timePickerDialog = TimePickerDialog(
            requireContext(),
            TimePickerDialog.OnTimeSetListener { view, _hourOfDay, _minute ->
                val selectedTime = "$_hourOfDay:$_minute"
                binding.etHoraRes.setText(selectedTime)
                val timeSelected = Calendar.getInstance()
                timeSelected.set(Calendar.HOUR_OF_DAY, _hourOfDay)
                timeSelected.set(Calendar.MINUTE, _minute)

                horaElegida = "$_hourOfDay:$_minute"

            }, 12, 0, true
        )

        btnAplicar.setOnClickListener {
            actualizarCliente()
        }

        binding.btnPlusComensales.setOnClickListener {
            cont++
            binding.etNumComensalesRes.setText(cont.toString())

        }

        binding.btnLessComensales.setOnClickListener {
            if (cont > 1) {
                cont--
                binding.etNumComensalesRes.setText(cont.toString())
            }


        }




    }


    fun inicializarComponentes() {

        btnAplicar = binding.btnAplicarRes
        btnAplicar.isEnabled = false
        etHora = binding.etHoraRes
        etFecha = binding.etFechaRes
        etNumComensales = binding.etNumComensalesRes
        etUbicacion = binding.spUbicacionRes
        etComentario = binding.etComentarioRes

        val editTexts = arrayOf(
            etHora, etFecha, etNumComensales, etUbicacion, etComentario
        )

        BotonHelper.habilitarBotonAdd(btnAplicar, *editTexts)

    }

    fun traerDatosReserva() {
        reservaActual = DataHolder.currentReserva

        etHora.setText(reservaActual.hora)
        etFecha.setText(reservaActual.fecha)
        etNumComensales.setText(reservaActual.numComensales)
        etUbicacion.setText(reservaActual.ubicacion)
        etComentario.setText(reservaActual.comentario)

    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun actualizarCliente() {

        //llenando objeto tarea para firebase
        var reserva = Reserva()
        reserva.id = reservaActual.id

        reserva.hora = etHora.text.toString()
        reserva.fecha = etFecha.text.toString()
        reserva.numComensales = etNumComensales.text.toString().toInt()
        reserva.ubicacion = etUbicacion.text.toString()
        reserva.comentario = etComentario.text.toString()
        var img = R.drawable.ic_terraza
        if(reserva.ubicacion == "Terraza"){
            var img = R.drawable.ic_terraza
        } else {
            var img = R.drawable.ic_comedor
        }
        reserva.foto = img
        reservaViewModel.updateReserva(reserva)




        DataHolder.currentReserva = reserva //Una vez se persisten los datos, actualizamos el DataHolder
        (parentFragment as ReservasFragment).mAdapter.notifyDataSetChanged()


        dismiss()

    }

    private fun elegirFecha() {
        datePickerDialog.show()
        datePickerDialog.setOnDateSetListener { _, _year, _monthOfYear, _dayOfMonth ->
            val selectedDate = "$_dayOfMonth/${_monthOfYear + 1}/$_year"
            binding.etFechaRes.setText(selectedDate)

            fechaElegida = String.format("%04d-%02d-%02d", _year, _monthOfYear + 1, _dayOfMonth)
            fechaElegida2 = "$_dayOfMonth/${_monthOfYear + 1}/$_year"

            dayOfMonthSelected = _dayOfMonth
            monthSelected = _monthOfYear
            yearSelected = _year


        }
    }

    private fun swithUbicacion() {
        ubi = !ubi
        if (ubi) {
            ubiSeleccionada = "Terraza"

        } else {
            ubiSeleccionada = "Salon"

        }

        binding.spUbicacionRes.setText(ubiSeleccionada)
    }



}
