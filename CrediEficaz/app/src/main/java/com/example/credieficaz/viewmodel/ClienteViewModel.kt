package com.example.credieficaz.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.credieficaz.models.DiarioClienteItem
import com.example.credieficaz.repository.ClienteRepository
import java.time.LocalDate

class ClienteViewModel : ViewModel() {

    private val repository = ClienteRepository()

    private val _listaClientes =
        MutableStateFlow<List<DiarioClienteItem>>(emptyList())

    val listaClientes: StateFlow<List<DiarioClienteItem>>
            = _listaClientes

    fun cargarClientes(fechaFin: LocalDate) {
        viewModelScope.launch {
            try {
                val fechaFormateada = String.format(
                    "%04d-%02d-%02d",
                    fechaFin.year,
                    fechaFin.monthValue,
                    fechaFin.dayOfMonth
                )
                val datos = repository.obtenerClientes(fechaFormateada)
                _listaClientes.value = datos
            } catch (e: Exception) {
                println(">>> ERROR: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}