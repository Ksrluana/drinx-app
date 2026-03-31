package com.example.drinx.cadastrogerais.ui

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.drinx.cadastrogerais.model.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaCadastroPessoa(
    pessoaInicial: Pessoa? = null,
    onVoltar: () -> Unit,
    onSalvar: (Pessoa) -> Unit
) {
    val contexto = LocalContext.current
    val modoEdicao = pessoaInicial != null

    var tipoDocumento by remember { mutableStateOf(pessoaInicial?.tipoDocumento ?: TipoDocumento.CPF) }
    var documento by remember { mutableStateOf(pessoaInicial?.documento ?: "") }
    var nome by remember { mutableStateOf(pessoaInicial?.nome ?: "") }
    var email by remember { mutableStateOf(pessoaInicial?.email ?: "") }
    var contato by remember { mutableStateOf(pessoaInicial?.contato ?: "") }
    var endereco by remember { mutableStateOf(pessoaInicial?.endereco ?: "") }
    var flag by remember { mutableStateOf(pessoaInicial?.flag ?: FlagPessoa.CLIENTE) }
    var situacao by remember { mutableStateOf(pessoaInicial?.situacao ?: SituacaoPessoa.ATIVO) }
    var observacao by remember { mutableStateOf(pessoaInicial?.observacao ?: "") }

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {

        // Header padrão DRINX
        CadastroGeralHeader(
            titulo = if (modoEdicao) "Editar Cadastro" else "Novo Cadastro",
            subtitulo = if (modoEdicao) "Atualize os dados cadastrais" else "Preencha as informações abaixo"
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Card com os campos
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = if (modoEdicao) "Atualize os dados cadastrais" else "Dados cadastrais",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    HorizontalDivider(color = Color(0xFFE0E0E0))

                    DropdownEnumCampo(
                        label = "Tipo de documento",
                        opcoes = TipoDocumento.entries,
                        valorSelecionado = tipoDocumento,
                        textoOpcao = { it.descricao },
                        onOpcaoSelecionada = { tipoDocumento = it }
                    )
                    OutlinedTextField(
                        value = documento, onValueChange = { documento = it },
                        label = { Text("CPF ou CNPJ") },
                        modifier = Modifier.fillMaxWidth(), singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color(0xFFCCCCCC)
                        )
                    )
                    OutlinedTextField(
                        value = nome, onValueChange = { nome = it },
                        label = { Text("Nome") },
                        modifier = Modifier.fillMaxWidth(), singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color(0xFFCCCCCC)
                        )
                    )
                    OutlinedTextField(
                        value = email, onValueChange = { email = it },
                        label = { Text("E-mail") },
                        modifier = Modifier.fillMaxWidth(), singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color(0xFFCCCCCC)
                        )
                    )
                    OutlinedTextField(
                        value = contato, onValueChange = { contato = it },
                        label = { Text("Número para contato") },
                        modifier = Modifier.fillMaxWidth(), singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color(0xFFCCCCCC)
                        )
                    )
                    OutlinedTextField(
                        value = endereco, onValueChange = { endereco = it },
                        label = { Text("Endereço") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color(0xFFCCCCCC)
                        )
                    )
                    DropdownEnumCampo(
                        label = "Flag",
                        opcoes = FlagPessoa.entries,
                        valorSelecionado = flag,
                        textoOpcao = { it.descricao },
                        onOpcaoSelecionada = { flag = it }
                    )
                    DropdownEnumCampo(
                        label = "Situação",
                        opcoes = SituacaoPessoa.entries,
                        valorSelecionado = situacao,
                        textoOpcao = { it.descricao },
                        onOpcaoSelecionada = { situacao = it }
                    )
                    OutlinedTextField(
                        value = observacao, onValueChange = { observacao = it },
                        label = { Text("Observação") },
                        modifier = Modifier.fillMaxWidth(), minLines = 3,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color(0xFFCCCCCC)
                        )
                    )
                }
            }

            // Botão Salvar — preto
            Button(
                onClick = {
                    when {
                        documento.isBlank() || nome.isBlank() || email.isBlank() ||
                                contato.isBlank() || endereco.isBlank() ->
                            Toast.makeText(contexto, "Preencha todos os campos obrigatórios", Toast.LENGTH_SHORT).show()
                        !Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                            Toast.makeText(contexto, "E-mail inválido", Toast.LENGTH_SHORT).show()
                        else -> onSalvar(
                            Pessoa(
                                id = pessoaInicial?.id ?: 0,
                                tipoDocumento = tipoDocumento, documento = documento,
                                nome = nome, email = email, contato = contato,
                                endereco = endereco, flag = flag, situacao = situacao,
                                observacao = observacao
                            )
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                )
            ) {
                Text(
                    if (modoEdicao) "Atualizar" else "Salvar",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }

            // Botão Cancelar — outlined preto
            OutlinedButton(
                onClick = onVoltar,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black),
                border = BorderStroke(1.dp, Color.Black)
            ) {
                Text("Cancelar", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
