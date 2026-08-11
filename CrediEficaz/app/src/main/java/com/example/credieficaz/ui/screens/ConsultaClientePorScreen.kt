package com.example.credieficaz.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.credieficaz.models.ConsultaClientePorItem
import com.example.credieficaz.viewmodel.ConsultaClientePorViewModel
import java.time.format.DateTimeFormatter

@Composable
fun ConsultaClientePorScreen(
    viewModel: ConsultaClientePorViewModel = viewModel(),
    onBack: () -> Unit = {}
) {
    var nombreBusqueda by remember { mutableStateOf("") }
    val listaClientesPor by viewModel.listaClientesPor.collectAsState()
    val listaFiltrada by remember(nombreBusqueda, listaClientesPor) {
        derivedStateOf {
            listaClientesPor.filter { clientePor ->
                val coincideNombre = clientePor.Nombre.contains(nombreBusqueda, ignoreCase = true)
                coincideNombre 
            }
        }
    }

    Spacer(Modifier.height(20.dp))
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "CONSULTA CLIENTES %",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Clientes %:",
                    modifier = Modifier.padding(end = 8.dp)
                )

                TextField(
                    value = listaFiltrada.size.toString(),
                    onValueChange = {},
                    enabled = false,
                    modifier = Modifier.width(80.dp)
                )
            }

            Button(onClick = onBack) {
                Text("VOLVER")
            }
        }

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = nombreBusqueda,
            onValueChange = { nombreBusqueda = it },
            label = { Text("Buscar por nombre") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null)
            },
            trailingIcon = {
                if (nombreBusqueda.isNotEmpty()) {
                    IconButton(onClick = { nombreBusqueda = "" }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Limpiar"
                        )
                    }
                }
            }
        )

        Spacer(Modifier.height(16.dp))

        if (listaFiltrada.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No se encontraron resultados", color = Color.Gray)
            }
        } else {
            ClientesPorTable(lista = listaFiltrada)
            Spacer(Modifier.height(26.dp))
        }
    }
}

object ConsultaClientesPorTableWeights {
    val ACCION = 90.dp
    val CLIENTE = 90.dp
    val NOMBRE = 150.dp
    val PRESTAMO = 100.dp
    val PRESTAMO_TOTAL = 100.dp
    val DEBE = 100.dp
    val ABONA = 100.dp
    val TOTAL_PAGOS = 90.dp
    val DOMICILIO = 150.dp
    val TELEFONO = 140.dp
    val GARANTIA = 140.dp
    val PORCENTAJE = 90.dp
    val CANTIDAD_PORCENTAJE = 90.dp
    val TRABAJO = 150.dp
    val INGRESOS = 100.dp
    val REFERENCIA = 150.dp
    val PLAZO = 90.dp
    val FECHA_COBRO = 130.dp
    val HORA_COBRO = 90.dp
    val ULTIMO_MOVIMIENTO = 150.dp
    val CONFIANZA = 90.dp
    val RENOVACION = 90.dp
}

