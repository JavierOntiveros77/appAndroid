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
import com.example.credieficaz.models.ConsultaClienteItem
import com.example.credieficaz.models.DiarioGrupoItem
import com.example.credieficaz.viewmodel.GrupoViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun DiarioGrupoScreen(
    viewModel: GrupoViewModel = viewModel(),
    onBack: () -> Unit = {}
) {
    var fechaFin by remember { mutableStateOf(LocalDate.now()) }
    var nombreBusqueda by remember { mutableStateOf("") }
    val listaGrupos by viewModel.listaGrupos.collectAsState()

    // 👇 Llama al servidor cada vez que cambia la fecha
    LaunchedEffect(fechaFin) {
        viewModel.cargarGrupos(fechaFin)
    }

    // 👇 Filtro de nombre sigue siendo local
    val listaFiltrada by remember(nombreBusqueda, listaGrupos) {
        derivedStateOf {
            listaGrupos.filter { grupo ->
                grupo.NombreGrupo.contains(nombreBusqueda, ignoreCase = true)
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
            text = "DIARIO GRUPO",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            DateBoxGrupo(
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
            GrupoTable(lista = listaFiltrada)
            Spacer(Modifier.height(26.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateBoxGrupo(
    label: String,
    fecha: LocalDate,
    onFechaChange: (LocalDate) -> Unit
) {

    var showDialog by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = fecha
            .atStartOfDay(ZoneId.of("UTC"))
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
                                .atZone(ZoneId.of("UTC"))
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

object GrupoTableWeights {
    const val ACCION = 1.2f
    const val GRUPO = 0.6f
    const val NOMBRE_GRUPO = 1.8f
    const val NUM_CLICLO = 0.6f
    const val NOMBRE_REPRESENTANTE = 1.5f
    const val NUM_INTEGRANTES = 1.0f
    const val FECHA_PAGO = 1.5f
    const val NUM_PAGO = 0.5f
    const val DE = 0.5f
    const val PRESTAMO_TOTAL = 1.0f
    const val LIQUIDA = 1.0f
    const val PLAZO = 1.0f
}

@Composable
fun GrupoTable(
    lista: List<DiarioGrupoItem>,
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
                /*TableGrupoHeader("Acción", GrupoTableWeights.ACCION)*/
                TableGrupoHeader("Grupo", GrupoTableWeights.GRUPO)
                TableGrupoHeader("NombreGrupo", GrupoTableWeights.NOMBRE_GRUPO)
                TableGrupoHeader("Ciclo", GrupoTableWeights.NUM_CLICLO)
                TableGrupoHeader("Representante", GrupoTableWeights.NOMBRE_REPRESENTANTE)
                TableGrupoHeader("Integrantes", GrupoTableWeights.NUM_INTEGRANTES)
                TableGrupoHeader("FechaPago", GrupoTableWeights.FECHA_PAGO)
                TableGrupoHeader("Pago", GrupoTableWeights.NUM_PAGO)
                TableGrupoHeader("De", GrupoTableWeights.DE)
                TableGrupoHeader("Prestamo", GrupoTableWeights.PRESTAMO_TOTAL)
                TableGrupoHeader("Liquida", GrupoTableWeights.LIQUIDA)
                TableGrupoHeader("Plazo", GrupoTableWeights.PLAZO)
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
                        /*TableGrupoCell("", GrupoTableWeights.ACCION)*/
                        TableGrupoCell(item.Grupo.toString(), GrupoTableWeights.GRUPO)
                        TableGrupoCell(item.NombreGrupo, GrupoTableWeights.NOMBRE_GRUPO)
                        TableGrupoCell(item.NumCiclo.toString(), GrupoTableWeights.NUM_CLICLO)
                        TableGrupoCell(item.NombreRepresentante, GrupoTableWeights.NOMBRE_REPRESENTANTE)
                        TableGrupoCell(item.NumIntegrantes.toString(), GrupoTableWeights.NUM_INTEGRANTES)
                        TableGrupoCell(item.FechaPago.toString(), GrupoTableWeights.FECHA_PAGO)
                        TableGrupoCell(item.NumPago.toString(), GrupoTableWeights.NUM_PAGO)
                        TableGrupoCell(item.De.toString(), GrupoTableWeights.DE)
                        TableGrupoCell(item.PrestamoTotal.toString(), GrupoTableWeights.PRESTAMO_TOTAL)
                        TableGrupoCell(item.Liquida.toString(), GrupoTableWeights.LIQUIDA)
                        TableGrupoCell(item.Plazo.toString(), GrupoTableWeights.PLAZO)

                    }
                }
            }
        }
    }
}

@Composable
fun RowScope.TableGrupoHeader(text: String, weight: Float) {
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
fun RowScope.TableGrupoCell(text: String, weight: Float) {
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