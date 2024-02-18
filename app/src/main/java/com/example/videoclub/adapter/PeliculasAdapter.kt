package com.example.videoclub.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.videoclub.R

class PeliculasAdapter(private val listaImagenes: ArrayList<Pair<String, String>>): RecyclerView.Adapter<PeliculasViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeliculasViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return PeliculasViewHolder(layoutInflater.inflate(R.layout.portada, parent, false))
    }
    // funcion para que sea vea todos los items
    override fun getItemCount(): Int = listaImagenes.size
    override fun onBindViewHolder(holder: PeliculasViewHolder, position: Int) {
        val item = listaImagenes[position]
        holder.render(item)
    }
}
class PeliculasCarruselAdapter(private val listaImagenes: List<String>): RecyclerView.Adapter<PeliculasCarruselViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeliculasCarruselViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return PeliculasCarruselViewHolder(layoutInflater.inflate(R.layout.carrusel, parent, false))
    }
    // funcion para que sea vea todos los items
    override fun getItemCount(): Int = listaImagenes.size
    override fun onBindViewHolder(holder: PeliculasCarruselViewHolder, position: Int) {
        val item = listaImagenes[position]
        holder.render(item)
    }
}

