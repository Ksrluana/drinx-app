package com.example.drinx.cadastrogerais.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.drinx.R
import com.example.drinx.cadastrogerais.model.*

// ============================================================
// HEADER PADRÃO DRINX — reutilizado nas 3 telas
// ============================================================
@Composable
fun CadastroGeralHeader(
    titulo: String,
    subtitulo: String,
    onAbrirMenu: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
            .background(Color.Black)
            .padding(bottom = 28.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onAbrirMenu) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
                }
                Image(
                    painter = painterResource(id = R.drawable.logo_app),
                    contentDescription = "Logo",
                    modifier = Modifier.height(40.dp).widthIn(max = 130.dp),
                    contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.tint(Color.White)
                )
                IconButton(onClick = {}) {
                    Icon(Icons.Default.MoreVert, contentDescription = null, tint = Color.White)
                }
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(titulo, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text(subtitulo, color = Color.White.copy(alpha = 0.7f), fontSize = 13.sp)
            }
        }
    }
}

// ============================================================
// TELA LISTA
// ============================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaListaPessoas(
    pessoas: List<Pessoa>,
    onCadastrarClick: () -> Unit,
    onPessoaClick: (Pessoa) -> Unit,
    onAbrirMenu: () -> Unit = {}
) {
    var filtroFlag by remember { mutableStateOf("Todos") }
    var filtroSituacao by remember { mutableStateOf("Todos") }

    val flags = listOf("Todos") + FlagPessoa.entries.map { it.descricao }
    val situacoes = listOf("Todos") + SituacaoPessoa.entries.map { it.descricao }

    val pessoasFiltradas = pessoas.filter { pessoa ->
        val flagOk = filtroFlag == "Todos" || pessoa.flag.descricao == filtroFlag
        val situacaoOk = filtroSituacao == "Todos" || pessoa.situacao.descricao == filtroSituacao
        flagOk && situacaoOk
    }

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        // Header padrão DRINX
        CadastroGeralHeader(
            titulo = "Cadastro Geral",
            subtitulo = "Clientes, fornecedores e funcionários",
            onAbrirMenu = onAbrirMenu
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Card de filtros
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        "Filtros de busca",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    DropdownCampo(
                        label = "Flag", opcoes = flags,
                        valorSelecionado = filtroFlag, onOpcaoSelecionada = { filtroFlag = it }
                    )
                    DropdownCampo(
                        label = "Situação", opcoes = situacoes,
                        valorSelecionado = filtroSituacao, onOpcaoSelecionada = { filtroSituacao = it }
                    )
                    Button(
                        onClick = onCadastrarClick,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Novo cadastro", fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))
            Text(
                "Registros encontrados",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                "${pessoasFiltradas.size} cadastro(s)",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(14.dp))

            if (pessoasFiltradas.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                ) {
                    Text(
                        "Nenhum cadastro encontrado com os filtros selecionados.",
                        modifier = Modifier.padding(16.dp),
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(pessoasFiltradas) { pessoa ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onPessoaClick(pessoa) },
                            shape = RoundedCornerShape(18.dp),
                            border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(
                                    pessoa.nome,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                                Text(
                                    "${pessoa.tipoDocumento.descricao}: ${pessoa.documento}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Gray
                                )
                                HorizontalDivider(color = Color(0xFFE0E0E0))
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    AssistChip(
                                        onClick = {},
                                        label = { Text(pessoa.flag.descricao) },
                                        shape = RoundedCornerShape(10.dp),
                                        colors = AssistChipDefaults.assistChipColors(
                                            containerColor = Color(0xFFF0F0F0),
                                            labelColor = Color.Black
                                        ),
                                        border = AssistChipDefaults.assistChipBorder(
                                            borderColor = Color(0xFFCCCCCC),
                                            enabled = true
                                        )
                                    )
                                    AssistChip(
                                        onClick = {},
                                        label = { Text(pessoa.situacao.descricao) },
                                        shape = RoundedCornerShape(10.dp),
                                        colors = AssistChipDefaults.assistChipColors(
                                            containerColor = Color(0xFFF0F0F0),
                                            labelColor = Color.Black
                                        ),
                                        border = AssistChipDefaults.assistChipBorder(
                                            borderColor = Color(0xFFCCCCCC),
                                            enabled = true
                                        )
                                    )
                                }
                                Text(pessoa.email, style = MaterialTheme.typography.bodyMedium, color = Color.Black)
                                Text(pessoa.contato, style = MaterialTheme.typography.bodyMedium, color = Color.Black)
                            }
                        }
                    }
                }
            }
        }
    }
}

// ============================================================
// TELA DETALHES
// ============================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaDetalhesPessoa(
    pessoa: Pessoa,
    onVoltar: () -> Unit,
    onEditar: () -> Unit,
    onExcluir: () -> Unit
) {
    var mostrarDialogoExclusao by remember { mutableStateOf(false) }

    if (mostrarDialogoExclusao) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoExclusao = false },
            title = { Text("Confirmar exclusão", fontWeight = FontWeight.Bold) },
            text = { Text("Deseja realmente excluir o cadastro de ${pessoa.nome}?") },
            confirmButton = {
                Button(
                    onClick = { mostrarDialogoExclusao = false; onExcluir() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                ) { Text("Excluir") }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoExclusao = false }) {
                    Text("Cancelar", color = Color.Black)
                }
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        // Header padrão DRINX
        CadastroGeralHeader(
            titulo = "Detalhes do Cadastro",
            subtitulo = pessoa.nome
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

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
                        pessoa.nome,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        "${pessoa.tipoDocumento.descricao}: ${pessoa.documento}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                    HorizontalDivider(color = Color(0xFFE0E0E0))
                    CampoDetalhe("Tipo de documento", pessoa.tipoDocumento.descricao)
                    CampoDetalhe("CPF/CNPJ", pessoa.documento)
                    CampoDetalhe("Nome", pessoa.nome)
                    CampoDetalhe("E-mail", pessoa.email)
                    CampoDetalhe("Número para contato", pessoa.contato)
                    CampoDetalhe("Endereço", pessoa.endereco)
                    CampoDetalhe("Flag", pessoa.flag.descricao)
                    CampoDetalhe("Situação", pessoa.situacao.descricao)
                    CampoDetalhe(
                        "Observação",
                        if (pessoa.observacao.isBlank()) "Sem observação" else pessoa.observacao
                    )
                }
            }
            // Botão Voltar — outlined preto
            OutlinedButton(
                onClick = onVoltar,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black),
                border = BorderStroke(1.dp, Color.Black)
            ) {
                Text("Voltar", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
            // Botão Editar — preto
            Button(
                onClick = onEditar,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                )
            ) {
                Text("Editar", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }

            // Botão Excluir — outlined vermelho
            OutlinedButton(
                onClick = { mostrarDialogoExclusao = true },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                border = BorderStroke(1.dp, Color.Red)
            ) {
                Text("Excluir cadastro", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun CampoDetalhe(rotulo: String, valor: String) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            rotulo,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )
        Text(valor, style = MaterialTheme.typography.bodyLarge, color = Color.Black)
        HorizontalDivider(
            modifier = Modifier.padding(top = 6.dp),
            color = Color(0xFFE0E0E0)
        )
    }
}
