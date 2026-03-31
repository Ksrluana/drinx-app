package com.example.drinx.estoque.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.drinx.R
import com.example.drinx.estoque.model.*
import com.example.drinx.estoque.viewmodel.EstoqueViewModel
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstoqueScreen(
    viewModel: EstoqueViewModel,
    onNavigateCadastro: () -> Unit,
    onNavigateDetalhe: (Produto) -> Unit,
    onAbrirMenu: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var menuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            // Header preto com borda inferior arredondada
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
                        // Ícone hambúrguer (futuro menu lateral)
                        IconButton(onClick = onAbrirMenu) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu lateral",
                                tint = Color.White
                            )
                        }

                        // Logo centralizada e maior
                        Image(
                            painter = painterResource(id = R.drawable.logo_app),
                            contentDescription = "Logo",
                            modifier = Modifier
                                .height(40.dp)
                                .widthIn(max = 130.dp),
                            contentScale = ContentScale.Fit,
                            colorFilter = ColorFilter.tint(Color.White)
                        )

                        // Menu 3 pontos
                        Box {
                            IconButton(onClick = { menuExpanded = true }) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "Mais opções",
                                    tint = Color.White
                                )
                            }
                            DropdownMenu(
                                expanded = menuExpanded,
                                onDismissRequest = { menuExpanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Opção 1") },
                                    onClick = { menuExpanded = false }
                                )
                            }
                        }
                    }

                    // Título e subtítulo
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Estoque",
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Controle de produtos",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 13.sp
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateCadastro,
                containerColor = Color.Black,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Adicionar Produto")
            }
        },
        containerColor = Color.White
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Produtos Cadastrados",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            FiltroCategoria(
                categoriaSelecionada = uiState.filtroCategoria,
                onSelecionarCategoria = { viewModel.setFiltroCategoria(it) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            FiltroValidade(
                filtroSelecionado = uiState.filtroValidade,
                onSelecionarFiltro = { viewModel.setFiltroValidade(it) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            TabelaProdutos(
                produtos = uiState.produtosFiltrados,
                onClickProduto = { produto ->
                    viewModel.selecionarProduto(produto)
                    onNavigateDetalhe(produto)
                }
            )
        }
    }
}

@Composable
fun FiltroCategoria(
    categoriaSelecionada: Categoria?,
    onSelecionarCategoria: (Categoria?) -> Unit
) {
    val scrollState = rememberScrollState()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ChipFiltro(
            label = "Todas",
            selecionado = categoriaSelecionada == null,
            onClick = { onSelecionarCategoria(null) }
        )
        Categoria.entries.forEach { categoria ->
            ChipFiltro(
                label = categoria.label,
                selecionado = categoriaSelecionada == categoria,
                onClick = { onSelecionarCategoria(categoria) }
            )
        }
    }
}

@Composable
fun FiltroValidade(
    filtroSelecionado: FiltroValidade,
    onSelecionarFiltro: (FiltroValidade) -> Unit
) {
    val scrollState = rememberScrollState()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FiltroValidade.entries.forEach { filtro ->
            ChipFiltro(
                label = filtro.label,
                selecionado = filtroSelecionado == filtro,
                onClick = { onSelecionarFiltro(filtro) }
            )
        }
    }
}

@Composable
fun ChipFiltro(label: String, selecionado: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (selecionado) Color.Black else Color.White)
            .border(1.dp, Color.Black, RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            color = if (selecionado) Color.White else Color.Black,
            fontSize = 12.sp,
            fontWeight = if (selecionado) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun TabelaProdutos(
    produtos: List<Produto>,
    onClickProduto: (Produto) -> Unit
) {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yy")

    if (produtos.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Nenhum produto encontrado",
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        items(produtos) { produto ->
            val bgColor = when (produto.statusValidade()) {
                FiltroValidade.VENCIDO -> Color(0xFFFFEEEE)
                FiltroValidade.PROXIMO_VENCER -> Color(0xFFFFFBEE)
                else -> Color.White
            }
            val corValidade = when (produto.statusValidade()) {
                FiltroValidade.VENCIDO -> Color.Red
                FiltroValidade.PROXIMO_VENCER -> Color(0xFFB8860B)
                else -> Color(0xFF2E7D32)
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onClickProduto(produto) },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = bgColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Esquerda: nome e categoria
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = produto.nome,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.Black,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = produto.categoria.label,
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Direita: medida, quantidade, validade e tipo
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "${produto.medida} ${produto.unidadeMedida.label}  •  Qtd: ${produto.quantidade}",
                            fontSize = 11.sp,
                            color = Color.DarkGray
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = produto.dataValidade?.format(formatter) ?: "Sem validade",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = corValidade
                        )
                        Text(
                            text = produto.tipo.label,
                            fontSize = 10.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CelulaHeader(
    texto: String,
    largura: androidx.compose.ui.unit.Dp
) {
    Text(
        text = texto,
        modifier = Modifier.width(largura),
        color = Color.White,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun CelulaTexto(
    texto: String,
    largura: androidx.compose.ui.unit.Dp,
    textAlign: TextAlign = TextAlign.Start,
    fontWeight: FontWeight = FontWeight.Normal,
    color: Color = Color.Black
) {
    Text(
        text = texto,
        modifier = Modifier.width(largura),
        fontSize = 11.sp,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        textAlign = textAlign,
        fontWeight = fontWeight,
        color = color
    )
}
