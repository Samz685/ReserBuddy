package com.example.reservarapp.models

import com.google.firebase.Timestamp
import java.util.Date


class Reserva {

    var id: String = ""
    var grupo: String? = ""
    var cliente: String? = ""
    var telefono: String? = ""
    var fecha: String = ""
    var hora: String = ""
    var numComensales: Int = 0
    var ubicacion: String? = ""
    var estado: String? = ""
    var foto: Int = 0
    var comentario: String? = ""


    override fun toString(): String {
        return "Reserva(id='$id', grupo=$grupo, telefono=$telefono, cliente=$cliente, fecha=$fecha, numComensales=$numComensales, disposicion=$ubicacion, comentario=$comentario"
    }


}