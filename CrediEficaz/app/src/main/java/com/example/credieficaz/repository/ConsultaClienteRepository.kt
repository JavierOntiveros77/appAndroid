package com.example.credieficaz.repository

import com.example.credieficaz.api.RetrofitClient
import com.example.credieficaz.models.ConsultaClienteItem

class ConsultaClienteRepository {

    suspend fun obtenerClientes(): List<ConsultaClienteItem> {

        return RetrofitClient
            .api
            .obtenerConsultaClientes()
    }
}