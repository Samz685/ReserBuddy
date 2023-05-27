package com.example.reserbuddy.adapters

import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.reserbuddy.R
import com.example.reservarapp.models.Cliente
import com.example.reservarapp.viewmodels.ClienteViewModel


class ClienteAdapter(var listaClientes:MutableList<Cliente>, var listener: OnItemClickListener, var clienteViewModel : ClienteViewModel):
    RecyclerView.Adapter<ClienteAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.cardview_cliente, parent, false)
        return ViewHolder(v, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listaClientes.get(position)
        holder.tvNombreCliente.text = item.nombre
        holder.tvTelefono.text = item.telefono
        holder.ivCliente.setImageResource(item.foto)





        holder.ivCliente.setOnClickListener {
            listener.onImageClick(position)
        }


    }

    override fun getItemCount(): Int {
        return listaClientes.size
    }

    inner class ViewHolder(
        v: View,
        var listener: OnItemClickListener
    ) : RecyclerView.ViewHolder(v), View.OnClickListener, View.OnCreateContextMenuListener {


        var tvNombreCliente: TextView = v.findViewById(R.id.tvNombreCliente)
        var tvTelefono: TextView = v.findViewById(R.id.tvTelefonoCliente)
        var ivCliente: ImageView = v.findViewById(R.id.ivCliente)


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

            menu!!.setHeaderTitle("Borrar cliente " + listaClientes[adapterPosition].nombre)
            val item = menu!!.add("Confirmar")
            val item2 = menu.add("Eliminar")

            item.setOnMenuItemClickListener {


                true
            }

            item2.setOnMenuItemClickListener {
                var res = listaClientes[adapterPosition]

                clienteViewModel.deleteCliente(res)

                listaClientes.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
                notifyDataSetChanged()

                true
            }

        }
    }
}
