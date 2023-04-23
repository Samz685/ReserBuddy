package com.example.reserbuddy.ui.reservas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reserbuddy.adapters.OnItemClickListener
import com.example.reserbuddy.adapters.ReservaAdapter
import com.example.reserbuddy.databinding.FragmentReservasBinding
import com.example.reservarapp.models.Grupo
import com.example.reservarapp.models.Reserva
import com.example.reservarapp.viewmodels.ReservaViewModel
import com.example.reservarapp.viewmodels.SolicitudViewModel

class ReservasFragment : Fragment() {

    private var _binding: FragmentReservasBinding? = null

    private lateinit var listaReservas: MutableList<Reserva>
    private lateinit var mAdapter: RecyclerView.Adapter<ReservaAdapter.ViewHolder>
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private var contador:Int=0
    private lateinit var mRecyclerView: RecyclerView
    private val reservaViewModel by lazy { ViewModelProvider(this).get(ReservaViewModel::class.java) }
    private lateinit var grupoActual : Grupo


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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

        listaReservas = mutableListOf<Reserva>()
        grupoActual = Grupo()
        grupoActual.id = "El Pikon"

        getReservasByGroup()
        inicializarAdapters()


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


            }

            override fun onImageClick(position: Int) {

            }


        },reservaViewModel)
        mRecyclerView.adapter=mAdapter
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.itemAnimator= DefaultItemAnimator()
        mRecyclerView.layoutManager=mLayoutManager
    }

    private fun getReservasByGroup() {
        reservaViewModel.getByGroup(grupoActual).observe(viewLifecycleOwner, Observer {
//            listaReservas.clear()
            for( reserva in it){
                listaReservas.add(reserva)
            }

            mAdapter.notifyDataSetChanged()
        })
    }
}