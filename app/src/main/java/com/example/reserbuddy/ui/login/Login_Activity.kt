package com.example.reserbuddy.ui.login

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import com.example.reserbuddy.DataHolder
import com.example.reserbuddy.MainActivity
import com.example.reserbuddy.ProviderType
import com.example.reserbuddy.databinding.ActivityLoginBinding
import com.example.reserbuddy.ui.registro.RegistroActivity
import com.example.reservarapp.models.Usuario
import com.example.reservarapp.viewmodels.UsuarioViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase





class Login_Activity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var btnIniciar : Button
    private lateinit var btnRegistrar : Button
    private lateinit var etEmail : EditText
    private lateinit var etPassword : EditText
    private lateinit var auth: FirebaseAuth;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btnIniciar = binding.btnIniciar
        btnRegistrar = binding.btnRegistrarte
        etEmail = binding.username
        etPassword = binding.password
        auth = Firebase.auth

        setup()
    }

    private fun setup() {

        btnRegistrar.setOnClickListener {

            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)

        }


        btnIniciar.setOnClickListener {

            if(etEmail.text.isNotEmpty() && etPassword.text.isNotEmpty()){

                auth.
                signInWithEmailAndPassword(etEmail.text.toString(),
                    etPassword.text.toString()).addOnCompleteListener {
                    if(it.isSuccessful){

                        FirebaseAuth.getInstance().currentUser?.let { user ->
                            FirebaseAuth.getInstance().updateCurrentUser(user)
                        }
                        showHome(it.result.user?.email.toString(), ProviderType.BASIC)

                    } else{
                        showAlert()
                    }
                }
            }
        }



    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("El usuario o contraseña con incorrectos")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()

    }

    private fun showHome(email: String, provider: ProviderType){
        val home = MainActivity()
        val bundle = Bundle()
        bundle.putString("username", email) // Suponiendo que "username" es el nombre del argumento que deseas enviar
        bundle.putString("provider", provider.name) // Suponiendo que "password" es el nombre del segundo argumento que deseas enviar
//        home.arguments = bundle

        // Después de la autenticación exitosa, navega al Fragment Home
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)


    }

}