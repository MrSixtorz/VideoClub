package com.example.videoclub

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

fun obtenerNombre(callback: (String, String?) -> Unit) {
    val usuarioActual: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    if (usuarioActual != null) {
        val uidUsuario = usuarioActual.uid
        val database = FirebaseDatabase.getInstance()
        val usuariosRef = database.getReference("usuarios")
        usuariosRef.child(uidUsuario).child("perfil")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val nombreUsuario = dataSnapshot.child("nombre").getValue(String::class.java)
                    val nombreImagen = dataSnapshot.child("avatar").getValue(String::class.java)

                    if (nombreUsuario != null && nombreUsuario.isNotEmpty()) {
                        callback(nombreUsuario, nombreImagen)
                    } else {
                        Log.d("Firebase", "El nombre no está configurado en la base de datos")
                        callback("Desconocido", null)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e(
                        "Firebase",
                        "Error al leer el nombre desde la base de datos: ${databaseError.message}"
                    )
                    callback("Error al leer el nombre", null)
                }
            })
    } else {
        Log.d("Firebase", "El usuario no está autenticado")
        callback("Usuario no autenticado", null)
    }
}
