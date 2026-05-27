package com.example.drinx.estoque.viewmodel

import androidx.lifecycle.ViewModel
import com.example.drinx.estoque.model.*
import com.google.firebase.database.*
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
                lista = lista.filter {
                    it.categoria == filtroCategoria
                }
            }

            if (filtroValidade != FiltroValidade.TODOS) {
                lista = lista.filter {
                    it.statusValidade() == filtroValidade
                }
            }

            return lista
        }
}

class EstoqueViewModel : ViewModel() {

    private val _uiState =
        MutableStateFlow(EstoqueUiState())

    val uiState: StateFlow<EstoqueUiState> =
        _uiState.asStateFlow()

    private var nextId = 1

    private val database =
        FirebaseDatabase
            .getInstance()
            .getReference("produtos")

    init {
        carregarProdutos()
    }

    private fun carregarProdutos() {

        database.addValueEventListener(
            object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    val lista =
                        mutableListOf<Produto>()

                    snapshot.children.forEach {

                        val produtoFirebase =
                            it.getValue(
                                ProdutoFirebase::class.java
                            )

                        produtoFirebase?.let { p ->

                            val produto = Produto(
                                id = p.id,
                                nome = p.nome,
                                categoria = Categoria.valueOf(p.categoria),
                                unidadeMedida = UnidadeMedida.valueOf(p.unidadeMedida),
                                medida = p.medida,
                                quantidade = p.quantidade,
                                dataValidade = p.dataValidade?.let {
                                    LocalDate.parse(it)
                                },
                                tipo = TipoProduto.valueOf(p.tipo)
                            )

                            lista.add(produto)

                            if (produto.id >= nextId) {
                                nextId = produto.id + 1
                            }
                        }
                    }

                    _uiState.value =
                        _uiState.value.copy(
                            produtos = lista
                        )
                }

                override fun onCancelled(error: DatabaseError) {
                }
            }
        )
    }

    fun adicionarProduto(produto: Produto) {

        val novo =
            produto.copy(id = nextId++)

        val firebaseProduto =
            ProdutoFirebase(
                id = novo.id,
                nome = novo.nome,
                categoria = novo.categoria.name,
                unidadeMedida = novo.unidadeMedida.name,
                medida = novo.medida,
                quantidade = novo.quantidade,
                dataValidade = novo.dataValidade?.toString(),
                tipo = novo.tipo.name
            )

        database
            .child(novo.id.toString())
            .setValue(firebaseProduto)
    }

    fun atualizarProduto(produto: Produto) {

        val firebaseProduto =
            ProdutoFirebase(
                id = produto.id,
                nome = produto.nome,
                categoria = produto.categoria.name,
                unidadeMedida = produto.unidadeMedida.name,
                medida = produto.medida,
                quantidade = produto.quantidade,
                dataValidade = produto.dataValidade?.toString(),
                tipo = produto.tipo.name
            )

        database
            .child(produto.id.toString())
            .setValue(firebaseProduto)
    }

    fun excluirProduto(id: Int) {

        database
            .child(id.toString())
            .removeValue()
    }

    fun selecionarProduto(produto: Produto?) {

        _uiState.value =
            _uiState.value.copy(
                produtoSelecionado = produto
            )
    }

    fun setFiltroCategoria(categoria: Categoria?) {

        _uiState.value =
            _uiState.value.copy(
                filtroCategoria = categoria
            )
    }

    fun setFiltroValidade(filtro: FiltroValidade) {

        _uiState.value =
            _uiState.value.copy(
                filtroValidade = filtro
            )
    }
}