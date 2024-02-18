package com.example.videoclub



import com.google.firebase.auth.auth
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.videoclub.adapter.PeliculasAdapter
import com.example.videoclub.databinding.ActivityMainBinding
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth



class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        acceder()

    }

    private fun acceder(){

        val email : TextInputLayout = binding.correo
        val pass : TextInputLayout = binding.pass

        binding.login.setOnClickListener{
            if (email.editText?.text?.isNotEmpty() == true && pass.editText?.text?.isNotEmpty() == true){

                auth.signInWithEmailAndPassword(
                    email.editText?.text.toString(),
                    pass.editText?.text.toString()).addOnCompleteListener {
                    if (it.isSuccessful){
                        Log.d(ContentValues.TAG, "Login de usuario")
                        val logueado = Intent (this, Welcome::class.java)
                        logueado.putExtra("email",email.editText?.text?.toString())
                        startActivity(logueado)
                    } else {
                        showAlert()
                    }
                }
            }
        }
        binding.reg.setOnClickListener {
            val registrarse = Intent (this, Register::class.java)
            startActivity(registrarse)
        }
    }

    private fun showAlert(){
        Log.d(ContentValues.TAG, "Error creando nuevo usuario")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error en el login de usuario")
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

}