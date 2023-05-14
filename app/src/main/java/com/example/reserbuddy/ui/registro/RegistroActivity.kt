package com.example.reserbuddy.ui.registro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import com.example.reserbuddy.ProviderType
import com.example.reserbuddy.R
import com.example.reserbuddy.databinding.ActivityLoginBinding
import com.example.reserbuddy.databinding.ActivityRegistroBinding
import com.example.reservarapp.models.Usuario
import com.example.reservarapp.viewmodels.UsuarioViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegistroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding
    private lateinit var btnRegistrar : Button
    private lateinit var etNombre : EditText
    private lateinit var etEmail : EditText
    private lateinit var etPassword1 : EditText
    private lateinit var etPassword2 : EditText
    private lateinit var etTelefono : EditText
    private lateinit var auth: FirebaseAuth;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btnRegistrar = binding.btnRegistrar
        etNombre = binding.etUsuarioNombre
        etEmail = binding.etUsuarioEmail
        etPassword1 = binding.etPassword1
        etPassword2 = binding.etPassword1
        etTelefono = binding.etUsuarioTelefono
        auth = Firebase.auth

        setup()
    }

    private fun setup() {
        btnRegistrar.setOnClickListener {

            val nombre = etNombre.text.toString().trim()
            val password1 = etPassword1.text.toString().trim()
            val password2 = etPassword2.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val telefono = etEmail.text.toString().trim()

            if(email.isNotEmpty() && password1.isNotEmpty() && password2.isNotEmpty() && nombre.isNotEmpty() && telefono.isNotEmpty()){

                // Verificar si el email es válido
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    etEmail.error = "Email inválido"
                    etEmail.requestFocus()
                    return@setOnClickListener
                }

                // Verificar si la contraseña es válida
                if(password1.length < 6 && password2.length < 6){
                    etPassword1.error = "La contraseña debe tener al menos 6 caracteres"
                    etPassword1.requestFocus()
                    return@setOnClickListener
                }

                // Verificar si las contraseñas coinciden
                if(password1 == password2){
                    etPassword1.error = "La contraseñas no coinciden"
                    etPassword2.error = "La contraseñas no coinciden"
                    etPassword1.requestFocus()
                    etPassword2.requestFocus()
                    return@setOnClickListener
                }

                auth.
                createUserWithEmailAndPassword(email, password1).addOnCompleteListener {
                    if(it.isSuccessful){



                        var userProfile = Usuario()
                        userProfile.email = email
                        userProfile.alias = nombre
                        userProfile.telefono = telefono
                        userProfile.alias = "Nuevo usuario"

                        var usuario = UsuarioViewModel()
                        usuario.addUsuario(userProfile)

                    } else{
                        showAlert()
                    }
                }
            }
        }
    }


}