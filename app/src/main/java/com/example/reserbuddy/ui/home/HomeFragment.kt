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
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.firebase.auth.FirebaseAuth
import java.util.ArrayList
import java.util.LinkedList

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var auth: FirebaseAuth;
    private val reservaViewModel by lazy { ViewModelProvider(this).get(ReservaViewModel::class.java) }
    private var countTotal = 0
    private var countConfirmadas = 0
    private var countCanceladas = 0

    private var reservasPorMes = mutableListOf<Int>()
    private lateinit var barChart: BarChart
    private lateinit var pieChart: PieChart



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

        getChartYear(2023)













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



    fun getChartYear(year: Int) {
        reservaViewModel.getChartYear(year).observe(viewLifecycleOwner, Observer { reservas ->

            reservasPorMes = reservas
            inicializarChartBars()
        })


    }

    fun inicializarChartBars() {

        barChart = binding.chartReservasMes
        val entries = fillEntries()

        //Crear datos para el grafico
        val dataSet = BarDataSet(entries, "Reservas por mes")

        dataSet.valueTextSize = 18f
        dataSet.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return value.toInt().toString()
            }
        }

        //Crear objeto PieData
        val data = BarData(dataSet)

        barChart.data = data
        barChart.description.isEnabled = false
        barChart.invalidate() // Actualiza el gráfico
        barChart.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        // Animación del gráfico
        barChart.animateY(1000, Easing.EaseInOutCubic)


    }

    private fun fillEntries(): MutableList<BarEntry> {
        val nombreMeses = listOf(
            "Ene",
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

        val entries = mutableListOf<BarEntry>()

        for (i in 0 until reservasPorMes.size) {
            val count = reservasPorMes[i].toFloat()
            val entry = BarEntry(i.toFloat(), count)
            entries.add(entry)
        }

        // Asignar etiquetas personalizadas a los valores del eje x
        val xAxisLabels = nombreMeses.toTypedArray()
        val xAxisValueFormatter = IndexAxisValueFormatter(xAxisLabels)
        barChart.xAxis.valueFormatter = xAxisValueFormatter

        return entries
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