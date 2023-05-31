package com.example.reserbuddy.ui.usuarios

import android.graphics.Color
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.LinearLayout
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
import com.example.reserbuddy.adapters.TareaAdapterBasico
import com.example.reserbuddy.databinding.FragmentDetalleClienteBinding
import com.example.reserbuddy.databinding.FragmentDetalleUsuarioBinding
import com.example.reserbuddy.ui.newTarea.NewTareaFragment
import com.example.reservarapp.models.Reserva
import com.example.reservarapp.models.Tarea
import com.example.reservarapp.viewmodels.ClienteViewModel
import com.example.reservarapp.viewmodels.ReservaViewModel
import com.example.reservarapp.viewmodels.TareaViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class DetalleUsuarioFragment : Fragment() {

    private var _binding: FragmentDetalleUsuarioBinding? = null
    private lateinit var listaTareasTotales: MutableList<Tarea>
    lateinit var mAdapter: RecyclerView.Adapter<TareaAdapterBasico.ViewHolder>
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private lateinit var mRecyclerView: RecyclerView
    private val tareaViewModel by lazy { ViewModelProvider(this).get(TareaViewModel::class.java) }
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var ivFoto : ImageView
    private lateinit var tvNombre : TextView
    private lateinit var tvTelefono : TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvComentario: TextView
    private lateinit var pieChart: PieChart
    private var countConfirmadas : Int = 0
    private var countCanceladas : Int = 0
    private var countTotal : Int = 0
    private lateinit var expandibleCliente: LinearLayout
    private var isExpanded = false



    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentDetalleUsuarioBinding.inflate(inflater, container, false)
        val root: View = binding.root



        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listaTareasTotales = mutableListOf<Tarea>()

        getTareasByCliente(DataHolder.currentCliente.telefono)
        getTareasByClienteByEstado(DataHolder.currentCliente.telefono, "Confirmada")
        getTareasByClienteByEstado(DataHolder.currentCliente.telefono, "Cancelada")

        inicializarAdapters()

        inicializarDetalles()

        iniciarExpandible()



        binding.ivEditarUs.setOnClickListener {



            val scaleAnimation = ScaleAnimation(1f, 1.2f, 1f, 1.2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
            scaleAnimation.duration = 200
            it.startAnimation(scaleAnimation)

//            EditarUsuarioFragment().show(childFragmentManager, "EditarClienteFragment")
        }








    }

    fun iniciarExpandible() {
        val expandibleCliente = binding.expandibleUsuario
        val transition = AutoTransition()
        transition.duration = 150 // Duración de la animación en milisegundos

        binding.bloqueDetalleUsuario.setOnClickListener {
            isExpanded = !isExpanded

            if (isExpanded) {
                expandibleCliente.visibility = View.VISIBLE
            } else {
                expandibleCliente.visibility = View.GONE
            }

            TransitionManager.beginDelayedTransition(binding.root as ViewGroup, transition)
        }
    }



    fun inicializarDetalles(){

        var usuario = DataHolder.currentUser

        ivFoto = binding.ivDetalleUsuario
        tvNombre = binding.tvDetalleUsuarioNombre
        tvTelefono = binding.tvDetalleUsuarioTel
        tvEmail = binding.tvDetalleUsuarioEmail
        tvComentario = binding.tvComentarioUsuario

        ivFoto.setImageResource(usuario.foto)
        tvNombre.text = usuario.alias
        tvTelefono.text = usuario.telefono
        tvEmail.text = usuario.email
        tvComentario.text = usuario.comentario


    }
    fun refreshUsuarios() {
        swipeRefresh.setOnRefreshListener {
            getTareasByCliente(DataHolder.currentCliente.telefono)
            swipeRefresh.isRefreshing = false

        }
    }

    fun inicializarChart(){

        pieChart = binding.chartUsuario

        var countPendientes = countTotal-countConfirmadas-countCanceladas

        val entries = mutableListOf<PieEntry>()

        if (countPendientes > 0) {
            entries.add(PieEntry(countPendientes.toFloat(), "Pendientes"))
        }
        if (countConfirmadas > 0) {
            entries.add(PieEntry(countConfirmadas.toFloat(), "Confirmadas"))
        }
        if (countCanceladas > 0) {
            entries.add(PieEntry(countCanceladas.toFloat(), "Canceladas"))
        }
        //Crear datos para el grafico
        val dataSet = PieDataSet(entries, "")


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

    private fun inicializarAdapters() {


        mRecyclerView = binding.recyclerTareas2
        mLayoutManager = LinearLayoutManager(activity)
        mAdapter = TareaAdapterBasico(listaTareasTotales, object : OnItemClickListener {
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

    fun getTareasByCliente(usuario: String) {
        tareaViewModel.getByUsuario(usuario).observe(viewLifecycleOwner, Observer { reservas ->
            listaTareasTotales.clear()
            listaTareasTotales.addAll(reservas)
            countTotal = listaTareasTotales.size

            mAdapter.notifyDataSetChanged()
            inicializarChart()
        })
    }

    fun getTareasByClienteByEstado(numCliente: String, estado: String) {
        tareaViewModel.getByUsuarioEstado(numCliente, estado).observe(viewLifecycleOwner, Observer { reservas ->
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