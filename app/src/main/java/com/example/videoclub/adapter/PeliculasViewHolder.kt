package com.example.videoclub.adapter

import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.videoclub.InformacionUsuario
import com.example.videoclub.R
import com.example.videoclub.databinding.PortadaBinding

class PeliculasViewHolder(view: View) : ViewHolder(view) {
    val binding = PortadaBinding.bind(view)

    // Referencio a la imageView de portada
    val tituloPortada = view.findViewById<TextView>(R.id.ivTitulo)
    val peliculaPortada = view.findViewById<ImageView>(R.id.ivPortada)

    // Funcion que recibe los datos de la pelicula
    fun render(portadaModel: Pair<String, String>) {
        val (title, urlImagen) = portadaModel
        tituloPortada.text = title
        Glide.with(peliculaPortada.context).load(urlImagen).into(peliculaPortada)
        peliculaPortada.setOnClickListener {
            val intent = Intent(peliculaPortada.context, InformacionUsuario::class.java)
            peliculaPortada.context.startActivity(intent)
        }
    }
}

class PeliculasCarruselViewHolder(view: View) : ViewHolder(view) {
    // Referencio a la imageView de portada
    val peliculaCarrusel = view.findViewById<ImageView>(R.id.ivCarrusel)

    // Funcion que recibe los datos de la pelicula
    fun render(portadaModel: String) {
        Glide.with(peliculaCarrusel.context).load(portadaModel).into(peliculaCarrusel)
        peliculaCarrusel.setOnClickListener {
            val intent = Intent(peliculaCarrusel.context, InformacionUsuario::class.java)
            peliculaCarrusel.context.startActivity(intent)
        }
    }
}