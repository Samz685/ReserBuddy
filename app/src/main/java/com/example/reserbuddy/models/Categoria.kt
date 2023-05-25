package com.example.reservarapp.models

class Categoria {
    var nombre: String = ""
    var foto: Int = 0
    var isSelected = false

    constructor() // Constructor sin parámetros

    constructor(nombre: String, foto: Int) { // Constructor con parámetros
        this.nombre = nombre
        this.foto = foto
    }

    fun seleccionar() {
        isSelected = true
    }

    fun deseleccionar() {
        isSelected = false
    }
}