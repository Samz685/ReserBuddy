package com.example.reserbuddy.ui.reservas


import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
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
import com.example.reserbuddy.R
import com.example.reserbuddy.adapters.OnItemClickListener
import com.example.reserbuddy.adapters.ReservaAdapter
import com.example.reserbuddy.databinding.FragmentReservasBinding
import com.example.reserbuddy.CurrentFragment
import com.example.reservarapp.models.Reserva
import com.example.reservarapp.viewmodels.ReservaViewModel
import com.example.reservarapp.viewmodels.UsuarioViewModel
import java.text.SimpleDateFormat
import java.util.*


class ReservasFragment : Fragment() {

    private var _binding: FragmentReservasBinding? = null
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
    private val usuarioViewModel by lazy { ViewModelProvider(this).get(UsuarioViewModel::class.java) }


    private val binding get() = _binding!!

    override fun onResume() {
        super.onResume()
        traerReservas()
        true
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentReservasBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CurrentFragment.currentFragment = "FragmentReservas"
        getAllUsuarios()

        datePickerDialog = DatePickerDialog(requireContext())
        swipeRefresh = binding.swipeRefresh

        listaReservas = mutableListOf<Reserva>()
        contador = binding.tvContadorReservas
        tvMensajeReserva = binding.tvMensajeReserva


        getReservasToday()
        inicializarAdapters()

        refreshReservas()



        mensajeListaVacia()

        binding.btnHoy.setOnClickListener {
            getReservasToday()
            reservaTiming = "Hoy"

        }

        binding.btnSemana.setOnClickListener {
            getReservasWeek()
            reservaTiming = "Semana"
        }

        binding.btnMes.setOnClickListener {
            getReservasMonth()
            reservaTiming = "Mes"
        }

        binding.btnPeriodo.setOnClickListener {
            //getReservasPeriod se ejecuta dentro del la función elegirFecha
            elegirFecha()
            reservaTiming = "Periodo"

        }

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
            override fun OnItemClick(vista: View, position: Int) {

                val expandible_reserva: LinearLayout = vista.findViewById(R.id.expandible_reserva)

                if (expandible_reserva.visibility == VISIBLE) {
                    expandible_reserva.visibility = GONE
                } else {
                    expandible_reserva.visibility = VISIBLE
                }

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

    fun getAllUsuarios() {
        usuarioViewModel.getAll().observe(viewLifecycleOwner, Observer {
            CurrentFragment.listaUsuariosTemp.clear()
            for (usuario in it) {
                CurrentFragment.listaUsuariosTemp.add(usuario)
            }



        })
    }


}