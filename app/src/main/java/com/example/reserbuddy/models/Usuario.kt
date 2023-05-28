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
    var token : String = ""

    override fun toString(): String {
        return alias
    }


}