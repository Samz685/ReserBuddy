package com.example.reservarapp.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.reserbuddy.FechaGenerator
import com.example.reservarapp.models.Reserva
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.util.*



class ReservaRepo {

    val db = Firebase.firestore
//    val db = FirebaseDatabase.getInstance().reference



    fun addReserva(reserva: Reserva) : String{


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


        val solicitudRef = db.collection("reservas").document(reserva.id)

        reserva.id = solicitudRef.id

        solicitudRef.delete().addOnSuccessListener {
            Log.i("Firebase", "Datos borrados correctamente")
        }.addOnFailureListener { error ->
            Log.e("FirebaseError", error.message.toString())
        }

        return reserva.id
    }

    fun updateEstado(reserva: Reserva) {

        var reservaRef = db.collection("reservas").document(reserva.id)


        val datos = hashMapOf(

            "estado" to reserva.estado
        )
        reservaRef.update(datos as Map<String, Any>).addOnSuccessListener {
            Log.i("FireBase", "Estado reserva Actualizado")
        }.addOnFailureListener { error ->
            Log.e("FirebaseError", error.message.toString())
        }
    }

    fun updateReserva(reserva: Reserva) {

        var reservaRef = db.collection("reservas").document(reserva.id)


        val datos = hashMapOf(

            "fecha" to reserva.fecha,
            "hora" to reserva.hora,
            "numComensales" to reserva.numComensales,
            "ubicacion" to reserva.ubicacion,
            "comentario" to reserva.comentario,
            "foto" to reserva.foto
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

    fun getByClienteByEstado(numCliente: String, estado : String): LiveData<LinkedList<Reserva>> {
        var reservasData = MutableLiveData<LinkedList<Reserva>>()
        val reservaRef = db.collection("reservas")

        val query = reservaRef.whereEqualTo("telefono", numCliente).whereEqualTo("estado", estado)
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

    fun getToday(): MutableLiveData<LinkedList<Reserva>> {

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



    fun getChartTotal(): LiveData<Int> {

        val reservasData = MutableLiveData<Int>()
        val reservaRef = db.collection("reservas")

        reservaRef.get().addOnSuccessListener { result ->
            var countReserva = result.size()
            reservasData.value = countReserva
        }
        return reservasData
    }


    fun getChartEstado(estado: String): LiveData<Int> {

        val reservasData = MutableLiveData<Int>()
        val reservaRef = db.collection("reservas")

        var query = reservaRef.whereEqualTo("estado", estado)

        query.get().addOnSuccessListener { result ->
            var countReserva = result.size()
            reservasData.value = countReserva
        }
        return reservasData
    }

    fun getChartMonth(mes: Int): LiveData<Int> {

        val calendar = Calendar.getInstance()

        val year = calendar.get(Calendar.YEAR)
        val month = mes
        val daysInMonth = FechaGenerator.getDaysInMonth(year, month)

        val fechaInicial = String.format("%04d-%02d-01", year, month)
        val fechaFinal = String.format("%04d-%02d-%02d", year, month, daysInMonth)

        val reservasData = MutableLiveData<Int>()
        val reservaRef = db.collection("reservas")

        val query = reservaRef.whereGreaterThanOrEqualTo("fecha", fechaInicial)
            .whereLessThanOrEqualTo("fecha", fechaFinal)

        query.get().addOnSuccessListener { result ->
          var countReserva = result.size()
            reservasData.value = countReserva
        }
        return reservasData
    }

    fun getChartYear(year: Int): LiveData<MutableList<Int>> {
        val reservasData = MutableLiveData<MutableList<Int>>()
        val reservaRef = db.collection("reservas")

        val mesesChart = MutableList(12) { 0 }
        var count = 0

        for (mes in 1..12) {
            val daysInMonth = FechaGenerator.getDaysInMonth(year, mes)
            val fechaInicial = String.format("%04d-%02d-01", year, mes)
            val fechaFinal = String.format("%04d-%02d-%02d", year, mes, daysInMonth)

            val query = reservaRef.whereGreaterThanOrEqualTo("fecha", fechaInicial)
                .whereLessThanOrEqualTo("fecha", fechaFinal)

            query.get().addOnSuccessListener { result ->
                val countReserva = result.size()
                mesesChart[mes - 1] = countReserva
                count++

                if (count == 12) {
                    reservasData.value = mesesChart
                }
            }
        }

        return reservasData
    }



//    fun getBestClientes(): LiveData<List<Pair<String, Int>>> {
//        val topFiveClientsData = MutableLiveData<List<Pair<String, Int>>>()
//        val reservaRef = db.collection("reservas")
//
//        reservaRef
//            .groupBy("cliente") // Agrupar por el campo "cliente"
//            .orderByDescending("count") // Ordenar por el conteo de reservas en orden descendente
//            .limit(5) // Obtener solo los 5 primeros resultados
//            .get()
//            .addOnSuccessListener { result ->
//                val topFiveClients = result.documents.mapNotNull { document ->
//                    val cliente = document.getString("cliente")
//                    val numReservas = document.getLong("count")?.toInt()
//                    if (cliente != null && numReservas != null) {
//                        cliente to numReservas
//                    } else {
//                        null
//                    }
//                }
//                topFiveClientsData.value = topFiveClients
//            }
//            .addOnFailureListener { exception ->
//                // Manejar la excepción en caso de error
//            }
//
//        return topFiveClientsData
//    }







}