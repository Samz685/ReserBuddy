package com.example.reserbuddy.adapters

import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.reserbuddy.R
import com.example.reservarapp.models.Reserva
import com.example.reservarapp.viewmodels.ReservaViewModel
import com.squareup.picasso.Picasso

class ReservaAdapter(var listaReservas:MutableList<Reserva>, var listener: OnItemClickListener, var reservaViewmodel: ReservaViewModel):
    RecyclerView.Adapter<ReservaAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v=LayoutInflater.from(parent.context).inflate(R.layout.cardview_reserva, parent, false)
        return ViewHolder(v,listener,reservaViewmodel)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item=listaReservas.get(position)
        holder.tvNombreCliente.text=item.cliente
        holder.tvTelefono.text = item.telefono
        holder.tvFechaReserva.text = item.fecha.toString()
        holder.tvCantidad.text = item.numComensales.toString()
        holder.tvHora.text = item.hora.toString()
//        Picasso.get().load(item.foto).fit().into(holder.ivReserva)
        holder.ivReserva.setImageResource(item.foto)
        holder.ivReserva.setOnClickListener {
            listener.onImageClick(position)
        }

    }

    override fun getItemCount(): Int {
        return listaReservas.size
    }

    inner class ViewHolder(
        v: View,
        var listener: OnItemClickListener,
        reservaViewmodel: ReservaViewModel
    ) : RecyclerView.ViewHolder(v), View.OnClickListener, View.OnCreateContextMenuListener {

        var tvNombreCliente: TextView = v.findViewById(R.id.tvNombreCliente)
        var tvTelefono: TextView = v.findViewById(R.id.tvTelefono)
        var tvFechaReserva: TextView = v.findViewById(R.id.tvFechaReserva)
        var tvHora: TextView = v.findViewById(R.id.tvHora)
        var tvCantidad: TextView = v.findViewById(R.id.tvCantidad)
        var ivReserva: ImageView = v.findViewById(R.id.ivReserva)




        init {
            v.setOnClickListener(this)
            v.setOnCreateContextMenuListener(this)
        }

        override fun onClick(p0: View?) {
            this.listener.OnItemClick(p0!!,adapterPosition)

        }

       override fun onCreateContextMenu(
           menu: ContextMenu?,
           v: View?,
           menuInfo: ContextMenu.ContextMenuInfo?
       ) {

           menu!!.setHeaderTitle(""+listaReservas[adapterPosition].cliente)
           val item = menu!!.add("Confirmar")
           val item2 = menu.add("Cancelar")

           item.setOnMenuItemClickListener {

//               var fruta : Fruta = listaReservas[adapterPosition]
//
//
//               listaReservas.removeAt(adapterPosition)
//               reservaViewmodel.deleteFruta(fruta)
//
//               notifyItemRemoved(adapterPosition)
//               notifyDataSetChanged()
               true    }

           item2.setOnMenuItemClickListener {
//               listaReservas[adapterPosition].cantidad = 0
//               notifyItemChanged(adapterPosition)
               true    }
       }

   }

}