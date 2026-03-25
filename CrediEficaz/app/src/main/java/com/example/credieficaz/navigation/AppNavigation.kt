package com.example.credieficaz.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.credieficaz.api.ApiService
import com.example.credieficaz.api.RetrofitClient
import com.example.credieficaz.ui.screens.*
import com.example.credieficaz.viewmodel.AuthViewModel

// --- FACTORY PARA EL VIEWMODEL ---
class AuthViewModelFactory(private val apiService: ApiService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(apiService) as T
        }
        throw IllegalArgumentException("Clase ViewModel desconocida")
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // 1. Obtenemos la instancia de tu API desde el objeto RetrofitClient
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(RetrofitClient.api)
    )

    NavHost(
        navController = navController,
        startDestination = "auth" // Iniciamos con la validación de MariaDB
    ) {
        // --- PANTALLA DE AUTORIZACIÓN ---
        composable("auth") {
            AuthScreen(
                viewModel = authViewModel,
                onAccessGranted = {
                    // Navegamos al Home y limpiamos "auth" del historial
                    navController.navigate("home") {
                        popUpTo("auth") { inclusive = true }
                    }
                }
            )
        }

        // --- PANTALLA PRINCIPAL ---
        composable("home") {
            HomeScreen(
                onClienteClick = { navController.navigate("cliente") },
                onGrupoClick = { navController.navigate("grupo") },
                onConsultaClienteClick = { navController.navigate("consultaCliente") },
                onConsultaGrupoClick = { navController.navigate("consultaGrupo") },
                onConsultaClientePorClick = { navController.navigate("consultaClientePor") }
            )
        }

        composable("cliente") {
            DiarioClienteScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        composable("grupo") {
            DiarioGrupoScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable("consultaCliente") {
            ConsultaClienteScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        composable("consultaGrupo") {
            ConsultaGrupoScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        composable("consultaClientePor") {
            ConsultaClientePorScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}