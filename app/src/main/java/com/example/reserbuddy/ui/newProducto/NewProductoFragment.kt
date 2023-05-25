package com.example.reserbuddy.ui.newProducto

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reserbuddy.CategoriasGenerator
import com.example.reserbuddy.adapters.CategoriaAdapter
import com.example.reserbuddy.adapters.OnItemClickListener
import com.example.reserbuddy.databinding.FragmentNewProductoBinding
import com.example.reserbuddy.ui.listaCompra.ListaCompraFragment
import com.example.reservarapp.models.Categoria
import com.example.reservarapp.models.Producto
import com.example.reservarapp.viewmodels.ProductoViewModel

import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class NewProductoFragment : BottomSheetDialogFragment() {


    private var _binding: FragmentNewProductoBinding? = null
    private val binding get() = _binding!!

    lateinit var mAdapter: RecyclerView.Adapter<CategoriaAdapter.ViewHolder>
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private lateinit var mRecyclerView: RecyclerView
    private val productoViewModel by lazy { ViewModelProvider(this).get(ProductoViewModel::class.java) }

    private lateinit var listaCategorias: MutableList<Categoria>
    private var categoriaElegida = Categoria()
    private lateinit var btnCrear: Button
    private lateinit var etNombreProducto: EditText


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentNewProductoBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        btnCrear = binding.btnCrearProducto
        etNombreProducto = binding.etNombreProducto

        btnCrear.isEnabled = false


        listaCategorias = CategoriasGenerator.crearCategorias()
        inicializarAdapters()


        binding.btnCrearProducto.setOnClickListener {
            crearProducto()

        }

        habilitarBotonAdd()


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun crearProducto() {
        //recogiendo datos del form
        var nombre = etNombreProducto.text.toString()
        var categoria = categoriaElegida.nombre
        var foto = categoriaElegida.foto


        //crear objeto para firebase
        var producto = Producto()
        producto.nombre = nombre
        producto.categoria = categoriaElegida.nombre
        producto.foto = categoriaElegida.foto
        producto.estado = "Pendiente"

        productoViewModel.addProducto(producto)
        dismiss()
//       (parentFragment as ListaCompraFragment)?.mAdapter?.notifyDataSetChanged()
//        ListaCompraFragment().mAdapter.notifyDataSetChanged()
    }

    fun habilitarBotonAdd() {

        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Verificar si ambos EditText están vacíos
                btnCrear.isEnabled =
                    !binding.etNombreProducto.text.isNullOrEmpty() && categoriaElegida.nombre != ""
            }

            //Estos dos metodos no necesitan ser implementados
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        }

        // Agregar el TextWatcher a ambos EditText
        binding.etNombreProducto.addTextChangedListener(textWatcher)

    }

    private fun inicializarAdapters() {


        mRecyclerView = binding.recyclerCategorias
        mLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        mAdapter = CategoriaAdapter(listaCategorias, object : OnItemClickListener {
            override fun OnItemClick(vista: View, position: Int) {
                categoriaElegida = listaCategorias[position]
                categoriaElegida.isSelected = !categoriaElegida.isSelected
                enableButton()
                mAdapter.notifyItemChanged(position)

            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onClick2(position: Int) {


            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onClick3(position: Int) {

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

    fun enableButton() {
        val isNombreProductoNotEmpty = etNombreProducto.text.isNotEmpty()
        val isCategoriaSelected = categoriaElegida.isSelected

        btnCrear.isEnabled = isNombreProductoNotEmpty && isCategoriaSelected
    }


}