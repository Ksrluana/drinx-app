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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Remove
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
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.drinx.R
import com.example.drinx.estoque.model.Produto
import com.example.drinx.estoque.viewmodel.CardapioViewModel
import com.example.drinx.estoque.viewmodel.DrinkFirebase

@Composable
fun CardapioScreen(
    onAbrirMenu: () -> Unit = {},
    produtosEstoque: List<Produto> = emptyList()
) {
    val viewModel: CardapioViewModel = viewModel()
    val drinks = viewModel.drinks

    var drinkSelecionadoId by remember { mutableStateOf<Int?>(null) }
    var mostrarFormulario by remember { mutableStateOf(false) }
    var drinkParaExcluir by remember { mutableStateOf<DrinkFirebase?>(null) }
    var drinkParaEditar by remember { mutableStateOf<DrinkFirebase?>(null) }
    var menuExpandido by remember { mutableStateOf(false) }

    val selecionado = drinks.firstOrNull { it.id == drinkSelecionadoId } ?: drinks.firstOrNull()

    LaunchedEffect(drinks.size) {
        if (drinkSelecionadoId == null && drinks.isNotEmpty()) {
            drinkSelecionadoId = drinks.first().id
        }
    }

    drinkParaExcluir?.let { drink ->
        AlertDialog(
            onDismissRequest = { drinkParaExcluir = null },
            title = { Text("Excluir Drink", fontWeight = FontWeight.Bold) },
            text = { Text("Tem certeza que quer excluir '${drink.nome}' do cardápio?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.excluirDrink(drink.id)
                    drinkParaExcluir = null
                    drinkSelecionadoId = null
                }) {
                    Text("Excluir", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { drinkParaExcluir = null }) {
                    Text("Cancelar", color = Color.Black)
                }
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(270.dp)
                        .clip(RoundedCornerShape(bottomStart = 150.dp, bottomEnd = 150.dp))
                        .background(Color(0xFF1A1A1A))
                ) {
                    androidx.compose.foundation.Image(
                        painter = painterResource(id = R.drawable.logo_app),
                        contentDescription = "Logo DRINX",
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(start = 24.dp, top = 24.dp)
                            .height(36.dp),
                        contentScale = ContentScale.Fit
                    )

                    IconButton(
                        onClick = onAbrirMenu,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(top = 60.dp)
                    ) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
                    }

                    Column(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 75.dp, start = 16.dp, end = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = selecionado?.nome ?: "Cardápio",
                            color = Color.White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            maxLines = 1
                        )
                        Text("Drink em Destaque", color = Color.LightGray, fontSize = 14.sp)
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    val imagem = selecionado?.imagemUri

                    if (!imagem.isNullOrBlank()) {
                        AsyncImage(
                            model = Uri.parse(imagem),
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
                            onClick = {
                                selecionado?.let { drinkParaEditar = it }
                                menuExpandido = false
                            }
                        )

                        DropdownMenuItem(
                            text = { Text("Excluir drink", color = Color.Red) },
                            onClick = {
                                selecionado?.let { drinkParaExcluir = it }
                                menuExpandido = false
                            }
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 10.dp)
            ) {
                Text("Todos os drinks", fontWeight = FontWeight.Bold, fontSize = 18.sp)

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(vertical = 15.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    items(drinks) { drink ->
                        val isSelected = selecionado?.id == drink.id

                        Card(
                            modifier = Modifier
                                .size(90.dp, 80.dp)
                                .clickable { drinkSelecionadoId = drink.id },
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
                                if (!drink.imagemUri.isNullOrBlank()) {
                                    AsyncImage(
                                        model = Uri.parse(drink.imagemUri),
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

                if (selecionado == null) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Nenhum drink cadastrado ainda.",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                } else {
                    Text("Descrição", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(
                        text = selecionado.desc,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 8.dp),
                        textAlign = TextAlign.Justify,
                        lineHeight = 20.sp
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 50.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            selecionado?.let { drinkParaEditar = it }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                        shape = RoundedCornerShape(12.dp),
                        enabled = selecionado != null
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

        AnimatedVisibility(visible = mostrarFormulario) {
            FormularioDrink(
                titulo = "Novo Drink",
                subtitulo = "Configure os detalhes do item",
                drinkInicial = null,
                onCancelar = { mostrarFormulario = false },
                onSalvar = { nome, desc, uri ->
                    viewModel.adicionarDrink(
                        nome = nome,
                        desc = desc,
                        imagemUri = uri?.toString()
                    )
                    mostrarFormulario = false
                }
            )
        }

        drinkParaEditar?.let { drink ->
            FormularioDrink(
                titulo = "Editar Drink",
                subtitulo = drink.nome,
                drinkInicial = drink,
                onCancelar = { drinkParaEditar = null },
                onSalvar = { nome, desc, uri ->
                    val atualizado = drink.copy(
                        nome = nome,
                        desc = desc,
                        imagemUri = uri?.toString()
                    )

                    viewModel.atualizarDrink(atualizado)
                    drinkSelecionadoId = atualizado.id
                    drinkParaEditar = null
                }
            )
        }
    }
}

@Composable
fun FormularioDrink(
    titulo: String,
    subtitulo: String,
    drinkInicial: DrinkFirebase?,
    onCancelar: () -> Unit,
    onSalvar: (nome: String, desc: String, uri: Uri?) -> Unit
) {
    var novoNome by remember(drinkInicial) {
        mutableStateOf(drinkInicial?.nome ?: "")
    }

    var novaDesc by remember(drinkInicial) {
        mutableStateOf(drinkInicial?.desc ?: "")
    }

    var imagemUri by remember(drinkInicial) {
        mutableStateOf(
            drinkInicial?.imagemUri?.let {
                if (it.isNotBlank()) Uri.parse(it) else null
            }
        )
    }

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
                        Text(
                            "Remover",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
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
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )

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
                            onSalvar(novoNome, novaDesc, imagemUri)
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