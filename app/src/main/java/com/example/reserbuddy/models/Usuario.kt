package com.example.reservarapp.models


class Usuario {

    var id : String = ""
    var alias : String = ""
    var email : String = ""
    var password : String = ""
    var foto: Int = 0
    var listaGrupos : MutableList<String> = mutableListOf()
    var grupoActual : String? = ""

    override fun toString(): String {
        return "Usuario(id=$id, alias='$alias', email='$email', listaGrupos=$listaGrupos, grupoActual=$grupoActual)"
    }


}