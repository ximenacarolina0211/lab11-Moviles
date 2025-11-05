package com.chumbirayco.firebase.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore

@Composable
fun RegisterScreen(onRegistered: (FirebaseUser) -> Unit) {
  var nombre by remember { mutableStateOf("") }
  var email by remember { mutableStateOf("") }
  var pass by remember { mutableStateOf("") }
  var loading by remember { mutableStateOf(false) }
  var error by remember { mutableStateOf<String?>(null) }
  val auth = Firebase.auth

  Column(Modifier.padding(16.dp)) {
    OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre completo") })
    OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Correo") })
    OutlinedTextField(value = pass, onValueChange = { pass = it }, label = { Text("Contraseña") }, visualTransformation = PasswordVisualTransformation())
    Button(
      onClick = {
        loading = true; error = null
        auth.createUserWithEmailAndPassword(email, pass)
          .addOnCompleteListener { task ->
            loading = false
            if (task.isSuccessful) {
              val user = auth.currentUser!!
              // guardar perfil en Firestore
              val db = Firebase.firestore
              val perfil = mapOf(
                "uid" to user.uid,
                "nombreCompleto" to nombre.ifBlank { user.email ?: "" },
                "rol" to "Alumno",
                "email" to user.email
              )
              db.collection("perfiles").document(user.uid).set(perfil)
                .addOnSuccessListener { onRegistered(user) }
                .addOnFailureListener { e -> error = e.localizedMessage }
            } else error = task.exception?.localizedMessage
          }
      },
      enabled = !loading
    ) { Text("Registrarse") }
    if (error != null) Text(error!!, color = Color.Red)
  }
}
