package com.example.reservarapp.models

class Grupo {

    var id: String = ""
    var alias: String = ""
    var foto: Int = 0
    var owner: String? = ""
    var admin: String? = ""
    var listaMiembros: MutableList<String> = mutableListOf()
    var bloqueados: MutableList<String> = mutableListOf()
    override fun toString(): String {
        return alias
    }


}
