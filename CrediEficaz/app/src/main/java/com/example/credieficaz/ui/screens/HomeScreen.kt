package com.example.credieficaz.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview(showBackground = true)
@Composable
fun HomeScreen(
    onClienteClick: () -> Unit = {},
    onGrupoClick: () -> Unit = {},
    onConsultaClienteClick: () -> Unit = {},
    onConsultaGrupoClick: () -> Unit = {},
    onConsultaClientePorClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Título arriba
            Text(
                text = "CREDIEFICAZ",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Fila con los botones al mismo nivel
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = onClienteClick,
                    modifier = Modifier
                        .width(150.dp)
                        .height(64.dp)
                ) {
                    Text(
                        "DIARIO CLIENTE",
                        fontSize = 20.sp
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = onGrupoClick,
                    modifier = Modifier
                        .width(150.dp)
                        .height(64.dp)
                ) {
                    Text(
                        "DIARIO GRUPO",
                        fontSize = 20.sp
                    )

                }
            }


            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = onConsultaClienteClick,
                    modifier = Modifier
                        .width(150.dp)
                        .height(64.dp)
                ) {
                    Text(
                        "CONSULTA CLIENTE",
                        fontSize = 20.sp
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = onConsultaGrupoClick,
                    modifier = Modifier
                        .width(150.dp)
                        .height(64.dp)
                ) {
                    Text(
                        "CONSULTA GRUPO",
                        fontSize = 20.sp
                    )

                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = onConsultaClientePorClick,
                    modifier = Modifier
                        .width(150.dp)
                        .height(64.dp)
                ) {
                    Text(
                        "CONSULTA CLIENTE %",
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun HomeScreenPreview() {
    MaterialTheme {
        Surface {
            HomeScreen()
        }
    }
}