package com.example.reservarapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.reservarapp.models.Tarea
import com.example.reservarapp.repositories.TareaRepo
import java.util.*


class TareaViewModel : ViewModel() {

    private val tareaRepo = TareaRepo()

    fun addTarea(tarea: Tarea): String {
        return tareaRepo.addTarea(tarea)

    }

    fun updateTarea(tarea: Tarea) {
        tareaRepo.updateTarea(tarea)

    }

    fun updateAsignacion(tarea: Tarea) {
        tareaRepo.updateAsignacion(tarea)

    }

    fun quitarAsignacion(tarea: Tarea){
        tareaRepo.quitarAsignacion(tarea)
    }
    fun updateEstado(tarea: Tarea) {
        tareaRepo.updateEstado(tarea)

    }

    fun deleteTarea(tarea: Tarea) : String{
        return tareaRepo.deleteTarea(tarea)

    }


    fun getAll(): LiveData<LinkedList<Tarea>> {
        val tareasData = MutableLiveData<LinkedList<Tarea>>()
        tareaRepo.getAll().observeForever {
            tareasData.value = it
        }
        return tareasData
    }

    fun getByUsuario(idUsuario : String): LiveData<LinkedList<Tarea>> {
        val tareasData = MutableLiveData<LinkedList<Tarea>>()
        tareaRepo.getByUsuario(idUsuario).observeForever {
            tareasData.value = it
        }

        return tareasData
    }

    fun getByEstado(estado : String): LiveData<LinkedList<Tarea>> {
        val tareasData = MutableLiveData<LinkedList<Tarea>>()
        tareaRepo.getByEstado(estado).observeForever {
            tareasData.value = it
        }

        return tareasData
    }





//



//    fun getByGroup(grupo : Grupo): LiveData<LinkedList<Tarea>> {
//        val TareasData = MutableLiveData<LinkedList<Tarea>>()
//        TareaRepo.getByGroup(grupo).observeForever {
//            TareasData.value = it
//        }
//        return TareasData
//    }
}