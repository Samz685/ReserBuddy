package com.example.reserbuddy.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.reserbuddy.DataHolder
import com.example.reserbuddy.R
import com.example.reserbuddy.databinding.FragmentHomeBinding
import com.example.reservarapp.viewmodels.ReservaViewModel
import com.example.reservarapp.viewmodels.TareaViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val reservaViewModel by lazy { ViewModelProvider(this).get(ReservaViewModel::class.java) }
    private val tareaViewModel by lazy { ViewModelProvider(this).get(TareaViewModel::class.java) }
    private var countReservaTotal = 0
    private var countReservaConfirmadas = 0
    private var countReservaCanceladas = 0
    private var countTareas = mutableListOf<Int>()
    private var reservasPorMes = mutableListOf<Int>()
    private lateinit var barChart: BarChart
    private lateinit var barChart2: HorizontalBarChart
    private lateinit var pieChart: PieChart




    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

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
        getChartTareas()










    }

    fun getChartTotal() {
        reservaViewModel.getChartTotal().observe(viewLifecycleOwner, Observer { reservas ->

            countReservaTotal = reservas

            inicializarChartPie()
        })
    }

    fun getChartEstado(estado : String) {
        reservaViewModel.getChartEstado(estado).observe(viewLifecycleOwner, Observer { reservas ->
            if(estado == "Confirmada"){
                countReservaConfirmadas = reservas
            } else {
                countReservaCanceladas = reservas
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

    private fun fillEntries2(): MutableList<BarEntry> {
        val nombreEstados = listOf(
            "Sin Asignar",
            "Pendientes",
            "Completadas"
        )

        val entries = mutableListOf<BarEntry>()

        for (i in 0 until countTareas.size) {
            val count = countTareas[i].toFloat()
            val entry = BarEntry(i.toFloat(), count)
            entries.add(entry)
        }


        // Asignar etiquetas personalizadas a los valores del eje x
        val xAxisLabels = nombreEstados.toTypedArray()
        val xAxisValueFormatter = IndexAxisValueFormatter(xAxisLabels)
        barChart2.xAxis.valueFormatter = xAxisValueFormatter
        barChart2.notifyDataSetChanged()

        return entries
    }





    private fun inicializarChartPie() {
        pieChart = binding.chartReservasTotal
        var pendientes = countReservaTotal-countReservaConfirmadas-countReservaCanceladas

        val entries = mutableListOf<PieEntry>()

        if (pendientes > 0) {
            entries.add(PieEntry(pendientes.toFloat(), "Pendientes"))
        }
        if (countReservaConfirmadas > 0) {
            entries.add(PieEntry(countReservaConfirmadas.toFloat(), "Confirmadas"))
        }
        if (countReservaCanceladas > 0) {
            entries.add(PieEntry(countReservaCanceladas.toFloat(), "Canceladas"))
        }
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
        pieChart.setCenterText(countReservaTotal.toString()) // Establece el número total de reservas en el centro
        pieChart.setCenterTextColor(Color.BLACK) // Establece el color del texto en el centro
        pieChart.setCenterTextSize(25f) // Establece el tamaño del texto en el centro
        pieChart.invalidate() // Actualiza el gráfico
        pieChart.setDrawEntryLabels(false)
        pieChart.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        // Animación del gráfico
        pieChart.animateY(1000, Easing.EaseInOutCubic)
    }

    fun getChartTareas() {
        tareaViewModel.getByEstadoCount().observe(viewLifecycleOwner, Observer { reservas ->

            countTareas = reservas

                inicializarChartTareas()


        })
    }

    private fun inicializarChartTareas() {
        barChart2 = binding.chartTareas
        val entries = fillEntries2()

        //Crear datos para el grafico
        val dataSet = BarDataSet(entries, " Estado Tareas")

        dataSet.valueTextSize = 18f
        dataSet.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return value.toInt().toString()
            }
        }

        val pieAzulClaro = ContextCompat.getColor(requireContext(), R.color.pieAzulClaro)
        val verdeMenta = ContextCompat.getColor(requireContext(), R.color.pieRojoCoral)
        val rojoCoral = ContextCompat.getColor(requireContext(), R.color.pieVerdeMenta)

        dataSet.colors = listOf(pieAzulClaro, verdeMenta, rojoCoral)

        //Crear objeto PieData
        val data = BarData(dataSet)

        barChart2.data = data
        barChart2.description.isEnabled = false
        barChart2.invalidate() // Actualiza el gráfico
        barChart2.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        // Animación del gráfico
        barChart2.animateY(1000, Easing.EaseInOutCubic)


    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}