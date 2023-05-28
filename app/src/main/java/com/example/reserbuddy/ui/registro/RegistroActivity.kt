package com.example.reserbuddy.ui.registro

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.example.reserbuddy.DialogUtils
import com.example.reserbuddy.ImageController
import com.example.reserbuddy.MainActivity
import com.example.reserbuddy.R
import com.example.reserbuddy.databinding.ActivityRegistroBinding
import com.example.reservarapp.models.Usuario
import com.example.reservarapp.viewmodels.UsuarioViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging

class RegistroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding
    private lateinit var btnRegistrar : Button
    private lateinit var etNombre : EditText
    private lateinit var etEmail : EditText
    private lateinit var etPassword1 : EditText
    private lateinit var etPassword2 : EditText
    private lateinit var etTelefono : EditText
    private lateinit var ivFoto : ImageView
    private lateinit var auth: FirebaseAuth;
    private val SELECT_ACTIVITY = 50
    private var imageUri: Uri? = null
    private var foto = 0
    var token = ""

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
        ivFoto = binding.ivUsuarioFoto
        auth = Firebase.auth

        registrarToken()

        setup()
    }

    //Aqui recojo la imagen de la galeria y se la asigno al imageview de la pantalla
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when {
            requestCode == SELECT_ACTIVITY && resultCode == Activity.RESULT_OK -> {
                imageUri = data!!.data
                ivFoto.setImageURI(imageUri)

                val imageBytes = ImageController.convertImageToByteArray(contentResolver, imageUri!!)
                foto = imageBytes.hashCode() // Almacenar el hash del array de bytes como un entero
            }
        }
    }

    private fun setup() {

        //elegir imagen desde archivos del dispositivo

        ivFoto.setOnClickListener {

            ImageController.selectPhotoFromGallery(this, SELECT_ACTIVITY)

        }



        //boton registrar para crear el usuario, en usuarios y en profile_usuarios
        btnRegistrar.setOnClickListener {

            val nombre = etNombre.text.toString().trim()
            val password1 = etPassword1.text.toString().trim()
            val password2 = etPassword2.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val telefono = etTelefono.text.toString().trim()

            //Aqui uso funcion para convertir imageUri selecciona a Int (para poder asignarla al atributo de usuario)
            val fotoUsuario = R.drawable.ic_usuario


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
                if(password1 != password2){
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
                        userProfile.foto = fotoUsuario
                        userProfile.rol = "normal"
                        userProfile.token = token
                        val usuario = UsuarioViewModel()
                        usuario.addUsuario(userProfile)
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
//                        DialogUtils.showSuccessDialog(this, "Registro exitoso")

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

    fun registrarToken() {

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                token = task.result
                Log.d("FCM Token", token)
                // Aquí puedes enviar el token a tu servidor, guardar en SharedPreferences, etc.
            } else {
                Log.e("FCM Token", "Error al obtener el token")
            }
        }

    }



}