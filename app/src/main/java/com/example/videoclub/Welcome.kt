package com.example.videoclub

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.videoclub.adapter.PeliculasAdapter
import com.example.videoclub.adapter.PeliculasCarruselAdapter
import com.example.videoclub.databinding.ActivityWelcomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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
            // Desplazar 100dp hacia la izquierda
            binding.carrusel.smoothScrollBy(-convertirDPaPX(200), 0)
        }

        binding.derecha.setOnClickListener {
            // Desplazar 100dp hacia la derecha
            binding.carrusel.smoothScrollBy(convertirDPaPX(200), 0)
        }

    }
    private fun initRecyclerViewPortada(listaImagenes: ArrayList<Pair<String, String>>) {
        binding.portada.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.portada.adapter = PeliculasAdapter(listaImagenes)
    }
    private fun initRecyclerViewCarrusel(listaImagenes: List<String>) {
        binding.carrusel.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.carrusel.adapter = PeliculasCarruselAdapter(listaImagenes)
    }
    private fun convertirDPaPX(dp: Int): Int {
        val escala = resources.displayMetrics.density
        return (dp * escala + 0.5f).toInt()
    }
    fun obtenerPortadas(referencePath: String) {
        val reference = FirebaseDatabase.getInstance().getReference(referencePath)
        val portadas = ArrayList<Pair<String, String>>() // Una lista de pares (title, urlImagen)

        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Escogemos tres portadas aleatorias, con shuffle "las barajamos"
                val portadasAleatorias = snapshot.children.shuffled().take(3)
                for (peliculaSnapshot in portadasAleatorias) {
                    val backdropPath = peliculaSnapshot.child("backdrop_path").getValue(String::class.java)
                    val title = peliculaSnapshot.child("title").getValue(String::class.java)
                    backdropPath?.let {
                        val urlImagen = "https://image.tmdb.org/t/p/original$backdropPath"
                        portadas.add(Pair(title, urlImagen) as Pair<String, String>)
                    }
                }
                initRecyclerViewPortada(portadas)
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar el error si es necesario
            }
        })
    }
    fun obtenerCarrusel(referencePath: String){
        val reference = FirebaseDatabase.getInstance().getReference(referencePath)
        val carruselImagenes = ArrayList<String>()

        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (peliculaSnapshot in snapshot.children) {
                    val posterPath = peliculaSnapshot.child("poster_path").getValue(String::class.java)
                    posterPath?.let {
                        val urlImagen = "https://image.tmdb.org/t/p/original$posterPath"
                        carruselImagenes.add(urlImagen)
                    }
                }
                initRecyclerViewCarrusel(carruselImagenes)
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}



