package com.example.drinx.equipamentos.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.drinx.R
import com.example.drinx.equipamentos.model.InventoryCategory
import com.example.drinx.equipamentos.model.InventoryItem

// ============================================================
// TELA PRINCIPAL — EQUIPAMENTOS (apenas categorias)
// ============================================================

@Composable
fun EquipamentosScreen(
    items: List<InventoryItem>,
    onCategoryClick: (InventoryCategory) -> Unit,
    onAddItemClick: () -> Unit,
    onGrupoClick: () -> Unit = {},
    onAbrirMenu: () -> Unit = {}
) {
    val categories = listOf(
        InventoryCategory("Utensílios", "Facas, tábuas, pegadores, espátulas", R.drawable.ic_utensilios),
        InventoryCategory("Decoração", "Velas, flores, enfeites, toalhas", R.drawable.ic_decoracao),
        InventoryCategory("Copos", "Taças, canecas, copos de drinks", R.drawable.ic_copos),
        InventoryCategory("Materiais de Apoio", "Toalhas de papel, potes, embalagens", R.drawable.ic_apoio)
    )

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
                        IconButton(onClick = onAddItemClick) {
                            Icon(Icons.Default.Add, contentDescription = "Adicionar", tint = Color.White)
                        }
                    }
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Equipamentos", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Text("Inventário de materiais", color = Color.White.copy(alpha = 0.7f), fontSize = 13.sp)
                    }
                }
            }

            // Lista de categorias
            Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
                Spacer(modifier = Modifier.height(20.dp))
                Text("Categorias do Inventário", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 100.dp)
                ) {
                    items(categories) { category ->
                        CategoryItemCard(
                            category = category,
                            itemCount = items.count { it.categoryName == category.title },
                            onClick = { onCategoryClick(category) }
                        )
                    }
                }
            }
        }

        // Botão flutuante
        FloatingActionButton(
            onClick = onAddItemClick,
            modifier = Modifier.align(Alignment.BottomEnd).padding(24.dp),
            containerColor = Color.Black,
            contentColor = Color.White,
            shape = CircleShape
        ) {
            Icon(Icons.Default.Add, contentDescription = "Adicionar item")
        }
    }
}

@Composable
fun CategoryItemCard(category: InventoryCategory, itemCount: Int, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF0F0F0)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = category.iconRes),
                    contentDescription = null,
                    modifier = Modifier.padding(10.dp),
                    tint = Color(0xFF444444)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(category.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.Black)
                Text(category.description, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                Text("$itemCount item(s) cadastrado(s)", fontSize = 11.sp, color = Color.Gray)
            }
            Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = Color.LightGray)
        }
    }
}

// ============================================================
// TELA DETALHE DA CATEGORIA — com clique nos itens
// ============================================================

@Composable
fun CategoryDetailScreen(
    categoryName: String?,
    items: List<InventoryItem>,
    onBack: () -> Unit,
    onRemoveItem: (InventoryItem) -> Unit = {},
    onEditItem: (InventoryItem, InventoryItem) -> Unit = { _, _ -> }
) {
    val filteredItems = items.filter { it.categoryName == categoryName }
    var itemSelecionado by remember { mutableStateOf<InventoryItem?>(null) }
    var itemParaExcluir by remember { mutableStateOf<InventoryItem?>(null) }

    // Diálogo de confirmação de exclusão
    itemParaExcluir?.let { item ->
        AlertDialog(
            onDismissRequest = { itemParaExcluir = null },
            title = { Text("Excluir item?") },
            text = { Text("Deseja excluir \"${item.name}\"?") },
            confirmButton = {
                TextButton(onClick = { onRemoveItem(item); itemParaExcluir = null }) {
                    Text("Excluir", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { itemParaExcluir = null }) { Text("Cancelar") }
            }
        )
    }

    // Tela de detalhe/edição do item
    if (itemSelecionado != null) {
        DetalheItemScreen(
            item = itemSelecionado!!,
            onVoltar = { itemSelecionado = null },
            onSalvar = { itemAtualizado ->
                onEditItem(itemSelecionado!!, itemAtualizado)
                itemSelecionado = null
            },
            onExcluir = {
                onRemoveItem(itemSelecionado!!)
                itemSelecionado = null
            }
        )
        return
    }

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        // Header
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
                    IconButton(onClick = onBack) {
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
                    Text(categoryName ?: "Itens", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Text("${filteredItems.size} item(s) nesta categoria", color = Color.White.copy(alpha = 0.7f), fontSize = 13.sp)
                }
            }
        }

        if (filteredItems.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Inventory, contentDescription = null, modifier = Modifier.size(64.dp), tint = Color.LightGray)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Nenhum item cadastrado.", color = Color.Gray)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                contentPadding = PaddingValues(vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredItems) { item ->
                    ItemInventoryCard(
                        item = item,
                        onClick = { itemSelecionado = item },
                        onExcluir = { itemParaExcluir = item }
                    )
                }
            }
        }
    }
}

