package com.example.drinx.eventos.model

data class Evento(
    val id: Int,
    val contratante: String,
    val cpfCnpj: String,
    val telefone: String,
    val email: String,
    val dataEvento: String,
    val dataCadastro: String,
    val status: String,
    val local: String,
    val quantidadePessoas: Int,
    val duracao: String,
    val cardapio: String,
    val grupoEquipamentos: String,
    val quantidadeIntegrantes: Int,
    val equipe: String,
    val funcaoEquipe: String,
    val observacao: String
)
