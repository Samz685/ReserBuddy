//package com.example.reservarapp.viewmodels
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import com.example.reservarapp.models.Grupo
//import com.example.reservarapp.models.Solicitud
//import com.example.reservarapp.models.Usuario
//import com.example.reservarapp.repositories.SolicitudRepo
//import com.example.reservarapp.repositories.UsuarioRepo
//import java.util.*
//
//
//class SolicitudViewModel : ViewModel() {
//
//    private val solicitudRepo = SolicitudRepo()
//
////    fun getById(userId: String): LiveData<Usuario> {
////        val usuarioData = MutableLiveData<Usuario>()
////        usuarioRepo.getById(userId).observeForever {
////            usuarioData.value = it
////        }
////
////        return usuarioData
////    }
//
//    fun getAll(): LiveData<LinkedList<Solicitud>> {
//        val solicitudesData = MutableLiveData<LinkedList<Solicitud>>()
//        solicitudRepo.getAll().observeForever {
//            solicitudesData.value = it
//        }
//        return solicitudesData
//    }
//
//    fun getByGroup(grupo : Grupo): LiveData<LinkedList<Solicitud>> {
//        val solicitudesData = MutableLiveData<LinkedList<Solicitud>>()
//        solicitudRepo.getByGroup(grupo).observeForever {
//            solicitudesData.value = it
//        }
//        return solicitudesData
//    }
//
//    fun addSolicitud(solicitud: Solicitud) : String{
//        return solicitudRepo.addSolicitud(solicitud)
//
//    }
//
//    fun updateSolicitud(solicitud: Solicitud){
//        solicitudRepo.updateSolicitud(solicitud)
//    }
//}