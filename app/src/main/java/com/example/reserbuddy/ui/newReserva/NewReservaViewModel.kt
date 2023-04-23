package com.example.reserbuddy.ui.newReserva

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalTime
import java.util.*

class NewReservaViewModel: ViewModel()
{
    var cliente = MutableLiveData<String>()
    var telefono = MutableLiveData<String>()
    var nComensales = MutableLiveData<Int>()
    var fecha = MutableLiveData<Date>()
    var hora = MutableLiveData<LocalTime>()
    var ubicacion = MutableLiveData<String>()
    var comentario = MutableLiveData<String>()

}