package com.example.drinx.subtelas

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.drinx.R
import com.example.drinx.estoque.model.Produto
import com.example.drinx.estoque.model.UnidadeMedida

// ============================================================
// MODELOS
// ============================================================

data class IngredienteDrink(
    val produto: Produto,
    val quantidade: String
)

data class DrinkItem(
    val id: Int,
    val nome: String,
    val desc: String,
    val imagemUri: Uri? = null,
    val ingredientes: List<IngredienteDrink> = emptyList()
)

// Produto placeholder usado quando o estoque está vazio
private val produtoVazio = Produto(
    id = -1,
    nome = "Selecione um produto",
    categoria = com.example.drinx.estoque.model.Categoria.OUTROS,
    unidadeMedida = UnidadeMedida.UNIDADE,
    medida = "",
    quantidade = 0
)

// ============================================================
// TELA PRINCIPAL
// ============================================================

@Composable
fun CardapioScreen(
    onAbrirMenu: () -> Unit = {},
    produtosEstoque: List<Produto> = emptyList()
) {
    val drinks = remember {
        mutableStateListOf(
            DrinkItem(1, "Mojito Especial", "Uma releitura luxuosa do clássico cubano. Combina hortelã fresca e limão taiti com rum branco premium."),
            DrinkItem(2, "Negroni Clássico", "Equilíbrio perfeito entre Gin, Campari e Vermute. Um drink intenso e sofisticado para paladares apurados."),
            DrinkItem(3, "Caipirinha Gourmet", "A versão premium da paixão nacional. Cachaça de alambique, limões selecionados e gelo cristalino."),
            DrinkItem(4, "Cosmopolitan", "O ícone da sofisticação urbana. Vodka premium com toque cítrico do licor de laranja.")
        )
    }

    var selecionado by remember { mutableStateOf(drinks[0]) }
    var mostrarFormulario by remember { mutableStateOf(false) }
    var drinkParaExcluir by remember { mutableStateOf<DrinkItem?>(null) }
    var drinkParaEditar by remember { mutableStateOf<DrinkItem?>(null) }
    var menuExpandido by remember { mutableStateOf(false) }

    // Confirmação de exclusão
    drinkParaExcluir?.let { drink ->
        AlertDialog(
            onDismissRequest = { drinkParaExcluir = null },
            title = { Text("Excluir Drink", fontWeight = FontWeight.Bold) },
            text = { Text("Tem certeza que quer excluir '${drink.nome}' do cardápio?") },
            confirmButton = {
                TextButton(onClick = {
                    val index = drinks.indexOf(drink)
                    drinks.remove(drink)
                    if (drinks.isNotEmpty()) {
                        selecionado = if (index > 0) drinks[index - 1] else drinks[0]
                    }
                    drinkParaExcluir = null
                }) { Text("Excluir", color = Color.Red, fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { drinkParaExcluir = null }) {
                    Text("Cancelar", color = Color.Black)
                }
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier
            .fillMaxSize()
            .background(Color.White)) {

            // ===== TOPO =====
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                // Fundo preto curvado
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(270.dp)
                        .clip(RoundedCornerShape(bottomStart = 150.dp, bottomEnd = 150.dp))
                        .background(Color(0xFF1A1A1A))
                ) {
                    // Logo
                    androidx.compose.foundation.Image(
                        painter = painterResource(id = R.drawable.logo_app),
                        contentDescription = "Logo DRINX",
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(start = 24.dp, top = 24.dp)
                            .height(36.dp),
                        contentScale = ContentScale.Fit
                    )

                    // Ícone hambúrguer
                    IconButton(
                        onClick = onAbrirMenu,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(top = 60.dp)
                    ) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
                    }

                    // Nome e subtítulo
                    Column(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 75.dp, start = 16.dp, end = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = selecionado.nome,
                            color = Color.White, fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center, maxLines = 1
                        )
                        Text("Drink em Destaque", color = Color.LightGray, fontSize = 14.sp)
                    }
                }

                // Imagem do drink selecionado
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    if (selecionado.imagemUri != null) {
                        AsyncImage(
                            model = selecionado.imagemUri,
                            contentDescription = selecionado.nome,
                            modifier = Modifier.size(250.dp),
                            contentScale = ContentScale.Fit
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.BrokenImage,
                            contentDescription = null,
                            modifier = Modifier
                                .size(150.dp)
                                .padding(bottom = 40.dp),
                            tint = Color.Gray
                        )
                    }
                }

                // Menu 3 pontinhos
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, end = 16.dp),
                    contentAlignment = Alignment.TopEnd
                ) {
                    IconButton(onClick = { menuExpandido = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menu", tint = Color.White)
                    }
                    DropdownMenu(
                        expanded = menuExpandido,
                        onDismissRequest = { menuExpandido = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Editar drink") },
                            onClick = { drinkParaEditar = selecionado; menuExpandido = false }
                        )
                        DropdownMenuItem(
                            text = { Text("Excluir drink", color = Color.Red) },
                            onClick = { drinkParaExcluir = selecionado; menuExpandido = false }
                        )
                    }
                }
            }

            // ===== CONTEÚDO INFERIOR =====
            Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 10.dp)) {
                Text("Todos os drinks", fontWeight = FontWeight.Bold, fontSize = 18.sp)

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(vertical = 15.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    items(drinks) { drink ->
                        val isSelected = selecionado.id == drink.id
                        Card(
                            modifier = Modifier
                                .size(90.dp, 80.dp)
                                .clickable { selecionado = drink },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = BorderStroke(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) Color.Black else Color.LightGray.copy(alpha = 0.5f)
                            )
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                if (drink.imagemUri != null) {
                                    AsyncImage(
                                        model = drink.imagemUri,
                                        contentDescription = null,
                                        modifier = Modifier.size(50.dp),
                                        contentScale = ContentScale.Fit
                                    )
                                } else {
                                    Icon(
                                        Icons.Default.BrokenImage,
                                        contentDescription = null,
                                        modifier = Modifier.size(30.dp),
                                        tint = Color.Black
                                    )
                                }

                                if (isSelected) {
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .size(36.dp)
                                            .clickable { drinkParaExcluir = drink },
                                        contentAlignment = Alignment.TopEnd
                                    ) {
                                        Surface(
                                            modifier = Modifier
                                                .padding(top = 4.dp, end = 4.dp)
                                                .size(22.dp),
                                            shape = CircleShape,
                                            color = Color.Black.copy(alpha = 0.8f)
                                        ) {
                                            Icon(
                                                Icons.Default.Remove,
                                                contentDescription = "Excluir",
                                                tint = Color.White,
                                                modifier = Modifier.padding(2.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Text("Descrição", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(
                    text = selecionado.desc,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 8.dp),
                    textAlign = TextAlign.Justify,
                    lineHeight = 20.sp
                )

                // Ingredientes do drink selecionado
                if (selecionado.ingredientes.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Ingredientes", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    selecionado.ingredientes.forEach { ing ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "• ${ing.produto.nome}",
                                fontSize = 14.sp,
                                color = Color.Black,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                "${ing.quantidade} ${ing.produto.unidadeMedida.label}",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Botões inferiores
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 50.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { drinkParaEditar = selecionado },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Editar Drink", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                    Surface(
                        modifier = Modifier
                            .size(50.dp)
                            .clickable { mostrarFormulario = true },
                        shape = CircleShape,
                        color = Color.Black,
                        shadowElevation = 4.dp
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }
        }

        // ===== FORMULÁRIO NOVO DRINK =====
        AnimatedVisibility(visible = mostrarFormulario) {
            FormularioDrink(
                titulo = "Novo Drink",
                subtitulo = "Configure os detalhes do item",
                drinkInicial = null,
                produtosEstoque = produtosEstoque,
                onCancelar = { mostrarFormulario = false },
                onSalvar = { nome, desc, uri, ingredientes ->
                    val novoId = if (drinks.isEmpty()) 1 else drinks.maxOf { it.id } + 1
                    val novo = DrinkItem(novoId, nome, desc, uri, ingredientes)
                    drinks.add(novo)
                    selecionado = novo
                    mostrarFormulario = false
                }
            )
        }

        // ===== FORMULÁRIO EDIÇÃO =====
        drinkParaEditar?.let { drink ->
            FormularioDrink(
                titulo = "Editar Drink",
                subtitulo = drink.nome,
                drinkInicial = drink,
                produtosEstoque = produtosEstoque,
                onCancelar = { drinkParaEditar = null },
                onSalvar = { nome, desc, uri, ingredientes ->
                    val index = drinks.indexOfFirst { it.id == drink.id }
                    if (index != -1) {
                        val atualizado = drink.copy(
                            nome = nome, desc = desc,
                            imagemUri = uri, ingredientes = ingredientes
                        )
                        drinks[index] = atualizado
                        selecionado = atualizado
                    }
                    drinkParaEditar = null
                }
            )
        }
    }
}

// ============================================================
// FORMULÁRIO DE CADASTRO / EDIÇÃO
// ============================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioDrink(
    titulo: String,
    subtitulo: String,
    drinkInicial: DrinkItem?,
    produtosEstoque: List<Produto>,
    onCancelar: () -> Unit,
    onSalvar: (nome: String, desc: String, uri: Uri?, ingredientes: List<IngredienteDrink>) -> Unit
) {
    var novoNome by remember(drinkInicial) { mutableStateOf(drinkInicial?.nome ?: "") }
    var novaDesc by remember(drinkInicial) { mutableStateOf(drinkInicial?.desc ?: "") }
    var imagemUri by remember(drinkInicial) { mutableStateOf<Uri?>(drinkInicial?.imagemUri) }
    var qtdIngredientesTexto by remember(drinkInicial) {
        mutableStateOf(drinkInicial?.ingredientes?.size?.takeIf { it > 0 }?.toString() ?: "")
    }

    // Lista tipada explicitamente
    var ingredientes by remember(drinkInicial) {
        mutableStateOf<List<IngredienteDrink>>(drinkInicial?.ingredientes ?: emptyList())
    }

    val produtoDefault = produtosEstoque.firstOrNull() ?: produtoVazio

    // Atualiza a lista quando o usuário muda a quantidade
    fun atualizarQuantidadeIngredientes(novaQtd: Int) {
        val listaAtual = ingredientes.toMutableList()
        while (listaAtual.size < novaQtd) {
            listaAtual.add(IngredienteDrink(produto = produtoDefault, quantidade = ""))
        }
        while (listaAtual.size > novaQtd) {
            listaAtual.removeAt(listaAtual.size - 1)
        }
        ingredientes = listaAtual
    }

    // Launcher para galeria
    val galeriaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) imagemUri = uri
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Text(titulo, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(subtitulo, color = Color.Gray)

            Spacer(modifier = Modifier.height(20.dp))

            // Área de imagem (abre galeria)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF5F5F5))
                    .clickable { galeriaLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (imagemUri == null) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.Collections,
                            contentDescription = null,
                            modifier = Modifier.size(40.dp),
                            tint = Color.Gray
                        )
                        Text("Anexar Foto", color = Color.Gray, fontSize = 12.sp)
                        Text("(Toque para abrir a galeria)", color = Color.LightGray, fontSize = 10.sp)
                    }
                } else {
                    AsyncImage(
                        model = imagemUri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .background(Color.Red, CircleShape)
                            .clickable { imagemUri = null }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text("Remover", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = novoNome,
                onValueChange = { novoNome = it },
                label = { Text("Nome do Drink") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = novaDesc,
                onValueChange = { novaDesc = it },
                label = { Text("Descrição detalhada") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Campo de quantidade de ingredientes
            Text("Ingredientes", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = qtdIngredientesTexto,
                onValueChange = { novo ->
                    qtdIngredientesTexto = novo
                    val qtd = novo.toIntOrNull() ?: 0
                    atualizarQuantidadeIngredientes(qtd)
                },
                label = { Text("Quantos ingredientes?") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(12.dp)
            )

            // Campos dinâmicos de ingredientes
            if (ingredientes.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))

                if (produtosEstoque.isEmpty()) {
                    Text(
                        "⚠️ Nenhum produto cadastrado no estoque ainda.",
                        color = Color.Gray, fontSize = 12.sp
                    )
                } else {
                    ingredientes.forEachIndexed { index, ingrediente ->
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            "Ingrediente ${index + 1}",
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        // Dropdown de produto do estoque
                        var dropdownAberto by remember { mutableStateOf(false) }

                        ExposedDropdownMenuBox(
                            expanded = dropdownAberto,
                            onExpandedChange = { dropdownAberto = it }
                        ) {
                            OutlinedTextField(
                                value = ingrediente.produto.nome,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Produto do estoque") },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownAberto)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(),
                                shape = RoundedCornerShape(10.dp)
                            )
                            ExposedDropdownMenu(
                                expanded = dropdownAberto,
                                onDismissRequest = { dropdownAberto = false }
                            ) {
                                produtosEstoque.forEach { produto ->
                                    DropdownMenuItem(
                                        text = {
                                            Text("${produto.nome} (${produto.unidadeMedida.label})")
                                        },
                                        onClick = {
                                            val lista = ingredientes.toMutableList()
                                            lista[index] = ingrediente.copy(produto = produto)
                                            ingredientes = lista
                                            dropdownAberto = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        OutlinedTextField(
                            value = ingrediente.quantidade,
                            onValueChange = { qtd ->
                                val lista = ingredientes.toMutableList()
                                lista[index] = ingrediente.copy(quantidade = qtd)
                                ingredientes = lista
                            },
                            label = { Text("Quantidade (${ingrediente.produto.unidadeMedida.label})") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onCancelar,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Cancelar", color = Color.Black)
                }

                Button(
                    onClick = {
                        if (novoNome.isNotBlank()) {
                            onSalvar(novoNome, novaDesc, imagemUri, ingredientes)
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Salvar", color = Color.White)
                }
            }
        }
    }
}
