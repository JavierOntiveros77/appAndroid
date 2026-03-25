package com.example.credieficaz.models

data class ConsultaClienteItem(
    val Numcliente: Int,
    val Nombre: String,
    val Préstamo: Int,
    val PréstamoTotal: Int,
    val Debe: Int,
    val Abona: Int,
    val TotalPagos: Int,
    val PagosRestantes: Int,
    val Domicilio: String,
    val Teléfono: String,
    val Trabajo: String,
    val Ingresos: Int,
    val Referencia: String,
    val Plazo: String,
    val FechaCobro: String,
    val HoraCobro: String,
    val ÚltimoMovimiento: String,
    val Confianza: String,
    val Renovación: Int,
    val Atrasos: Int
)