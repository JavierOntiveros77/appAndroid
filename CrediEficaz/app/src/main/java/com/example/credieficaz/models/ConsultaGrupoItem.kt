package com.example.credieficaz.models

data class ConsultaGrupoItem(
    val Grupo: Int,
    val NombreGrupo: String,
    val NumCiclo: Int,
    val NombreRepresentante: String,
    val Domicilio: String,
    val NumTelefono: String,
    val NumIntegrantes: Int,
    val NumPagos: String,
    val FechaPróximoPago: String,
    val Préstamo: String,
    val PréstamoTotal: String,
    val Pendiente: String,
    val Plazo: String
)