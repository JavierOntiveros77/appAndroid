package com.example.credieficaz.models

data class DiarioClienteItem(
    val NumeroCliente: String,
    val Nombre: String,
    val Referencia: String,
    val FechaPago: String,
    val HoraCobro: String,
    val NumPago: Int,
    val PagosRestantes: Int,
    val Abono: Int,
    val Liquida: Int,
    val Plazo: String,
    val Renovación: Int
)