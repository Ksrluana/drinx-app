package com.example.drinx.eventos.viewmodel

import androidx.lifecycle.ViewModel
import com.example.drinx.eventos.model.Evento
import com.example.drinx.eventos.model.EventoFirebase
import com.google.firebase.database.*

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class EventoViewModel : ViewModel() {

    private val database =
        FirebaseDatabase.getInstance().getReference("eventos")

    private val _eventos = MutableStateFlow<List<Evento>>(emptyList())
    val eventos: StateFlow<List<Evento>> = _eventos.asStateFlow()

    private val _eventoSelecionado = MutableStateFlow<Evento?>(null)
    val eventoSelecionado: StateFlow<Evento?> = _eventoSelecionado.asStateFlow()

    init {
        carregarEventos()
    }

    private fun carregarEventos() {

        database.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                val lista = mutableListOf<Evento>()

                snapshot.children.forEach {

                    val eventoFirebase =
                        it.getValue(EventoFirebase::class.java)

                    eventoFirebase?.let { e ->

                        val evento = Evento(
                            id = e.id,
                            contratante = e.contratante,
                            cpfCnpj = e.cpfCnpj,
                            telefone = e.telefone,
                            email = e.email,
                            dataEvento = e.dataEvento,
                            dataCadastro = e.dataCadastro,
                            status = e.status,
                            local = e.local,
                            quantidadePessoas = e.quantidadePessoas,
                            duracao = e.duracao,
                            cardapio = e.cardapio,
                            grupoEquipamentos = e.grupoEquipamentos,
                            quantidadeIntegrantes = e.quantidadeIntegrantes,
                            equipe = e.equipe,
                            funcaoEquipe = e.funcaoEquipe,
                            observacao = e.observacao
                        )

                        lista.add(evento)
                    }
                }

                _eventos.value = lista
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun adicionarEvento(evento: Evento) {

        val id = database.push().key ?: return

        val eventoFirebase = EventoFirebase(
            id = id,
            contratante = evento.contratante,
            cpfCnpj = evento.cpfCnpj,
            telefone = evento.telefone,
            email = evento.email,
            dataEvento = evento.dataEvento,
            dataCadastro = evento.dataCadastro,
            status = evento.status,
            local = evento.local,
            quantidadePessoas = evento.quantidadePessoas,
            duracao = evento.duracao,
            cardapio = evento.cardapio,
            grupoEquipamentos = evento.grupoEquipamentos,
            quantidadeIntegrantes = evento.quantidadeIntegrantes,
            equipe = evento.equipe,
            funcaoEquipe = evento.funcaoEquipe,
            observacao = evento.observacao
        )

        database.child(id).setValue(eventoFirebase)
    }

    fun editarEvento(eventoAtualizado: Evento) {

        val eventoFirebase = EventoFirebase(
            id = eventoAtualizado.id,
            contratante = eventoAtualizado.contratante,
            cpfCnpj = eventoAtualizado.cpfCnpj,
            telefone = eventoAtualizado.telefone,
            email = eventoAtualizado.email,
            dataEvento = eventoAtualizado.dataEvento,
            dataCadastro = eventoAtualizado.dataCadastro,
            status = eventoAtualizado.status,
            local = eventoAtualizado.local,
            quantidadePessoas = eventoAtualizado.quantidadePessoas,
            duracao = eventoAtualizado.duracao,
            cardapio = eventoAtualizado.cardapio,
            grupoEquipamentos = eventoAtualizado.grupoEquipamentos,
            quantidadeIntegrantes = eventoAtualizado.quantidadeIntegrantes,
            equipe = eventoAtualizado.equipe,
            funcaoEquipe = eventoAtualizado.funcaoEquipe,
            observacao = eventoAtualizado.observacao
        )

        database.child(eventoAtualizado.id).setValue(eventoFirebase)

        _eventoSelecionado.value = eventoAtualizado
    }

    fun excluirEvento(id: String) {
        database.child(id).removeValue()
    }

    fun selecionarEvento(evento: Evento) {
        _eventoSelecionado.value = evento
    }
}