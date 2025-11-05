package com.chumbirayco.firebase.storage

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

@Composable
fun UploadImageScreen(user: FirebaseUser, onBack: () -> Unit) {
  val storage = Firebase.storage
  var imageUrl by remember { mutableStateOf<String?>(null) }
  var error by remember { mutableStateOf<String?>(null) }
  var uploading by remember { mutableStateOf(false) }

  val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
    if (uri != null) {
      uploading = true; error = null
      val ref = storage.reference.child("images/${user.uid}/${System.currentTimeMillis()}.jpg")
      ref.putFile(uri)
        .continueWithTask { ref.downloadUrl }
        .addOnSuccessListener { url -> imageUrl = url.toString() }
        .addOnFailureListener { e -> error = e.localizedMessage }
        .addOnCompleteListener { uploading = false }
    }
  }

  Column(Modifier.padding(16.dp)) {
    Text("Subir imagen a Storage")
    Spacer(Modifier.height(8.dp))
    Button(onClick = { launcher.launch("image/*") }, enabled = !uploading) { Text(if (uploading) "Subiendo..." else "Seleccionar imagen") }
    Spacer(Modifier.height(12.dp))
    if (imageUrl != null) {
      Image(
        painter = rememberAsyncImagePainter(model = imageUrl),
        contentDescription = null,
        modifier = Modifier
          .height(200.dp)
          .clip(RoundedCornerShape(8.dp))
      )
      Text(imageUrl!!)
    }
    if (error != null) Text(error!!)
    Spacer(Modifier.height(16.dp))
    TextButton(onClick = onBack) { Text("Volver") }
  }
}
