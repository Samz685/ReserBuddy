package com.example.reserbuddy

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object FechaGenerator{

    @RequiresApi(Build.VERSION_CODES.O)
    fun elegirFecha() : Fecha {
        val currentDate = LocalDate.now()

        // Formatear la fecha utilizando el patrón AAAA-mm-dd
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        var Asignada = currentDate.format(formatter)
        // Formatear la fecha utilizando el patrón AAAA-mm-dd
        val formatter2 = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        var AsignadaCard = currentDate.format(formatter2)

        return Fecha(Asignada, AsignadaCard)


    }

    data class Fecha(val Asignada: String, val AsignadaCard: String)

}