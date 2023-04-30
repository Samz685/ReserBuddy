package com.example.reserbuddy.ui.reservas


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
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
import com.example.reservarapp.models.Grupo
import com.example.reservarapp.models.Reserva
import com.example.reservarapp.viewmodels.ReservaViewModel


class ReservasFragment : Fragment() {

    private var _binding: FragmentReservasBinding? = null

    private lateinit var listaReservas: MutableList<Reserva>
    lateinit var mAdapter: RecyclerView.Adapter<ReservaAdapter.ViewHolder>
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private var contador:Int=0
    private lateinit var mRecyclerView: RecyclerView
    private val reservaViewModel by lazy { ViewModelProvider(this).get(ReservaViewModel::class.java) }
    private lateinit var grupoActual : Grupo
    private lateinit var swipeRefresh : SwipeRefreshLayout



    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onResume() {
        super.onResume()
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


        return root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipeRefresh = binding.swipeRefresh

        listaReservas = mutableListOf<Reserva>()
        grupoActual = Grupo()
        grupoActual.id = "El Pikon"

        getReservasByGroup()
        inicializarAdapters()

        refreshReservas()

        binding.btnHoy.setOnClickListener {
            listaReservas.clear()
            getReservasToday()
        }

        binding.btnSemana.setOnClickListener {
            listaReservas.clear()
            getReservasWeek()
        }


    }

    fun refreshReservas() {
        swipeRefresh.setOnRefreshListener {
//            getReservasByGroup()
            getReservasToday()
            swipeRefresh.isRefreshing = false

        }
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

                if (expandible_reserva.visibility == View.VISIBLE) {
                    expandible_reserva.visibility = View.GONE
                } else {
                    expandible_reserva.visibility = View.VISIBLE
                }

            }

            override fun onImageClick(position: Int) {


            }


        },reservaViewModel)
        mRecyclerView.adapter=mAdapter
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.itemAnimator= DefaultItemAnimator()
        mRecyclerView.layoutManager=mLayoutManager
    }

    fun getReservasByGroup() {
        reservaViewModel.getByGroup(grupoActual).observe(viewLifecycleOwner, Observer {
            listaReservas.clear()
            for( reserva in it){
                listaReservas.add(reserva)
            }


            mAdapter.notifyDataSetChanged()
        })
    }

    fun getReservasToday() {
        reservaViewModel.getToday().observe(viewLifecycleOwner, Observer {
            listaReservas.clear()
            for( reserva in it){
                listaReservas.add(reserva)
            }


            mAdapter.notifyDataSetChanged()
        })
    }

    fun getReservasWeek() {
        reservaViewModel.getWeek().observe(viewLifecycleOwner, Observer {
            listaReservas.clear()
            for( reserva in it){
                listaReservas.add(reserva)
            }


            mAdapter.notifyDataSetChanged()
        })
    }








}