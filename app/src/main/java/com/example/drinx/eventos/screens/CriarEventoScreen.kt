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
fun CriarEventoScreen(
    onVoltar: () -> Unit,
    onSalvar: (Evento) -> Unit
) {
    var contratante by remember { mutableStateOf("") }
    var cpfCnpj by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var dataEvento by remember { mutableStateOf("") }
    var dataCadastro by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("Agendado") }
    var local by remember { mutableStateOf("") }
    var quantidade by remember { mutableStateOf("") }
    var duracao by remember { mutableStateOf("") }
    var cardapio by remember { mutableStateOf("") }
    var grupoEquipamentos by remember { mutableStateOf("") }
    var quantidadeIntegrantes by remember { mutableStateOf("") }
    var equipe by remember { mutableStateOf("") }
    var funcaoEquipe by remember { mutableStateOf("") }
    var observacao by remember { mutableStateOf("") }
    var erro by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = "< Voltar", modifier = Modifier.clickable { onVoltar() }, color = Color.Black)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Cadastrar Evento", style = MaterialTheme.typography.headlineSmall, color = Color.Black)
        Spacer(modifier = Modifier.height(20.dp))

        if (erro.isNotBlank()) {
            Text(text = erro, color = Color.Red, style = MaterialTheme.typography.bodyMedium)
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
        OutlinedTextField(value = dataEvento, onValueChange = { dataEvento = it; erro = "" }, label = { Text("Data do evento * (dd/MM/yyyy)") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(value = dataCadastro, onValueChange = { dataCadastro = it; erro = "" }, label = { Text("Data de cadastro * (dd/MM/yyyy)") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))

        Text("Status", style = MaterialTheme.typography.titleSmall, color = Color.Black)
        Spacer(modifier = Modifier.height(8.dp))
        listOf("Agendado", "Realizado", "Cancelado").forEach { opcao ->
            StatusOption(texto = opcao, selecionado = status == opcao, onSelecionar = { status = opcao })
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
                    quantidade.toIntOrNull() == null -> erro = "A quantidade de pessoas deve ser um número."
                    else -> {
                        onSalvar(Evento(
                            id = (1000..9999).random(),
                            contratante = contratante, cpfCnpj = cpfCnpj,
                            telefone = telefone, email = email,
                            dataEvento = dataEvento, dataCadastro = dataCadastro,
                            status = status, local = local,
                            quantidadePessoas = quantidade.toInt(),
                            duracao = duracao, cardapio = cardapio,
                            grupoEquipamentos = grupoEquipamentos,
                            quantidadeIntegrantes = quantidadeIntegrantes.toIntOrNull() ?: 0,
                            equipe = equipe, funcaoEquipe = funcaoEquipe, observacao = observacao
                        ))
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black, contentColor = Color.White)
        ) { Text("Salvar") }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun StatusOption(texto: String, selecionado: Boolean, onSelecionar: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable { onSelecionar() },
        horizontalArrangement = Arrangement.Start
    ) {
        RadioButton(selected = selecionado, onClick = { onSelecionar() })
        Text(text = texto, modifier = Modifier.padding(top = 12.dp), color = Color.Black)
    }
}
