package com.example.drinx

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.drinx.cadastrogerais.ui.CadastroGeralApp
import com.example.drinx.equipamentos.EquipamentosNavigation
import com.example.drinx.estoque.ui.EstoqueNavigation
import com.example.drinx.estoque.viewmodel.EstoqueViewModel
import com.example.drinx.eventos.EventosNavigation
import com.example.drinx.subtelas.CardapioScreen
import kotlinx.coroutines.launch

// ============================================================
// ROTAS — duas rotas para Equipamentos:
// uma para tela principal, outra para abrir direto no Grupo
// ============================================================
object RotasApp {
    const val ESTOQUE = "estoque"
    const val CADASTRO_GERAL = "cadastro_geral"
    const val EVENTOS = "eventos"
    const val CARDAPIO = "cardapio"
    const val EQUIPAMENTOS = "equipamentos"
    const val EQUIPAMENTOS_GRUPO = "equipamentos_grupo_direto"
}

// ============================================================
// NAVEGAÇÃO PRINCIPAL
// ============================================================
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val estoqueViewModel: EstoqueViewModel = viewModel()
    val estoqueUiState by estoqueViewModel.uiState.collectAsState()

    val abrirMenu: () -> Unit = { scope.launch { drawerState.open() } }
    val fecharMenu: () -> Unit = { scope.launch { drawerState.close() } }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(navController = navController, fecharMenu = fecharMenu)
        }
    ) {
        NavHost(navController = navController, startDestination = RotasApp.ESTOQUE) {

            composable(RotasApp.ESTOQUE) {
                EstoqueNavigation(onAbrirMenu = abrirMenu)
            }

            composable(RotasApp.CADASTRO_GERAL) {
                CadastroGeralApp(onAbrirMenu = abrirMenu)
            }

            composable(RotasApp.EVENTOS) {
                EventosNavigation(onAbrirMenu = abrirMenu)
            }

            composable(RotasApp.CARDAPIO) {
                CardapioScreen(
                    onAbrirMenu = abrirMenu,
                    produtosEstoque = estoqueUiState.produtos
                )
            }

            // Equipamentos — abre na tela principal (categorias)
            composable(RotasApp.EQUIPAMENTOS) {
                EquipamentosNavigation(
                    onAbrirMenu = abrirMenu,
                    iniciarNoGrupo = false
                )
            }

            // Equipamentos — abre direto na subtela Grupo
            composable(RotasApp.EQUIPAMENTOS_GRUPO) {
                EquipamentosNavigation(
                    onAbrirMenu = abrirMenu,
                    iniciarNoGrupo = true
                )
            }
        }
    }
}

