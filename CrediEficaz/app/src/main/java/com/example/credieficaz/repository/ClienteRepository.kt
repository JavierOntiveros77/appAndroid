package com.example.credieficaz.repository

import com.example.credieficaz.api.RetrofitClient
import com.example.credieficaz.models.DiarioClienteItem

class ClienteRepository {
    private val api = RetrofitClient.api

    suspend fun obtenerClientes(fechaFin: String): List<DiarioClienteItem> {
        return api.obtenerClientes(fechaFin)
    }
}