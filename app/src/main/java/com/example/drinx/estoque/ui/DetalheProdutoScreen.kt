package com.example.drinx.estoque.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.drinx.estoque.model.*
import com.example.drinx.estoque.viewmodel.EstoqueViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.drinx.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalheProdutoScreen(
    produto: Produto,
    viewModel: EstoqueViewModel,
    onVoltar: () -> Unit
) {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    var modoEdicao by remember { mutableStateOf(false) }
    var mostrarDialogoExcluir by remember { mutableStateOf(false) }

    // Campos editáveis
    var nome by remember { mutableStateOf(produto.nome) }
    var categoria by remember { mutableStateOf(produto.categoria) }
    var unidade by remember { mutableStateOf(produto.unidadeMedida) }
    var medida by remember { mutableStateOf(produto.medida) }
    var quantidade by remember { mutableStateOf(produto.quantidade.toString()) }
    var dataValidade by remember {
        mutableStateOf(produto.dataValidade?.format(formatter) ?: "")
    }
    var tipo by remember { mutableStateOf(produto.tipo) }

    var dropdownCategoriaAberto by remember { mutableStateOf(false) }
    var dropdownUnidadeAberto by remember { mutableStateOf(false) }
    var dropdownTipoAberto by remember { mutableStateOf(false) }

    // Diálogo de confirmação de exclusão
    if (mostrarDialogoExcluir) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoExcluir = false },
            title = { Text("Excluir produto?") },
            text = { Text("Tem certeza que deseja excluir \"${produto.nome}\"? Esta ação não pode ser desfeita.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.excluirProduto(produto.id)
                        mostrarDialogoExcluir = false
                        onVoltar()
                    }
                ) {
                    Text("Excluir", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoExcluir = false }) {
                    Text("Cancelar", color = Color.Black)
                }
            }
        )
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
                    .padding(bottom = 24.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onVoltar) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = Color.White)
                    }
                    // Logo placeholder
                    Image(
                        painter = painterResource(id = R.drawable.logo_app),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .height(32.dp)
                            .widthIn(max = 100.dp),
                        contentScale = ContentScale.Fit,
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menu", tint = Color.White)
                    }
                }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (modoEdicao) "Editar Produto" else "Detalhe do Produto",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = produto.nome,
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 13.sp
                    )
                }
            }
        },
        containerColor = Color.White
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            if (!modoEdicao) {

                InfoCard(produto = produto, formatter = formatter)
            } else {

                CampoTexto(
                    label = "Nome do Produto",
                    value = nome,
                    onValueChange = { nome = it }
                )
                DropdownCampo(
                    label = "Categoria",
                    opcaoSelecionada = categoria.label,
                    expanded = dropdownCategoriaAberto,
                    onExpandedChange = { dropdownCategoriaAberto = it },
                    opcoes = Categoria.entries.map { it.label },
                    onOpcaoSelecionada = { index ->
                        categoria = Categoria.entries[index]
                        dropdownCategoriaAberto = false
                    }
                )
                DropdownCampo(
                    label = "Unidade de Medida",
                    opcaoSelecionada = unidade.label,
                    expanded = dropdownUnidadeAberto,
                    onExpandedChange = { dropdownUnidadeAberto = it },
                    opcoes = UnidadeMedida.entries.map { it.label },
                    onOpcaoSelecionada = { index ->
                        unidade = UnidadeMedida.entries[index]
                        dropdownUnidadeAberto = false
                    }
                )
                CampoTexto(
                    label = "Medida (ex: 1, 500, 2.5)",
                    value = medida,
                    onValueChange = { medida = it },
                    keyboardType = KeyboardType.Decimal
                )
                CampoTexto(
                    label = "Quantidade em Estoque",
                    value = quantidade,
                    onValueChange = { quantidade = it },
                    keyboardType = KeyboardType.Number
                )
                CampoTexto(
                    label = "Data de Validade (dd/mm/aaaa)",
                    value = dataValidade,
                    onValueChange = { dataValidade = it },
                    keyboardType = KeyboardType.Number
                )
                DropdownCampo(
                    label = "Tipo",
                    opcaoSelecionada = tipo.label,
                    expanded = dropdownTipoAberto,
                    onExpandedChange = { dropdownTipoAberto = it },
                    opcoes = TipoProduto.entries.map { it.label },
                    onOpcaoSelecionada = { index ->
                        tipo = TipoProduto.entries[index]
                        dropdownTipoAberto = false
                    }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))


            if (!modoEdicao) {
                // Botão Editar
                Button(
                    onClick = { modoEdicao = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Editar", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }


                OutlinedButton(
                    onClick = { mostrarDialogoExcluir = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = androidx.compose.ui.graphics.SolidColor(Color.Red)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Excluir", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }

            } else {

                Button(
                    onClick = {
                        val validadeDate = try {
                            if (dataValidade.isNotBlank())
                                LocalDate.parse(dataValidade, formatter)
                            else null
                        } catch (e: Exception) { null }

                        val produtoAtualizado = produto.copy(
                            nome = nome.trim(),
                            categoria = categoria,
                            unidadeMedida = unidade,
                            medida = medida.trim(),
                            quantidade = quantidade.toIntOrNull() ?: 0,
                            dataValidade = validadeDate,
                            tipo = tipo
                        )
                        viewModel.atualizarProduto(produtoAtualizado)
                        viewModel.selecionarProduto(produtoAtualizado)
                        modoEdicao = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Salvar Alterações", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }


                OutlinedButton(
                    onClick = {

                        nome = produto.nome
                        categoria = produto.categoria
                        unidade = produto.unidadeMedida
                        medida = produto.medida
                        quantidade = produto.quantidade.toString()
                        dataValidade = produto.dataValidade?.format(formatter) ?: ""
                        tipo = produto.tipo
                        modoEdicao = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = androidx.compose.ui.graphics.SolidColor(Color.Black)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Cancelar", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }
        }
    }
}

@Composable
fun InfoCard(produto: Produto, formatter: DateTimeFormatter) {
    val statusValidade = produto.statusValidade()
    val corValidade = when (statusValidade) {
        FiltroValidade.VENCIDO -> Color.Red
        FiltroValidade.PROXIMO_VENCER -> Color(0xFFB8860B)
        else -> Color(0xFF2E7D32)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            LinhaInfo("Nome", produto.nome)
            Divider(color = Color(0xFFE0E0E0))
            LinhaInfo("Categoria", produto.categoria.label)
            Divider(color = Color(0xFFE0E0E0))
            LinhaInfo("Medida", "${produto.medida} ${produto.unidadeMedida.label}")
            Divider(color = Color(0xFFE0E0E0))
            LinhaInfo("Quantidade em estoque", produto.quantidade.toString())
            Divider(color = Color(0xFFE0E0E0))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Validade", fontSize = 13.sp, color = Color.Gray)
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = produto.dataValidade?.format(formatter) ?: "Sem validade",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = corValidade
                    )
                    Text(
                        text = statusValidade.label,
                        fontSize = 11.sp,
                        color = corValidade
                    )
                }
            }
            Divider(color = Color(0xFFE0E0E0))
            LinhaInfo("Tipo", produto.tipo.label)
        }
    }
}

@Composable
fun LinhaInfo(label: String, valor: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 13.sp, color = Color.Gray)
        Text(
            text = valor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
    }
}