package com.example.drinx.eventos.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.drinx.eventos.model.Evento

@Composable
fun DetalheEventoScreen(
    evento: Evento,
    onVoltar: () -> Unit,
    onExcluir: () -> Unit,
    onEditar: () -> Unit
) {
    var mostrarDialogo by remember { mutableStateOf(false) }

    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = { Text("Excluir evento?") },
            text = { Text("Tem certeza que deseja excluir o evento de \"${evento.contratante}\"?") },
            confirmButton = {
                TextButton(onClick = { mostrarDialogo = false; onExcluir() }) {
                    Text("Excluir", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogo = false }) { Text("Cancelar") }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = "< Voltar", modifier = Modifier.clickable { onVoltar() }, color = Color.Black)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Detalhes do Evento", style = MaterialTheme.typography.headlineSmall, color = Color.Black)
        Spacer(modifier = Modifier.height(20.dp))

        Text("Dados da contratada", style = MaterialTheme.typography.titleMedium, color = Color.Black)
        Spacer(modifier = Modifier.height(12.dp))
        Text("Nome: ${evento.contratante}")
        Text("CPF/CNPJ: ${evento.cpfCnpj}")
        Text("Telefone: ${evento.telefone}")
        Text("E-mail: ${evento.email}")
        Spacer(modifier = Modifier.height(20.dp))

        Text("Detalhes do evento", style = MaterialTheme.typography.titleMedium, color = Color.Black)
        Spacer(modifier = Modifier.height(12.dp))
        Text("Data do evento: ${evento.dataEvento}")
        Text("Data de cadastro: ${evento.dataCadastro}")
        Text("Status: ${evento.status}")
        Text("Local: ${evento.local}")
        Text("Quantidade de pessoas: ${evento.quantidadePessoas}")
        Text("Duração: ${evento.duracao}")
        Spacer(modifier = Modifier.height(20.dp))

        Text("Cardápio e equipamentos", style = MaterialTheme.typography.titleMedium, color = Color.Black)
        Spacer(modifier = Modifier.height(12.dp))
        Text("Cardápio: ${evento.cardapio}")
        Text("Grupo de equipamentos: ${evento.grupoEquipamentos}")
        Spacer(modifier = Modifier.height(20.dp))

        Text("Informações da equipe", style = MaterialTheme.typography.titleMedium, color = Color.Black)
        Spacer(modifier = Modifier.height(12.dp))
        Text("Quantidade de integrantes: ${evento.quantidadeIntegrantes}")
        Text("Equipe: ${evento.equipe}")
        Text("Função da equipe: ${evento.funcaoEquipe}")
        Spacer(modifier = Modifier.height(20.dp))

        Text("Observação", style = MaterialTheme.typography.titleMedium, color = Color.Black)
        Spacer(modifier = Modifier.height(12.dp))
        Text(evento.observacao)
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { onEditar() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black, contentColor = Color.White)
        ) { Text("Editar Evento") }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = { mostrarDialogo = true },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color.Red)
        ) { Text("Excluir Evento") }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
