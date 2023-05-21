package com.example.reserbuddy

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

object FechaGenerator{

    data class Fecha(val Asignada: String, val AsignadaCard: String)

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


    data class Mes(val numMes: String, val diasMes: String)

    // Función auxiliar para obtener el número de mes a partir del nombre del mes
    fun getMonthNumber(mes: String): Int {
        val dateFormat = SimpleDateFormat("MMMM", Locale.getDefault())
        val date = dateFormat.parse(mes)
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar.get(Calendar.MONTH) + 1
    }

    // Función auxiliar para obtener la cantidad de días en un mes específico
    fun getDaysInMonth(year: Int, month: Int): Int {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month - 1)
        var diasMes = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        return diasMes
    }

}