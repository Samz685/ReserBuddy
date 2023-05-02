//package com.example.reservarapp.repositories
//
//import android.util.Log
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import com.example.reservarapp.models.Grupo
//import com.example.reservarapp.models.Solicitud
//import com.example.reservarapp.models.Usuario
//import com.google.firebase.database.DataSnapshot
//import com.google.firebase.database.GenericTypeIndicator
//import com.google.firebase.database.ValueEventListener
//import com.google.firebase.firestore.ktx.firestore
//import com.google.firebase.firestore.ktx.toObject
//import com.google.firebase.ktx.Firebase
//import java.util.LinkedList
//
//class GrupoRepo {
//
//    val db = Firebase.firestore
//
//
//
//    fun getAll() : LiveData<LinkedList<Grupo>>{
//
//        val grupoRef = db.collection("grupos")
//        val dataGrupos = MutableLiveData<LinkedList<Grupo>>()
//
//        grupoRef.get().addOnSuccessListener { result ->
//            val listaGrupos = LinkedList<Grupo>()
//            for (document in result){
//                var grupo = document.toObject<Grupo>()!!
//                listaGrupos.add(grupo)
//            }
//            dataGrupos.value = listaGrupos
//        }
//        return dataGrupos
//    }
//
//    fun getAllmine(usuario : Usuario) : LiveData<LinkedList<Grupo>>{
//
//        val grupoRef = db.collection("grupos")
//        val dataGrupos = MutableLiveData<LinkedList<Grupo>>()
//
//        val query = grupoRef.whereArrayContains("listaMiembros",usuario.id)
//
//        query.get().addOnSuccessListener { result ->
//            var listaGrupos = LinkedList<Grupo>()
//            for (document in result){
//                var grupo = document.toObject<Grupo>()
//                listaGrupos.add(grupo)
//            }
//            dataGrupos.value = listaGrupos
//        }
//
//        return dataGrupos
//    }
//
//    fun addGrupo(grupo: Grupo) : String {
//
//        val db = Firebase.firestore
//        val grupoRef = db.collection("grupos").document()
//        grupo.id = grupoRef.id
//
//
//        val datos = hashMapOf(
//            "id" to grupo.id,
//            "alias" to grupo.alias,
//            "foto" to grupo.foto,
//            "owner" to grupo.owner,
//            "admin" to grupo.admin,
//            "listaMiembros" to grupo.listaMiembros,
//            "bloqueados" to grupo.bloqueados
//        )
//        grupoRef.set(datos).addOnSuccessListener {
//            Log.i("Firebase", "Datos insertados correctamente")
//        }.addOnFailureListener { error ->
//            Log.e("FirebaseError", error.message.toString())
//        }
//        return grupo.id
//    }
//    fun updateGrupo(grupo: Grupo) {
//
//        var grupoRef = db.collection("grupos").document(grupo.id)
//
//
//        val datos = hashMapOf(
//
//            "listaMiembros" to grupo.listaMiembros
//        )
//        grupoRef.update(datos as Map<String, Any>).addOnSuccessListener {
//            Log.i("FireBase", "Usuario Actualizado")
//        }.addOnFailureListener { error ->
//            Log.e("FirebaseError", error.message.toString())
//        }
//    }
//
//
//    fun findByOwner(usuario: Usuario) {
//
//        val grupoRef = db.collection("grupos")
//        val query = grupoRef.whereEqualTo("owner", usuario.id)
//
//        query.get().addOnSuccessListener { result ->
//            for (document in result) {
//                Log.i("Grupo 1", "Grupo: ${document.data["alias"]} || ${document.data["owner"]}")
//            }
//
//        }.addOnFailureListener { error ->
//            Log.e("FirebaseError", error.message.toString())
//        }
//    }
//
//    fun findIntegrants(grupo: Grupo) {
//        val db = Firebase.firestore
//
//        val usuariosRef = db.collection("usuarios")
//        val query = usuariosRef.whereArrayContains("listaGrupos", grupo.id)
//
//        query.get().addOnSuccessListener { result ->
//            var cont = 1
//            for (document in result) {
//                Log.i("Integrante $cont", "Usuario: ${document.data["alias"]}")
//                cont++
//            }
//        }.addOnFailureListener { error ->
//            Log.e("FirebaseError", error.message.toString())
//        }
//    }
//
//    fun findById(id: String) : MutableLiveData<Grupo>{
//
//        var grupoData = MutableLiveData<Grupo>()
//        val grupoRef = db.collection("grupos")
//        val query = grupoRef.whereEqualTo("owner", id)
//
//        query.get().addOnSuccessListener { result ->
//            var grupoA = Grupo()
//            for (document in result) {
//                var grupo = document.toObject<Grupo>()
//                grupoA = grupo
//            }
//            grupoData.value = grupoA
//
//        }.addOnFailureListener { error ->
//            Log.e("FirebaseError", error.message.toString())
//        }
//        return grupoData
//    }
//
//    fun deleteIntegrant(usuario: Usuario, id_grupo: String) {
//
//        var usuarioRepo = UsuarioRepo()
//        val usuarioRef = db.collection("usuarios")
//        val query = usuarioRef.whereEqualTo("id", usuario.id)
//        val listaGrupos = mutableListOf<String>()
//        val listaBloqueados = mutableListOf<String>()
//        var user = Usuario()
//
//        query.get().addOnSuccessListener { result ->
//            for (document in result) {
//                user = document.toObject<Usuario>()
//                }
//            for (grupo in user.listaGrupos){
//                if(grupo != id_grupo){
//                    listaGrupos.add(grupo)
//                }
//            }
//            user.listaGrupos = listaGrupos
//
//
//
//
//
//
//        }.addOnFailureListener { error ->
//            Log.e("FirebaseError", error.message.toString())
//        }
//
//    }
//
//    fun addUsuarioBloqueado(grupo: Grupo){
//
//        val db = Firebase.firestore
//        val grupoRef = db.collection("grupos").document(grupo.id)
//
//        grupoRef.update("bloqueados", grupo.bloqueados).addOnSuccessListener {
//            Log.i("Firebase", "Datos actualizados correctamente")
//        }
//    }
//
//
//
//
//
//}