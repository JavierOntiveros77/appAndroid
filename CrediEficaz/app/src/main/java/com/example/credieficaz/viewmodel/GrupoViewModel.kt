package com.example.credieficaz.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.credieficaz.models.DiarioGrupoItem
import com.example.credieficaz.repository.GrupoRepository
import java.time.LocalDate

class GrupoViewModel : ViewModel() {

    private val repository = GrupoRepository()

    private val _listaGrupos = MutableStateFlow<List<DiarioGrupoItem>>(emptyList())
    val listaGrupos: StateFlow<List<DiarioGrupoItem>> = _listaGrupos

    fun cargarGrupos(fechaFin: LocalDate) {
        viewModelScope.launch {
            try {
                val fechaFormateada = String.format(
                    "%04d-%02d-%02d",
                    fechaFin.year,
                    fechaFin.monthValue,
                    fechaFin.dayOfMonth
                )
                val datos = repository.obtenerGrupos(fechaFormateada)
                _listaGrupos.value = datos
            } catch (e: Exception) {
                println(">>> ERROR GRUPOS: ${e.message}")
            }
        }
    }
}