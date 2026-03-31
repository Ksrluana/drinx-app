package com.example.drinx.cadastrogerais.model

enum class TipoDocumento(val descricao: String) {
    CPF("CPF"),
    CNPJ("CNPJ")
}

enum class FlagPessoa(val descricao: String) {
    CLIENTE("Cliente"),
    FORNECEDOR("Fornecedor"),
    FUNCIONARIO("Funcionário")
}

enum class SituacaoPessoa(val descricao: String) {
    ATIVO("Ativo"),
    INATIVO("Inativo")
}

enum class TelaAtualCadastro {
    LISTA, DETALHES, CADASTRO, EDITAR
}

data class Pessoa(
    val id: Int,
    val tipoDocumento: TipoDocumento,
    val documento: String,
    val nome: String,
    val email: String,
    val contato: String,
    val endereco: String,
    val flag: FlagPessoa,
    val situacao: SituacaoPessoa,
    val observacao: String
)
