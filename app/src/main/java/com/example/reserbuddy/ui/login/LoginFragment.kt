package com.example.reserbuddy.ui.login

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.reserbuddy.DataHolder
import com.example.reserbuddy.ProviderType
import com.example.reserbuddy.R
import com.example.reserbuddy.databinding.FragmentLoginBinding
import com.example.reserbuddy.ui.reservas.ReservasFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

enum class ProviderType{

        BASIC,
        FACEBOOK,
        GOOGLE
}

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        DataHolder.currentFragment = "FragmentLogin"
        setup()




    }

    private fun setup() {

        binding.btnIniciar.setOnClickListener {

            if(binding.username.text.isEmpty() && binding.password.text.isNotEmpty()){

                FirebaseAuth.getInstance().
                createUserWithEmailAndPassword(binding.username.text.toString(),
                    binding.password.text.toString()).addOnCompleteListener {
                        if(it.isSuccessful){
                            showHome(it.result.user?.email.toString(), ProviderType.BASIC)

                        } else{
                            showAlert()
                        }
                }
            }
        }

    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Error")
        builder.setMessage("El usuario o contraseña con incorrectos")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()

    }

    private fun showHome(email: String, provider: ProviderType){
        val reservasFragment = ReservasFragment()
        val bundle = Bundle()
        bundle.putString("username", email) // Suponiendo que "username" es el nombre del argumento que deseas enviar
        bundle.putString("provider", provider.name) // Suponiendo que "password" es el nombre del segundo argumento que deseas enviar
        reservasFragment.arguments = bundle

        // Luego, navega al Fragment Home
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, reservasFragment)
            .commit()

    }


}