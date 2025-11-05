package com.chumbirayco.firebase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chumbirayco.firebase.ui.theme.FirebaseTheme
import com.chumbirayco.firebase.auth.RegisterScreen
import com.chumbirayco.firebase.auth.LoginScreen
import com.chumbirayco.firebase.firestore.AddUsuarioScreen
import com.chumbirayco.firebase.firestore.UsuariosListScreen
import com.chumbirayco.firebase.storage.UploadImageScreen
import com.google.firebase.auth.FirebaseUser

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FirebaseTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    var current by remember { mutableStateOf(Screen.Login) }
                    var msg by remember { mutableStateOf<String?>(null) }
                    var userState by remember { mutableStateOf<FirebaseUser?>(null) }

                    when (current) {
                        Screen.Login -> Column(Modifier.padding(16.dp)) {
                            LoginScreen(onLoggedIn = { user: FirebaseUser ->
                                userState = user
                                msg = "Bienvenido"
                                current = Screen.Home
                            })
                            TextButton(onClick = { current = Screen.Register }) { Text("¿No tienes cuenta? Registrarse") }
                            if (msg != null) Text(msg!!)
                        }
                        Screen.Register -> Column(Modifier.padding(16.dp)) {
                            RegisterScreen(onRegistered = { user: FirebaseUser ->
                                userState = user
                                msg = "Bienvenido"
                                current = Screen.Home
                            })
                            TextButton(onClick = { current = Screen.Login }) { Text("¿Ya tienes cuenta? Ingresar") }
                            if (msg != null) Text(msg!!)
                        }
                        Screen.Home -> Column(Modifier.padding(16.dp)) {
                            Text("Menú")
                            Button(onClick = { current = Screen.AddUsuario }, modifier = Modifier.padding(top = 8.dp)) { Text("Agregar usuario (Firestore)") }
                            Button(onClick = { current = Screen.ListUsuarios }, modifier = Modifier.padding(top = 8.dp)) { Text("Lista usuarios (Firestore)") }
                            Button(onClick = { current = Screen.UploadImage }, modifier = Modifier.padding(top = 8.dp)) { Text("Subir imagen (Storage)") }
                            if (msg != null) Text(msg!!, modifier = Modifier.padding(top = 8.dp))
                        }
                        Screen.AddUsuario -> Column(Modifier.padding(16.dp)) {
                            AddUsuarioScreen(onBack = { current = Screen.Home })
                        }
                        Screen.ListUsuarios -> Column(Modifier.padding(16.dp)) {
                            UsuariosListScreen(onBack = { current = Screen.Home })
                        }
                        Screen.UploadImage -> Column(Modifier.padding(16.dp)) {
                            val u = userState
                            if (u != null) {
                                UploadImageScreen(user = u, onBack = { current = Screen.Home })
                            } else {
                                Text("Inicia sesión para subir imágenes")
                                TextButton(onClick = { current = Screen.Login }) { Text("Ir a Login") }
                            }
                        }
                    }
                }
            }
        }
    }
}

private enum class Screen { Login, Register, Home, AddUsuario, ListUsuarios, UploadImage }