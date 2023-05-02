package com.example.reservarapp.models


class Usuario {

    var id : String = ""
    var alias : String = ""
    var email : String = ""
    var telefono : String = ""
    var password : String = ""
    var rol : String = ""
    var foto: Int = 0
    var comentario : String = ""

    override fun toString(): String {
        return "Usuario(id=$id, alias='$alias', email='$email')"
    }


}