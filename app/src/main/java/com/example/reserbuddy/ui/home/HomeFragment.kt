package com.example.reserbuddy.ui.home

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.reserbuddy.DataHolder
import com.example.reserbuddy.databinding.FragmentHomeBinding
import com.example.reservarapp.models.Reserva
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var auth: FirebaseAuth;

    // This property is only valid between onCreateView and
    // onDestroyView.
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

        val textView: TextView = binding.etEmailRecuperado
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val auth = Firebase.auth
//        val currentUser = auth.currentUser
//        val email = currentUser?.email
//
//
//
//        if (email != ""){
//            DataHolder.emailRecuperado = email.toString()
//        }
        binding.etEmailRecuperado.text = DataHolder.emailRecuperado










    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}