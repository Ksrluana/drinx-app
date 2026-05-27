package com.example.drinx.eventos.model

data class EventoFirebase(
    val id: String = "",
    val contratante: String = "",
    val cpfCnpj: String = "",
    val telefone: String = "",
    val email: String = "",
    val dataEvento: String = "",
    val dataCadastro: String = "",
    val status: String = "",
    val local: String = "",
    val quantidadePessoas: Int = 0,
    val duracao: String = "",
    val cardapio: String = "",
    val grupoEquipamentos: String = "",
    val quantidadeIntegrantes: Int = 0,
    val equipe: String = "",
    val funcaoEquipe: String = "",
    val observacao: String = ""
)