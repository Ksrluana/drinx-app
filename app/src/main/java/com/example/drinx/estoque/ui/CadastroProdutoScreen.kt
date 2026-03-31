package com.example.drinx.estoque.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
fun CadastroProdutoScreen(
    viewModel: EstoqueViewModel,
    onVoltar: () -> Unit
) {
    var nome by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf(Categoria.OUTROS) }
    var unidade by remember { mutableStateOf(UnidadeMedida.UNIDADE) }
    var medida by remember { mutableStateOf("") }
    var quantidade by remember { mutableStateOf("") }
    var dataValidade by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf(TipoProduto.NAO_PERECIVEL) }

    var dropdownCategoriaAberto by remember { mutableStateOf(false) }
    var dropdownUnidadeAberto by remember { mutableStateOf(false) }
    var dropdownTipoAberto by remember { mutableStateOf(false) }

    var erroNome by remember { mutableStateOf(false) }
    var erroQuantidade by remember { mutableStateOf(false) }
    var erroMedida by remember { mutableStateOf(false) }

    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

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
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color.White
                        )
                    }

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
                        text = "Cadastro de Produto",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Preencha as informações abaixo",
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


            CampoTexto(
                label = "Nome do Produto",
                value = nome,
                onValueChange = { nome = it; erroNome = false },
                isError = erroNome,
                errorMessage = "Nome é obrigatório"
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
                onValueChange = { medida = it; erroMedida = false },
                isError = erroMedida,
                errorMessage = "Informe a medida",
                keyboardType = KeyboardType.Decimal
            )


            CampoTexto(
                label = "Quantidade em Estoque",
                value = quantidade,
                onValueChange = { quantidade = it; erroQuantidade = false },
                isError = erroQuantidade,
                errorMessage = "Quantidade é obrigatória",
                keyboardType = KeyboardType.Number
            )


            CampoTexto(
                label = "Data de Validade (dd/mm/aaaa)",
                value = dataValidade,
                onValueChange = { dataValidade = it },
                keyboardType = KeyboardType.Number,
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Data",
                        tint = Color.Gray
                    )
                }
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

            Spacer(modifier = Modifier.height(8.dp))


            Button(
                onClick = {
                    erroNome = nome.isBlank()
                    erroQuantidade = quantidade.isBlank()
                    erroMedida = medida.isBlank()

                    if (!erroNome && !erroQuantidade && !erroMedida) {
                        val validadeDate = try {
                            if (dataValidade.isNotBlank())
                                LocalDate.parse(dataValidade, formatter)
                            else null
                        } catch (e: Exception) { null }

                        val novoProduto = Produto(
                            nome = nome.trim(),
                            categoria = categoria,
                            unidadeMedida = unidade,
                            medida = medida.trim(),
                            quantidade = quantidade.toIntOrNull() ?: 0,
                            dataValidade = validadeDate,
                            tipo = tipo
                        )
                        viewModel.adicionarProduto(novoProduto)
                        onVoltar()
                    }
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
                Text("Salvar Produto", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }


            OutlinedButton(
                onClick = onVoltar,
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

@Composable
fun CampoTexto(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
    errorMessage: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    Column {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            isError = isError,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            trailingIcon = trailingIcon,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color(0xFFCCCCCC),
                errorBorderColor = Color.Red,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            shape = RoundedCornerShape(10.dp)
        )
        if (isError) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 11.sp,
                modifier = Modifier.padding(start = 4.dp, top = 2.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownCampo(
    label: String,
    opcaoSelecionada: String,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    opcoes: List<String>,
    onOpcaoSelecionada: (Int) -> Unit
) {
    Column {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = onExpandedChange
        ) {
            OutlinedTextField(
                value = opcaoSelecionada,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                trailingIcon = {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color(0xFFCCCCCC),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                shape = RoundedCornerShape(10.dp)
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { onExpandedChange(false) }
            ) {
                opcoes.forEachIndexed { index, opcao ->
                    DropdownMenuItem(
                        text = { Text(opcao) },
                        onClick = { onOpcaoSelecionada(index) }
                    )
                }
            }
        }
    }
}
