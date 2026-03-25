package com.example.credieficaz.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.credieficaz.models.DiarioClienteItem
import com.example.credieficaz.viewmodel.ClienteViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun DiarioClienteScreen(
    viewModel: ClienteViewModel = viewModel(),
    onBack: () -> Unit = {}
) {
    var fechaFin by remember { mutableStateOf(LocalDate.now()) }
    var nombreBusqueda by remember { mutableStateOf("") }
    val listaClientes by viewModel.listaClientes.collectAsState()
    val listaFiltrada by remember(nombreBusqueda, fechaFin, listaClientes) {
        derivedStateOf {
            val apiFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

            listaClientes.filter { cliente ->
                val coincideNombre = cliente.Nombre.contains(nombreBusqueda, ignoreCase = true)
                val fechaPagoDate = try {
                    LocalDate.parse(cliente.FechaPago, apiFormatter)
                } catch (e: Exception) {
                    LocalDate.MIN
                }
                val coincideFecha = !fechaPagoDate.isAfter(fechaFin)

                coincideNombre && coincideFecha
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "DIARIO CLIENTE",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            DateBox(
                label = "Fecha Fin",
                fecha = fechaFin
            ) { nuevaFecha ->
                fechaFin = nuevaFecha
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
            ClienteTable(lista = listaFiltrada)
            Spacer(Modifier.height(26.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateBox(
    label: String,
    fecha: LocalDate,
    onFechaChange: (LocalDate) -> Unit
) {

    var showDialog by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = fecha
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    )
    Box(
        modifier = Modifier
            .width(160.dp)
            .clickable { showDialog = true }
    ) {

        OutlinedTextField(
            value = fecha.format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy")
            ),
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            enabled = false,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Seleccionar fecha"
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
    }

    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        val nuevaFecha =
                            Instant.ofEpochMilli(it)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                        onFechaChange(nuevaFecha)
                    }
                    showDialog = false
                }) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDialog = false
                }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

object ClienteTableWeights {

    const val ACCION = 1.2f
    const val NUM_CLIENTE = 1.1f
    const val NOMBRE = 2.2f
    const val REFERENCIA = 1.9f
    const val FECHA_PAGO = 1.5f
    const val HORA = 1.0f
    const val NUM_PAGO = 0.6f
    const val RESTANTES = 1.0f
    const val ABONO = 1.0f
    const val LIQUIDA = 1.0f
    const val PLAZO = 1.0f
    const val RENOVACION = 1.2f

}

@Composable
fun ClienteTable(
    lista: List<DiarioClienteItem>,
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
                .width(1200.dp)
        ) {
            Row(
                modifier = Modifier
                    .height(48.dp)
                    .background(Color.LightGray)
            ) {
                TableHeader("Acción", ClienteTableWeights.ACCION)
                TableHeader("Cliente", ClienteTableWeights.NUM_CLIENTE)
                TableHeader("Nombre", ClienteTableWeights.NOMBRE)
                TableHeader("Referencia", ClienteTableWeights.REFERENCIA)
                TableHeader("FechaPago", ClienteTableWeights.FECHA_PAGO)
                TableHeader("Hora", ClienteTableWeights.HORA)
                TableHeader("Pago", ClienteTableWeights.NUM_PAGO)
                TableHeader("Restantes", ClienteTableWeights.RESTANTES)
                TableHeader("Abono", ClienteTableWeights.ABONO)
                TableHeader("Liquida", ClienteTableWeights.LIQUIDA)
                TableHeader("Plazo", ClienteTableWeights.PLAZO)
                TableHeader("Renovacion", ClienteTableWeights.RENOVACION)
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
                        TableCell("", ClienteTableWeights.ACCION)
                        TableCell(item.NumeroCliente, ClienteTableWeights.NUM_CLIENTE)
                        TableCell(item.Nombre, ClienteTableWeights.NOMBRE)
                        TableCell(item.Referencia, ClienteTableWeights.REFERENCIA)
                        TableCell(item.FechaPago.format(formato), ClienteTableWeights.FECHA_PAGO)
                        TableCell(item.HoraCobro, ClienteTableWeights.HORA)
                        TableCell(item.NumPago.toString(), ClienteTableWeights.NUM_PAGO)
                        TableCell(item.PagosRestantes.toString(), ClienteTableWeights.RESTANTES)
                        TableCell(item.Abono.toString(), ClienteTableWeights.ABONO)
                        TableCell(item.Liquida.toString(), ClienteTableWeights.LIQUIDA)
                        TableCell(item.Plazo.toString(), ClienteTableWeights.PLAZO)
                        TableCell(item.Renovación.toString(), ClienteTableWeights.RENOVACION)

                    }
                }
            }
        }
    }
}

@Composable
fun RowScope.TableHeader(text: String, weight: Float) {
    Box(
        modifier = Modifier
            .weight(weight)
            .fillMaxHeight()
            .border(1.dp, Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun RowScope.TableCell(text: String, weight: Float) {
    Box(
        modifier = Modifier
            .weight(weight)
            .fillMaxHeight()
            .border(1.dp, Color.Black),
        contentAlignment = Alignment.CenterStart
    ) {

        Text(
            text,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}