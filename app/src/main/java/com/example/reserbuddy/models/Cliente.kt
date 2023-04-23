package com.example.reservarapp.models

class Cliente {

    var id : String = ""
    var alias : String = ""
    var telefono : String = ""
    var email : String = ""
    override fun toString(): String {
        return "Cliente(id=$id, nombre='$alias', telefono='$telefono', email='$email')"
    }


}