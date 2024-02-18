package com.example.videoclub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.videoclub.databinding.ActivityInfoPeliSerieBinding

class InfoPeliSerie : AppCompatActivity() {
    private lateinit var binding : ActivityInfoPeliSerieBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoPeliSerieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        obtenerNombre { nombreUsuario, nombreImagen ->
            binding.welcome.text = "Hola, $nombreUsuario"
            val imagenResourceId = resources.getIdentifier(nombreImagen, "drawable", packageName)
            binding.avatar.setImageResource(imagenResourceId)

        }

        binding.Marca.setOnClickListener{
            val intent = Intent (this,Welcome::class.java)
            startActivity(intent)
        }
    }
}