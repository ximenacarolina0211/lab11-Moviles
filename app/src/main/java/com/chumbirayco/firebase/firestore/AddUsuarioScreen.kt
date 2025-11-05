package com.chumbirayco.firebase.firestore

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun AddUsuarioScreen(onBack: () -> Unit) {
  var nombre by remember { mutableStateOf("") }
  var edadText by remember { mutableStateOf("") }
  var msg by remember { mutableStateOf<String?>(null) }
  val db = Firebase.firestore

  Column(Modifier.padding(16.dp)) {
    Text("Agregar usuario (colección 'usuarios')")
    Spacer(Modifier.height(8.dp))
    OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
    OutlinedTextField(value = edadText, onValueChange = { edadText = it.filter { c -> c.isDigit() } }, label = { Text("Edad") })
    Spacer(Modifier.height(8.dp))
    Button(onClick = {
      val edad = edadText.toLongOrNull() ?: 0
      val data = mapOf("nombre" to nombre, "edad" to edad)
      db.collection("usuarios").add(data)
        .addOnSuccessListener { msg = "Guardado" }
        .addOnFailureListener { e -> msg = e.localizedMessage }
    }) { Text("Guardar") }
    if (msg != null) Text(msg!!, color = if (msg == "Guardado") Color(0xFF2E7D32) else Color.Red)
    Spacer(Modifier.height(16.dp))
    TextButton(onClick = onBack) { Text("Volver") }
  }
}
