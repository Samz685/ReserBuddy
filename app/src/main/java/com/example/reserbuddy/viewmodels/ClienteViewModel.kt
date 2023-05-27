package com.example.reservarapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.reservarapp.models.Cliente
import com.example.reservarapp.repositories.ClienteRepo
import com.example.reservarapp.repositories.UsuarioRepo
import java.util.*


class ClienteViewModel : ViewModel() {

    private val clienteRepo = ClienteRepo()

    fun addCliente(cliente: Cliente): String {
        return clienteRepo.addCliente(cliente)

    }

    fun getById(id : String): LiveData<Cliente> {
        val clienteData = MutableLiveData<Cliente>()
        clienteRepo.getById(id).observeForever {
            clienteData.value = it
        }

        return clienteData
    }

    fun getByTelefono(telefono : String): LiveData<Cliente> {
        val clienteData = MutableLiveData<Cliente>()
        clienteRepo.getByTelefono(telefono).observeForever {
            clienteData.value = it
        }

        return clienteData
    }

    fun exists(telefono: String): LiveData<Boolean> {
        return clienteRepo.exists(telefono)
    }

    fun getAll(): LiveData<LinkedList<Cliente>> {
        val clientesData = MutableLiveData<LinkedList<Cliente>>()
        clienteRepo.getAll().observeForever {
            clientesData.value = it
        }
        return clientesData
    }

    fun updateCliente(cliente: Cliente) {
        clienteRepo.updateCliente(cliente)

    }



    fun deleteCliente(cliente: Cliente) : String{
        return clienteRepo.deleteCliente(cliente)

    }




}