// ============================================================
// CONTEÚDO DO DRAWER
// ============================================================
@Composable
fun DrawerContent(
    navController: NavController,
    fecharMenu: () -> Unit
) {
    val rotaAtual by navController.currentBackStackEntryAsState()
    val rotaAtualStr = rotaAtual?.destination?.route

    var estoqueExpandido by remember { mutableStateOf(true) }
    var equipamentosExpandido by remember { mutableStateOf(false) }

    ModalDrawerSheet(
        drawerContainerColor = Color.White,
        drawerContentColor = Color.Black,
        modifier = Modifier.width(300.dp)
    ) {
        // ===== CABEÇALHO PRETO =====
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(vertical = 24.dp, horizontal = 20.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_app),
                        contentDescription = "Logo",
                        modifier = Modifier.height(36.dp).widthIn(max = 120.dp),
                        contentScale = ContentScale.Fit,
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                    IconButton(onClick = fecharMenu, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Fechar", tint = Color.White)
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF333333)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Avatar",
                            tint = Color.White,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Usuário", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text("usuario@drinx.com", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ===== ESTOQUE (com setinha — subtela: Cardápios) =====
        ItemDrawerPai(
            titulo = "Estoque",
            subtitulo = "Controle de produtos",
            selecionado = rotaAtualStr == RotasApp.ESTOQUE,
            expandido = estoqueExpandido,
            onClicarTitulo = {
                fecharMenu()
                navController.navigate(RotasApp.ESTOQUE) {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true; restoreState = true
                }
            },
            onClicarSeta = { estoqueExpandido = !estoqueExpandido }
        )
        AnimatedVisibility(visible = estoqueExpandido) {
            Column(modifier = Modifier.padding(start = 16.dp)) {
                ItemDrawerSubtela(
                    titulo = "Cardápios",
                    subtitulo = "Drinks e cardápios",
                    selecionado = rotaAtualStr == RotasApp.CARDAPIO,
                    onClick = {
                        fecharMenu()
                        navController.navigate(RotasApp.CARDAPIO) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true; restoreState = true
                        }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // ===== EQUIPAMENTOS (com setinha — subtela: Grupos) =====
        ItemDrawerPai(
            titulo = "Equipamentos",
            subtitulo = "Inventário de materiais",
            selecionado = rotaAtualStr == RotasApp.EQUIPAMENTOS,
            expandido = equipamentosExpandido,
            onClicarTitulo = {
                fecharMenu()
                navController.navigate(RotasApp.EQUIPAMENTOS) {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true; restoreState = true
                }
            },
            onClicarSeta = { equipamentosExpandido = !equipamentosExpandido }
        )
        AnimatedVisibility(visible = equipamentosExpandido) {
            Column(modifier = Modifier.padding(start = 16.dp)) {
                ItemDrawerSubtela(
                    titulo = "Grupos",
                    subtitulo = "Kits por tamanho de evento",
                    // Selecionado quando estiver na rota direta do Grupo
                    selecionado = rotaAtualStr == RotasApp.EQUIPAMENTOS_GRUPO,
                    onClick = {
                        fecharMenu()
                        // Navega para rota separada que inicia direto no Grupo
                        navController.navigate(RotasApp.EQUIPAMENTOS_GRUPO) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true; restoreState = true
                        }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // ===== CADASTRO GERAL =====
        ItemDrawerSimples(
            titulo = "Cadastro Geral",
            subtitulo = "Clientes, fornecedores e funcionários",
            selecionado = rotaAtualStr == RotasApp.CADASTRO_GERAL,
            onClick = {
                fecharMenu()
                navController.navigate(RotasApp.CADASTRO_GERAL) {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true; restoreState = true
                }
            }
        )

        Spacer(modifier = Modifier.height(4.dp))

        // ===== EVENTOS =====
        ItemDrawerSimples(
            titulo = "Eventos",
            subtitulo = "Gerenciamento de eventos",
            selecionado = rotaAtualStr == RotasApp.EVENTOS,
            onClick = {
                fecharMenu()
                navController.navigate(RotasApp.EVENTOS) {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true; restoreState = true
                }
            }
        )

        Spacer(modifier = Modifier.weight(1f))
        HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp))
        Text("DRINX v1.0", modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp), fontSize = 11.sp, color = Color.Gray)
    }
}

// ============================================================
// COMPONENTES DO DRAWER
// ============================================================

@Composable
fun ItemDrawerPai(
    titulo: String, subtitulo: String, selecionado: Boolean,
    expandido: Boolean, onClicarTitulo: () -> Unit, onClicarSeta: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 2.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (selecionado) Color.Black else Color.Transparent)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f).clickable { onClicarTitulo() }) {
            Text(titulo, fontWeight = if (selecionado) FontWeight.Bold else FontWeight.Normal, fontSize = 15.sp, color = if (selecionado) Color.White else Color.Black)
            Text(subtitulo, fontSize = 11.sp, color = if (selecionado) Color.White.copy(alpha = 0.7f) else Color.Gray)
        }
        IconButton(onClick = onClicarSeta, modifier = Modifier.size(32.dp)) {
            Icon(
                imageVector = if (expandido) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = null,
                tint = if (selecionado) Color.White else Color.Black,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun ItemDrawerSimples(titulo: String, subtitulo: String, selecionado: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 2.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (selecionado) Color.Black else Color.Transparent)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(titulo, fontWeight = if (selecionado) FontWeight.Bold else FontWeight.Normal, fontSize = 15.sp, color = if (selecionado) Color.White else Color.Black)
            Text(subtitulo, fontSize = 11.sp, color = if (selecionado) Color.White.copy(alpha = 0.7f) else Color.Gray)
        }
    }
}

@Composable
fun ItemDrawerSubtela(titulo: String, subtitulo: String, selecionado: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 12.dp, top = 2.dp, bottom = 2.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(if (selecionado) Color(0xFF333333) else Color(0xFFF5F5F5))
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.width(3.dp).height(28.dp).clip(RoundedCornerShape(2.dp)).background(if (selecionado) Color.White else Color.Black))
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(titulo, fontWeight = if (selecionado) FontWeight.Bold else FontWeight.Medium, fontSize = 14.sp, color = if (selecionado) Color.White else Color.Black)
            Text(subtitulo, fontSize = 11.sp, color = if (selecionado) Color.White.copy(alpha = 0.7f) else Color.Gray)
        }
    }
}