@Composable
fun ItemInventoryCard(item: InventoryItem, onClick: () -> Unit, onExcluir: () -> Unit = {}) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Surface(modifier = Modifier.size(52.dp), shape = RoundedCornerShape(12.dp), color = Color(0xFFF0F0F0)) {
                Icon(
                    painter = painterResource(id = item.iconRes ?: R.drawable.ic_utensilios),
                    contentDescription = null,
                    modifier = Modifier.padding(12.dp),
                    tint = Color(0xFF444444)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(item.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color(0xFF333333))
                Text("Material: ${item.material}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                Spacer(modifier = Modifier.height(6.dp))
                FragilityTag(item.fragility)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("${item.quantity}", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold, color = Color.Black)
                Text("unid.", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            }
        }
    }
}

// ============================================================
// TELA DETALHE E EDIÇÃO DO ITEM
// ============================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalheItemScreen(
    item: InventoryItem,
    onVoltar: () -> Unit,
    onSalvar: (InventoryItem) -> Unit,
    onExcluir: () -> Unit
) {
    var modoEdicao by remember { mutableStateOf(false) }
    var mostrarDialogoExcluir by remember { mutableStateOf(false) }

    // Campos editáveis
    var nome by remember { mutableStateOf(item.name) }
    var quantidade by remember { mutableStateOf(item.quantity.toString()) }
    var material by remember { mutableStateOf(item.material) }
    var fragilidade by remember { mutableStateOf(item.fragility) }
    var categoria by remember { mutableStateOf(item.categoryName) }
    var iconRes by remember { mutableStateOf(item.iconRes) }

    val materiais = listOf("Vidro", "Cristal", "Alumínio", "Cobre", "Aço Inox", "Madeira", "Papel")
    val fragilidades = listOf("Resistente", "Frágil", "Extra frágil", "Médio")
    val categorias = listOf("Utensílios", "Copos", "Decoração", "Materiais de Apoio")
    val availableIcons = listOf(R.drawable.ic_utensilios, R.drawable.ic_copos, R.drawable.ic_apoio, R.drawable.ic_decoracao)

    if (mostrarDialogoExcluir) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoExcluir = false },
            title = { Text("Excluir item?") },
            text = { Text("Deseja excluir \"${item.name}\"? Esta ação não pode ser desfeita.") },
            confirmButton = {
                TextButton(onClick = { mostrarDialogoExcluir = false; onExcluir() }) {
                    Text("Excluir", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoExcluir = false }) { Text("Cancelar") }
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {

        // Header
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
                    IconButton(onClick = onVoltar) {
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
                    Text(
                        if (modoEdicao) "Editar Item" else "Detalhe do Item",
                        color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold
                    )
                    Text(item.name, color = Color.White.copy(alpha = 0.7f), fontSize = 13.sp)
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

            if (!modoEdicao) {
                // ===== MODO VISUALIZAÇÃO =====
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        LinhaDetalheItem("Nome", item.name)
                        Divider(color = Color(0xFFE0E0E0))
                        LinhaDetalheItem("Categoria", item.categoryName)
                        Divider(color = Color(0xFFE0E0E0))
                        LinhaDetalheItem("Material", item.material)
                        Divider(color = Color(0xFFE0E0E0))
                        LinhaDetalheItem("Quantidade", "${item.quantity} unid.")
                        Divider(color = Color(0xFFE0E0E0))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text("Fragilidade", fontSize = 13.sp, color = Color.Gray)
                            FragilityTag(item.fragility)
                        }
                    }
                }

                // Botão Editar
                Button(
                    onClick = { modoEdicao = true },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black, contentColor = Color.White)
                ) { Text("Editar", fontWeight = FontWeight.Bold, fontSize = 15.sp) }

                // Botão Excluir
                OutlinedButton(
                    onClick = { mostrarDialogoExcluir = true },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                    border = BorderStroke(1.dp, Color.Red)
                ) { Text("Excluir", fontWeight = FontWeight.Bold, fontSize = 15.sp) }

            } else {
                // ===== MODO EDIÇÃO =====
                OutlinedTextField(
                    value = nome, onValueChange = { nome = it },
                    label = { Text("Nome do item") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color.Black, unfocusedBorderColor = Color(0xFFCCCCCC))
                )

                OutlinedTextField(
                    value = quantidade,
                    onValueChange = { if (it.all { c -> c.isDigit() }) quantidade = it },
                    label = { Text("Quantidade") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color.Black, unfocusedBorderColor = Color(0xFFCCCCCC))
                )

                SimpleSelectorField("Categoria", categorias, categoria) { categoria = it }
                SimpleSelectorField("Material/Tipo", materiais, material) { material = it }
                SimpleSelectorField("Fragilidade", fragilidades, fragilidade) { fragilidade = it }

                Text("Ícone:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(availableIcons) { ico ->
                        val isSelected = ico == iconRes
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .background(if (isSelected) Color.Black else Color(0xFFF0F0F0), RoundedCornerShape(12.dp))
                                .clickable { iconRes = ico }
                                .padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(painter = painterResource(id = ico), contentDescription = null, tint = if (isSelected) Color.White else Color.DarkGray)
                        }
                    }
                }

                // Botão Salvar
                Button(
                    onClick = {
                        onSalvar(item.copy(
                            name = nome.trim(),
                            quantity = quantidade.toIntOrNull() ?: item.quantity,
                            material = material,
                            fragility = fragilidade,
                            categoryName = categoria,
                            iconRes = iconRes
                        ))
                    },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black, contentColor = Color.White)
                ) { Text("Salvar Alterações", fontWeight = FontWeight.Bold, fontSize = 15.sp) }

                // Botão Cancelar edição
                OutlinedButton(
                    onClick = {
                        nome = item.name; quantidade = item.quantity.toString()
                        material = item.material; fragilidade = item.fragility
                        categoria = item.categoryName; iconRes = item.iconRes
                        modoEdicao = false
                    },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black),
                    border = BorderStroke(1.dp, Color.Black)
                ) { Text("Cancelar", fontWeight = FontWeight.Bold, fontSize = 15.sp) }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun LinhaDetalheItem(rotulo: String, valor: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(rotulo, fontSize = 13.sp, color = Color.Gray)
        Text(valor, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color.Black, textAlign = TextAlign.End)
    }
}

