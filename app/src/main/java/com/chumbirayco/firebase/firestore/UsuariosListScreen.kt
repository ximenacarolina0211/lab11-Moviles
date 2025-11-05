package com.chumbirayco.firebase.firestore

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun UsuariosListScreen(onBack: () -> Unit) {
  val db = Firebase.firestore
  var items by remember { mutableStateOf<List<Pair<String, Long>>>(emptyList()) }
  var error by remember { mutableStateOf<String?>(null) }

  LaunchedEffect(Unit) {
    db.collection("usuarios").addSnapshotListener { snap, e ->
      if (e != null) { error = e.localizedMessage; return@addSnapshotListener }
      items = snap?.documents?.mapNotNull {
        val nombre = it.getString("nombre") ?: return@mapNotNull null
        val edad = it.getLong("edad") ?: 0
        nombre to edad
      } ?: emptyList()
    }
  }

  Column(Modifier.padding(16.dp)) {
    Text("Lista de usuarios")
    Spacer(Modifier.height(8.dp))
    if (error != null) Text(error!!)
    LazyColumn {
      items(items) { (nombre, edad) ->
        Text("- ${'$'}nombre (${ '$'}edad años)")
      }
    }
    Spacer(Modifier.height(16.dp))
    TextButton(onClick = onBack) { Text("Volver") }
  }
}
