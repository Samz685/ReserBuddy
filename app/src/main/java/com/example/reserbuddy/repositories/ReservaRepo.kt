package com.example.reservarapp.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.reservarapp.models.Cliente
import com.example.reservarapp.models.Grupo
import com.example.reservarapp.models.Reserva
import com.example.reservarapp.models.Solicitud
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.util.*



class ReservaRepo {

    val db = Firebase.firestore

    fun addReserva(reserva: Reserva) : String{

        val db = Firebase.firestore
        val solicitudRef = db.collection("reservas").document()

        reserva.id = solicitudRef.id

        val datos = hashMapOf(
            "id" to reserva.id,
            "cliente" to reserva.cliente,
            "telefono" to reserva.telefono,
            "fecha" to reserva.fecha,
            "hora" to reserva.hora,
            "ubicacion" to reserva.ubicacion,
            "numComensales" to reserva.numComensales,
            "foto" to reserva.foto,
            "comentario" to reserva.comentario,
            "grupo" to reserva.grupo,
            "estado" to reserva.estado

        )
        solicitudRef.set(datos).addOnSuccessListener {
            Log.i("Firebase", "Datos insertados correctamente")
        }.addOnFailureListener { error ->
            Log.e("FirebaseError", error.message.toString())
        }

        return reserva.id
    }

    fun deleteReserva(reserva: Reserva) : String{

        val db = Firebase.firestore
        val solicitudRef = db.collection("reservas").document(reserva.id)

        reserva.id = solicitudRef.id

        solicitudRef.delete().addOnSuccessListener {
            Log.i("Firebase", "Datos borrados correctamente")
        }.addOnFailureListener { error ->
            Log.e("FirebaseError", error.message.toString())
        }

        return reserva.id
    }

    fun updateReserva(reserva: Reserva) {

        var reservaRef = db.collection("reservas").document(reserva.id)


        val datos = hashMapOf(

            "estado" to reserva.estado
        )
        reservaRef.update(datos as Map<String, Any>).addOnSuccessListener {
            Log.i("FireBase", "Reserva Actualizada")
        }.addOnFailureListener { error ->
            Log.e("FirebaseError", error.message.toString())
        }
    }

//    fun getAll() {
//        var reserva = Reserva()
//        db.collection("reservas").get().addOnSuccessListener { result ->
//            for (document in result) {
//                reserva = document.toObject<Reserva>()
//                println("0000000000000-----------------------------0000000000000000000")
//                println(reserva.toString())
//            }
//
//        }.addOnFailureListener { error ->
//            Log.e("FirebaseError", error.message.toString())
//        }
//    }

    fun getByGroup(grupo: Grupo): LiveData<LinkedList<Reserva>> {
        var reservasData = MutableLiveData<LinkedList<Reserva>>()
        val reservaRef = db.collection("reservas")

        val query = reservaRef.whereEqualTo("grupo", grupo.id)
        query.get().addOnSuccessListener { result ->
            var listaReservas = LinkedList<Reserva>()
            for (document in result) {
                var reserva = document.toObject<Reserva>()!!
                listaReservas.addLast(reserva)
            }
            reservasData.value = listaReservas
        }
        return reservasData
    }

//    fun getByCliente(cl : Cliente) {
//        var reserva = Reserva()
//        val db = Firebase.firestore
//
//        val reservasDb = db.collection("reservas")
//        val query = reservasDb.whereEqualTo("cliente.nombre", cl.alias)
//
//        query.get().addOnSuccessListener { result ->
//            for (document in result) {
//                reserva = document.toObject<Reserva>()
//                println("0000000000000-----------------------------0000000000000000000")
//                println(reserva.toString())
//            }
//
//        }.addOnFailureListener { error ->
//            Log.e("FirebaseError", error.message.toString())
//        }
//    }
//
//    fun addReserva(reserva: Reserva) : String{
//        val db = Firebase.firestore
//        val reservaRef = db.collection("reservas").document()
//        reserva.id = reservaRef.id
//
//        val datos = hashMapOf(
//            "id" to reserva.id,
//            "grupo" to reserva.grupo,
//            "cliente" to reserva.cliente,
//            "fecha" to reserva.fecha,
//            "numComensales" to reserva.numComensales,
//            "disposicion" to reserva.ubicacion,
//            "comentario" to reserva.comentario,
//
//        )
//        reservaRef.set(datos).addOnSuccessListener {
//            Log.i("Firebase", "Datos insertados correctamente")
//        }.addOnFailureListener { error ->
//            Log.e("FirebaseError", error.message.toString())
//        }
//        return reserva.id
//    }

}