package com.example.drinx.eventos.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.drinx.eventos.model.Evento
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ListaEventosScreen(
    eventos: List<Evento>,
    onVoltar: () -> Unit,
    onAbrirDetalhe: (Evento) -> Unit
) {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val eventosOrdenados = eventos.sortedByDescending { evento ->
        try { LocalDate.parse(evento.dataCadastro, formatter) } catch (e: Exception) { LocalDate.MIN }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = "< Voltar", modifier = Modifier.clickable { onVoltar() }, color = Color.Black)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Eventos cadastrados", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(bottom = 80.dp)) {
            items(eventosOrdenados) { evento ->
                EventoCard(evento = evento, onClick = { onAbrirDetalhe(evento) })
            }
        }
    }
}

@Composable
fun EventoCard(evento: Evento, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        border = BorderStroke(1.dp, Color.LightGray),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = evento.contratante, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Evento: ${evento.dataEvento}")
            Text("Cadastro: ${evento.dataCadastro}")
            Text("Status: ${evento.status}")
        }
    }
}
