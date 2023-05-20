package com.example.reserbuddy

import android.provider.ContactsContract
import java.io.File
import com.google.gson.Gson


data class Nombres(val hombres: List<String>, val mujeres: List<String>)

object BuscadorNombres {



    fun leerJson(){

        val jsonFilePath = "src/main/java/com/example/reserbuddy/data/nombres.json"
        val jsonFile = File(jsonFilePath)

        val gson = Gson()
        val jsonData = gson.fromJson(jsonFile.readText(), Nombres::class.java)

        // Recorrer lista1
        println("Lista1:")
        for (elemento in jsonData.hombres) {
            println(elemento)
        }

        // Recorrer lista1
        println("Lista2:")
        for (elemento in jsonData.mujeres) {
            println(elemento)
        }
    }
}