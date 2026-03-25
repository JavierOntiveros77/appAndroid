package com.example.credieficaz.api

import com.example.credieficaz.models.ConsultaClienteItem
import com.example.credieficaz.models.DiarioClienteItem
import com.example.credieficaz.models.DiarioGrupoItem
import com.example.credieficaz.models.ConsultaGrupoItem
import com.example.credieficaz.models.ConsultaClientePorItem
import com.example.credieficaz.models.AuthResponse
import retrofit2.http.GET
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    @GET("clientes_diario.php")
    suspend fun obtenerClientes(): List<DiarioClienteItem>

    @GET("grupo_diario.php")
    suspend fun obtenerGrupos(): List<DiarioGrupoItem>

    @GET("consulta_clientes.php")
    suspend fun obtenerConsultaClientes(): List<ConsultaClienteItem>

    @GET("consulta_grupo.php")
    suspend fun obtenerConsultaGrupos(): List<ConsultaGrupoItem>

    @GET("consulta_clientes_porcentaje.php")
    suspend fun obtenerClientesPor(): List<ConsultaClientePorItem>

    @FormUrlEncoded
    @POST("validar_acceso.php")
    suspend fun checkAccess(
        @Field("pass") password: String
    ): AuthResponse

}