package com.example.reserbuddy.ui.login

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Context
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
import com.example.reserbuddy.R
import com.example.reserbuddy.databinding.ActivityLoginBinding
import com.example.reserbuddy.ui.registro.RegistroActivity
import com.example.reservarapp.models.Usuario
import com.example.reservarapp.viewmodels.UsuarioViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class Login_Activity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var btnGoogle: Button
    private lateinit var btnIniciar: Button
    private lateinit var btnRegistrar: Button
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var auth: FirebaseAuth;
    private val GOOGLE_SIGN_IN = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btnGoogle = binding.btnIniciarGoogle
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

            if (etEmail.text.isNotEmpty() && etPassword.text.isNotEmpty()) {

                auth.signInWithEmailAndPassword(
                    etEmail.text.toString(),
                    etPassword.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {

                        FirebaseAuth.getInstance().currentUser?.let { user ->
                            FirebaseAuth.getInstance().updateCurrentUser(user)

                            // Después de la autenticación exitosa con Firebase
                            val sharedPreferences =
                                getSharedPreferences("login", Context.MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.putBoolean("isLoggedIn", true)
                            editor.apply()


                        }
                        showHome(it.result.user?.email.toString(), ProviderType.BASIC)

                    } else {
                        showAlert()
                    }
                }
            }
        }

        btnGoogle.setOnClickListener {

            //Configuración
            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            val googleClient: GoogleSignInClient = GoogleSignIn.getClient(this, googleConf)
            googleClient.signOut()
            startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)


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

    private fun showHome(email: String, provider: ProviderType) {
        val home = MainActivity()
        val bundle = Bundle()
        bundle.putString(
            "username",
            email
        ) // Suponiendo que "username" es el nombre del argumento que deseas enviar
        bundle.putString(
            "provider",
            provider.name
        ) // Suponiendo que "password" es el nombre del segundo argumento que deseas enviar
//        home.arguments = bundle

        // Después de la autenticación exitosa, navega al Fragment Home
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {

                val account = task.getResult(ApiException::class.java)

                if (account != null) {

                    val credencial = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credencial).addOnCompleteListener {

                        if (it.isSuccessful) {

                            showHome(account.email ?: "", ProviderType.GOOGLE)

                            // Después de la autenticación exitosa con Firebase
                            val sharedPreferences =
                                getSharedPreferences("login", Context.MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.putBoolean("isLoggedIn", true)
                            editor.apply()
                        } else {
                            showAlert()
                        }
                    }
                }

            } catch (e: ApiException) {
                showAlert()
            }

        }

    }
}