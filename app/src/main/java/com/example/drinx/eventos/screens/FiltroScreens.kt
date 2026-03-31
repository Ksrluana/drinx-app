package com.example.drinx.eventos.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.drinx.eventos.model.Evento
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FiltroDataEventoScreen(
    eventos: List<Evento>,
    onVoltar: () -> Unit,
    onAbrirDetalhe: (Evento) -> Unit
) {
    var filtro by remember { mutableStateOf("") }
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val eventosFiltrados = (if (filtro.isBlank()) eventos else eventos.filter { it.dataEvento.contains(filtro, ignoreCase = true) })
        .sortedByDescending { try { LocalDate.parse(it.dataCadastro, formatter) } catch (e: Exception) { LocalDate.MIN } }

    Column(modifier = Modifier.fillMaxSize().background(Color.White).padding(16.dp)) {
        Spacer(modifier = Modifier.height(24.dp))
        Text("< Voltar", modifier = Modifier.clickable { onVoltar() }, color = Color.Black)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Filtrar por Data do Evento", style = MaterialTheme.typography.headlineSmall, color = Color.Black)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = filtro, onValueChange = { filtro = it }, label = { Text("Digite a data do evento") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(20.dp))
        Text("Resultados: ${eventosFiltrados.size}", color = Color.Black)
        Spacer(modifier = Modifier.height(12.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(bottom = 80.dp)) {
            items(eventosFiltrados) { evento -> EventoCard(evento = evento, onClick = { onAbrirDetalhe(evento) }) }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FiltroDataCadastroScreen(
    eventos: List<Evento>,
    onVoltar: () -> Unit,
    onAbrirDetalhe: (Evento) -> Unit
) {
    var filtro by remember { mutableStateOf("") }
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val eventosFiltrados = (if (filtro.isBlank()) eventos else eventos.filter { it.dataCadastro.contains(filtro, ignoreCase = true) })
        .sortedByDescending { try { LocalDate.parse(it.dataCadastro, formatter) } catch (e: Exception) { LocalDate.MIN } }

    Column(modifier = Modifier.fillMaxSize().background(Color.White).padding(16.dp)) {
        Spacer(modifier = Modifier.height(24.dp))
        Text("< Voltar", modifier = Modifier.clickable { onVoltar() }, color = Color.Black)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Filtrar por Data de Cadastro", style = MaterialTheme.typography.headlineSmall, color = Color.Black)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = filtro, onValueChange = { filtro = it }, label = { Text("Digite a data de cadastro") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(20.dp))
        Text("Resultados: ${eventosFiltrados.size}", color = Color.Black)
        Spacer(modifier = Modifier.height(12.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(bottom = 80.dp)) {
            items(eventosFiltrados) { evento -> EventoCard(evento = evento, onClick = { onAbrirDetalhe(evento) }) }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FiltroStatusScreen(
    eventos: List<Evento>,
    onVoltar: () -> Unit,
    onAbrirDetalhe: (Evento) -> Unit
) {
    var statusSelecionado by remember { mutableStateOf("Agendado") }
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val eventosFiltrados = eventos
        .filter { it.status == statusSelecionado }
        .sortedByDescending { try { LocalDate.parse(it.dataCadastro, formatter) } catch (e: Exception) { LocalDate.MIN } }

    Column(modifier = Modifier.fillMaxSize().background(Color.White).padding(16.dp)) {
        Spacer(modifier = Modifier.height(24.dp))
        Text("< Voltar", modifier = Modifier.clickable { onVoltar() }, color = Color.Black)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Filtrar por Status", style = MaterialTheme.typography.headlineSmall, color = Color.Black)
        Spacer(modifier = Modifier.height(16.dp))

        listOf("Agendado", "Realizado", "Cancelado").forEach { status ->
            Row(modifier = Modifier.fillMaxWidth().clickable { statusSelecionado = status }, horizontalArrangement = Arrangement.Start) {
                RadioButton(selected = statusSelecionado == status, onClick = { statusSelecionado = status })
                Text(text = status, modifier = Modifier.padding(top = 12.dp), color = Color.Black)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Resultados: ${eventosFiltrados.size}", color = Color.Black)
        Spacer(modifier = Modifier.height(12.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(bottom = 80.dp)) {
            items(eventosFiltrados) { evento -> EventoCard(evento = evento, onClick = { onAbrirDetalhe(evento) }) }
        }
    }
}
