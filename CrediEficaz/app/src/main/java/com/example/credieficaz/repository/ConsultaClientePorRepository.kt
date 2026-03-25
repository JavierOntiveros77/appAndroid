package com.example.credieficaz.repository

import com.example.credieficaz.api.RetrofitClient
import com.example.credieficaz.models.ConsultaClientePorItem

class ConsultaClientePorRepository {

    suspend fun obtenerClientesPor(): List<ConsultaClientePorItem> {

        return RetrofitClient
            .api
            .obtenerClientesPor()
    }
}