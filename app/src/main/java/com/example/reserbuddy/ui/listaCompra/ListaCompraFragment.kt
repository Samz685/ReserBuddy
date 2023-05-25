package com.example.reserbuddy.ui.listaCompra

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.reserbuddy.DataHolder
import com.example.reserbuddy.R
import com.example.reserbuddy.adapters.OnItemClickListener
import com.example.reserbuddy.adapters.ProductoAdapter
import com.example.reserbuddy.adapters.TareaAdapter
import com.example.reserbuddy.databinding.FragmentListaCompraBinding
import com.example.reservarapp.models.Producto
import com.example.reservarapp.models.Usuario
import com.example.reservarapp.viewmodels.ProductoViewModel
import com.google.android.material.tabs.TabLayout

class ListaCompraFragment : Fragment() {

    private var _binding: FragmentListaCompraBinding? = null
    private lateinit var listaProductos: MutableList<Producto>
    lateinit var mAdapter: RecyclerView.Adapter<ProductoAdapter.ViewHolder>
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private lateinit var mRecyclerView: RecyclerView
    private val productoViewModel by lazy { ViewModelProvider(this).get(ProductoViewModel::class.java) }
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var tvMensajeProducto : TextView
    private lateinit var tabLayout: TabLayout
    var productoEstado = ""
    private lateinit var user : Usuario

    private val binding get() = _binding!!

    override fun onResume() {
        super.onResume()
        getProductosByEstado("Pendiente")
        true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentListaCompraBinding.inflate(inflater, container, false)
        val root: View = binding.root




        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        DataHolder.currentFragment = "FragmentListaCompra"

        user = DataHolder.currentUser
        tabLayout = binding.tabProductos

        productoEstado = "Pendiente"


        swipeRefresh = binding.swipeRefreshProductos

        listaProductos = mutableListOf()
        tvMensajeProducto = binding.tvMensajeProducto



        inicializarAdapters()
        refreshProductos()

        setupTabs()

//        crearProducto()




        mensajeListaVacia()



    }

    fun crearProducto(){

        var producto = Producto()
        producto.foto = R.drawable.ic_k_leche
        producto.categoria = "Lacteo"
        producto.nombre = "Leche"
        producto.estado = "Pendiente"

        productoViewModel.addProducto(producto)
    }




    private fun mensajeListaVacia() {
        if (listaProductos.size == 0) {
            tvMensajeProducto.visibility = VISIBLE
        } else {
            tvMensajeProducto.visibility = GONE
        }
    }

    fun refreshProductos() {
        swipeRefresh.setOnRefreshListener {
            getProductosByEstado(productoEstado)
            swipeRefresh.isRefreshing = false
            resetearContador()

        }
    }

    private fun resetearContador(){
        binding.tvContadorProductos.text = listaProductos.size.toString()
        mensajeListaVacia()
    }


    private fun inicializarAdapters() {


        mRecyclerView = binding.recyclerProductos
        mLayoutManager = LinearLayoutManager(activity)
        mAdapter = ProductoAdapter(listaProductos, object : OnItemClickListener {
            override fun OnItemClick(vista: View, position: Int) {

            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onClick2(position: Int) {
                var producto = listaProductos[position]
                producto.estado = "Comprado"
                productoViewModel.updateEstado(producto)
                mAdapter.notifyDataSetChanged()

            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onClick3(position: Int) {

                var producto = listaProductos[position]
                producto.estado = "Comprado"
                productoViewModel.updateEstado(producto)
                mAdapter.notifyDataSetChanged()

            }

            override fun onImageClick(position: Int) {
                TODO("Not yet implemented")
            }


        })
        mRecyclerView.adapter = mAdapter
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.itemAnimator = DefaultItemAnimator()
        mRecyclerView.layoutManager = mLayoutManager


    }

    fun getProductosByEstado(estado: String) {
        productoViewModel.getByEstado(estado).observe(viewLifecycleOwner, Observer {
            listaProductos.clear()
            for (producto in it) {
                listaProductos.add(producto)
            }


            resetearContador()
            mAdapter.notifyDataSetChanged()
        })
        productoEstado = estado
    }







    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }




    private fun setupTabs() {
        tabLayout.addTab(tabLayout.newTab().setText(R.string.pendientes))
        tabLayout.addTab(tabLayout.newTab().setText(R.string.comprado))



        // Cambiar la lista de reservas según la pestaña seleccionada
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> getProductosByEstado("Pendiente")
                    1 -> getProductosByEstado("Comprado")

                    else -> getProductosByEstado("Pendiente")
                }
                mRecyclerView.adapter?.notifyDataSetChanged()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }
}