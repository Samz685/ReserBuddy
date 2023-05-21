package com.example.reserbuddy.ui.clientes

import android.graphics.Color
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.reserbuddy.DataHolder
import com.example.reserbuddy.R
import com.example.reserbuddy.adapters.OnItemClickListener
import com.example.reserbuddy.adapters.ReservaAdapter
import com.example.reserbuddy.adapters.ReservaAdapterBasico
import com.example.reserbuddy.databinding.FragmentDetalleClienteBinding
import com.example.reservarapp.models.Reserva
import com.example.reservarapp.viewmodels.ClienteViewModel
import com.example.reservarapp.viewmodels.ReservaViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class DetalleClienteFragment : Fragment() {

    private var _binding: FragmentDetalleClienteBinding? = null
    private lateinit var listaReservasTotales: MutableList<Reserva>
    lateinit var mAdapter: RecyclerView.Adapter<ReservaAdapterBasico.ViewHolder>
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private lateinit var mRecyclerView: RecyclerView
    private val reservaViewModel by lazy { ViewModelProvider(this).get(ReservaViewModel::class.java) }
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var ivFoto : ImageView
    private lateinit var tvNombre : TextView
    private lateinit var tvTelefono : TextView
    private lateinit var tvEmail: TextView
    private lateinit var pieChart: PieChart
    private var countConfirmadas : Int = 0
    private var countCanceladas : Int = 0
    private var countTotal : Int = 0



    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentDetalleClienteBinding.inflate(inflater, container, false)
        val root: View = binding.root



        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listaReservasTotales = mutableListOf<Reserva>()

        getReservasByCliente(DataHolder.currentCliente.telefono)
        getReservasByClienteByEstado(DataHolder.currentCliente.telefono, "Confirmada")
        getReservasByClienteByEstado(DataHolder.currentCliente.telefono, "Cancelada")

        inicializarAdapters()

        inicializarDetalles()



//        swipeRefresh = binding.swipeRefreshDetalleCliente


    }


    fun inicializarDetalles(){

        var cliente = DataHolder.currentCliente

        ivFoto = binding.ivDetalleCliente
        tvNombre = binding.tvDetalleClienteNombre
        tvTelefono = binding.tvDetalleClienteTel
        tvEmail = binding.tvDetalleClienteEmail

        ivFoto.setImageResource(cliente.foto)
        tvNombre.text = cliente.nombre
        tvTelefono.text = cliente.telefono
        tvEmail.text = cliente.email.toString()

    }
    fun refreshUsuarios() {
        swipeRefresh.setOnRefreshListener {
            getReservasByCliente(DataHolder.currentCliente.telefono)
            swipeRefresh.isRefreshing = false

        }
    }

    fun inicializarChart(){

        pieChart = binding.chartCliente

        val entries = listOf(
            PieEntry(countTotal.toFloat(), "Totales"),
            PieEntry(countConfirmadas.toFloat(), "Confirmadas"),
            PieEntry(countCanceladas.toFloat(), "Canceladas")
        )
        //Crear datos para el grafico
        val dataSet = PieDataSet(entries, "Reservas Cliente")


        // Personalizar colores si lo deseas

        // Accede a los colores definidos en colors.xml utilizando el contexto de la aplicación
        val pieAzulClaro = ContextCompat.getColor(requireContext(), R.color.pieAzulClaro)
        val verdeMenta = ContextCompat.getColor(requireContext(), R.color.pieVerdeMenta)
        val rojoCoral = ContextCompat.getColor(requireContext(), R.color.pieRojoCoral)

        dataSet.colors = listOf(pieAzulClaro, verdeMenta, rojoCoral)
        dataSet.valueTextSize = 18f
        dataSet.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return value.toInt().toString()
            }
        }

        //Crear objeto PieData
        val data = PieData(dataSet)

        pieChart.data = data
        pieChart.description.isEnabled = false
        pieChart.setCenterText(countTotal.toString()) // Establece el número total de reservas en el centro
        pieChart.setCenterTextColor(Color.BLACK) // Establece el color del texto en el centro
        pieChart.setCenterTextSize(25f) // Establece el tamaño del texto en el centro
        pieChart.invalidate() // Actualiza el gráfico
        pieChart.setDrawEntryLabels(false)
        // Animación del gráfico
        pieChart.animateY(1000, Easing.EaseInOutCubic)









    }

    private fun inicializarAdapters() {


        mRecyclerView = binding.recyclerReservas2
        mLayoutManager = LinearLayoutManager(activity)
        mAdapter = ReservaAdapterBasico(listaReservasTotales, object : OnItemClickListener {
            override fun OnItemClick(vista: View, position: Int) {


            }

            override fun onClick2(position: Int) {

            }

            override fun onClick3(position: Int) {

            }

            override fun onImageClick(position: Int) {


            }


        })
        mRecyclerView.adapter = mAdapter
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = mLayoutManager
    }

    fun getReservasByCliente(numCliente: String) {
        reservaViewModel.getByCliente(numCliente).observe(viewLifecycleOwner, Observer { reservas ->
            listaReservasTotales.clear()
            listaReservasTotales.addAll(reservas)
            countTotal = listaReservasTotales.size

            mAdapter.notifyDataSetChanged()
            inicializarChart()
        })
    }

    fun getReservasByClienteByEstado(numCliente: String, estado: String) {
        reservaViewModel.getByClienteByEstado(numCliente, estado).observe(viewLifecycleOwner, Observer { reservas ->
            val listaReservas = reservas.toMutableList()
            val countReservas = listaReservas.size

            if (estado == "Confirmada") {
                countConfirmadas = countReservas
            } else {
                countCanceladas = countReservas
            }

            mAdapter.notifyDataSetChanged()
            inicializarChart()
        })
    }





    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}