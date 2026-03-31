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
fun EditarEventoScreen(
    evento: Evento,
    onVoltar: () -> Unit,
    onSalvar: (Evento) -> Unit
) {
    var contratante by remember { mutableStateOf(evento.contratante) }
    var cpfCnpj by remember { mutableStateOf(evento.cpfCnpj) }
    var telefone by remember { mutableStateOf(evento.telefone) }
    var email by remember { mutableStateOf(evento.email) }
    var dataEvento by remember { mutableStateOf(evento.dataEvento) }
    var dataCadastro by remember { mutableStateOf(evento.dataCadastro) }
    var status by remember { mutableStateOf(evento.status) }
    var local by remember { mutableStateOf(evento.local) }
    var quantidade by remember { mutableStateOf(evento.quantidadePessoas.toString()) }
    var duracao by remember { mutableStateOf(evento.duracao) }
    var cardapio by remember { mutableStateOf(evento.cardapio) }
    var grupoEquipamentos by remember { mutableStateOf(evento.grupoEquipamentos) }
    var quantidadeIntegrantes by remember { mutableStateOf(evento.quantidadeIntegrantes.toString()) }
    var equipe by remember { mutableStateOf(evento.equipe) }
    var funcaoEquipe by remember { mutableStateOf(evento.funcaoEquipe) }
    var observacao by remember { mutableStateOf(evento.observacao) }
    var erro by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("< Voltar", modifier = Modifier.clickable { onVoltar() }, color = Color.Black)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Editar Evento", style = MaterialTheme.typography.headlineSmall, color = Color.Black)
        Spacer(modifier = Modifier.height(20.dp))

        if (erro.isNotBlank()) {
            Text(text = erro, color = Color.Red)
            Spacer(modifier = Modifier.height(16.dp))
        }

        Text("Dados da contratada", style = MaterialTheme.typography.titleMedium, color = Color.Black)
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(value = contratante, onValueChange = { contratante = it; erro = "" }, label = { Text("Nome da pessoa ou empresa *") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(value = cpfCnpj, onValueChange = { cpfCnpj = it }, label = { Text("CPF ou CNPJ") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(value = telefone, onValueChange = { telefone = it }, label = { Text("Número para contato") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("E-mail") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(20.dp))

        Text("Detalhes do evento", style = MaterialTheme.typography.titleMedium, color = Color.Black)
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(value = dataEvento, onValueChange = { dataEvento = it; erro = "" }, label = { Text("Data do evento *") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(value = dataCadastro, onValueChange = { dataCadastro = it; erro = "" }, label = { Text("Data de cadastro *") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))

        Text("Status", style = MaterialTheme.typography.titleSmall, color = Color.Black)
        Spacer(modifier = Modifier.height(8.dp))
        listOf("Agendado", "Realizado", "Cancelado").forEach { opcao ->
            Row(modifier = Modifier.fillMaxWidth().clickable { status = opcao }, horizontalArrangement = Arrangement.Start) {
                RadioButton(selected = status == opcao, onClick = { status = opcao })
                Text(text = opcao, modifier = Modifier.padding(top = 12.dp), color = Color.Black)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(value = local, onValueChange = { local = it; erro = "" }, label = { Text("Local *") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(value = quantidade, onValueChange = { quantidade = it; erro = "" }, label = { Text("Quantidade de pessoas *") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(value = duracao, onValueChange = { duracao = it }, label = { Text("Tempo de duração") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(20.dp))

        Text("Cardápio e equipamentos", style = MaterialTheme.typography.titleMedium, color = Color.Black)
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(value = cardapio, onValueChange = { cardapio = it }, label = { Text("Cardápio") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(value = grupoEquipamentos, onValueChange = { grupoEquipamentos = it }, label = { Text("Grupo de equipamentos") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(20.dp))

        Text("Informações da equipe", style = MaterialTheme.typography.titleMedium, color = Color.Black)
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(value = quantidadeIntegrantes, onValueChange = { quantidadeIntegrantes = it }, label = { Text("Quantidade de integrantes") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(value = equipe, onValueChange = { equipe = it }, label = { Text("Nome da equipe") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(value = funcaoEquipe, onValueChange = { funcaoEquipe = it }, label = { Text("Função da equipe no evento") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(20.dp))

        Text("Observação", style = MaterialTheme.typography.titleMedium, color = Color.Black)
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(value = observacao, onValueChange = { observacao = it }, label = { Text("Observação") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                when {
                    contratante.isBlank() -> erro = "Preencha o nome da pessoa ou empresa."
                    dataEvento.isBlank() -> erro = "Preencha a data do evento."
                    dataCadastro.isBlank() -> erro = "Preencha a data de cadastro."
                    local.isBlank() -> erro = "Preencha o local do evento."
                    quantidade.isBlank() -> erro = "Preencha a quantidade de pessoas."
                    quantidade.toIntOrNull() == null -> erro = "Quantidade deve ser número."
                    else -> onSalvar(Evento(
                        id = evento.id, contratante = contratante, cpfCnpj = cpfCnpj,
                        telefone = telefone, email = email, dataEvento = dataEvento,
                        dataCadastro = dataCadastro, status = status, local = local,
                        quantidadePessoas = quantidade.toInt(), duracao = duracao,
                        cardapio = cardapio, grupoEquipamentos = grupoEquipamentos,
                        quantidadeIntegrantes = quantidadeIntegrantes.toIntOrNull() ?: 0,
                        equipe = equipe, funcaoEquipe = funcaoEquipe, observacao = observacao
                    ))
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black, contentColor = Color.White)
        ) { Text("Salvar Alterações") }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
