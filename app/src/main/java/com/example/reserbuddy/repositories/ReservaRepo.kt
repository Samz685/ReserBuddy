package com.example.reservarapp.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.reservarapp.models.Reserva
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
            "fechaCard" to reserva.fechaCard,
            "hora" to reserva.hora,
            "ubicacion" to reserva.ubicacion,
            "numComensales" to reserva.numComensales,
            "foto" to reserva.foto,
            "comentario" to reserva.comentario,
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



    fun getByCliente(numCliente: String): LiveData<LinkedList<Reserva>> {
        var reservasData = MutableLiveData<LinkedList<Reserva>>()
        val reservaRef = db.collection("reservas")

        val query = reservaRef.whereEqualTo("telefono", numCliente)
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

    fun getToday(): LiveData<LinkedList<Reserva>> {

        val calendar = Calendar.getInstance()

        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)
        var fechaInicial = String.format("%04d-%02d-%02d", year, month, day)

        var reservasData = MutableLiveData<LinkedList<Reserva>>()
        val reservaRef = db.collection("reservas")

        val query = reservaRef.whereEqualTo("fecha", fechaInicial)

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

    fun getWeek(): LiveData<LinkedList<Reserva>> {

        val fecha1 = Calendar.getInstance()
        val fecha2 = Calendar.getInstance()
        fecha2.add(Calendar.DATE, 7)

        val day1 = fecha1.get(Calendar.DAY_OF_MONTH)
        val month1 = fecha1.get(Calendar.MONTH) + 1
        val year1 = fecha1.get(Calendar.YEAR)
        var fechaInicial = String.format("%04d-%02d-%02d", year1, month1, day1)


        val day2 = fecha2.get(Calendar.DAY_OF_MONTH)
        val month2 = fecha2.get(Calendar.MONTH) + 1
        val year2 = fecha2.get(Calendar.YEAR)
        var fechaFinal = String.format("%04d-%02d-%02d", year2, month2, day2)

        var reservasData = MutableLiveData<LinkedList<Reserva>>()
        val reservaRef = db.collection("reservas")

        val query = reservaRef.orderBy("fecha").startAt(fechaInicial)
            .endAt(fechaFinal)
        println("reserva********-------------------------------$fechaFinal")
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

    fun getMonth(): LiveData<LinkedList<Reserva>> {

        val fecha1 = Calendar.getInstance()
        val fecha2 = Calendar.getInstance().apply {
            timeInMillis = fecha1.timeInMillis
            add(Calendar.MONTH, 1)
        }

        val day1 = fecha1.get(Calendar.DAY_OF_MONTH)
        val month1 = fecha1.get(Calendar.MONTH) + 1
        val year1 = fecha1.get(Calendar.YEAR)
        var fechaInicial = String.format("%04d-%02d-%02d", year1, month1, day1)


        val day2 = fecha2.get(Calendar.DAY_OF_MONTH)
        val month2 = fecha2.get(Calendar.MONTH) + 1
        val year2 = fecha2.get(Calendar.YEAR)
        var fechaFinal = String.format("%04d-%02d-%02d", year2, month2, day2)

        var reservasData = MutableLiveData<LinkedList<Reserva>>()
        val reservaRef = db.collection("reservas")

        val query = reservaRef.orderBy("fecha").startAt(fechaInicial)
            .endAt(fechaFinal)
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

    fun getPeriod(fecha1 : String, fecha2 : String): LiveData<LinkedList<Reserva>> {


        var reservasData = MutableLiveData<LinkedList<Reserva>>()
        val reservaRef = db.collection("reservas")

        val query = reservaRef.orderBy("fecha").startAt(fecha1)
            .endAt(fecha2)
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

}