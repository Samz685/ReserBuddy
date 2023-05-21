package com.example.reserbuddy.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.reserbuddy.R
import com.example.reserbuddy.databinding.FragmentHomeBinding
import com.example.reservarapp.viewmodels.ReservaViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var auth: FirebaseAuth;
    private val reservaViewModel by lazy { ViewModelProvider(this).get(ReservaViewModel::class.java) }
    private var countTotal = 0
    private var countConfirmadas = 0
    private var countCanceladas = 0

    private var countByMes = mutableMapOf<String, Int>()
    private lateinit var barChart: BarChart
    private lateinit var pieChart: PieChart
    private var mesesChart = mutableListOf<Int>()



    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getChartTotal()
        getChartEstado("Confirmada")
        getChartEstado("Cancelada")
        fillMesesData()












    }

    fun getChartTotal() {
        reservaViewModel.getChartTotal().observe(viewLifecycleOwner, Observer { reservas ->

            countTotal = reservas

            inicializarChartPie()
        })
    }

    fun getChartEstado(estado : String) {
        reservaViewModel.getChartEstado(estado).observe(viewLifecycleOwner, Observer { reservas ->
            if(estado == "Confirmada"){
                countConfirmadas = reservas
            } else {
                countCanceladas = reservas
            }

            inicializarChartPie()
        })
    }



    fun getChartMonth(mes: Int) : Int{
        reservaViewModel.getChartMonth(mes).observe(viewLifecycleOwner, Observer { reservas ->


            countTotal = reservas
            inicializarChartBars()
        })
        return countTotal
    }

    fun inicializarChartBars() {

        //llenar lista de meses y sus respectivas reservas
            val nombreMeses = mutableListOf<String>(
                "En",
                "Feb",
                "Mar",
                "Abr",
                "May",
                "Jun",
                "Jul",
                "Ago",
                "Sep",
                "Oct",
                "Nov",
                "Dic"
            )
            val mesesChart = mutableListOf<Int>()

            for (i in 1..12) {
                val count = getChartMonth(i)
                mesesChart.add(count)
            }

            val entries = mutableListOf<PieEntry>()

            for (i in 0 until mesesChart.size) {
                val mes = nombreMeses[i]
                val count = mesesChart[i].toFloat()
                val entry = PieEntry(count, mes)
                entries.add(entry)
            }

        barChart = binding.chartReservasMes


    }



    private fun inicializarChartPie() {
        pieChart = binding.chartReservasTotal
        var pendientes = countTotal-countConfirmadas-countCanceladas

        val entries = listOf(
            PieEntry(pendientes.toFloat(), "Pendientes"),
            PieEntry(countConfirmadas.toFloat(), "Confirmadas"),
            PieEntry(countCanceladas.toFloat(), "Canceladas")
        )
        //Crear datos para el grafico
        val dataSet = PieDataSet(entries, "Reservas Totales")


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
        pieChart.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        // Animación del gráfico
        pieChart.animateY(1000, Easing.EaseInOutCubic)
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}