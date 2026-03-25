package com.example.credieficaz.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.credieficaz.models.DiarioClienteItem
import com.example.credieficaz.repository.ClienteRepository


class ClienteViewModel : ViewModel() {

    private val repository = ClienteRepository()

    private val _listaClientes =
        MutableStateFlow<List<DiarioClienteItem>>(emptyList())

    val listaClientes: StateFlow<List<DiarioClienteItem>>
            = _listaClientes

    init {
        cargarClientes()
    }

    private fun cargarClientes() {

        viewModelScope.launch {

            try {

                val datos = repository.obtenerClientes()

                println("CLIENTES RECIBIDOS: ${datos.size}")

                _listaClientes.value = datos

            } catch (e: Exception) {

                println("Error API: ${e.message}")

            }

        }
    }
}