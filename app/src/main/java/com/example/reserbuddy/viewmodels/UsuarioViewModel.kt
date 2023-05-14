package com.example.reservarapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.reservarapp.models.Usuario
import com.example.reservarapp.repositories.UsuarioRepo
import java.util.*


class UsuarioViewModel : ViewModel() {

    private val usuarioRepo = UsuarioRepo()

    fun addUsuario(usuario: Usuario): String {
        return usuarioRepo.addUsuario(usuario)

    }

    fun updateUsuario(usuario: Usuario) {
        usuarioRepo.updateUsuario(usuario)

    }

    fun updateRol(usuario: Usuario) {
        usuarioRepo.updateRol(usuario)

    }

    fun deleteUsuario(usuario: Usuario) : String{
        return usuarioRepo.deleteUsuario(usuario)

    }


    fun getAll(): LiveData<LinkedList<Usuario>> {
        val usuariosData = MutableLiveData<LinkedList<Usuario>>()
        usuarioRepo.getAll().observeForever {
            usuariosData.value = it
        }
        return usuariosData
    }

    fun getById(usuario : Usuario): LiveData<Usuario> {
        val usuarioData = MutableLiveData<Usuario>()
        usuarioRepo.getById(usuario.id).observeForever {
            usuarioData.value = it
        }

        return usuarioData
    }

    fun getByEmail(email : String): LiveData<Usuario> {
        val usuarioData = MutableLiveData<Usuario>()
        usuarioRepo.getByEmail(email).observeForever {
            usuarioData.value = it
        }

        return usuarioData
    }



//



//    fun getByGroup(grupo : Grupo): LiveData<LinkedList<Usuario>> {
//        val usuariosData = MutableLiveData<LinkedList<Usuario>>()
//        usuarioRepo.getByGroup(grupo).observeForever {
//            usuariosData.value = it
//        }
//        return usuariosData
//    }
}