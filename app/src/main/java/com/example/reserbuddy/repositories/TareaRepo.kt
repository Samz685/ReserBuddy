package com.example.reservarapp.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.reservarapp.models.Tarea
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.util.*

class TareaRepo {

    val db = Firebase.firestore

    fun addTarea(tarea: Tarea): String {


        val tareaRef = db.collection("tareas").document()
        tarea.id = tareaRef.id

        val datos = hashMapOf(
            "id" to tarea.id,
            "alias" to tarea.alias,
            "owner" to tarea.owner,
            "foto" to tarea.foto,
            "asignedTo" to tarea.asignedTo,
            "asignedDate" to tarea.asignedDate,
            "doneDate" to tarea.doneDate,
            "asignedDateCard" to tarea.asignedDateCard,
            "doneDateCard" to tarea.doneDateCard,
            "comentario" to tarea.comentario,
            "estado" to tarea.estado

        )
        tareaRef.set(datos).addOnSuccessListener {
            Log.i("Firebase", "Tarea creada correctamente")
        }.addOnFailureListener { error ->
            Log.e("FirebaseError", error.message.toString())
        }
        return tarea.id
    }

    fun updateTarea(tarea: Tarea) {

        var tareaRef = db.collection("tareas").document(tarea.id)


        val datos = hashMapOf(
            "alias" to tarea.alias,
            "asignedTo" to tarea.asignedTo,
            "comentario" to tarea.comentario,
        )
        tareaRef.update(datos as Map<String, Any>).addOnSuccessListener {
            Log.i("FireBaase", "Tarea Actualizada")
        }.addOnFailureListener { error ->
            Log.e("FirebaseError", error.message.toString())
        }
    }

    fun updateAsignacion(tarea: Tarea) {

        var tareaRef = db.collection("tareas").document(tarea.id)


        val datos = hashMapOf(
            "asignedTo" to tarea.alias,
            "asignedDate" to tarea.asignedDate,
            "asignedDateCard" to tarea.asignedDateCard,
            "foto" to tarea.foto

        )
        tareaRef.update(datos as Map<String, Any>).addOnSuccessListener {
            Log.i("FireBaase", "Tarea asignacion Actualizada")
        }.addOnFailureListener { error ->
            Log.e("FirebaseError", error.message.toString())
        }
    }

    fun updateEstado(tarea: Tarea) {

        var tareaRef = db.collection("tareas").document(tarea.id)


        val datos = hashMapOf(
            "estado" to tarea.estado,
            "doneDate" to tarea.alias,
            "doneDateCard" to tarea.doneDateCard

        )
        tareaRef.update(datos as Map<String, Any>).addOnSuccessListener {
            Log.i("FireBase", "Tarea estado Actualizado")
        }.addOnFailureListener { error ->
            Log.e("FirebaseError", error.message.toString())
        }
    }

    fun deleteTarea(tarea: Tarea): String {

        val tareaRef = db.collection("tareas").document(tarea.id)

        tarea.id = tareaRef.id

        tareaRef.delete().addOnSuccessListener {
            Log.i("Firebase", "Tarea borrada correctamente")
        }.addOnFailureListener { error ->
            Log.e("FirebaseError", error.message.toString())
        }

        return tarea.id
    }

    fun getAll(): LiveData<LinkedList<Tarea>> {
        var tareasData = MutableLiveData<LinkedList<Tarea>>()
        val tareaRef = db.collection("perfil_usuarios")
        tareaRef.get().addOnSuccessListener { result ->
            var listaTareas = LinkedList<Tarea>()
            for (document in result) {
                var tarea = document.toObject<Tarea>()!!
                listaTareas.addLast(tarea)
            }
            tareasData.value = listaTareas
        }
        return tareasData
    }

    fun getByUsuario(idUsuario: String): LiveData<LinkedList<Tarea>> {
        var tareasData = MutableLiveData<LinkedList<Tarea>>()
        val tareaRef = db.collection("tareas")
        val query = tareaRef.whereEqualTo("asignedTo", idUsuario)
        query.get().addOnSuccessListener { result ->
            var listaTareas = LinkedList<Tarea>()
            for (document in result) {
                var tarea = document.toObject<Tarea>()!!
                listaTareas.addLast(tarea)
            }
            tareasData.value = listaTareas
        }
        return tareasData
    }
}