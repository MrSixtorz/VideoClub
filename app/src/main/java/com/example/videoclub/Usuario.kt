package com.example.videoclub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.viewpager2.widget.ViewPager2
import com.example.videoclub.databinding.ActivityUsuarioBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Usuario : AppCompatActivity() {
   private lateinit var binding : ActivityUsuarioBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var usuariosRef: DatabaseReference
    private var avatarPos: Int = 0
    private var imageNombre: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityUsuarioBinding.inflate(layoutInflater)
    setContentView(binding.root)
        val user = Firebase.auth.currentUser
        val email = user?.email
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
        val adapter =
            Register.AvatarAdapter(images, object : Register.AvatarAdapter.OnClickListener {
                override fun prevBoton() {
                    val viewPager: ViewPager2 = findViewById(R.id.viewAvatar)
                    val currentPosition = viewPager.currentItem
                    val maxPosition = viewPager.adapter?.itemCount ?: 0
                    val newPosition =
                        if (currentPosition == 0) maxPosition - 1 else currentPosition - 1
                    viewPager.setCurrentItem(newPosition, false)
                }

                override fun sigBoton() {
                    val viewPager: ViewPager2 = findViewById(R.id.viewAvatar)
                    val currentPosition = viewPager.currentItem
                    val maxPosition = viewPager.adapter?.itemCount ?: 0
                    val newPosition =
                        if (currentPosition == maxPosition - 1) 0 else currentPosition + 1
                    viewPager.setCurrentItem(newPosition, false)
                }
            })
        val auth = Firebase.auth
        database = FirebaseDatabase.getInstance()
        val usuarioActual: FirebaseUser? = auth.currentUser
        usuariosRef = database.getReference("usuarios/${usuarioActual?.uid}/perfil")
        var viewPager: ViewPager2 = findViewById(R.id.viewAvatar)
        viewPager.adapter = adapter

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                avatarPos = position
                val resNombre = resources.getResourceName(images[avatarPos])
                imageNombre = resNombre.substringAfterLast("/")
            }
        })

        obtenerNombre { nombreUsuario, nombreImagen ->
            binding.welcome.text = "Hola, $nombreUsuario"
            val imagenResourceId = resources.getIdentifier(nombreImagen, "drawable", packageName)
            binding.avatar.setImageResource(imagenResourceId)
            binding.mostrarNombre.text = "Nombre: $nombreUsuario"
        }

        binding.Marca.setOnClickListener{
            val intent = Intent (this,Welcome::class.java)
            startActivity(intent)
        }
        binding.avatar.setOnClickListener{
            val intent = Intent (this,Usuario::class.java)
            startActivity(intent)
        }
        binding.mostrarEmail.text = "Email: $email"

        binding.cambiarNombre.setOnClickListener {
            val nom = binding.campoNombre.editText?.text.toString()
            if (nom.isNotEmpty()){
                usuariosRef.child("nombre").setValue(nom)
                val intent = Intent (this,Usuario::class.java)
                startActivity(intent)
            } else {
                binding.campoNombre.error = "Campo requerido"
            }
        }
        // No funciona
//        binding.cambiarEmail.setOnClickListener {
//            val correo = binding.campoEmail
//            // Check de que se ha introducido el campo correo válido
////            if (!emailValido(correo.toString())) {
////                correo.error = "Correo electrónico no válido"
////                return@setOnClickListener
////            }
//            if (correo.toString().isNotEmpty()){
//                user!!.updateEmail(correo.toString())
//                    .addOnCompleteListener { task ->
//                        if (task.isSuccessful) {
//                            Log.d("EditarUsuario", "User email address updated.")
//                        }
//                    }
//                val intent = Intent (this,Usuario::class.java)
//                startActivity(intent)
//            } else {
//                binding.campoEmail.error = "Email requerido"
//            }
//        }
        binding.cambiarPass.setOnClickListener {
            val pass = binding.campoPass.editText?.text.toString()
            if (pass.length<6) {
                binding.campoPass.error = "Mínimo 6 caracteres"
                return@setOnClickListener
            }
            if (pass.isNotEmpty()){
                user!!.updatePassword(pass)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("EditarUsuario", "User password updated.")
                        }
                    }
                val intent = Intent (this,Usuario::class.java)
                startActivity(intent)
            } else {
                binding.campoPass.error = "Contraseña requerida"
            }
        }
        binding.Aceptar.setOnClickListener {
            usuariosRef.child("avatar").setValue(imageNombre)
        }
        binding.Atras.setOnClickListener {
            val intent = Intent(this,Welcome::class.java)
            startActivity(intent)
        }
    }

    private fun emailValido(email: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }
}