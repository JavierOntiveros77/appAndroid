package com.example.credieficaz.repository

import com.example.credieficaz.api.RetrofitClient
import com.example.credieficaz.models.ConsultaGrupoItem

class ConsultaGrupoRepository {

    suspend fun obtenerGrupos(): List<ConsultaGrupoItem> {

        return RetrofitClient
            .api
            .obtenerConsultaGrupos()
    }
}