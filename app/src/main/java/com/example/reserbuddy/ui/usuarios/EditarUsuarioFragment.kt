package com.example.reserbuddy.ui.usuarios

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.example.reserbuddy.BotonHelper
import com.example.reserbuddy.DataHolder
import com.example.reserbuddy.databinding.FragmentEditarUsuarioBinding
import com.example.reservarapp.models.Cliente
import com.example.reservarapp.models.Usuario
import com.example.reservarapp.viewmodels.UsuarioViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment



class EditarUsuarioFragment: BottomSheetDialogFragment() {
    private val usuarioViewModel by lazy { ViewModelProvider(this).get(UsuarioViewModel::class.java) }

    private var _binding: FragmentEditarUsuarioBinding? = null
    private val binding get() = _binding!!
    private lateinit var btnAplicar: Button
    private lateinit var spRolMas: ImageView
    private lateinit var spRolMenos: ImageView
    private lateinit var rol: EditText
    private lateinit var comentario: EditText
    private lateinit var usuarioActual: Usuario
    private var esAdmin = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentEditarUsuarioBinding.inflate(inflater, container, false)
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

        spRolMas.setOnClickListener {
            cambiarRol()
        }

        spRolMenos.setOnClickListener {
            cambiarRol()
        }


    }

    fun cambiarRol() {

        if (esAdmin) {
            rol.setText("normal")
        } else {
            rol.setText("admin")
        }
        esAdmin = !esAdmin

    }


    fun inicializarComponentes() {

        btnAplicar = binding.btnAplicarRol
        spRolMas = binding.btnRolMas
        spRolMenos = binding.btnRolMenos

        rol = binding.spRol
        comentario = binding.etComentarioUsuario

    }

    fun traerDatosCliente() {
        usuarioActual = DataHolder.selectedUser

        rol.setText(usuarioActual.rol)
        comentario.setText(usuarioActual.comentario)
        esAdmin = usuarioActual.rol == "admin"
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun actualizarCliente() {

        //llenando objeto usuario para firebase
        var usuario = Usuario()
        usuario  = usuarioActual

        usuario.rol = rol.text.toString()
        usuario.comentario = comentario.text.toString()
        usuarioViewModel.updateRol(usuario)

        DataHolder.selectedUser = usuario //Una vez se persisten los datos, actualizamos el DataHolder
        (parentFragment as DetalleUsuarioFragment).inicializarDetalles()


        dismiss()

    }


}
