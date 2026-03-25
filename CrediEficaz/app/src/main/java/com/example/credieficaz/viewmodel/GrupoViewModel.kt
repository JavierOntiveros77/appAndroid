package com.example.credieficaz.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.credieficaz.models.DiarioGrupoItem
import com.example.credieficaz.repository.GrupoRepository


class GrupoViewModel : ViewModel() {

    private val repository = GrupoRepository()

    private val _listaGrupos =
        MutableStateFlow<List<DiarioGrupoItem>>(emptyList())

    val listaGrupos: StateFlow<List<DiarioGrupoItem>>
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