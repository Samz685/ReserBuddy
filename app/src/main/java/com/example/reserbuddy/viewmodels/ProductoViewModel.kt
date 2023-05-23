package com.example.reservarapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.reservarapp.models.Producto
import com.example.reservarapp.models.Tarea
import com.example.reservarapp.repositories.ProductoRepo
import com.example.reservarapp.repositories.TareaRepo
import java.util.*


class ProductoViewModel : ViewModel() {

    private val productoRepo = ProductoRepo()

    fun addProducto(producto: Producto): String {
        return productoRepo.addProducto(producto)

    }

    fun updateProducto(producto: Producto) {
        productoRepo.updateProducto(producto)

    }

    fun updateEstado(producto: Producto) {
        productoRepo.updateEstado(producto)

    }

    fun deleteProducto(producto: Producto): String {
        return productoRepo.deleteProducto(producto)

    }


    fun getAll(): LiveData<LinkedList<Producto>> {
        val productosData = MutableLiveData<LinkedList<Producto>>()
        productoRepo.getAll().observeForever {
            productosData.value = it
        }
        return productosData
    }


    fun getByEstado(estado: String): LiveData<LinkedList<Producto>> {
        val productosData = MutableLiveData<LinkedList<Producto>>()
        productoRepo.getByEstado(estado).observeForever {
            productosData.value = it
        }

        return productosData
    }


}