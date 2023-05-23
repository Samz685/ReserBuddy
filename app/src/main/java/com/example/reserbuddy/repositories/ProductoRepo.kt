package com.example.reservarapp.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.reservarapp.models.Producto
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.util.*

class ProductoRepo {

    val db = Firebase.firestore

    fun addProducto(producto: Producto): String {


        val tareaRef = db.collection("productos").document()
        producto.id = tareaRef.id

        val datos = hashMapOf(
            "id" to producto.id,
            "nombre" to producto.nombre,
            "categoria" to producto.categoria,
            "estado" to producto.estado,
            "foto" to producto.foto

        )
        tareaRef.set(datos).addOnSuccessListener {
            Log.i("Firebase", "Producto creado correctamente")
        }.addOnFailureListener { error ->
            Log.e("FirebaseError", error.message.toString())
        }
        return producto.id
    }

    fun updateProducto(producto: Producto) {

        var productoRef = db.collection("productos").document(producto.id)


        val datos = hashMapOf(
            "nombre" to producto.nombre,
            "categoria" to producto.categoria,
            "foto" to producto.foto
        )
        productoRef.update(datos as Map<String, Any>).addOnSuccessListener {
            Log.i("FireBase", "Producto Actualizado")
        }.addOnFailureListener { error ->
            Log.e("FirebaseError", error.message.toString())
        }
    }

    fun updateEstado(producto: Producto) {

        var tareaRef = db.collection("productos").document(producto.id)

        val datos = hashMapOf(
            "estado" to producto.estado,

            )
        tareaRef.update(datos as Map<String, Any>).addOnSuccessListener {
            Log.i("FireBase", "Producto Actualizado")
        }.addOnFailureListener { error ->
            Log.e("FirebaseError", error.message.toString())
        }
    }


    fun deleteProducto(producto: Producto): String {

        val productoRef = db.collection("productos").document(producto.id)

        producto.id = productoRef.id

        productoRef.delete().addOnSuccessListener {
            Log.i("Firebase", "Producto borrado correctamente")
        }.addOnFailureListener { error ->
            Log.e("FirebaseError", error.message.toString())
        }

        return producto.id
    }

    fun getAll(): LiveData<LinkedList<Producto>> {
        var productoData = MutableLiveData<LinkedList<Producto>>()
        val productoRef = db.collection("productos")

        productoRef.get().addOnSuccessListener { result ->
            var listaTareas = LinkedList<Producto>()
            for (document in result) {
                var producto = document.toObject<Producto>()!!
                listaTareas.addLast(producto)
            }
            productoData.value = listaTareas
        }
        return productoData
    }


    fun getByEstado(estado: String): LiveData<LinkedList<Producto>> {

        var productosData = MutableLiveData<LinkedList<Producto>>()
        val productoRef = db.collection("productos")
        val query = productoRef.whereEqualTo("estado", estado)

        query.get().addOnSuccessListener { result ->
            var listaTareas = LinkedList<Producto>()
            for (document in result) {
                var producto = document.toObject<Producto>()!!
                listaTareas.addLast(producto)
            }
            productosData.value = listaTareas
        }
        return productosData
    }


}