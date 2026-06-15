package com.example.drinx.cadastrogerais.ui

import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.drinx.cadastrogerais.model.*
import com.example.drinx.cadastrogerais.viewmodel.CadastroGeralViewModel

@Composable
fun CadastroGeralApp(
    onAbrirMenu: () -> Unit = {}
) {
    val contexto = LocalContext.current
    val viewModel: CadastroGeralViewModel = viewModel()

    val pessoas = viewModel.pessoas

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
                        viewModel.excluirPessoa(pessoa.id)
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
                        viewModel.adicionarPessoa(novaPessoa)
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
                            Toast.makeText(
                                contexto,
                                "Já existe outro cadastro com esse CPF/CNPJ!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            val atualizada = pessoaEditada.copy(id = pessoaAtual.id)
                            viewModel.atualizarPessoa(atualizada)
                            pessoaSelecionada = atualizada
                            Toast.makeText(contexto, "Cadastro atualizado!", Toast.LENGTH_SHORT).show()
                            telaAtual = TelaAtualCadastro.DETALHES
                        }
                    }
                )
            }
        }
    }
}