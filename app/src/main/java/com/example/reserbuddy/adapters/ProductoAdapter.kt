package com.example.reserbuddy.adapters

import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.reserbuddy.R
import com.example.reservarapp.models.Producto

class ProductoAdapter(var listaProductos:MutableList<Producto>, var listener: OnItemClickListener):
    RecyclerView.Adapter<ProductoAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.cardview_producto, parent, false)
        return ViewHolder(v, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listaProductos.get(position)
        holder.tvNombreProducto.text = item.nombre
        holder.tvCategoriaProducto.text = item.categoria
        holder.ivProducto.setImageResource(item.foto)

        if (item.estado == "Pendiente"){
            holder.btnComprado.visibility = GONE
            holder.btnPendiente.visibility = VISIBLE
         } else if (item.estado == "Comprado"){
            holder.btnComprado.visibility = VISIBLE
            holder.btnPendiente.visibility = GONE
        }

        holder.btnPendiente.setOnClickListener {
            listener.onClick2(position)
        }

        holder.btnComprado.setOnClickListener {
            listener.onClick3(position)
        }


    }

    override fun getItemCount(): Int {
        return listaProductos.size
    }

    inner class ViewHolder(
        v: View,
        var listener: OnItemClickListener
    ) : RecyclerView.ViewHolder(v), View.OnClickListener, View.OnCreateContextMenuListener {


        var tvNombreProducto: TextView = v.findViewById(R.id.tvNombreProducto)
        var tvCategoriaProducto: TextView = v.findViewById(R.id.tvCategoriaProducto)
        var btnComprado: ImageView = v.findViewById(R.id.btnEstadoProductoOK)
        var btnPendiente: ImageView = v.findViewById(R.id.btnEstadoProductoNO)
        var ivProducto : ImageView = v.findViewById(R.id.ivProducto)




        init {
            v.setOnClickListener(this)
            v.setOnCreateContextMenuListener(this)
        }

        override fun onClick(p0: View?) {
            this.listener.OnItemClick(p0!!, adapterPosition)


        }

        override fun onCreateContextMenu(
            p0: ContextMenu?,
            p1: View?,
            p2: ContextMenu.ContextMenuInfo?
        ) {

        }


    }
}

