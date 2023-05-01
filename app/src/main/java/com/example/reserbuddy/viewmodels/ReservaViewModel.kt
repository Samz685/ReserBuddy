package com.example.reservarapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.reservarapp.models.Grupo
import com.example.reservarapp.models.Reserva
import com.example.reservarapp.models.Solicitud
import com.example.reservarapp.repositories.ReservaRepo

import java.util.*


class ReservaViewModel : ViewModel() {

    private val reservaRepo = ReservaRepo()


    fun getByGroup(grupo : Grupo): LiveData<LinkedList<Reserva>> {
        val reservasData = MutableLiveData<LinkedList<Reserva>>()
        reservaRepo.getByGroup(grupo).observeForever {
            reservasData.value = it
        }
        return reservasData
    }

    fun getToday(): LiveData<LinkedList<Reserva>> {
        val reservasData = MutableLiveData<LinkedList<Reserva>>()
        reservaRepo.getToday().observeForever {
            reservasData.value = it
        }
        return reservasData
    }

    fun getWeek(): LiveData<LinkedList<Reserva>> {
        val reservasData = MutableLiveData<LinkedList<Reserva>>()
        reservaRepo.getWeek().observeForever {
            reservasData.value = it
        }
        return reservasData
    }

    fun getMonth(): LiveData<LinkedList<Reserva>> {
        val reservasData = MutableLiveData<LinkedList<Reserva>>()
        reservaRepo.getMonth().observeForever {
            reservasData.value = it
        }
        return reservasData
    }

    fun getPeriod(fecha1 : String, fecha2 : String): LiveData<LinkedList<Reserva>> {
        val reservasData = MutableLiveData<LinkedList<Reserva>>()
        reservaRepo.getPeriod(fecha1, fecha2).observeForever {
            reservasData.value = it
        }
        return reservasData
    }

    fun addReserva(reserva: Reserva) : String{
        return reservaRepo.addReserva(reserva)

    }

    fun deleteReserva(reserva: Reserva) : String{
        return reservaRepo.deleteReserva(reserva)

    }

    fun updateReserva(reserva: Reserva){
        reservaRepo.updateReserva(reserva)
    }



//    fun getById(userId: String): LiveData<Usuario> {
//        val usuarioData = MutableLiveData<Usuario>()
//        usuarioRepo.getById(userId).observeForever {
//            usuarioData.value = it
//        }
//
//        return usuarioData
//    }

//    fun getAll(): LiveData<LinkedList<Solicitud>> {
//        val solicitudesData = MutableLiveData<LinkedList<Solicitud>>()
//        solicitudRepo.getAll().observeForever {
//            solicitudesData.value = it
//        }
//        return solicitudesData
//    }




//
//    fun updateSolicitud(solicitud: Solicitud){
//        solicitudRepo.updateSolicitud(solicitud)
//    }
}