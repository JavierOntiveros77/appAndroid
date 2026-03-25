package com.example.credieficaz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.example.credieficaz.navigation.AppNavigation

/*class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                /*HomeScreen()*/
                DiarioScreen(
                    onClienteClick = { /* navegar a pantalla cliente */ },
                    onGrupoClick = { /* navegar a pantalla grupo */ }
                )
                AppNavigation()
            }
        }
    }
}*/

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                AppNavigation()
            }
        }
    }
}
