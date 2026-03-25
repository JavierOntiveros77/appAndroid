package com.example.credieficaz.models

data class DiarioGrupoItem(
    val Grupo: Int,
    val NombreGrupo: String,
    val NumCiclo: Int,
    val NombreRepresentante: String,
    val NumIntegrantes: Int,
    val FechaPago: String,
    val NumPago: Int,
    val De: Int,
    val PrestamoTotal: Int,
    val Liquida: Int,
    val Plazo: String
)