package com.example.reserbuddy.ui.reservas


import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.reserbuddy.DataHolder
import com.example.reserbuddy.R
import com.example.reserbuddy.adapters.OnItemClickListener
import com.example.reserbuddy.adapters.ReservaAdapter
import com.example.reserbuddy.databinding.FragmentReservasBinding
import com.example.reservarapp.models.Reserva
import com.example.reservarapp.viewmodels.ReservaViewModel
import com.example.reservarapp.viewmodels.UsuarioViewModel
import com.google.android.material.tabs.TabLayout
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import java.text.SimpleDateFormat
import java.util.*


class ReservasFragment : Fragment() {

    private var _binding: FragmentReservasBinding? = null
    private val binding get() = _binding!!
    private lateinit var datePickerDialog: DatePickerDialog
    private lateinit var listaReservas: MutableList<Reserva>
    lateinit var mAdapter: RecyclerView.Adapter<ReservaAdapter.ViewHolder>
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private lateinit var mRecyclerView: RecyclerView
    private val reservaViewModel by lazy { ViewModelProvider(this).get(ReservaViewModel::class.java) }
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private var reservaTiming = ""
    private var fechaInicial = ""
    private var fechaFinal = ""
    private lateinit var contador : TextView
    private lateinit var tvMensajeReserva : TextView
    private lateinit var tabLayout: TabLayout



    override fun onResume() {
        super.onResume()
        traerReservas()
        mAdapter.notifyDataSetChanged()
        true
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentReservasBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //Aqui se recogen los datos del login
        val username = arguments?.getString("username")
        val password = arguments?.getString("password")

        return root


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        tabLayout = binding.tabReservas
        setupTabs()

        DataHolder.currentFragment = "FragmentReservas"

        datePickerDialog = DatePickerDialog(requireContext())
        swipeRefresh = binding.swipeRefresh

        listaReservas = mutableListOf<Reserva>()
        contador = binding.tvContadorReservas
        tvMensajeReserva = binding.tvMensajeReserva


        getReservasToday()
        inicializarAdapters()
        refreshReservas()
        mensajeListaVacia()


    }



    private fun mensajeListaVacia() {
        if (listaReservas.size == 0) {
            tvMensajeReserva.visibility = VISIBLE
        } else {
            tvMensajeReserva.visibility = GONE
        }
    }

    fun refreshReservas() {
        swipeRefresh.setOnRefreshListener {
            traerReservas()
            swipeRefresh.isRefreshing = false


        }
    }

    private fun traerReservas() {

        when (reservaTiming) {
            "Hoy" -> getReservasToday()
            "Semana" -> getReservasWeek()
            "Mes" -> getReservasMonth()
            "Periodo" -> getReservasPeriod()
            else -> getReservasToday()
        }
        resetearContador()


    }

