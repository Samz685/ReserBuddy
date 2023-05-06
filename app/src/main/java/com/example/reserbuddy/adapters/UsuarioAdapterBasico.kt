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
import com.example.reservarapp.models.Usuario
import com.example.reservarapp.viewmodels.UsuarioViewModel

class UsuarioAdapterBasico(var listaUsuarios:MutableList<Usuario>, var listener: OnItemClickListener):
    RecyclerView.Adapter<UsuarioAdapterBasico.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.cardview_usuario, parent, false)
        return ViewHolder(v, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listaUsuarios.get(position)
        holder.tvNombreUsuario.text = item.alias
        holder.tvEmail.text = item.email
        holder.tvTelefono.text = item.telefono
        holder.tvRol.text = item.rol
        holder.tvComentario.text = item.comentario
        holder.ivUsuario.setImageResource(item.foto)

        holder.ivUsuario.setOnClickListener {
            listener.onImageClick(position)
        }


    }

    override fun getItemCount(): Int {
        return listaUsuarios.size
    }

    inner class ViewHolder(
        v: View,
        var listener: OnItemClickListener
    ) : RecyclerView.ViewHolder(v), View.OnClickListener, View.OnCreateContextMenuListener {


        var tvNombreUsuario: TextView = v.findViewById(R.id.tvNombreUsuario)
        var tvEmail: TextView = v.findViewById(R.id.tvEmailUsuario)
        var tvRol: TextView = v.findViewById(R.id.tvRol)
        var tvTelefono: TextView = v.findViewById(R.id.tvTelefonoUsuario)
        var tvComentario: TextView = v.findViewById(R.id.tvComentarioUsuario)
        var ivUsuario : ImageView = v.findViewById(R.id.ivUsuario)




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

