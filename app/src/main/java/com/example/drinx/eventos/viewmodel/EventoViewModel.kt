package com.example.drinx.eventos.viewmodel

import androidx.lifecycle.ViewModel
import com.example.drinx.eventos.data.listaEventosFake
import com.example.drinx.eventos.model.Evento
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class EventoViewModel : ViewModel() {

    private val _eventos = MutableStateFlow(listaEventosFake.toMutableList())
    val eventos: StateFlow<List<Evento>> = _eventos.asStateFlow()

    private val _eventoSelecionado = MutableStateFlow<Evento?>(null)
    val eventoSelecionado: StateFlow<Evento?> = _eventoSelecionado.asStateFlow()

    fun adicionarEvento(evento: Evento) {
        val lista = _eventos.value.toMutableList()
        lista.add(0, evento)
        _eventos.value = lista
    }

    fun editarEvento(eventoAtualizado: Evento) {
        val lista = _eventos.value.toMutableList()
        val index = lista.indexOfFirst { it.id == eventoAtualizado.id }
        if (index != -1) {
            lista[index] = eventoAtualizado
            _eventos.value = lista
            _eventoSelecionado.value = eventoAtualizado
        }
    }

    fun excluirEvento(id: Int) {
        val lista = _eventos.value.toMutableList()
        lista.removeAll { it.id == id }
        _eventos.value = lista
        _eventoSelecionado.value = null
    }

    fun selecionarEvento(evento: Evento) {
        _eventoSelecionado.value = evento
    }
}
