package com.example.reserbuddy.ui.clientes

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
import com.example.reserbuddy.ui.usuarios.UsuariosFragment
import com.example.reservarapp.models.Cliente
import com.example.reservarapp.viewmodels.ClienteViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment



class EditarClienteFragment : BottomSheetDialogFragment() {
    private val clienteViewModel by lazy { ViewModelProvider(this).get(ClienteViewModel::class.java) }

    private var _binding: FragmentEditarClienteBinding? = null
    private val binding get() = _binding!!
    private lateinit var btnAplicar: Button
    private lateinit var nombre: EditText
    private lateinit var email: EditText
    private lateinit var telefono: EditText
    private lateinit var comentario: EditText
    private lateinit var clienteActual: Cliente


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentEditarClienteBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        inicializarComponentes()
        traerDatosCliente()

        btnAplicar.setOnClickListener {
            actualizarCliente()
        }


    }


    fun inicializarComponentes() {

        btnAplicar = binding.btnAplicarCliente
        btnAplicar.isEnabled = false
        nombre = binding.etNombreClienteEd
        telefono = binding.etTelefonoClienteEd
        email = binding.etEmailClienteEd
        comentario = binding.etComentarioClienteEd

        val editTexts = arrayOf(
            nombre, telefono
        )

        BotonHelper.habilitarBotonAdd(btnAplicar, *editTexts)

    }

    fun traerDatosCliente() {
        clienteActual = DataHolder.currentCliente

        nombre.setText(clienteActual.nombre)
        telefono.setText(clienteActual.telefono)
        email.setText(clienteActual.email)
        comentario.setText(clienteActual.descripcion)

    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun actualizarCliente() {

        //llenando objeto tarea para firebase
        var cliente = Cliente()
        cliente.id = clienteActual.id

        cliente.nombre = nombre.text.toString()
        cliente.telefono = telefono.text.toString()
        cliente.email = email.text.toString()
        cliente.descripcion = comentario.text.toString()
        clienteViewModel.updateCliente(cliente)

        var img = DataHolder.currentCliente.foto
        cliente.foto = img
        DataHolder.currentCliente = cliente //Una vez se persisten los datos, actualizamos el DataHolder
        (parentFragment as DetalleClienteFragment).inicializarDetalles()


        dismiss()

    }


}
