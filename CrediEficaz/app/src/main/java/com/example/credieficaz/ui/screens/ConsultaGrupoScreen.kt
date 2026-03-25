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
import com.example.credieficaz.models.ConsultaGrupoItem
import com.example.credieficaz.models.DiarioGrupoItem
import com.example.credieficaz.viewmodel.ConsultaGrupoViewModel
import java.time.format.DateTimeFormatter
import kotlin.text.format

@Composable
fun ConsultaGrupoScreen(
    viewModel: ConsultaGrupoViewModel = viewModel(),
    onBack: () -> Unit = {}
) {
    var nombreBusqueda by remember { mutableStateOf("") }
    val listaGrupos by viewModel.listaGrupos.collectAsState()
    val listaFiltrada by remember(nombreBusqueda, listaGrupos) {
        derivedStateOf {
            listaGrupos.filter { grupo ->
                val coincideNombre = grupo.NombreGrupo.contains(nombreBusqueda, ignoreCase = true)
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
            text = "CONSULTA GRUPOS",
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
                    text = "Grupos:",
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
            GrupoTableConsulta(lista = listaFiltrada)
            Spacer(Modifier.height(26.dp))
        }
    }
}

object GrupoTableConsultaWeights {

    val ACCION = 90.dp
    val GRUPO = 80.dp
    val NOMBRE_GRUPO = 150.dp
    val NUM_CLICLO = 80.dp
    val NOMBRE_REPRESENTANTE = 150.dp
    val DOMICILIO = 150.dp
    val NUM_TELEFONO = 140.dp
    val NUM_INTEGRANTES = 90.dp
    val NUM_PAGOS = 90.dp
    val FECHA_PAGO = 130.dp
    val PRESTAMO = 100.dp
    val PRESTAMO_TOTAL = 100.dp
    val PENDIENTE = 100.dp
    val PLAZO = 90.dp
}

@Composable
fun GrupoTableConsulta(
    lista: List<ConsultaGrupoItem>,
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
                TableGrupoConsultaHeader("", GrupoTableConsultaWeights.ACCION)
                TableGrupoConsultaHeader("Grupo", GrupoTableConsultaWeights.GRUPO)
                TableGrupoConsultaHeader("NombreGrupo", GrupoTableConsultaWeights.NOMBRE_GRUPO)
                TableGrupoConsultaHeader("Ciclo", GrupoTableConsultaWeights.NUM_CLICLO)
                TableGrupoConsultaHeader("Representante", GrupoTableConsultaWeights.NOMBRE_REPRESENTANTE)
                TableGrupoConsultaHeader("Domicilio", GrupoTableConsultaWeights.DOMICILIO)
                TableGrupoConsultaHeader("NumTelefono", GrupoTableConsultaWeights.NUM_TELEFONO)
                TableGrupoConsultaHeader("Integrantes", GrupoTableConsultaWeights.NUM_INTEGRANTES)
                TableGrupoConsultaHeader("Pagos", GrupoTableConsultaWeights.NUM_PAGOS)
                TableGrupoConsultaHeader("FechaPago", GrupoTableConsultaWeights.FECHA_PAGO)
                TableGrupoConsultaHeader("Préstamo", GrupoTableConsultaWeights.PRESTAMO)
                TableGrupoConsultaHeader("Total", GrupoTableConsultaWeights.PRESTAMO_TOTAL)
                TableGrupoConsultaHeader("Pendiente", GrupoTableConsultaWeights.PENDIENTE)
                TableGrupoConsultaHeader("Plazo", GrupoTableConsultaWeights.PLAZO)
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
                        TableGrupoConsultaCell("", GrupoTableConsultaWeights.ACCION)
                        TableGrupoConsultaCell(item.Grupo.toString(), GrupoTableConsultaWeights.GRUPO)
                        TableGrupoConsultaCell(item.NombreGrupo, GrupoTableConsultaWeights.NOMBRE_GRUPO)
                        TableGrupoConsultaCell(item.NumCiclo.toString(), GrupoTableConsultaWeights.NUM_CLICLO)
                        TableGrupoConsultaCell(item.NombreRepresentante, GrupoTableConsultaWeights.NOMBRE_REPRESENTANTE)
                        TableGrupoConsultaCell(item.Domicilio, GrupoTableConsultaWeights.DOMICILIO)
                        TableGrupoConsultaCell(item.NumTelefono, GrupoTableConsultaWeights.NUM_TELEFONO)
                        TableGrupoConsultaCell(item.NumIntegrantes.toString(), GrupoTableConsultaWeights.NUM_INTEGRANTES)
                        TableGrupoConsultaCell(item.NumPagos, GrupoTableConsultaWeights.NUM_PAGOS)
                        TableGrupoConsultaCell(item.FechaPróximoPago.format(formato), GrupoTableConsultaWeights.FECHA_PAGO)
                        TableGrupoConsultaCell(item.Préstamo, GrupoTableConsultaWeights.PRESTAMO)
                        TableGrupoConsultaCell(item.PréstamoTotal, GrupoTableConsultaWeights.PRESTAMO_TOTAL)
                        TableGrupoConsultaCell(item.Pendiente, GrupoTableConsultaWeights.PENDIENTE)
                        TableGrupoConsultaCell(item.Plazo, GrupoTableConsultaWeights.PLAZO)
                    }
                }
            }
        }
    }
}

@Composable
fun RowScope.TableGrupoConsultaHeader(text: String, width: Dp) {
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
fun RowScope.TableGrupoConsultaCell(text: String, width: Dp) {
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