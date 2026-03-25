package com.example.credieficaz.models

data class ConsultaClientePorItem(
    val Numcliente: Int,
    val Nombre: String,
    val Préstamo: Int,
    val PréstamoTotal: Int,
    val Debe: Int,
    val Abona: Int,
    val TotalPagos: Int,
    val Domicilio: String,
    val Teléfono: String,
    val Garantía: String,
    val Porcentaje: String,
    val CantidadPorcentaje: String,
    val Trabajo: String,
    val Ingresos: Int,
    val Referencia: String,
    val Plazo: String,
    val FechaCobro: String,
    val HoraCobro: String,
    val ÚltimoMovimiento: String,
    val Confianza: String,
    val Renovación: Int
)