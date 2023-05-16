package com.example.reserbuddy.adapters

import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.reserbuddy.R
import com.example.reservarapp.models.Reserva
import com.example.reservarapp.models.Tarea
import com.example.reservarapp.viewmodels.TareaViewModel

class TareaAdapterBasico(var listaTareas:MutableList<Tarea>, var listener: OnItemClickListener):
    RecyclerView.Adapter<TareaAdapterBasico.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.cardview_tarea2, parent, false)
        return ViewHolder(v, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listaTareas.get(position)
        holder.tvNombreTarea.text = item.nombre
        holder.ivTarea.setImageResource(item.foto)

        holder.ivTarea.setOnClickListener {
            listener.onImageClick(position)
        }


    }

    override fun getItemCount(): Int {
        return listaTareas.size
    }

    inner class ViewHolder(
        v: View,
        var listener: OnItemClickListener
    ) : RecyclerView.ViewHolder(v), View.OnClickListener, View.OnCreateContextMenuListener {


        var tvNombreTarea: TextView = v.findViewById(R.id.tvNombreTarea2)
        var ivTarea : ImageView = v.findViewById(R.id.ivTarea2)




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
            TODO("Not yet implemented")
        }


    }
}

