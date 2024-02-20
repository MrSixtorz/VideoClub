package com.example.videoclub


import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.appcompat.app.AlertDialog
import com.example.videoclub.databinding.ActivityLoginBinding
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth


class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        acceder()

    }

    private fun acceder() {
        binding.login.setOnClickListener {

            val email = binding.correo.editText?.text.toString()
            val pass = binding.pass.editText?.text.toString()

            // Check de que se ha introducido el campo correo
            if (email.isEmpty()) {
                binding.correo.error = "El correo electrónico es requerido"
                return@setOnClickListener
            }
            // Check de que se ha introducido el campo correo válido
            if (!emailValido(email)) {
                binding.correo.error = "Correo electrónico no válido"
                return@setOnClickListener
            }
            // Check de que se ha introducido el campo contraseña
            if (pass.isEmpty()) {
                binding.pass.error = "La contraseña es requerida"
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(
                email,
                pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.d(ContentValues.TAG, "Login de usuario")
                        val logueado = Intent(this, Welcome::class.java)
                        logueado.putExtra("email", email)
                        startActivity(logueado)
                    } else {
                        showAlert("Error de autentificación")
                    }
                }
        }

        binding.reg.setOnClickListener {
            val registrarse = Intent(this, Register::class.java)
            startActivity(registrarse)
        }
    }

    private fun showAlert(mensaje: String) {
        Log.d(ContentValues.TAG, "Error creando nuevo usuario")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(mensaje)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    // Función para comprobar si el email es correcto
    private fun emailValido(email: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

}