package com.example.reserbuddy.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.reservarapp.models.Usuario

class UsuarioAdapterSP(var usuarios: MutableList<Usuario>) : BaseAdapter(){


        override fun getCount(): Int {
            return usuarios.size
        }

        override fun getItem(position: Int): Any {
            return usuarios[position]
        }

        override fun getItemId(position: Int): Long {
            return usuarios[position].id.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = convertView ?: LayoutInflater.from(parent?.context).inflate(
                android.R.layout.simple_spinner_item, parent, false
            )
            view.findViewById<TextView>(android.R.id.text1).text = usuarios[position].alias
            return view
        }


}