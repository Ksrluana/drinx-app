package com.example.drinx.cadastrogerais.ui

import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.drinx.cadastrogerais.model.*

@Composable
fun CadastroGeralApp(
    onAbrirMenu: () -> Unit = {}
) {
    val contexto = LocalContext.current

    val pessoas = remember {
        mutableStateListOf(
            Pessoa(
                id = 1, tipoDocumento = TipoDocumento.CPF, documento = "123.456.789-00",
                nome = "Maria Oliveira", email = "maria@email.com", contato = "(62) 99999-1111",
                endereco = "Rua das Flores", flag = FlagPessoa.CLIENTE,
                situacao = SituacaoPessoa.ATIVO, observacao = "Cliente VIP"
            ),
            Pessoa(
                id = 2, tipoDocumento = TipoDocumento.CNPJ, documento = "12.345.678/0001-99",
                nome = "Buffet Sabor", email = "buffet@email.com", contato = "(62) 3333-2222",
                endereco = "Av. Central", flag = FlagPessoa.FORNECEDOR,
                situacao = SituacaoPessoa.ATIVO, observacao = "Fornecedor principal"
            )
        )
    }

    var telaAtual by remember { mutableStateOf(TelaAtualCadastro.LISTA) }
    var pessoaSelecionada by remember { mutableStateOf<Pessoa?>(null) }

    when (telaAtual) {
        TelaAtualCadastro.LISTA -> {
            TelaListaPessoas(
                pessoas = pessoas,
                onCadastrarClick = {
                    pessoaSelecionada = null
                    telaAtual = TelaAtualCadastro.CADASTRO
                },
                onPessoaClick = {
                    pessoaSelecionada = it
                    telaAtual = TelaAtualCadastro.DETALHES
                },
                onAbrirMenu = onAbrirMenu
            )
        }

        TelaAtualCadastro.DETALHES -> {
            pessoaSelecionada?.let { pessoa ->
                TelaDetalhesPessoa(
                    pessoa = pessoa,
                    onVoltar = { telaAtual = TelaAtualCadastro.LISTA },
                    onEditar = { telaAtual = TelaAtualCadastro.EDITAR },
                    onExcluir = {
                        pessoas.removeAll { it.id == pessoa.id }
                        pessoaSelecionada = null
                        Toast.makeText(contexto, "Cadastro excluído!", Toast.LENGTH_SHORT).show()
                        telaAtual = TelaAtualCadastro.LISTA
                    }
                )
            }
        }

        TelaAtualCadastro.CADASTRO -> {
            TelaCadastroPessoa(
                pessoaInicial = null,
                onVoltar = { telaAtual = TelaAtualCadastro.LISTA },
                onSalvar = { novaPessoa ->
                    val jaExiste = pessoas.any { it.documento == novaPessoa.documento }
                    if (jaExiste) {
                        Toast.makeText(contexto, "CPF/CNPJ já cadastrado!", Toast.LENGTH_SHORT).show()
                    } else {
                        val novoId = if (pessoas.isEmpty()) 1 else pessoas.maxOf { it.id } + 1
                        pessoas.add(novaPessoa.copy(id = novoId))
                        Toast.makeText(contexto, "Cadastro realizado!", Toast.LENGTH_SHORT).show()
                        telaAtual = TelaAtualCadastro.LISTA
                    }
                }
            )
        }

        TelaAtualCadastro.EDITAR -> {
            pessoaSelecionada?.let { pessoaAtual ->
                TelaCadastroPessoa(
                    pessoaInicial = pessoaAtual,
                    onVoltar = { telaAtual = TelaAtualCadastro.DETALHES },
                    onSalvar = { pessoaEditada ->
                        val documentoDuplicado = pessoas.any {
                            it.id != pessoaAtual.id && it.documento == pessoaEditada.documento
                        }
                        if (documentoDuplicado) {
                            Toast.makeText(contexto, "Já existe outro cadastro com esse CPF/CNPJ!", Toast.LENGTH_SHORT).show()
                        } else {
                            val indice = pessoas.indexOfFirst { it.id == pessoaAtual.id }
                            if (indice != -1) {
                                val atualizada = pessoaEditada.copy(id = pessoaAtual.id)
                                pessoas[indice] = atualizada
                                pessoaSelecionada = atualizada
                                Toast.makeText(contexto, "Cadastro atualizado!", Toast.LENGTH_SHORT).show()
                                telaAtual = TelaAtualCadastro.DETALHES
                            }
                        }
                    }
                )
            }
        }
    }
}
