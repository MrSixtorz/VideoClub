package com.example.videoclub

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
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.videoclub.databinding.ActivityRegisterBinding
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Register : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var usuariosRef: DatabaseReference
    private var avatarPos: Int = 0
    private var imageNombre: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)


        auth = Firebase.auth
        database = FirebaseDatabase.getInstance()
        usuariosRef = database.getReference("usuarios")

        val registrar: Button = binding.Registrar
        val cancel: Button = binding.Cancelar
        val email: TextInputLayout = binding.campoEmail
        val pass: TextInputLayout = binding.campoContrase
        val confPass: TextInputLayout = binding.campoRepetirContrase
        val nom: TextInputLayout = binding.campoNombre
        val images = listOf(
            R.drawable.delfin,
            R.drawable.elefante,
            R.drawable.gato,
            R.drawable.indy,
            R.drawable.jamesbond,
            R.drawable.leon,
            R.drawable.perro,
            R.drawable.starwars
        ) // Lista de IDs de recursos de las imágenes
        // Con la lógica de los botones hacemos el efecto de carrusel infinito
        val adapter = AvatarAdapter(images, object : AvatarAdapter.OnClickListener {
            override fun prevBoton() {
                val viewPager: ViewPager2 = findViewById(R.id.viewAvatar)
                val currentPosition = viewPager.currentItem
                val maxPosition = viewPager.adapter?.itemCount ?: 0
                val newPosition = if (currentPosition == 0) maxPosition - 1 else currentPosition - 1
                viewPager.setCurrentItem(newPosition, false)
            }
            override fun sigBoton() {
                val viewPager: ViewPager2 = findViewById(R.id.viewAvatar)
                val currentPosition = viewPager.currentItem
                val maxPosition = viewPager.adapter?.itemCount ?: 0
                val newPosition = if (currentPosition == maxPosition - 1) 0 else currentPosition + 1
                viewPager.setCurrentItem(newPosition, false)
            }
        })


        var viewPager: ViewPager2 = findViewById(R.id.viewAvatar)
        viewPager.adapter = adapter

        // Manejar la selección de la imagen
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                avatarPos = position
                val resNombre = resources.getResourceName(images[avatarPos])
                imageNombre = resNombre.substringAfterLast("/")
            }
        })

        //  Password de 6 caracteres !!!!!!!!!!!!!
        registrar.setOnClickListener {
            if (email.editText?.text?.isNotEmpty() == true
                && pass.editText?.text?.isNotEmpty() == true
                && avatarPos >= 0
            ) {
                Log.d("Prueba", "Creado nuevo usuario")
                if ((pass.editText?.text.toString() == confPass.editText?.text.toString())) {
                    Log.d("Prueba", "Comprobando contraseña")
                    auth.createUserWithEmailAndPassword(
                        email.editText?.text.toString(),
                        pass.editText?.text.toString()
                    ).addOnCompleteListener {
                        if (it.isSuccessful) {
                            datosUsuario(
                                email.editText?.text.toString(),
                                nom.editText?.text.toString(),
                                imageNombre
                            )
                            val registrado = Intent(this, Login::class.java)
                            startActivity(registrado)
                        } else {
                            showAlert("Error creando el usuario")
                        }
                    }
                } else {
                    showAlert("Error, contraseñas distintas")
                }

            }
        }

        cancel.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
    }

    private fun showAlert(mensaje: String) {
        //Log.d(TAG, "Error creando nuevo usuario")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(mensaje)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun datosUsuario(email: String, nombre: String, nombreImg: String) {
        // Creamos una variable para obtener la id del usuario que se loguea
        val usuarioActual: FirebaseUser? = auth.currentUser
        val correo = hashMapOf("email" to email)
        if (usuarioActual != null) {
            // insertamos los datos del usuario actual en nuestra Base de Datos
            usuariosRef.child(usuarioActual.uid).setValue(correo)
            val perfil = hashMapOf(
                "nombre" to nombre,
                "avatar" to nombreImg)
            usuariosRef.child(usuarioActual.uid).child("perfil").setValue(perfil)
        } else {

        }
    }

    class AvatarAdapter(private val images: List<Int>, private val onClickListener: OnClickListener) :
        RecyclerView.Adapter<AvatarAdapter.ImageViewHolder>() {
        interface OnClickListener {
            fun prevBoton()
            fun sigBoton()
        }

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

            init {
                itemView.findViewById<ImageButton>(R.id.previo).setOnClickListener {
                    onClickListener.prevBoton()
                }

                itemView.findViewById<ImageButton>(R.id.siguiente).setOnClickListener {
                    onClickListener.sigBoton()
                }
            }
        }

    }

}
