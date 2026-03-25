package com.example.credieficaz.repository

import com.example.credieficaz.api.RetrofitClient
import com.example.credieficaz.models.DiarioClienteItem

class ClienteRepository {

    suspend fun obtenerClientes(): List<DiarioClienteItem> {

        return RetrofitClient
            .api
            .obtenerClientes()
    }
}