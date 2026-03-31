package com.example.drinx.eventos

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.drinx.eventos.screens.*
import com.example.drinx.eventos.viewmodel.EventoViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

object RotasEventos {
    const val MENU = "eventos_menu"
    const val LISTA = "eventos_lista"
    const val CRIAR = "eventos_criar"
    const val DETALHE = "eventos_detalhe"
    const val EDITAR = "eventos_editar"
    const val FILTRO_DATA_EVENTO = "eventos_filtro_data_evento"
    const val FILTRO_DATA_CADASTRO = "eventos_filtro_data_cadastro"
    const val FILTRO_STATUS = "eventos_filtro_status"
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventosNavigation(
    onAbrirMenu: () -> Unit = {}
) {
    val navController = rememberNavController()
    val viewModel: EventoViewModel = viewModel()

    NavHost(navController = navController, startDestination = RotasEventos.MENU) {

        composable(RotasEventos.MENU) {
            MenuEventosScreen(
                onListaClick = { navController.navigate(RotasEventos.LISTA) },
                onCadastrarClick = { navController.navigate(RotasEventos.CRIAR) },
                onFiltroDataEventoClick = { navController.navigate(RotasEventos.FILTRO_DATA_EVENTO) },
                onFiltroDataCadastroClick = { navController.navigate(RotasEventos.FILTRO_DATA_CADASTRO) },
                onFiltroStatusClick = { navController.navigate(RotasEventos.FILTRO_STATUS) },
                onAbrirMenu = onAbrirMenu
            )
        }

        composable(RotasEventos.LISTA) {
            val eventos by viewModel.eventos.collectAsState()
            ListaEventosScreen(
                eventos = eventos,
                onVoltar = { navController.popBackStack() },
                onAbrirDetalhe = { evento ->
                    viewModel.selecionarEvento(evento)
                    navController.navigate(RotasEventos.DETALHE)
                }
            )
        }

        composable(RotasEventos.CRIAR) {
            CriarEventoScreen(
                onVoltar = { navController.popBackStack() },
                onSalvar = { novoEvento ->
                    viewModel.adicionarEvento(novoEvento)
                    navController.navigate(RotasEventos.LISTA) {
                        popUpTo(RotasEventos.MENU)
                    }
                }
            )
        }

        composable(RotasEventos.DETALHE) {
            val evento by viewModel.eventoSelecionado.collectAsState()
            evento?.let {
                DetalheEventoScreen(
                    evento = it,
                    onVoltar = { navController.popBackStack() },
                    onEditar = { navController.navigate(RotasEventos.EDITAR) },
                    onExcluir = {
                        viewModel.excluirEvento(it.id)
                        navController.navigate(RotasEventos.LISTA) {
                            popUpTo(RotasEventos.MENU)
                        }
                    }
                )
            }
        }

        composable(RotasEventos.EDITAR) {
            val evento by viewModel.eventoSelecionado.collectAsState()
            evento?.let {
                EditarEventoScreen(
                    evento = it,
                    onVoltar = { navController.popBackStack() },
                    onSalvar = { eventoAtualizado ->
                        viewModel.editarEvento(eventoAtualizado)
                        navController.popBackStack()
                    }
                )
            }
        }

        composable(RotasEventos.FILTRO_DATA_EVENTO) {
            val eventos by viewModel.eventos.collectAsState()
            FiltroDataEventoScreen(
                eventos = eventos,
                onVoltar = { navController.popBackStack() },
                onAbrirDetalhe = { evento ->
                    viewModel.selecionarEvento(evento)
                    navController.navigate(RotasEventos.DETALHE)
                }
            )
        }

        composable(RotasEventos.FILTRO_DATA_CADASTRO) {
            val eventos by viewModel.eventos.collectAsState()
            FiltroDataCadastroScreen(
                eventos = eventos,
                onVoltar = { navController.popBackStack() },
                onAbrirDetalhe = { evento ->
                    viewModel.selecionarEvento(evento)
                    navController.navigate(RotasEventos.DETALHE)
                }
            )
        }

        composable(RotasEventos.FILTRO_STATUS) {
            val eventos by viewModel.eventos.collectAsState()
            FiltroStatusScreen(
                eventos = eventos,
                onVoltar = { navController.popBackStack() },
                onAbrirDetalhe = { evento ->
                    viewModel.selecionarEvento(evento)
                    navController.navigate(RotasEventos.DETALHE)
                }
            )
        }
    }
}
