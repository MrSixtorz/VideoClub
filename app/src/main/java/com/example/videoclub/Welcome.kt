package com.example.videoclub


import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.example.videoclub.databinding.ActivityWelcomeBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch


class Welcome : AppCompatActivity() {

    private lateinit var saludo : TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var database : FirebaseDatabase
    private lateinit var usuariosRef : DatabaseReference
    private lateinit var binding: ActivityWelcomeBinding

    @SuppressLint("SetTextI18n", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var avatar: ImageView = binding.avatar
        auth = Firebase.auth
        database = FirebaseDatabase.getInstance()
        usuariosRef = database.getReference("usuarios")
        saludo = binding.welcome
        var servicio = RetrofitServiceFactory.makeRetrofitService()

        lifecycleScope.launch{
            val discMovies = servicio.listPopularMovies("30d6404fc8d3086411532ee450413606", "EU")
        }

        obtenerNombre { nombreUsuario, nombreImagen ->
            saludo.text = "Hola, $nombreUsuario"
            val imagenResourceId = resources.getIdentifier(nombreImagen, "drawable", packageName)
            avatar.setImageResource(imagenResourceId)

        }

        binding.desloguear.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    // callback es una función lambda que se ejecuta una vez que se realiza la lectura de datos
    // unit devolverá el resultado de lo leído
    private fun obtenerNombre(callback: (String, String?) -> Unit) {
        val usuarioActual: FirebaseUser? = auth.currentUser

        if (usuarioActual != null) {
            val uidUsuario = usuarioActual.uid
            // Leemos a través del dataSnapshot el nombre del usuario actual
            usuariosRef.child(uidUsuario).child("perfil").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Guardamos el nombre y el nombre de la imagen en variables
                    val nombreUsuario = dataSnapshot.child("nombre").getValue(String::class.java)
                    val nombreImagen = dataSnapshot.child("avatar").getValue(String::class.java)

                    if (nombreUsuario != null && nombreUsuario.isNotEmpty()) {
                        // Devolvemos el nombre de usuario y el nombre de la imagen
                        callback(nombreUsuario, nombreImagen)
                    } else {
                        Log.d("Firebase", "El nombre no está configurado en la base de datos")
                        // Si fuera nulo o estuviera en blanco devolvemos el siguiente mensaje
                        callback("Desconocido", null)
                    }
                }

                // Por si hubiera un error, estamos obligados hacer esta función al hacer addListenerForSingleValueEvent
                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("Firebase", "Error al leer el nombre desde la base de datos: ${databaseError.message}")
                    callback("Error al leer el nombre", null)
                }
            })
        } else {
            Log.d("Firebase", "El usuario no está autenticado")
            callback("Usuario no autenticado", null)
        }
    }


}