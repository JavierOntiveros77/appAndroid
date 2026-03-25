package com.example.credieficaz.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.credieficaz.models.ConsultaClienteItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.credieficaz.repository.ConsultaClienteRepository


class ConsultaClienteViewModel : ViewModel() {

    private val repository = ConsultaClienteRepository()

    private val _listaClientes =
        MutableStateFlow<List<ConsultaClienteItem>>(emptyList())

    val listaClientes: StateFlow<List<ConsultaClienteItem>>
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