package com.example.reservarapp.repositories

import android.util.Log
import com.example.reservarapp.models.Cliente
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ClienteRepo {

    fun addCliente(cliente: Cliente): String {

        val db = Firebase.firestore
        val clienteRef = db.collection("clientes").document()
        cliente.id = clienteRef.id

        val datos = hashMapOf(
            "id" to cliente.id,
            "alias" to cliente.alias,
            "telefono" to cliente.telefono,
            "email" to cliente.email
        )
        clienteRef.set(datos).addOnSuccessListener {
            Log.i("Firebase", "Datos insertados correctamente")
        }.addOnFailureListener { error ->
            Log.e("FirebaseError", error.message.toString())
        }

        return cliente.id
    }






}
















