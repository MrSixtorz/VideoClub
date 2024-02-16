package com.example.videoclub


import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
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
//        var servicio = RetrofitServiceFactory.makeRetrofitService()

//        lifecycleScope.launch{
//            val discMovies = servicio.listPopularMovies("30d6404fc8d3086411532ee450413606", "EU")
//        }

        obtenerNombre { nombreUsuario, nombreImagen ->
            saludo.text = "Hola, $nombreUsuario"
            val imagenResourceId = resources.getIdentifier(nombreImagen, "drawable", packageName)
            avatar.setImageResource(imagenResourceId)

        }

        val seriesList = mutableListOf<SeriesVotos>()

        val seriesRef = FirebaseDatabase.getInstance().getReference("series/results")

        seriesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val serie = snapshot.getValue(SeriesVotos::class.java)
                    serie?.let { seriesList.add(it) }
                }
                // Aquí ya tienes todas las series en la lista seriesList
                // Puedes utilizar esta lista en tu RecyclerView
                // Por ejemplo, pasándola a tu adaptador
                val adapter = PrincipalAdapter(seriesList.)
                recyclerView.adapter = adapter
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })


        var viewPager: ViewPager2 = findViewById(R.id.viewAvatar)
        viewPager.adapter = adapter

        // Manejar la selección de la imagen
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {

            }
        })

        binding.avatar.setOnClickListener(){
            val intent = Intent (this, InformacionUsuario::class.java)
            startActivity(intent)
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

    class PrincipalAdapter(private val images: List<Int>) :
        RecyclerView.Adapter<PrincipalAdapter.ImageViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.avatar, parent, false)
            return ImageViewHolder(view)
        }

        override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
            holder.imageView.setImageResource(images[position])
        }

        override fun getItemCount(): Int = images.size

        inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val imageView: ImageView = itemView.findViewById(R.id.imageView)
        }

    }

}


