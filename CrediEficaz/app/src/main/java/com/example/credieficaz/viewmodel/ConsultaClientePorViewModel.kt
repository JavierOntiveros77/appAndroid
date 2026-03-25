package com.example.credieficaz.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.credieficaz.models.ConsultaClientePorItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.credieficaz.repository.ConsultaClientePorRepository


class ConsultaClientePorViewModel : ViewModel() {

    private val repository = ConsultaClientePorRepository()

    private val _listaClientesPor =
        MutableStateFlow<List<ConsultaClientePorItem>>(emptyList())

    val listaClientesPor: StateFlow<List<ConsultaClientePorItem>>
            = _listaClientesPor

    init {
        cargarClientes()
    }

    private fun cargarClientes() {

        viewModelScope.launch {

            try {

                val datos = repository.obtenerClientesPor()

                println("CLIENTES % RECIBIDOS: ${datos.size}")

                _listaClientesPor.value = datos

            } catch (e: Exception) {

                println("Error API: ${e.message}")

            }

        }
    }
}