package com.example.reserbuddy

class AvatarGenerator {



    fun asignarIcono(nombre: String): Int {
        var soloNombre = obtenerPrimeraPalabra(nombre)

        return when {
            hombres.contains(soloNombre) -> R.drawable.ic_man
            mujeres.contains(soloNombre) -> R.drawable.ic_woman
            else -> R.drawable.ic_bin
        }
    }

    fun obtenerPrimeraPalabra(cadena: String): String {
        val palabras = cadena.trim().split(" ")
        return palabras.firstOrNull() ?: ""
    }





    companion object{

        val hombres = mutableListOf(
            "adrian",
            "alberto",
            "alejandro",
            "alfonso",
            "andres",
            "angel",
            "antonio",
            "carlos",
            "cesar",
            "daniel",
            "david",
            "diego",
            "eduardo",
            "emilio",
            "enrique",
            "fernando",
            "francisco",
            "fran",
            "paco",
            "gabriel",
            "gerardo",
            "gonzalo",
            "ignacio",
            "javier",
            "jesus",
            "jorge",
            "jose",
            "juan",
            "lucas",
            "luis",
            "manuel",
            "marcos",
            "mario",
            "martin",
            "miguel",
            "oscar",
            "pablo",
            "pedro",
            "rafael",
            "rafa",
            "ricardo",
            "roberto",
            "ruben",
            "sergio",
            "victor"
        )

        val mujeres = mutableListOf(
            "adriana",
            "alba",
            "alejandra",
            "alicia",
            "ana",
            "andrea",
            "angela",
            "beatriz",
            "carla",
            "carmen",
            "clara",
            "diana",
            "elena",
            "emilia",
            "esther",
            "eva",
            "florencia",
            "francisca",
            "gabriela",
            "gisela",
            "irene",
            "isabel",
            "juana",
            "laura",
            "lucia",
            "luisa",
            "marta",
            "maria",
            "mariana",
            "marina",
            "marta",
            "mireia",
            "monica",
            "nuria",
            "olivia",
            "patricia",
            "patri",
            "raquel",
            "rocio",
            "rosa",
            "sandra",
            "silvia",
            "valeria"
        )
    }
}