package com.example.videoclub

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.videoclub.adapter.PeliculasAdapter
import com.example.videoclub.adapter.PeliculasCarruselAdapter
import com.example.videoclub.databinding.ActivityWelcomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Welcome : AppCompatActivity() {

    private lateinit var saludo: TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var usuariosRef: DatabaseReference
    private lateinit var binding: ActivityWelcomeBinding


    @SuppressLint("SetTextI18n", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        var avatar: ImageView = binding.avatar
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        usuariosRef = database.getReference("usuarios")
        saludo = binding.welcome
        val refPelicula = "peliculas/results"
        val refSerie = "series/results"

        obtenerPortadas(refPelicula)
        obtenerCarrusel(refPelicula)

        obtenerNombre { nombreUsuario, nombreImagen ->
            saludo.text = "Hola, $nombreUsuario"
            val imagenResourceId = resources.getIdentifier(nombreImagen, "drawable", packageName)
            avatar.setImageResource(imagenResourceId)

        }

        binding.peliculas.setOnClickListener {
            obtenerPortadas(refPelicula)
            obtenerCarrusel(refPelicula)
        }

        binding.series.setOnClickListener {
            obtenerPortadas(refSerie)
            obtenerCarrusel(refSerie)
        }

        binding.avatar.setOnClickListener() {
            val intent = Intent(this, InformacionUsuario::class.java)
            startActivity(intent)
        }
        binding.desloguear.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        binding.izquierda.setOnClickListener {
            // Desplazar 200dp a la izquierda
            binding.carrusel.smoothScrollBy(-convertirDPaPX(200), 0)
        }

        binding.derecha.setOnClickListener {
            // Desplazar 200dp a la derecha
            binding.carrusel.smoothScrollBy(convertirDPaPX(200), 0)
        }

    }
    private fun initRecyclerViewPortada(lista: ArrayList<PeliSerie>) {
        binding.portada.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.portada.adapter = PeliculasAdapter(lista)
    }
    private fun initRecyclerViewCarrusel(lista: ArrayList<PeliSerie>) {
        binding.carrusel.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.carrusel.adapter = PeliculasCarruselAdapter(lista)
    }
    private fun convertirDPaPX(dp: Int): Int {
        val escala = resources.displayMetrics.density
        return (dp * escala + 0.5f).toInt()
    }
    fun obtenerPortadas(referencePath: String) {
        val reference = FirebaseDatabase.getInstance().getReference(referencePath)
        val portadas = ArrayList<PeliSerie>()

        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Escogemos 3 peliseries de la lista
                val portadasAleatorias = snapshot.children.shuffled().take(3)
                portadasAleatorias.forEach { portadaSnapshot ->

                    val backdropPath = portadaSnapshot.child("backdrop_path").getValue(String::class.java)
                    val id = portadaSnapshot.child("id").getValue(Int::class.java)
                    val overview = portadaSnapshot.child("overview").getValue(String::class.java)
                    val posterPath = portadaSnapshot.child("poster_path").getValue(String::class.java)
                    val title = portadaSnapshot.child("title").getValue(String::class.java)

                    backdropPath?.let {
                        val portada = PeliSerie(backdropPath, id ?: 0, overview ?: "", posterPath ?: "", title ?: "")
                        portadas.add(portada)
                    }
                }

                initRecyclerViewPortada(portadas)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Welcome,"Error de conexion",Toast.LENGTH_SHORT).show()
            }
        })
    }


    fun obtenerCarrusel(referencePath: String) {
        val reference = FirebaseDatabase.getInstance().getReference(referencePath)
        val posters = ArrayList<PeliSerie>()

        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (portadaSnapshot in snapshot.children) {

                    val backdropPath = portadaSnapshot.child("backdrop_path").getValue(String::class.java)
                    val id = portadaSnapshot.child("id").getValue(Int::class.java)
                    val overview = portadaSnapshot.child("overview").getValue(String::class.java)
                    val posterPath = portadaSnapshot.child("poster_path").getValue(String::class.java)
                    val title = portadaSnapshot.child("title").getValue(String::class.java)

                    backdropPath?.let {
                        val portada = PeliSerie(backdropPath, id ?: 0, overview ?: "", posterPath ?: "", title ?: "")
                        posters.add(portada)
                    }
                }

                initRecyclerViewCarrusel(posters)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Welcome,"Error de conexion",Toast.LENGTH_SHORT).show()
            }
        })
    }

}



