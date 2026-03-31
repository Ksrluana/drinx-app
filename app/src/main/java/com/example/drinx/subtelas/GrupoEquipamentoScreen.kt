package com.example.drinx.subtelas

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.drinx.R
import com.example.drinx.equipamentos.model.InventoryItem

// Modelos internos do Grupo
data class ItemGrupoEquip(val item: InventoryItem, val quantidade: String)
data class GrupoEquipamento(val id: Int, val nome: String, val itens: List<ItemGrupoEquip>)

// ============================================================
// TELA PRINCIPAL
// ============================================================

@Composable
fun GrupoEquipamentoScreen(
    onAbrirMenu: () -> Unit = {},
    itensEquipamentos: List<InventoryItem> = listOf()
) {
    // Lista de grupos cadastrados
    val grupos = remember {
        mutableStateListOf(
            GrupoEquipamento(id = 1, nome = "Grupo 50 Pessoas", itens = emptyList()),
            GrupoEquipamento(id = 2, nome = "Grupo 100 Pessoas", itens = emptyList())
        )
    }

    // Estados de navegação interna
    var grupoSelecionado by remember { mutableStateOf<GrupoEquipamento?>(null) }
    var modoEdicao by remember { mutableStateOf(false) }
    var mostrarFormularioCadastro by remember { mutableStateOf(false) }
    var grupoParaExcluir by remember { mutableStateOf<GrupoEquipamento?>(null) }

    // Diálogo de confirmação de exclusão
    grupoParaExcluir?.let { grupo ->
        AlertDialog(
            onDismissRequest = { grupoParaExcluir = null },
            title = { Text("Excluir grupo?", fontWeight = FontWeight.Bold) },
            text = { Text("Deseja excluir o grupo \"${grupo.nome}\"?") },
            confirmButton = {
                TextButton(onClick = {
                    grupos.remove(grupo)
                    if (grupoSelecionado == grupo) grupoSelecionado = null
                    grupoParaExcluir = null
                }) { Text("Excluir", color = Color.Red, fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { grupoParaExcluir = null }) { Text("Cancelar") }
            }
        )
    }

    // ===== TELA DE EDIÇÃO =====
    if (modoEdicao && grupoSelecionado != null) {
        FormularioGrupo(
            titulo = "Editar Grupo",
            grupoInicial = grupoSelecionado,
            itensDisponiveis = itensEquipamentos,
            onCancelar = { modoEdicao = false },
            onSalvar = { nomeAtualizado, itensAtualizados ->
                val index = grupos.indexOfFirst { it.id == grupoSelecionado!!.id }
                if (index != -1) {
                    val atualizado = grupoSelecionado!!.copy(nome = nomeAtualizado, itens = itensAtualizados)
                    grupos[index] = atualizado
                    grupoSelecionado = atualizado
                }
                modoEdicao = false
            }
        )
        return
    }

    // ===== TELA DE CADASTRO NOVO GRUPO =====
    if (mostrarFormularioCadastro) {
        FormularioGrupo(
            titulo = "Novo Grupo",
            grupoInicial = null,
            itensDisponiveis = itensEquipamentos,
            onCancelar = { mostrarFormularioCadastro = false },
            onSalvar = { nome, itens ->
                val novoId = if (grupos.isEmpty()) 1 else grupos.maxOf { it.id } + 1
                grupos.add(GrupoEquipamento(id = novoId, nome = nome, itens = itens))
                mostrarFormularioCadastro = false
            }
        )
        return
    }

    // ===== TELA PRINCIPAL =====
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize().background(Color.White)) {

            // Header padrão DRINX
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                    .background(Color.Black)
                    .padding(bottom = 28.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 12.dp),
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
                    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Grupos de Equipamentos", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Text("Logística por tamanho de evento", color = Color.White.copy(alpha = 0.7f), fontSize = 13.sp)
                    }
                }
            }

            // Lista de grupos + painel de detalhe
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                Text("Grupos Cadastrados", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = if (grupoSelecionado != null) 240.dp else 100.dp)
                ) {
                    items(grupos) { grupo ->
                        val isSelecionado = grupoSelecionado?.id == grupo.id
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    grupoSelecionado = if (isSelecionado) null else grupo
                                },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelecionado) Color.Black else Color.White
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                            border = BorderStroke(1.dp, if (isSelecionado) Color.Black else Color(0xFFE0E0E0))
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(48.dp)
                                            .clip(CircleShape)
                                            .background(if (isSelecionado) Color.White else Color.Black),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            Icons.Default.Inventory,
                                            contentDescription = null,
                                            tint = if (isSelecionado) Color.Black else Color.White,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            grupo.nome,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp,
                                            color = if (isSelecionado) Color.White else Color.Black
                                        )
                                        Text(
                                            "${grupo.itens.size} tipos de itens vinculados",
                                            fontSize = 12.sp,
                                            color = if (isSelecionado) Color.White.copy(alpha = 0.7f) else Color.Gray
                                        )
                                    }
                                }
                                IconButton(onClick = { grupoParaExcluir = grupo }) {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "Excluir",
                                        tint = if (isSelecionado) Color.White else Color.Gray,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // ===== PAINEL DE DETALHE + BOTÕES (aparece ao selecionar um grupo) =====
        grupoSelecionado?.let { grupo ->
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                HorizontalDivider(color = Color(0xFFE0E0E0))
                Spacer(modifier = Modifier.height(4.dp))

                // Itens do grupo
                if (grupo.itens.isNotEmpty()) {
                    Text("Itens do grupo:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.Black)
                    grupo.itens.forEach { itemGrupo ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("• ${itemGrupo.item.name}", fontSize = 13.sp, color = Color.Black, modifier = Modifier.weight(1f))
                            Text("${itemGrupo.quantidade} unid.", fontSize = 13.sp, color = Color.Gray)
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                } else {
                    Text("Nenhum item vinculado ainda.", fontSize = 13.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(4.dp))
                }

                // Botão Editar — preto
                Button(
                    onClick = { modoEdicao = true },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black, contentColor = Color.White)
                ) {
                    Text("Editar", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }
        }

        // Botão flutuante (só aparece quando nenhum grupo está selecionado)
        if (grupoSelecionado == null) {
            FloatingActionButton(
                onClick = { mostrarFormularioCadastro = true },
                modifier = Modifier.align(Alignment.BottomEnd).padding(24.dp),
                containerColor = Color.Black,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Novo grupo")
            }
        }
    }
}

// ============================================================
// FORMULÁRIO DE CADASTRO / EDIÇÃO DE GRUPO
// ============================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioGrupo(
    titulo: String,
    grupoInicial: GrupoEquipamento?,
    itensDisponiveis: List<InventoryItem>,
    onCancelar: () -> Unit,
    onSalvar: (nome: String, itens: List<ItemGrupoEquip>) -> Unit
) {
    var nomeGrupo by remember(grupoInicial) { mutableStateOf(grupoInicial?.nome ?: "") }

    // Mapa de quantidade por item: item -> quantidade digitada
    var quantidades by remember(grupoInicial) {
        mutableStateOf<Map<InventoryItem, String>>(
            grupoInicial?.itens?.associate { it.item to it.quantidade } ?: emptyMap()
        )
    }

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {

        // Header padrão DRINX
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                .background(Color.Black)
                .padding(bottom = 28.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onCancelar) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = Color.White)
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
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(titulo, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Text("Defina os itens do grupo", color = Color.White.copy(alpha = 0.7f), fontSize = 13.sp)
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Nome do grupo
            OutlinedTextField(
                value = nomeGrupo,
                onValueChange = { nomeGrupo = it },
                label = { Text("Nome do grupo") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color(0xFFCCCCCC)
                )
            )

            // Itens disponíveis (vindos dos Equipamentos)
            Text("Itens disponíveis em Equipamentos:", fontWeight = FontWeight.Bold, fontSize = 14.sp)

            if (itensDisponiveis.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                ) {
                    Text(
                        "⚠️ Nenhum item cadastrado em Equipamentos ainda.\nCadastre itens na tela de Equipamentos primeiro.",
                        modifier = Modifier.padding(16.dp),
                        fontSize = 13.sp, color = Color.Gray
                    )
                }
            } else {
                // Para cada item disponível, mostra nome + campo de quantidade
                itensDisponiveis.forEach { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)),
                        border = BorderStroke(1.dp, Color(0xFFE0E0E0))
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(item.name, fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.Black)
                                Text("${item.material} • ${item.quantity} em estoque", fontSize = 11.sp, color = Color.Gray)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            OutlinedTextField(
                                value = quantidades[item] ?: "",
                                onValueChange = { qtd ->
                                    val novoMap = quantidades.toMutableMap()
                                    if (qtd.isBlank()) novoMap.remove(item) else novoMap[item] = qtd
                                    quantidades = novoMap
                                },
                                label = { Text("Qtd") },
                                modifier = Modifier.width(80.dp),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color.Black,
                                    unfocusedBorderColor = Color(0xFFCCCCCC)
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botão Salvar
            Button(
                onClick = {
                    if (nomeGrupo.isNotBlank()) {
                        val itensGrupo = quantidades
                            .filter { it.value.isNotBlank() }
                            .map { ItemGrupoEquip(it.key, it.value) }
                        onSalvar(nomeGrupo, itensGrupo)
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black, contentColor = Color.White)
            ) {
                Text("Salvar", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }

            // Botão Cancelar
            OutlinedButton(
                onClick = onCancelar,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black),
                border = BorderStroke(1.dp, Color.Black)
            ) {
                Text("Cancelar", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
