package com.example.videoclub

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inicio)

        auth = Firebase.auth
        // Evento personalizado para Google Analytics
        val analytics : FirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("message","Integración de mensaje completa")
        analytics.logEvent("InitScreen", bundle)

        acceder()

    }

    private fun acceder(){

        val login : Button = findViewById(R.id.login)
        val registrar : Button = findViewById(R.id.reg)
        val email : EditText = findViewById(R.id.correo)
        val pass : EditText = findViewById(R.id.pass)

        login.setOnClickListener{
            if (email.text.isNotEmpty() && pass.text.isNotEmpty()){

                auth.signInWithEmailAndPassword(email.text.toString(),
                    pass.text.toString()).addOnCompleteListener {
                    if (it.isSuccessful){
                        Log.d(ContentValues.TAG, "Login de usuario")
                        val logueado = Intent (this, Welcome::class.java)
                        logueado.putExtra("email",email.text.toString())
                        startActivity(logueado)
                    } else {
                        showAlert()
                    }
                }
            }
        }
        registrar.setOnClickListener {
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