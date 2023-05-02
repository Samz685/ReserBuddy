//package com.example.reservarapp.viewmodels
//
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import com.example.reservarapp.models.Grupo
//import com.example.reservarapp.models.Usuario
//import com.example.reservarapp.repositories.GrupoRepo
//import java.util.LinkedList
//
//class GrupoViewModel : ViewModel() {
//
//
//    private var grupoRepo = GrupoRepo()
//
//
//    fun getAll() : MutableLiveData<LinkedList<Grupo>>{
//
//        var dataGrupo = MutableLiveData<LinkedList<Grupo>>()
//        grupoRepo.getAll().observeForever {
//            dataGrupo.value = it
//        }
//
//        return dataGrupo
//    }
//
//    fun getAllmine(usuario: Usuario) : MutableLiveData<LinkedList<Grupo>>{
//
//        var dataGrupo = MutableLiveData<LinkedList<Grupo>>()
//
//        grupoRepo.getAllmine(usuario).observeForever {
//            dataGrupo.value = it
//        }
//        return dataGrupo
//    }
//
//    fun addGrupo(grupo: Grupo) : String{
//        var grupoID = grupoRepo.addGrupo(grupo)
//
//        return grupoID
//    }
//
//    fun updateGrupo(grupo: Grupo){
//
//        grupoRepo.updateGrupo(grupo)
//    }
//
//    fun findById(id: String) : MutableLiveData<Grupo>{
//        var grupoData = MutableLiveData<Grupo>()
//
//        grupoRepo.findById(id).observeForever {
//            grupoData.value = it
//        }
//
//        return grupoData
//    }
//}