package com.example.drinx.estoque.model

import java.time.LocalDate

enum class Categoria(val label: String) {
    ALIMENTOS("Alimentos"),
    BEBIDAS("Bebidas"),
    LIMPEZA("Limpeza"),
    HIGIENE("Higiene"),
    OUTROS("Outros")
}

enum class UnidadeMedida(val label: String) {
    LITRO("Litro"),
    KG("KG"),
    GRAMA("Grama"),
    MG("MG"),
    UNIDADE("Unidade")
}

enum class TipoProduto(val label: String) {
    PERECIVEL("Perecível"),
    NAO_PERECIVEL("Não Perecível")
}

enum class FiltroValidade(val label: String) {
    TODOS("Todos"),
    VENCIDO("Vencido"),
    PROXIMO_VENCER("Próximo a vencer"),
    NO_PRAZO("No prazo")
}

data class Produto(
    val id: Int = 0,
    val nome: String = "",
    val categoria: Categoria = Categoria.OUTROS,
    val unidadeMedida: UnidadeMedida = UnidadeMedida.UNIDADE,
    val medida: String = "",
    val quantidade: Int = 0,
    val dataValidade: LocalDate? = null,
    val tipo: TipoProduto = TipoProduto.NAO_PERECIVEL
) {
    fun statusValidade(): FiltroValidade {
        if (dataValidade == null) return FiltroValidade.NO_PRAZO
        val hoje = LocalDate.now()
        return when {
            dataValidade.isBefore(hoje) -> FiltroValidade.VENCIDO
            dataValidade.isBefore(hoje.plusDays(30)) -> FiltroValidade.PROXIMO_VENCER
            else -> FiltroValidade.NO_PRAZO
        }
    }
}