package com.example.credieficaz.repository

import com.example.credieficaz.api.RetrofitClient
import com.example.credieficaz.models.DiarioGrupoItem

class GrupoRepository {

    suspend fun obtenerGrupos(): List<DiarioGrupoItem> {

        return RetrofitClient
            .api
            .obtenerGrupos()
    }
}