@Composable
fun FragilityTag(fragility: String) {
    val (backgroundColor, textColor) = when (fragility.lowercase()) {
        "extra frágil" -> Color(0xFFFFEBEE) to Color(0xFFD32F2F)
        "frágil" -> Color(0xFFFFF3E0) to Color(0xFFF57C00)
        "médio" -> Color(0xFFE3F2FD) to Color(0xFF1976D2)
        else -> Color(0xFFE8F5E9) to Color(0xFF388E3C)
    }
    Surface(color = backgroundColor, shape = RoundedCornerShape(6.dp)) {
        Text(fragility.uppercase(), modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = textColor)
    }
}

// ============================================================
// TELA CADASTRO DE ITENS
// ============================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemScreen(
    onBack: () -> Unit,
    onItemSaved: (InventoryItem) -> Unit
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }

    val categorias = listOf("Utensílios", "Copos", "Decoração", "Materiais de Apoio")
    val materiais = listOf("Vidro", "Cristal", "Alumínio", "Cobre", "Aço Inox", "Madeira", "Papel")
    val fragilidades = listOf("Resistente", "Frágil", "Extra frágil", "Médio")

    var selectedCategory by remember { mutableStateOf(categorias.first()) }
    var selectedMaterial by remember { mutableStateOf(materiais.first()) }
    var selectedFragility by remember { mutableStateOf(fragilidades.first()) }

    val availableIcons = listOf(R.drawable.ic_utensilios, R.drawable.ic_copos, R.drawable.ic_apoio, R.drawable.ic_decoracao)
    var selectedIconRes by remember { mutableStateOf(availableIcons.first()) }

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
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
                    IconButton(onClick = onBack) {
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
                    Text("Cadastro de Item", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Text("Adicione um novo equipamento", color = Color.White.copy(alpha = 0.7f), fontSize = 13.sp)
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nome do item") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color.Black, unfocusedBorderColor = Color(0xFFCCCCCC)))
            OutlinedTextField(value = quantity, onValueChange = { if (it.all { c -> c.isDigit() }) quantity = it }, label = { Text("Quantidade") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color.Black, unfocusedBorderColor = Color(0xFFCCCCCC)))

            SimpleSelectorField("Categoria do Inventário", categorias, selectedCategory) { selectedCategory = it }
            SimpleSelectorField("Material/Tipo", materiais, selectedMaterial) { selectedMaterial = it }
            SimpleSelectorField("Categoria de fragilidade", fragilidades, selectedFragility) { selectedFragility = it }

            Text("Escolha um ícone:", fontWeight = FontWeight.Bold)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                items(availableIcons) { iconRes ->
                    val isSelected = iconRes == selectedIconRes
                    Box(
                        modifier = Modifier.size(60.dp).background(if (isSelected) Color.Black else Color(0xFFF0F0F0), RoundedCornerShape(12.dp)).clickable { selectedIconRes = iconRes }.padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(painter = painterResource(id = iconRes), contentDescription = null, tint = if (isSelected) Color.White else Color.DarkGray)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (name.isNotEmpty() && quantity.isNotEmpty()) {
                        val newItem = InventoryItem(name = name, quantity = quantity.toIntOrNull() ?: 0, material = selectedMaterial, fragility = selectedFragility, iconRes = selectedIconRes, categoryName = selectedCategory)
                        onItemSaved(newItem)
                        Toast.makeText(context, "Item '${newItem.name}' salvo!", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black, contentColor = Color.White)
            ) { Text("Salvar no Inventário", fontWeight = FontWeight.Bold, fontSize = 15.sp) }

            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black),
                border = BorderStroke(1.dp, Color.Black)
            ) { Text("Cancelar", fontWeight = FontWeight.Bold, fontSize = 15.sp) }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleSelectorField(label: String, opcoes: List<String>, selectedOption: String, onOptionSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(
            value = selectedOption, onValueChange = {}, readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.fillMaxWidth().menuAnchor(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color.Black, unfocusedBorderColor = Color(0xFFCCCCCC))
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            opcoes.forEach { option ->
                DropdownMenuItem(text = { Text(option) }, onClick = { onOptionSelected(option); expanded = false })
            }
        }
    }
}
