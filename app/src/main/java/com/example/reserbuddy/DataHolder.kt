package com.example.reserbuddy

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.reserbuddy.adapters.TareaAdapter
import com.example.reservarapp.models.Categoria
import com.example.reservarapp.models.Cliente
import com.example.reservarapp.models.Tarea
import com.example.reservarapp.models.Usuario
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DataHolder {


    companion object {

        var currentFragment = ""
        var listaUsuariosSpinner = mutableListOf<Usuario>()
        var listaUsuarios = mutableListOf<Usuario>()
        var listaTareas = mutableListOf<Tarea>()
        var currentUser = Usuario()
        var currentTarea = Tarea()
        var emailRecuperado = ""
        var currentCliente = Cliente()
        var listaTokens = mutableListOf<String>()



    }


}