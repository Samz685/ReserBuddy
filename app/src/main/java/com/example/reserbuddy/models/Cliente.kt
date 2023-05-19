package com.example.reservarapp.models

class Cliente {


    var id : String = ""
    var nombre : String = ""
    var telefono : String = ""
    var email : String = ""
    var foto : Int = 0
    var descripcion: String = ""
    override fun toString(): String {
        return "Cliente(id=$id, nombre='$nombre', telefono='$telefono', email='$email')"
    }


}