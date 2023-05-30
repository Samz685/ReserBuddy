package com.example.reservarapp.repositories

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.reserbuddy.FechaGenerator
import com.example.reservarapp.models.Cliente
import com.example.reservarapp.models.Usuario
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.util.*

class ClienteRepo {

    val db = Firebase.firestore


    fun addCliente(cliente: Cliente): String {

        val clienteRef = db.collection("clientes").document()
        cliente.id = clienteRef.id

        val datos = hashMapOf(
            "id" to cliente.id,
            "nombre" to cliente.nombre,
            "telefono" to cliente.telefono,
            "email" to cliente.email,
            "foto" to cliente.foto,
            "fecha_creacion" to cliente.fecha_creacion
        )
        clienteRef.set(datos).addOnSuccessListener {
            Log.i("Firebase", "Cliente creado correctamente")
        }.addOnFailureListener { error ->
            Log.e("FirebaseError", error.message.toString())
        }

        return cliente.id
    }

    fun getById(id : String) : LiveData<Cliente> {
        var clienteData = MutableLiveData<Cliente>()
        val clienteRef = db.collection("clientes").document(id)
        clienteRef.get().addOnSuccessListener { result->
            var cliente = result.toObject<Cliente>()!!
            clienteData.value = cliente
        }

        return clienteData
    }

    fun getByTelefono(telefono : String): LiveData<Cliente> {
        var clienteData = MutableLiveData<Cliente>()
        val clienteRef = db.collection("clientes")
        val query = clienteRef.whereEqualTo("telefono", telefono)
        query.get().addOnSuccessListener { result ->
            var clienteEncontrado = Cliente()
            for (document in result) {
                clienteEncontrado = document.toObject<Cliente>()!!

            }
            clienteData.value = clienteEncontrado
        }
        return clienteData
    }

    fun exists(telefono: String): LiveData<Boolean> {
        val clienteExiste = MutableLiveData<Boolean>()
        val clienteRef = db.collection("clientes")
        val query = clienteRef.whereEqualTo("telefono", telefono)

        query.get().addOnSuccessListener { result ->
            val documents = result.documents
            clienteExiste.value = !documents.isEmpty()
        }.addOnFailureListener {
            clienteExiste.value = false
        }

        return clienteExiste
    }


    fun getAll(): LiveData<LinkedList<Cliente>> {
        var clientesData = MutableLiveData<LinkedList<Cliente>>()
        val clienteRef = db.collection("clientes")
        clienteRef.get().addOnSuccessListener { result ->
            var listaClientes = LinkedList<Cliente>()
            for (document in result) {
                var cliente = document.toObject<Cliente>()!!
                listaClientes.addLast(cliente)
            }
            clientesData.value = listaClientes
        }
        return clientesData
    }

    fun deleteCliente(cliente: Cliente) : String{

        val clienteRef = db.collection("clientes").document(cliente.id)

        cliente.id = clienteRef.id

        clienteRef.delete().addOnSuccessListener {
            Log.i("Firebase", "Cliente borrado correctamente")
        }.addOnFailureListener { error ->
            Log.e("FirebaseError", error.message.toString())
        }

        return cliente.id
    }


    fun updateCliente(cliente: Cliente) {

        var clienteRef = db.collection("clientes").document(cliente.id)


        val datos = hashMapOf(
            "nombre" to cliente.nombre,
            "email" to cliente.email,
            "telefono" to cliente.telefono,
            "descripcion" to cliente.descripcion
        )
        clienteRef.update(datos as Map<String, Any>).addOnSuccessListener {
            Log.i("FireBaase", "Cliente Actualizado")
        }.addOnFailureListener { error ->
            Log.e("FirebaseError", error.message.toString())
        }
    }




}
















