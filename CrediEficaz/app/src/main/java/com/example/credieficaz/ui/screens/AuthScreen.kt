package com.example.credieficaz.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.credieficaz.viewmodel.AuthViewModel

@Composable
fun AuthScreen(
    viewModel: AuthViewModel,
    onAccessGranted: () -> Unit
) {
    val passwordRequerido = "SDGFSFDH45635426DFGHS5467."

    // Dispara la validación al entrar
    LaunchedEffect(Unit) {
        viewModel.validarEntrada(passwordRequerido)
    }

    // 👇 Escucha cambios de authStatus en el nivel raíz del Composable
    LaunchedEffect(viewModel.authStatus) {
        if (viewModel.authStatus == 1) {
            onAccessGranted()
        }
    }

    // Solo maneja la UI, sin lógica de navegación aquí
    when (viewModel.authStatus) {
        0 -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        2 -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    color = Color.Red,
                    text = "⚠️ DISPOSITIVO NO AUTORIZADO\nNo tienes permiso para usar esta aplicación.",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        // Estado 1 no necesita UI — ya navegó
    }
}