    private fun resetearContador(){
        contador.text = listaReservas.size.toString()
        mensajeListaVacia()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun inicializarAdapters() {


        mRecyclerView = binding.recyclerReservas
        mLayoutManager = LinearLayoutManager(activity)
        mAdapter = ReservaAdapter(listaReservas, object : OnItemClickListener {

            private var posicionExpandida = RecyclerView.NO_POSITION

            override fun OnItemClick(vista: View, posicion: Int) {
                val expandible_reserva: LinearLayout = vista.findViewById(R.id.expandible_reserva)

                // Utiliza TransitionManager para animar el cambio de visibilidad
                TransitionManager.beginDelayedTransition(mRecyclerView)

                if (posicion == posicionExpandida) {
                    // Al hacer clic en el elemento ya expandido, se colapsa
                    expandible_reserva.visibility = View.GONE
                    posicionExpandida = RecyclerView.NO_POSITION
                } else {
                    // Colapsa el elemento previamente expandido (si hay alguno)
                    if (posicionExpandida != RecyclerView.NO_POSITION) {
                        val vistaExpandidaPrevia = mLayoutManager.findViewByPosition(posicionExpandida)
                        val expandibleReservaPrevia: LinearLayout? = vistaExpandidaPrevia?.findViewById(R.id.expandible_reserva)
                        expandibleReservaPrevia?.visibility = View.GONE
                    }

                    // Expande el elemento clicado
                    expandible_reserva.visibility = View.VISIBLE
                    posicionExpandida = posicion
                }
            }

            override fun onClick2(position: Int) {
                var reserva = listaReservas[position]
                reserva.estado = "Confirmada"
                reservaViewModel.updateReserva(reserva)
                mAdapter.notifyDataSetChanged()

            }

            override fun onClick3(position: Int) {
                var reserva = listaReservas[position]
                reserva.estado = "Cancelada"
                reservaViewModel.updateReserva(reserva)
                mAdapter.notifyDataSetChanged()



            }

            override fun onImageClick(position: Int) {


            }


        }, reservaViewModel)
        mRecyclerView.adapter = mAdapter
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.itemAnimator = DefaultItemAnimator()
        mRecyclerView.layoutManager = mLayoutManager
    }


    fun getReservasToday() {
        reservaViewModel.getToday().observe(viewLifecycleOwner, Observer {
            listaReservas.clear()
            for (reserva in it) {
                listaReservas.add(reserva)
            }


            resetearContador()
            mAdapter.notifyDataSetChanged()
        })
        reservaTiming = "Hoy"
    }

    fun getReservasWeek() {
        reservaViewModel.getWeek().observe(viewLifecycleOwner, Observer {
            listaReservas.clear()
            for (reserva in it) {
                listaReservas.add(reserva)
            }

            resetearContador()
            mAdapter.notifyDataSetChanged()
        })
        reservaTiming = "Semana"
    }

    fun getReservasMonth() {
        reservaViewModel.getMonth().observe(viewLifecycleOwner, Observer {
            listaReservas.clear()
            for (reserva in it) {
                listaReservas.add(reserva)
            }
            resetearContador()
            mAdapter.notifyDataSetChanged()
        })
        reservaTiming = "Mes"
    }

    fun getReservasPeriod() {
        reservaViewModel.getPeriod(fechaInicial, fechaFinal).observe(viewLifecycleOwner, Observer {
            listaReservas.clear()
            for (reserva in it) {
                listaReservas.add(reserva)
            }
            resetearContador()
            mAdapter.notifyDataSetChanged()
        })
        reservaTiming = "Periodo"
    }


    private fun elegirFecha() {
        val startDatePicker = DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                val startDate = Calendar.getInstance().apply {
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, month)
                    set(Calendar.DAY_OF_MONTH, day)
                }
                val endDatePicker = DatePickerDialog(
                    requireContext(),
                    { _, year, month, day ->
                        val endDate = Calendar.getInstance().apply {
                            set(Calendar.YEAR, year)
                            set(Calendar.MONTH, month)
                            set(Calendar.DAY_OF_MONTH, day)
                        }
                        fechaInicial = SimpleDateFormat(
                            "yyyy-MM-dd",
                            Locale.getDefault()
                        ).format(startDate.time)
                        fechaFinal =
                            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(endDate.time)
                        getReservasPeriod()
                    },
                    year,
                    month,
                    day
                )
                endDatePicker.show()
            },
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        )
        startDatePicker.show()
    }

    private fun setupTabs() {
        tabLayout.addTab(tabLayout.newTab().setText(R.string.hoy))
        tabLayout.addTab(tabLayout.newTab().setText(R.string.semana))
        tabLayout.addTab(tabLayout.newTab().setText(R.string.mes))
        tabLayout.addTab(tabLayout.newTab().setText(R.string.periodo))

        // Cambiar la lista de reservas según la pestaña seleccionada
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> getReservasToday()
                    1 -> getReservasWeek()
                    2 -> getReservasMonth()
                    3 -> elegirFecha()

                    else -> getReservasToday()
                }
                mRecyclerView.adapter?.notifyDataSetChanged()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }





}