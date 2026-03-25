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
import com.example.credieficaz.models.ConsultaClienteItem
import com.example.credieficaz.viewmodel.ConsultaClienteViewModel
import java.time.format.DateTimeFormatter

@Composable
fun ConsultaClienteScreen(
    viewModel: ConsultaClienteViewModel = viewModel(),
    onBack: () -> Unit = {}
) {
    var nombreBusqueda by remember { mutableStateOf("") }
    val listaClientes by viewModel.listaClientes.collectAsState()
    val listaFiltrada by remember(nombreBusqueda, listaClientes) {
        derivedStateOf {
            listaClientes.filter { cliente ->
                val coincideNombre = cliente.Nombre.contains(nombreBusqueda, ignoreCase = true)
                coincideNombre 
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "CONSULTA CLIENTES",
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
                    text = "Clientes:",
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
            ClientesTable(lista = listaFiltrada)
            Spacer(Modifier.height(26.dp))
        }
    }
}

object ClientesTableWeights {
    val ACCION = 90.dp
    val CLIENTE = 90.dp
    val NOMBRE = 150.dp
    val PRESTAMO = 100.dp
    val PRESTAMO_TOTAL = 100.dp
    val DEBE = 100.dp
    val ABONA = 100.dp
    val TOTAL_PAGOS = 90.dp
    val PAGOS_RESTANTES = 90.dp
    val DOMICILIO = 150.dp
    val TELEFONO = 140.dp
    val TRABAJO = 150.dp
    val INGRESOS = 100.dp
    val REFERENCIA = 150.dp
    val PLAZO = 90.dp
    val FECHA_COBRO = 130.dp
    val HORA_COBRO = 90.dp
    val ULTIMO_MOVIMIENTO = 150.dp
    val CONFIANZA = 90.dp
    val RENOVACION = 90.dp
    val ATRASOS = 90.dp
}

@Composable
fun ClientesTable(
    lista: List<ConsultaClienteItem>,
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
                TableHeaderCliente("Acción", ClientesTableWeights.ACCION)
                TableHeaderCliente("Cliente", ClientesTableWeights.CLIENTE)
                TableHeaderCliente("Nombre", ClientesTableWeights.NOMBRE)
                TableHeaderCliente("Préstamo", ClientesTableWeights.PRESTAMO)
                TableHeaderCliente("Total", ClientesTableWeights.PRESTAMO_TOTAL)
                TableHeaderCliente("Debe", ClientesTableWeights.DEBE)
                TableHeaderCliente("Abona", ClientesTableWeights.ABONA)
                TableHeaderCliente("Pagos", ClientesTableWeights.TOTAL_PAGOS)
                TableHeaderCliente("Restantes", ClientesTableWeights.PAGOS_RESTANTES)
                TableHeaderCliente("Domicilio", ClientesTableWeights.DOMICILIO)
                TableHeaderCliente("Teléfono", ClientesTableWeights.TELEFONO)
                TableHeaderCliente("Trabajo", ClientesTableWeights.TRABAJO)
                TableHeaderCliente("Ingresos", ClientesTableWeights.INGRESOS)
                TableHeaderCliente("Referencia", ClientesTableWeights.REFERENCIA)
                TableHeaderCliente("Plazo", ClientesTableWeights.PLAZO)
                TableHeaderCliente("FechaCobro", ClientesTableWeights.FECHA_COBRO)
                TableHeaderCliente("HoraCobro", ClientesTableWeights.HORA_COBRO)
                TableHeaderCliente("ÚltimoMovimiento", ClientesTableWeights.ULTIMO_MOVIMIENTO)
                TableHeaderCliente("Confianza", ClientesTableWeights.CONFIANZA)
                TableHeaderCliente("Renovación", ClientesTableWeights.RENOVACION)
                TableHeaderCliente("Atrasos", ClientesTableWeights.ATRASOS)
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
                        TableCellCliente("", ClientesTableWeights.ACCION)
                        TableCellCliente(item.Numcliente.toString(), ClientesTableWeights.CLIENTE)
                        TableCellCliente(item.Nombre, ClientesTableWeights.NOMBRE)
                        TableCellCliente(item.Préstamo.toString(), ClientesTableWeights.PRESTAMO)
                        TableCellCliente(item.PréstamoTotal.toString(), ClientesTableWeights.PRESTAMO_TOTAL)
                        TableCellCliente(item.Debe.toString(), ClientesTableWeights.DEBE)
                        TableCellCliente(item.Abona.toString(), ClientesTableWeights.ABONA)
                        TableCellCliente(item.TotalPagos.toString(), ClientesTableWeights.TOTAL_PAGOS)
                        TableCellCliente(item.PagosRestantes.toString(), ClientesTableWeights.PAGOS_RESTANTES)
                        TableCellCliente(item.Domicilio, ClientesTableWeights.DOMICILIO)
                        TableCellCliente(item.Teléfono, ClientesTableWeights.TELEFONO)
                        TableCellCliente(item.Trabajo, ClientesTableWeights.TRABAJO)
                        TableCellCliente(item.Ingresos.toString(), ClientesTableWeights.INGRESOS)
                        TableCellCliente(item.Referencia, ClientesTableWeights.REFERENCIA)
                        TableCellCliente(item.Plazo, ClientesTableWeights.PLAZO)
                        TableCellCliente(item.FechaCobro.format(formato), ClientesTableWeights.FECHA_COBRO)
                        TableCellCliente(item.HoraCobro, ClientesTableWeights.HORA_COBRO)
                        TableCellCliente(item.ÚltimoMovimiento.format(formato), ClientesTableWeights.ULTIMO_MOVIMIENTO)
                        TableCellCliente(item.Confianza, ClientesTableWeights.CONFIANZA)
                        TableCellCliente(item.Renovación.toString(), ClientesTableWeights.RENOVACION)
                        TableCellCliente(item.Atrasos.toString(), ClientesTableWeights.ATRASOS)
                    }
                }
            }
        }
    }
}

@Composable
fun TableHeaderCliente(text: String, width: Dp) {
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
fun TableCellCliente(text: String, width: Dp) {
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