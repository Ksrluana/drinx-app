package com.example.drinx.estoque.model

data class ProdutoFirebase(
    val id: Int = 0,
    val nome: String = "",
    val categoria: String = "",
    val unidadeMedida: String = "",
    val medida: String = "",
    val quantidade: Int = 0,
    val dataValidade: String? = null,
    val tipo: String = ""
)