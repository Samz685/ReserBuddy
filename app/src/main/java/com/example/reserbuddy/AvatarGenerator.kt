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
            R.drawable.ic_man, R.drawable.ic_man2, R.drawable.ic_man3, R.drawable.ic_man4, R.drawable.ic_man5, R.drawable.ic_man6,
            R.drawable.ic_man7, R.drawable.ic_man8, R.drawable.ic_man9, R.drawable.ic_man10
        )

        val avatarMujeres = mutableListOf(
            R.drawable.ic_woman, R.drawable.ic_woman2, R.drawable.ic_woman3, R.drawable.ic_woman4, R.drawable.ic_woman5, R.drawable.ic_woman6,
            R.drawable.ic_woman7, R.drawable.ic_woman8, R.drawable.ic_woman9, R.drawable.ic_woman10
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
            "alvaro",
            "angel",
            "angelito",
            "armando",
            "arturo",
            "bruno",
            "carlos",
            "carlitos",
            "cesar",
            "cesarito",
            "cristian",
            "daniel",
            "dani",
            "dario",
            "david",
            "dav",
            "diego",
            "edgar",
            "eduardo",
            "edu",
            "elias",
            "emilio",
            "emilito",
            "emmanuel",
            "enrique",
            "esteban",
            "felipe",
            "fernando",
            "fer",
            "francisco",
            "fran",
            "gabriel",
            "gabi",
            "gerardo",
            "gerry",
            "gilberto",
            "gonzalo",
            "gonza",
            "gustavo",
            "hector",
            "hecto",
            "hugo",
            "ismael",
            "ivan",
            "javier",
            "javi",
            "jesus",
            "chus",
            "jorge",
            "jorgito",
            "jose",
            "josue",
            "juan",
            "juanito",
            "julian",
            "juli",
            "leonardo",
            "lucas",
            "luki",
            "luis",
            "manuel",
            "manu",
            "marco",
            "marcos",
            "mari",
            "mario",
            "mariito",
            "martin",
            "tin",
            "mauricio",
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
            "ramon",
            "ricardo",
            "rici",
            "roberto",
            "rober",
            "rodrigo",
            "ruben",
            "santiago",
            "sebastian",
            "sergio",
            "sergi",
            "victor",
            "viti",
            "walter"
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
            "julia",
            "juli",
            "laura",
            "laurita",
            "lucia",
            "luci",
            "luciana",
            "marta",
            "maria",
            "marita",
            "mariana",
            "marina",
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
            "adela",
            "aila",
            "amalia",
            "amanda",
            "ana",
            "anabel",
            "andrea",
            "angelica",
            "annette",
            "araceli",
            "beatriz",
            "belen",
            "belinda",
            "bernarda",
            "camila",
            "carina",
            "carla",
            "cecilia",
            "clara",
            "clarissa",
            "constanza"
        )




    }
}