@Composable
fun ClientesPorTable(
    lista: List<ConsultaClientePorItem>,
    modifier: Modifier = Modifier
) {

    val scrollX = rememberScrollState()
    val formato = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, Color.Black)
    ) {
        Column(
            modifier = Modifier
                .horizontalScroll(scrollX)
        ) {
            Row(
                modifier = Modifier
                    .height(48.dp)
                    .background(Color.LightGray)
            ) {
                /*TableHeaderPorCliente("Acción", ConsultaClientesPorTableWeights.ACCION)*/
                TableHeaderPorCliente("Cliente", ConsultaClientesPorTableWeights.CLIENTE)
                TableHeaderPorCliente("Nombre", ConsultaClientesPorTableWeights.NOMBRE)
                TableHeaderPorCliente("Préstamo", ConsultaClientesPorTableWeights.PRESTAMO)
                TableHeaderPorCliente("Total", ConsultaClientesPorTableWeights.PRESTAMO_TOTAL)
                TableHeaderPorCliente("Debe", ConsultaClientesPorTableWeights.DEBE)
                TableHeaderPorCliente("Abona", ConsultaClientesPorTableWeights.ABONA)
                TableHeaderPorCliente("Pagos", ConsultaClientesPorTableWeights.TOTAL_PAGOS)
                TableHeaderPorCliente("Domicilio", ConsultaClientesPorTableWeights.DOMICILIO)
                TableHeaderPorCliente("Teléfono", ConsultaClientesPorTableWeights.TELEFONO)
                TableHeaderPorCliente("Garantía", ConsultaClientesPorTableWeights.GARANTIA)
                TableHeaderPorCliente("Porcentaje", ConsultaClientesPorTableWeights.PORCENTAJE)
                TableHeaderPorCliente("CantidadPorcentaje", ConsultaClientesPorTableWeights.CANTIDAD_PORCENTAJE)
                TableHeaderPorCliente("Trabajo", ConsultaClientesPorTableWeights.TRABAJO)
                TableHeaderPorCliente("Ingresos", ConsultaClientesPorTableWeights.INGRESOS)
                TableHeaderPorCliente("Referencia", ConsultaClientesPorTableWeights.REFERENCIA)
                TableHeaderPorCliente("Plazo", ConsultaClientesPorTableWeights.PLAZO)
                TableHeaderPorCliente("FechaCobro", ConsultaClientesPorTableWeights.FECHA_COBRO)
                TableHeaderPorCliente("HoraCobro", ConsultaClientesPorTableWeights.HORA_COBRO)
                TableHeaderPorCliente("ÚltimoMovimiento", ConsultaClientesPorTableWeights.ULTIMO_MOVIMIENTO)
                TableHeaderPorCliente("Confianza", ConsultaClientesPorTableWeights.CONFIANZA)
                TableHeaderPorCliente("Renovación", ConsultaClientesPorTableWeights.RENOVACION)
            }

            Divider(color = Color.Black)

            LazyColumn(
                contentPadding = PaddingValues(bottom = 120.dp)
            ) {
                itemsIndexed(lista) { index, item ->
                    val bg =
                        if (index % 2 == 0)
                            Color(0xFFF5F5F5)
                        else
                            Color.White
                    Row(
                        modifier = Modifier
                            .height(48.dp)
                            .background(bg)
                    ) {
                        /*TableCellClientePor(" ", ConsultaClientesPorTableWeights.ACCION)*/
                        TableCellClientePor(item.Numcliente.toString(), ConsultaClientesPorTableWeights.CLIENTE)
                        TableCellClientePor(item.Nombre, ConsultaClientesPorTableWeights.NOMBRE)
                        TableCellClientePor(item.Préstamo.toString(), ConsultaClientesPorTableWeights.PRESTAMO)
                        TableCellClientePor(item.PréstamoTotal.toString(), ConsultaClientesPorTableWeights.PRESTAMO_TOTAL)
                        TableCellClientePor(item.Debe.toString(), ConsultaClientesPorTableWeights.DEBE)
                        TableCellClientePor(item.Abona.toString(), ConsultaClientesPorTableWeights.ABONA)
                        TableCellClientePor(item.TotalPagos.toString(), ConsultaClientesPorTableWeights.TOTAL_PAGOS)
                        TableCellClientePor(item.Domicilio, ConsultaClientesPorTableWeights.DOMICILIO)
                        TableCellClientePor(item.Teléfono, ConsultaClientesPorTableWeights.TELEFONO)
                        TableCellClientePor(item.Garantía, ConsultaClientesPorTableWeights.GARANTIA)
                        TableCellClientePor(item.Porcentaje, ConsultaClientesPorTableWeights.PORCENTAJE)
                        TableCellClientePor(item.CantidadPorcentaje, ConsultaClientesPorTableWeights.CANTIDAD_PORCENTAJE)
                        TableCellClientePor(item.Trabajo, ConsultaClientesPorTableWeights.TRABAJO)
                        TableCellClientePor(item.Ingresos.toString(), ConsultaClientesPorTableWeights.INGRESOS)
                        TableCellClientePor(item.Referencia, ConsultaClientesPorTableWeights.REFERENCIA)
                        TableCellClientePor(item.Plazo, ConsultaClientesPorTableWeights.PLAZO)
                        TableCellCliente(item.FechaCobro?.format(formato) ?: " ", ClientesTableWeights.FECHA_COBRO)
                        TableCellClientePor(item.HoraCobro, ConsultaClientesPorTableWeights.HORA_COBRO)
                        TableCellClientePor(item.ÚltimoMovimiento?.format(formato) ?: " ", ConsultaClientesPorTableWeights.ULTIMO_MOVIMIENTO)
                        TableCellClientePor(item.Confianza, ConsultaClientesPorTableWeights.CONFIANZA)
                        TableCellClientePor(item.Renovación.toString(), ConsultaClientesPorTableWeights.RENOVACION)
                    }
                }
            }
        }
    }
}

@Composable
fun TableHeaderPorCliente(text: String, width: Dp) {
    Box(
        modifier = Modifier
            .width(width)
            .fillMaxHeight()
            .border(1.dp, Color.Black)
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        Text(text, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun TableCellClientePor(text: String, width: Dp) {
    Box(
        modifier = Modifier
            .width(width)
            .fillMaxHeight()
            .border(1.dp, Color.Black),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}