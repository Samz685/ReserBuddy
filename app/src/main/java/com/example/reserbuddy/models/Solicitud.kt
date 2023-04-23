package com.example.reservarapp.models

import java.util.Date

class Solicitud {

    var id : String = ""
    var fromUser : String = ""
    var toGroup : String = ""
    var foto : Int = 0
    var date : Date = Date()
    var estado : String = ""

    override fun toString(): String {
        return "Solicitud(fromUser='$fromUser', toGroup='$toGroup', date=$date, estado='$estado')"
    }


}
