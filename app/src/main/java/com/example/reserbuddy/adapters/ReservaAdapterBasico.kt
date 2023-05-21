package com.example.reserbuddy.adapters

import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.reserbuddy.R
import com.example.reservarapp.models.Reserva
import com.example.reservarapp.viewmodels.ReservaViewModel

class ReservaAdapterBasico(var listaReservas:MutableList<Reserva>, var listener: OnItemClickListener):
    RecyclerView.Adapter<ReservaAdapterBasico.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.cardview_reserva_basico, parent, false)
        return ViewHolder(v, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listaReservas.get(position)
        holder.tvNombreCliente.text = item.cliente
        holder.tvFechaReserva.text = item.fechaCard.toString()
        holder.tvCantidad.text = item.numComensales.toString()
        holder.tvHora.text = item.hora.toString()
        holder.ivReserva.setImageResource(item.foto)
        holder.ivReserva.setOnClickListener {
        }


        //Setear color fondo estado
        if(item.estado == "Confirmada"){
            holder.tvEstado.setBackgroundResource(R.drawable.estado_green_rectangle)
        } else if(item.estado == "Cancelada"){
            holder.tvEstado.setBackgroundResource(R.drawable.estado_red_rectangle)
        } else {
            holder.tvEstado.setBackgroundResource(R.drawable.estado_grey_rectangle)
        }

    }

    override fun getItemCount(): Int {
        return listaReservas.size
    }

    inner class ViewHolder(
        v: View,
        var listener: OnItemClickListener
    ) : RecyclerView.ViewHolder(v), View.OnClickListener, View.OnCreateContextMenuListener {


        var tvNombreCliente: TextView = v.findViewById(R.id.tvNombreCliente)
        var tvFechaReserva: TextView = v.findViewById(R.id.tvFechaReserva)
        var tvHora: TextView = v.findViewById(R.id.tvHora)
        var tvCantidad: TextView = v.findViewById(R.id.tvCantidad)
        var ivReserva: ImageView = v.findViewById(R.id.ivReserva)
        var tvEstado : TextView = v.findViewById(R.id.tvEstado)



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

