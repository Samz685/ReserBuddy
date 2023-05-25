package com.example.reserbuddy

import com.example.reservarapp.models.Categoria

class CategoriasGenerator {

    companion object {

        fun crearCategorias() : MutableList<Categoria>{

            var categorias = mutableListOf<Categoria>(
                Categoria("Huevos",R.drawable.ic_k_huevos),
                Categoria("Verdura",R.drawable.ic_k_verdura),
                Categoria("Fruta",R.drawable.ic_k_fruta),
                Categoria("Lácteos",R.drawable.ic_k_lacteos),
                Categoria("Leche",R.drawable.ic_k_leche),
                Categoria("Pescado",R.drawable.ic_k_pescados),
                Categoria("Carnes",R.drawable.ic_k_carnes),
                Categoria("Pollo",R.drawable.ic_k_pollo),
                Categoria("Mantequilla",R.drawable.ic_k_mantequilla),
                Categoria("Pan",R.drawable.ic_k_pan),
                Categoria("Quesos",R.drawable.ic_k_queso),
                Categoria("Refrescos",R.drawable.ic_k_refrescos),
                Categoria("Alcohol",R.drawable.ic_k_alcohol),
                Categoria("Cerveza",R.drawable.ic_k_cervezas),
                Categoria("Varios",R.drawable.ic_k_varios),
                Categoria("Limpieza",R.drawable.ic_k_limpieza),

                )
            return categorias
        }
    }
}