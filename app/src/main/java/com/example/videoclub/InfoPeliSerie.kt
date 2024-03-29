package com.example.videoclub

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.videoclub.databinding.ActivityInfoPeliSerieBinding

class InfoPeliSerie : AppCompatActivity() {
    private lateinit var binding : ActivityInfoPeliSerieBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoPeliSerieBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val peliserie = intent.getParcelableExtra<PeliSerie>("peliserie")


        obtenerNombre { nombreUsuario, nombreImagen ->
            binding.welcome.text = "Hola, $nombreUsuario"
            val imagenResourceId = resources.getIdentifier(nombreImagen, "drawable", packageName)
            binding.avatar.setImageResource(imagenResourceId)
        }

        binding.Marca.setOnClickListener{
            val intent = Intent (this,Welcome::class.java)
            startActivity(intent)
        }

        binding.titulo.text = peliserie!!.title
        Glide.with(this).load(peliserie.poster_path).into(binding.poster)
        binding.sinopsis.text = peliserie!!.overview
        Glide.with(this).load(peliserie.backdrop_path).into(binding.trailer)
        binding.trailer.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(peliserie.trailer)
            startActivity(intent)
        }
        binding.avatar.setOnClickListener{
            val intent = Intent(this, Usuario::class.java)
            startActivity(intent)
        }

    }
}