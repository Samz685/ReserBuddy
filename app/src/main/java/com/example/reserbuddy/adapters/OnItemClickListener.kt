package com.example.reserbuddy.adapters


import android.view.View

interface OnItemClickListener {
    fun OnItemClick(view : View, position:Int)
    fun onImageClick(position: Int)
}