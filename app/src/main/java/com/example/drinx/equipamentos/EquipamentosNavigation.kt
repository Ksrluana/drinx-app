package com.example.drinx.equipamentos

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.drinx.equipamentos.screens.AddItemScreen
import com.example.drinx.equipamentos.screens.CategoryDetailScreen
import com.example.drinx.equipamentos.screens.EquipamentosScreen
import com.example.drinx.equipamentos.viewmodel.InventoryViewModel
import com.example.drinx.subtelas.GrupoEquipamentoScreen

object RotasEquipamentos {
    const val PRINCIPAL = "equipamentos_principal"
    const val DETALHE_CATEGORIA = "equipamentos_detalhe/{categoryId}"
    const val ADICIONAR_ITEM = "equipamentos_adicionar"
    const val GRUPO = "equipamentos_grupo"

    fun rotaDetalhe(categoryId: String) = "equipamentos_detalhe/$categoryId"
}

@Composable
fun EquipamentosNavigation(
    onAbrirMenu: () -> Unit = {},
    iniciarNoGrupo: Boolean = false
) {
    val navController = rememberNavController()
    val viewModel: InventoryViewModel = viewModel()

    val startDestination = if (iniciarNoGrupo) RotasEquipamentos.GRUPO else RotasEquipamentos.PRINCIPAL

    NavHost(navController = navController, startDestination = startDestination) {

        composable(RotasEquipamentos.PRINCIPAL) {
            EquipamentosScreen(
                items = viewModel.items,
                onCategoryClick = { category ->
                    navController.navigate(RotasEquipamentos.rotaDetalhe(category.title))
                },
                onAddItemClick = { navController.navigate(RotasEquipamentos.ADICIONAR_ITEM) },
                onAbrirMenu = onAbrirMenu
            )
        }

        composable(
            route = RotasEquipamentos.DETALHE_CATEGORIA,
            arguments = listOf(navArgument("categoryId") { type = NavType.StringType })
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId")
            CategoryDetailScreen(
                categoryName = categoryId,
                items = viewModel.items,
                onBack = { navController.popBackStack() },
                onRemoveItem = { viewModel.removeItem(it) },
                onEditItem = { antigo, novo -> viewModel.editItem(antigo, novo) }
            )
        }

        composable(RotasEquipamentos.ADICIONAR_ITEM) {
            AddItemScreen(
                onBack = { navController.popBackStack() },
                onItemSaved = { newItem ->
                    viewModel.addItem(newItem)
                    navController.popBackStack()
                }
            )
        }

        // Subtela Grupo — recebe os itens cadastrados em Equipamentos
        composable(RotasEquipamentos.GRUPO) {
            GrupoEquipamentoScreen(
                onAbrirMenu = onAbrirMenu,
                itensEquipamentos = viewModel.items  // ← passa os itens para o Grupo
            )
        }
    }
}
