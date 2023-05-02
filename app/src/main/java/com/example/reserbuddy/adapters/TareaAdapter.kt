package com.example.reserbuddy.adapters
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.reserbuddy.R
import com.example.reservarapp.models.Tarea
import com.example.reservarapp.viewmodels.TareaViewModel

class TareaAdapter(var listaTareas:MutableList<Tarea>, var listener: OnItemClickListener, var tareaViewModel: TareaViewModel):
    RecyclerView.Adapter<TareaAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.cardview_usuario, parent, false)
        return ViewHolder(v, listener, tareaViewModel)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listaTareas.get(position)
        holder.tvNombreTarea.text = item.alias
        holder.tvUsuarioAsignado.text = item.asignedTo
        holder.tvFechaAsignada.text = item.asignedDateCard
        holder.tvFechaCompletada.text = item.doneDateCard
        holder.tvComentario.text = item.comentario
        holder.ivTarea.setImageResource(item.foto)

        holder.btnAsignar.setOnClickListener {

        }
        holder.btnCompletada.setOnClickListener {

        }

        holder.ivTarea.setOnClickListener {
            listener.onImageClick(position)
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
        var btnAsignar: TextView = v.findViewById(R.id.btnAsignar)
        var btnCompletada: TextView = v.findViewById(R.id.btnCompletada)
        var tvFechaCompletada : TextView = v.findViewById(R.id.tvFechaCompletada)
        var tvComentario : TextView = v.findViewById(R.id.tvComentarioTarea)
        var ivTarea : ImageView = v.findViewById(R.id.ivTarea)





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

            menu!!.setHeaderTitle("" + listaTareas[adapterPosition].alias)
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

