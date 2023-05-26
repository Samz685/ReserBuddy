package com.example.reserbuddy.adapters

import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.reserbuddy.R
import com.example.reservarapp.models.Tarea
import com.example.reservarapp.viewmodels.TareaViewModel

class TareaAdapter(
    var listaTareas: MutableList<Tarea>,
    var listener: OnItemClickListener,
    var tareaViewModel: TareaViewModel
) :
    RecyclerView.Adapter<TareaAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.cardview_tarea, parent, false)
        return ViewHolder(v, listener, tareaViewModel)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listaTareas.get(position)
        holder.tvNombreTarea.text = item.nombre
        holder.tvUsuarioAsignado.text = item.asignedTo
        holder.tvFechaAsignada.text = item.asignedDateCard
        holder.tvFechaCompletada.text = item.doneDateCard
        holder.tvComentario.text = item.comentario
        holder.ivTarea.setImageResource(item.foto)
        holder.tvEstadoTarea.text = item.estado



        //Visibilidad de componentes del CardView
        if (item.asignedToId =="") {
            holder.bloqueFecha.visibility = GONE
            holder.tvEstadoTarea.visibility = GONE
            holder.btnCompletada.visibility = GONE
            holder.btnNoCompletada.visibility = GONE
            holder.btnQuitar.visibility = GONE
            holder.btnAsignar.visibility = VISIBLE
            holder.ivTarea.setImageResource(R.drawable.ic_task1)
        } else {
            holder.btnQuitar.visibility = VISIBLE
            holder.btnAsignar.visibility = GONE
            holder.tvEstadoTarea.visibility = VISIBLE

            if (item.estado.equals("Pendiente")) {
                holder.btnCompletada.visibility = GONE
                holder.btnNoCompletada.visibility = VISIBLE
                holder.bloqueFecha.visibility = GONE
                holder.tvEstadoTarea.visibility = VISIBLE
                holder.ivTarea.setImageResource(R.drawable.ic_task2)

            } else if (item.estado.equals("Completada")) {
                holder.btnQuitar.visibility = GONE
                holder.btnAsignar.visibility = GONE
                holder.btnCompletada.visibility = VISIBLE
                holder.btnNoCompletada.visibility = GONE
                holder.bloqueFecha.visibility = VISIBLE
                holder.ivTarea.setImageResource(R.drawable.ic_task3)

            } else if(item.estado.equals("Sin asignar")) {
                holder.btnAsignar.visibility = VISIBLE
                holder.btnQuitar.visibility = GONE
                holder.tvEstadoTarea.visibility = GONE
                holder.btnCompletada.visibility = GONE
                holder.btnNoCompletada.visibility = GONE
                holder.bloqueFecha.visibility = GONE
                holder.ivTarea.setImageResource(R.drawable.ic_task1)
            }
        }



        holder.btnCompletada.setOnClickListener {
            listener.onClick3(position)

        }

        holder.btnNoCompletada.setOnClickListener {
            listener.onClick3(position)

        }

        holder.btnAsignar.setOnClickListener {
            listener.onClick2(position)

        }

        holder.btnQuitar.setOnClickListener {
            listener.onClick2(position)

        }


    }

    override fun getItemCount(): Int {
        return listaTareas.size
    }

    inner class ViewHolder(
        v: View,
        var listener: OnItemClickListener,
        TareaViewModel: TareaViewModel
    ) : RecyclerView.ViewHolder(v), View.OnClickListener, View.OnCreateContextMenuListener {


        var tvNombreTarea: TextView = v.findViewById(R.id.tvNombreTarea)
        var tvUsuarioAsignado: TextView = v.findViewById(R.id.tvUsuarioAsignado)
        var tvFechaAsignada: TextView = v.findViewById(R.id.tvFechaAsignada)
        var btnAsignar: LinearLayout = v.findViewById(R.id.bloque_asignar)
        var btnQuitar: LinearLayout = v.findViewById(R.id.bloque_quitar)
        var btnCompletada: LinearLayout = v.findViewById(R.id.bloque_checkOK)
        var btnNoCompletada: LinearLayout = v.findViewById(R.id.bloque_checkNO)
        var tvFechaCompletada: TextView = v.findViewById(R.id.tvFechaCompletada)
        var tvComentario: TextView = v.findViewById(R.id.tvComentarioTarea)
        var ivTarea: ImageView = v.findViewById(R.id.ivTarea)
        var tvEstadoTarea: TextView = v.findViewById(R.id.tvEstadoTarea)
        var bloqueFecha: LinearLayout = v.findViewById(R.id.bloqueFecha)


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

            menu!!.setHeaderTitle("" + listaTareas[adapterPosition].nombre)
            val item = menu!!.add("Asignar")
            val item2 = menu.add("Borrar")

            item.setOnMenuItemClickListener {


                true
            }

            item2.setOnMenuItemClickListener {
                var tarea = listaTareas[adapterPosition]
                tareaViewModel.deleteTarea(tarea)

                listaTareas.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
                notifyDataSetChanged()

                true
            }

        }

    }
}

