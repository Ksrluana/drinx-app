package com.example.drinx.estoque.viewmodel

import androidx.lifecycle.ViewModel
import com.example.drinx.estoque.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate

data class EstoqueUiState(
    val produtos: List<Produto> = emptyList(),
    val filtroCategoria: Categoria? = null,
    val filtroValidade: FiltroValidade = FiltroValidade.TODOS,
    val produtoSelecionado: Produto? = null
) {
    val produtosFiltrados: List<Produto>
        get() {
            var lista = produtos
            if (filtroCategoria != null) {
                lista = lista.filter { it.categoria == filtroCategoria }
            }
            if (filtroValidade != FiltroValidade.TODOS) {
                lista = lista.filter { it.statusValidade() == filtroValidade }
            }
            return lista
        }
}

class EstoqueViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(EstoqueUiState())
    val uiState: StateFlow<EstoqueUiState> = _uiState.asStateFlow()

    private var nextId = 1

    init {

        val exemplos = listOf(
            Produto(
                id = nextId++, nome = "Vodka", categoria = Categoria.BEBIDAS,
                unidadeMedida = UnidadeMedida.LITRO, medida = "1", quantidade = 20,
                dataValidade = LocalDate.now().plusMonths(6), tipo = TipoProduto.NAO_PERECIVEL
            ),
            Produto(
                id = nextId++, nome = "Tequila", categoria = Categoria.BEBIDAS,
                unidadeMedida = UnidadeMedida.LITRO, medida = "1", quantidade = 10,
                dataValidade = LocalDate.now().plusDays(10), tipo = TipoProduto.PERECIVEL
            ),
            Produto(
                id = nextId++, nome = "Limão", categoria = Categoria.ALIMENTOS,
                unidadeMedida = UnidadeMedida.KG, medida = "0.5", quantidade = 15,
                dataValidade = null, tipo = TipoProduto.NAO_PERECIVEL
            ),
            Produto(
                id = nextId++, nome = "Morango", categoria = Categoria.ALIMENTOS,
                unidadeMedida = UnidadeMedida.KG, medida = "0.1", quantidade = 8,
                dataValidade = LocalDate.now().minusDays(3), tipo = TipoProduto.PERECIVEL
            )
        )
        _uiState.value = _uiState.value.copy(produtos = exemplos)
    }

    fun adicionarProduto(produto: Produto) {
        val novo = produto.copy(id = nextId++)
        _uiState.value = _uiState.value.copy(
            produtos = _uiState.value.produtos + novo
        )
    }

    fun atualizarProduto(produto: Produto) {
        _uiState.value = _uiState.value.copy(
            produtos = _uiState.value.produtos.map {
                if (it.id == produto.id) produto else it
            }
        )
    }

    fun excluirProduto(id: Int) {
        _uiState.value = _uiState.value.copy(
            produtos = _uiState.value.produtos.filter { it.id != id }
        )
    }

    fun selecionarProduto(produto: Produto?) {
        _uiState.value = _uiState.value.copy(produtoSelecionado = produto)
    }

    fun setFiltroCategoria(categoria: Categoria?) {
        _uiState.value = _uiState.value.copy(filtroCategoria = categoria)
    }

    fun setFiltroValidade(filtro: FiltroValidade) {
        _uiState.value = _uiState.value.copy(filtroValidade = filtro)
    }
}