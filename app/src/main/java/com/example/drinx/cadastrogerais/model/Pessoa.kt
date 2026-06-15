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
    val id: Int = 0,
    val tipoDocumento: TipoDocumento = TipoDocumento.CPF,
    val documento: String = "",
    val nome: String = "",
    val email: String = "",
    val contato: String = "",
    val endereco: String = "",
    val flag: FlagPessoa = FlagPessoa.CLIENTE,
    val situacao: SituacaoPessoa = SituacaoPessoa.ATIVO,
    val observacao: String = ""
)

data class PessoaFirebase(
    val id: Int = 0,
    val tipoDocumento: String = "",
    val documento: String = "",
    val nome: String = "",
    val email: String = "",
    val contato: String = "",
    val endereco: String = "",
    val flag: String = "",
    val situacao: String = "",
    val observacao: String = ""
)