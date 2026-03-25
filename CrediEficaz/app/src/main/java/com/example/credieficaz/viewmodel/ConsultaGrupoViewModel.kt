package com.example.credieficaz.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.credieficaz.models.ConsultaGrupoItem
import com.example.credieficaz.repository.ConsultaGrupoRepository


class ConsultaGrupoViewModel : ViewModel() {

    private val repository = ConsultaGrupoRepository()

    private val _listaGrupos =
        MutableStateFlow<List<ConsultaGrupoItem>>(emptyList())

    val listaGrupos: StateFlow<List<ConsultaGrupoItem>>
            = _listaGrupos

    init {
        cargarGrupos()
    }

    private fun cargarGrupos() {

        viewModelScope.launch {

            try {

                val datos = repository.obtenerGrupos()

                println("GRUPOS RECIBIDOS: ${datos.size}")

                _listaGrupos.value = datos

            } catch (e: Exception) {

                println("Error API: ${e.message}")

            }

        }
    }
}