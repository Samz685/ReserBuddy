package com.example.reserbuddy.adapters

import android.graphics.Color
import android.provider.CalendarContract.Colors
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.reserbuddy.R
import com.example.reservarapp.models.Categoria

class CategoriaAdapter(
    var listaCategoria: MutableList<Categoria>,
    var listener: OnItemClickListener
) :
    RecyclerView.Adapter<CategoriaAdapter.ViewHolder>() {

    private var selectedIndex: Int = -1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.cardview_categoria, parent, false)
        return ViewHolder(v, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listaCategoria.get(position)
        holder.tvNombre.text = item.nombre
        holder.ivCategoria.setImageResource(item.foto)

        //Cambia color si es seleccionado
        if (item.isSelected) {
            val color = ContextCompat.getColor(holder.itemView.context, R.color.verdeSeleccion)
            holder.itemView.setBackgroundColor(color)
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE)
        }


    }

    override fun getItemCount(): Int {
        return listaCategoria.size
    }

    inner class ViewHolder(
        v: View,
        var listener: OnItemClickListener
    ) : RecyclerView.ViewHolder(v), View.OnClickListener {


        var tvNombre: TextView = v.findViewById(R.id.tvNombreCategoria)
        var ivCategoria: ImageView = v.findViewById(R.id.ivCategoria)


        init {
            v.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            this.listener.OnItemClick(p0!!, adapterPosition)

            val clickedIndex = adapterPosition
            val previouslySelectedIndex = selectedIndex

            selectedIndex = clickedIndex
            notifyItemChanged(clickedIndex)

            if (previouslySelectedIndex != -1 && previouslySelectedIndex != clickedIndex) {
                listaCategoria[previouslySelectedIndex].deseleccionar()
                notifyItemChanged(previouslySelectedIndex)
            }




        }



    }
}

