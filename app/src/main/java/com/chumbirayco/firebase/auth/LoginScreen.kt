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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun LoginScreen(onLoggedIn: (FirebaseUser) -> Unit) {
  var email by remember { mutableStateOf("") }
  var pass by remember { mutableStateOf("") }
  var msg by remember { mutableStateOf<String?>(null) }
  val auth = Firebase.auth

  Column(Modifier.padding(16.dp)) {
    OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Correo") })
    OutlinedTextField(value = pass, onValueChange = { pass = it }, label = { Text("Contraseña") }, visualTransformation = PasswordVisualTransformation())
    Button(onClick = {
      auth.signInWithEmailAndPassword(email, pass)
        .addOnCompleteListener { t ->
          msg = if (t.isSuccessful) "Bienvenido" else t.exception?.localizedMessage
          if (t.isSuccessful) onLoggedIn(auth.currentUser!!)
        }
    }) { Text("Ingresar") }
    if (msg != null) Text(msg!!)
  }
}
