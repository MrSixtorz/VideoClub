package com.example.videoclub.adapter

import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.videoclub.InfoPeliSerie
import com.example.videoclub.PeliSerie
import com.example.videoclub.R
import com.example.videoclub.databinding.PortadaBinding

class PeliculasViewHolder(view: View) : ViewHolder(view) {
    val binding = PortadaBinding.bind(view)

    // Referencio a la imageView de portada
    val tituloPortada = view.findViewById<TextView>(R.id.ivTitulo)
    val peliculaPortada = view.findViewById<ImageView>(R.id.ivPortada)

    // Funcion que recibe los datos de la pelicula
    fun render(peliSerie: PeliSerie) {
        tituloPortada.text = peliSerie.title
        Glide.with(peliculaPortada.context).load(peliSerie.backdrop_path).into(peliculaPortada)
        peliculaPortada.setOnClickListener {
            val intent = Intent(peliculaPortada.context, InfoPeliSerie::class.java)
            intent.putExtra("peliserie", peliSerie)
            peliculaPortada.context.startActivity(intent)
        }
    }
}

class PeliculasCarruselViewHolder(view: View) : ViewHolder(view) {
    // Referencio a la imageView de portada
    val peliculaCarrusel = view.findViewById<ImageView>(R.id.ivCarrusel)

    // Funcion que recibe los datos de la pelicula
    fun render(peliSerie: PeliSerie) {
        Glide.with(peliculaCarrusel.context).load(peliSerie.poster_path).into(peliculaCarrusel)
        peliculaCarrusel.setOnClickListener {
            val intent = Intent(peliculaCarrusel.context, InfoPeliSerie::class.java)
            intent.putExtra("peliserie", peliSerie)
            peliculaCarrusel.context.startActivity(intent)
        }
    }
}