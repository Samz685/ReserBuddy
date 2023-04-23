package com.example.reservarapp.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.reservarapp.models.Grupo
import com.example.reservarapp.models.Solicitud
import com.example.reservarapp.models.Usuario
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.util.LinkedList

class SolicitudRepo {

    private val db = Firebase.firestore

    fun addSolicitud(solicitud: Solicitud) : String{

        val db = Firebase.firestore
        val solicitudRef = db.collection("solicitudes").document()

        solicitud.id = solicitudRef.id

        val datos = hashMapOf(
            "id" to solicitud.id,
            "fromUser" to solicitud.fromUser,
            "toGroup" to solicitud.toGroup,
            "estado" to solicitud.estado,
            "foto" to solicitud.foto
        )
        solicitudRef.set(datos).addOnSuccessListener {
            Log.i("Firebase", "Datos insertados correctamente")
        }.addOnFailureListener { error ->
            Log.e("FirebaseError", error.message.toString())
        }

        return solicitud.id
    }

    fun updateSolicitud(solicitud: Solicitud) {

        var solicitudRef = db.collection("solicitudes").document(solicitud.id)


        val datos = hashMapOf(

            "estado" to solicitud.estado
        )
        solicitudRef.update(datos as Map<String, Any>).addOnSuccessListener {
            Log.i("FireBase", "Usuario Actualizado")
        }.addOnFailureListener { error ->
            Log.e("FirebaseError", error.message.toString())
        }
    }
    fun getById(id : String) : LiveData<Usuario>{
        var usuarioData = MutableLiveData<Usuario>()
        val usuarioRef = db.collection("usuarios").document(id)
        usuarioRef.get().addOnSuccessListener { result->
            var usuario = result.toObject<Usuario>()!!
            usuarioData.value = usuario
        }

        return usuarioData

    }

    fun getAll(): LiveData<LinkedList<Solicitud>> {
        var solicitudesData = MutableLiveData<LinkedList<Solicitud>>()
        val solicitudRef = db.collection("solicitudes")

        solicitudRef.get().addOnSuccessListener { result ->
            var listaSolitudes = LinkedList<Solicitud>()
            for (document in result) {
                var solicitud = document.toObject<Solicitud>()!!
                listaSolitudes.addLast(solicitud)
            }
            solicitudesData.value = listaSolitudes
        }
        return solicitudesData
    }

    fun getByGroup(grupo : Grupo): LiveData<LinkedList<Solicitud>> {
        var solicitudesData = MutableLiveData<LinkedList<Solicitud>>()
        val solicitudRef = db.collection("solicitudes")

        val query = solicitudRef.whereEqualTo("toGroup", grupo.alias)
        query.get().addOnSuccessListener { result ->
            var listaSolicitudes = LinkedList<Solicitud>()
            for (document in result) {
                var solicitud = document.toObject<Solicitud>()!!
                listaSolicitudes.addLast(solicitud)
            }
            solicitudesData.value = listaSolicitudes
        }
        return solicitudesData
    }




//    fun findMyGrupos(usuario: Usuario) {
//        val db = Firebase.firestore
//
//        val grupoRef = db.collection("grupos")
//        val query = grupoRef.whereIn("id", usuario.listaGrupos)
//
//        query.get().addOnSuccessListener { result ->
//            var cont = 1
//            for (document in result) {
//                Log.i("Grupo $cont", "Grupo: ${document.data["alias"]} || ${usuario.alias}")
//                cont++
//            }
//        }.addOnFailureListener { error ->
//            Log.e("FirebaseError", error.message.toString())
//        }
//    }
//
//    fun findMyTareas(usuario: Usuario) {
//        val db = Firebase.firestore
//
//        val grupoRef = db.collection("tareas")
//        val query = grupoRef.whereEqualTo("asignedTo", usuario.id)
//
//        query.get().addOnSuccessListener { result ->
//            var cont = 1
//            for (document in result) {
//                Log.i("Tarea $cont", "Tarea: ${document.data["alias"]} || ${usuario.alias}")
//                cont++
//            }
//        }.addOnFailureListener { error ->
//            Log.e("FirebaseError", error.message.toString())
//        }
//    }
//
//    fun updateUsuarioGrupo(usuario: Usuario){
//
//        val db = Firebase.firestore
//        val usuarioRef = db.collection("usuarios").document(usuario.id)
//
//        usuarioRef.update("listaGrupos", usuario.listaGrupos).addOnSuccessListener {
//            Log.i("Firebase", "Datos actualizados correctamente")
//        }
//    }





}