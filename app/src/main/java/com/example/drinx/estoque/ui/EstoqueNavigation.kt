package com.example.drinx.estoque.ui

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.drinx.estoque.viewmodel.EstoqueViewModel

object Rotas {
    const val ESTOQUE = "estoque"
    const val CADASTRO = "cadastro"
    const val DETALHE = "detalhe"
}

// onAbrirMenu agora é passado para EstoqueScreen abrir o drawer lateral
@Composable
fun EstoqueNavigation(
    onAbrirMenu: () -> Unit = {}
) {
    val navController = rememberNavController()
    val viewModel: EstoqueViewModel = viewModel()

    NavHost(navController = navController, startDestination = Rotas.ESTOQUE) {

        composable(Rotas.ESTOQUE) {
            EstoqueScreen(
                viewModel = viewModel,
                onNavigateCadastro = { navController.navigate(Rotas.CADASTRO) },
                onNavigateDetalhe = { produto ->
                    viewModel.selecionarProduto(produto)
                    navController.navigate(Rotas.DETALHE)
                },
                onAbrirMenu = onAbrirMenu  // ← conecta ao drawer do MainNavigation
            )
        }

        composable(Rotas.CADASTRO) {
            CadastroProdutoScreen(
                viewModel = viewModel,
                onVoltar = { navController.popBackStack() }
            )
        }

        composable(Rotas.DETALHE) {
            val uiState by viewModel.uiState.collectAsState()
            val produto = uiState.produtoSelecionado
            if (produto != null) {
                DetalheProdutoScreen(
                    produto = produto,
                    viewModel = viewModel,
                    onVoltar = { navController.popBackStack() }
                )
            }
        }
    }
}
