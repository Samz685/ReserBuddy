package com.example.reserbuddy

import kotlin.random.Random

class AvatarGenerator {



    fun asignarIcono(nombre: String): Int {
        var soloNombre = obtenerPrimeraPalabra(nombre)

        return when {
            hombres.contains(soloNombre) -> randomAvatar(avatarHombres)
            mujeres.contains(soloNombre) -> randomAvatar(avatarMujeres)
            else -> R.drawable.ic_bin
        }
    }

    fun obtenerPrimeraPalabra(cadena: String): String {
        val palabras = cadena.trim().split(" ")
        return palabras.firstOrNull() ?: ""
    }

    fun randomAvatar(listaAvatar: MutableList<Int>): Int{
        var avatar = 0

        if (listaAvatar.isNotEmpty()){

            var indiceAleatorio = Random.nextInt(listaAvatar.size)
            avatar = listaAvatar.get(indiceAleatorio)
        }

        return avatar
    }







    companion object{

        val avatarHombres = mutableListOf(
            R.drawable.ic_man, R.drawable.ic_man2, R.drawable.ic_man3, R.drawable.ic_man4, R.drawable.ic_man5, R.drawable.ic_man6
        )

        val avatarMujeres = mutableListOf(
            R.drawable.ic_woman, R.drawable.ic_woman2, R.drawable.ic_woman3, R.drawable.ic_woman4, R.drawable.ic_woman5, R.drawable.ic_woman6
        )

        val hombres = mutableListOf(
            "adrian",
            "adri",
            "alberto",
            "albertito",
            "alejandro",
            "ale",
            "alex",
            "alfonso",
            "alfonsito",
            "andres",
            "andrecito",
            "angel",
            "angelito",
            "antonio",
            "toni",
            "carlos",
            "carlitos",
            "cesar",
            "cesarito",
            "daniel",
            "dan",
            "dani",
            "david",
            "dav",
            "diego",
            "eduardo",
            "edu",
            "emilio",
            "emilito",
            "enrique",
            "quique",
            "fernando",
            "fer",
            "francisco",
            "fran",
            "paco",
            "gabriel",
            "gabi",
            "gerardo",
            "gerry",
            "gonzalo",
            "gonza",
            "ignacio",
            "nacho",
            "javier",
            "javi",
            "jesus",
            "chus",
            "jorge",
            "jorgito",
            "jose",
            "pepe",
            "juan",
            "juanito",
            "lucas",
            "luki",
            "luis",
            "manuel",
            "manu",
            "marcos",
            "mari",
            "mario",
            "mariito",
            "martin",
            "tin",
            "miguel",
            "mi",
            "oscar",
            "oski",
            "pablo",
            "pablito",
            "pedro",
            "pedrito",
            "rafael",
            "rafa",
            "ricardo",
            "rici",
            "roberto",
            "rober",
            "ruben",
            "sergio",
            "sergi",
            "victor",
            "viti",
            "julian",
            "juli",
            "hector",
            "hecto"
        )


        val mujeres = mutableListOf(
            "adriana",
            "adri",
            "alba",
            "alejandra",
            "ale",
            "alex",
            "alicia",
            "ana",
            "anita",
            "andrea",
            "andy",
            "angela",
            "angelita",
            "beatriz",
            "bea",
            "carla",
            "carlita",
            "carmen",
            "carmencita",
            "clara",
            "clarita",
            "diana",
            "di",
            "elena",
            "elenita",
            "emilia",
            "emi",
            "esther",
            "estrellita",
            "eva",
            "evita",
            "florencia",
            "flor",
            "francisca",
            "fran",
            "gabriela",
            "gabi",
            "gisela",
            "gise",
            "irene",
            "irenita",
            "isabel",
            "isa",
            "juana",
            "juanita",
            "laura",
            "laurita",
            "lucia",
            "luci",
            "luisa",
            "marta",
            "maria",
            "marita",
            "mariana",
            "marina",
            "marta",
            "mireia",
            "mire",
            "monica",
            "nuria",
            "nuri",
            "olivia",
            "olivi",
            "patricia",
            "pati",
            "raquel",
            "raque",
            "rocio",
            "rosa",
            "rosita",
            "sandra",
            "sandri",
            "silvia",
            "silvi",
            "valeria",
            "vale",
            "julia",
            "juli",
            "luciana",
            "luci"
        )



    }
}