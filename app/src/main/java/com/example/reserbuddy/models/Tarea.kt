package com.example.reservarapp.models

import java.util.Date

class Tarea {

    var id : String = ""
    var alias : String = ""
    var owner : String? = ""
    var foto : Int = 0
    var asignedTo : String? = ""
    var asignedDate : String? = ""
    var asignedDateCard : String? = ""
    var doneDate : String? = ""
    var doneDateCard : String? = ""
    var comentario : String = ""
    var estado : String = ""


    override fun toString(): String {
        return "Tarea(id='$id', alias='$alias', createdBy=$owner, asignedTo=$asignedTo, doneDate=$doneDate, comentario='$comentario')"
    }


}