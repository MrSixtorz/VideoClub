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
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Register : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var usuariosRef: DatabaseReference
    private var avatarPos: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = Firebase.auth
        database = FirebaseDatabase.getInstance()
        usuariosRef = database.getReference("usuarios")

        val registrar: Button = findViewById(R.id.Registrar)
        val cancel: Button = findViewById(R.id.Cancelar)
        val email: TextInputLayout = findViewById(R.id.campoEmail)
        val pass: TextInputLayout = findViewById(R.id.campoContraseña)
        val confPass: TextInputLayout = findViewById(R.id.campoRepetirContraseña)
        val nom: TextInputLayout = findViewById(R.id.campoNombre)
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
        val adapter = AvatarAdapter(images)
        var viewPager: ViewPager2 = findViewById(R.id.viewAvatar)
        viewPager.adapter = adapter

        // Manejar la selección de la imagen
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                avatarPos = position
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
                            //Log.d(TAG, "Creado nuevo usuario")
                            //El email será nuestra id
                            // Añadimos datos al usuario creado
                            datosUsuario(
                                email.editText?.text.toString(),
                                nom.editText?.text.toString(),
                                avatarPos
                            )
                            val registrado = Intent(this, MainActivity::class.java)
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
            val intent = Intent(this, MainActivity::class.java)
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

    private fun datosUsuario(correo: String, nombre: String, imagenResId: Int) {
        // Creamos una variable para obtener la id del usuario que se loguea
        val usuarioActual: FirebaseUser? = auth.currentUser
        if (usuarioActual != null) {
            // insertamos los datos del usuario actual en nuestra Base de Datos
            val user = Clases(correo, nombre, imagenResId)
            usuariosRef.child(usuarioActual.uid).setValue(user)
        } else {

        }
    }

    private fun obtenerImagenResId(nombreImagen: String): Int {
        val resources = applicationContext.resources
        return resources.getIdentifier(nombreImagen, "drawable", applicationContext.packageName)
    }

    class AvatarAdapter(private val images: List<Int>) :
        RecyclerView.Adapter<AvatarAdapter.ImageViewHolder>() {

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