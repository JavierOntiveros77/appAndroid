package com.example.credieficaz.viewmodel


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.credieficaz.api.ApiService
import android.util.Log

class AuthViewModel(private val apiService: ApiService) : ViewModel() {
    var authStatus by mutableStateOf(0)
        private set

    fun validarEntrada(passwordLocal: String) {
        viewModelScope.launch {
            try {
                val response = apiService.checkAccess(passwordLocal)

                Log.d("AUTH_DEBUG", "Respuesta recibida: ${response.authorized}")

                authStatus = if (response.authorized) 1 else 2
            } catch (e: Exception) {
                Log.e("AUTH_DEBUG", "Error en la validación: ${e.message}")
                e.printStackTrace()
                authStatus = 2
            }
        }
    }
}