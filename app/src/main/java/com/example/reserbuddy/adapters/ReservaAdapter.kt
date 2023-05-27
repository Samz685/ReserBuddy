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

class ReservaAdapter(var listaReservas:MutableList<Reserva>, var listener: OnItemClickListener, var reservaViewmodel: ReservaViewModel):
    RecyclerView.Adapter<ReservaAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.cardview_reserva, parent, false)
        return ViewHolder(v, listener, reservaViewmodel)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listaReservas.get(position)
        holder.tvNombreCliente.text = item.cliente
        holder.tvTelefono.text = item.telefono
        holder.tvFechaReserva.text = item.fechaCard.toString()
        holder.tvCantidad.text = item.numComensales.toString()
        holder.tvHora.text = item.hora.toString()
//        Picasso.get().load(item.foto).fit().into(holder.ivReserva)
        holder.ivReserva.setImageResource(item.foto)
        holder.ivReserva.setOnClickListener {
            listener.onImageClick(position)
        }
        holder.etComentario.text = item.comentario
        holder.tvEstado.text = item.estado.toString()

        holder.btnConfirmar.setOnClickListener {
            listener.onClick2(position)

        }

        holder.btnCancelar.setOnClickListener {
            listener.onClick3(position)

        }

        //Actualizar estado desde los botones del cardView



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
        var listener: OnItemClickListener,
        reservaViewmodel: ReservaViewModel
    ) : RecyclerView.ViewHolder(v), View.OnClickListener, View.OnCreateContextMenuListener {


        var tvNombreCliente: TextView = v.findViewById(R.id.tvNombreCliente)
        var tvTelefono: TextView = v.findViewById(R.id.tvTelefono)
        var tvFechaReserva: TextView = v.findViewById(R.id.tvFechaReserva)
        var tvHora: TextView = v.findViewById(R.id.tvHora)
        var tvCantidad: TextView = v.findViewById(R.id.tvCantidad)
        var ivReserva: ImageView = v.findViewById(R.id.ivReserva)
        var etComentario: TextView = v.findViewById(R.id.etComentario)
        var btnConfirmar: LinearLayout = v.findViewById(R.id.bloque_confirmar)
        var btnCancelar: LinearLayout = v.findViewById(R.id.bloque_cancelar)
        var tvEstado : TextView = v.findViewById(R.id.tvEstado)



        init {
            v.setOnClickListener(this)
            v.setOnCreateContextMenuListener(this)
        }

        override fun onClick(p0: View?) {
            this.listener.OnItemClick(p0!!, adapterPosition)


        }

        override fun onCreateContextMenu(
            menu: ContextMenu?,
            v: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {

            menu!!.setHeaderTitle("" + listaReservas[adapterPosition].cliente)
            val item = menu!!.add("Confirmar")
            val item2 = menu.add("Cancelar")

            item.setOnMenuItemClickListener {


                true
            }

            item2.setOnMenuItemClickListener {
                var res = listaReservas[adapterPosition]
                reservaViewmodel.deleteReserva(res)

                listaReservas.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
                notifyDataSetChanged()

                true
            }

        }

    }
}

