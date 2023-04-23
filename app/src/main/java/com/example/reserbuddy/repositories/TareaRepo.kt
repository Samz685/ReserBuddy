package com.example.reservarapp.repositories

import android.util.Log
import com.example.reservarapp.models.Tarea
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class TareaRepo {

    fun addTarea(tarea: Tarea): String {

        val db = Firebase.firestore
        val tareaRef = db.collection("tareas").document()
        tarea.id = tareaRef.id

        val datos = hashMapOf(
            "id" to tarea.id,
            "alias" to tarea.alias,
            "owner" to tarea.owner,
            "asignedTo" to tarea.asignedTo,
            "createdDate" to tarea.createdDate,
            "doneDate" to tarea.doneDate,
            "grupo" to tarea.grupo,
            "comentario" to tarea.comentario
        )
        tareaRef.set(datos).addOnSuccessListener {
            Log.i("Firebase", "Datos insertados correctamente")
        }.addOnFailureListener { error ->
            Log.e("FirebaseError", error.message.toString())
        }
        return tarea.id
    }
}