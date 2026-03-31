package com.example.drinx.eventos.data

import com.example.drinx.eventos.model.Evento

val listaEventosFake = listOf(
    Evento(
        id = 1,
        contratante = "Maria Souza",
        cpfCnpj = "123.456.789-00",
        telefone = "(62) 99999-1111",
        email = "maria@email.com",
        dataEvento = "10/04/2026",
        dataCadastro = "20/03/2026",
        status = "Agendado",
        local = "Salão Central",
        quantidadePessoas = 100,
        duracao = "4 horas",
        cardapio = "Drinks Clássicos",
        grupoEquipamentos = "Grupo 100 Pessoas",
        quantidadeIntegrantes = 4,
        equipe = "João, Ana, Pedro, Lucas",
        funcaoEquipe = "Coordenador, Bartender, Bartender, Keeper",
        observacao = "Cliente pediu atenção com gelo"
    ),
    Evento(
        id = 2,
        contratante = "Empresa Alfa",
        cpfCnpj = "12.345.678/0001-90",
        telefone = "(62) 98888-2222",
        email = "contato@empresaalfa.com",
        dataEvento = "15/04/2026",
        dataCadastro = "18/03/2026",
        status = "Cancelado",
        local = "Chácara Boa Vista",
        quantidadePessoas = 50,
        duracao = "3 horas",
        cardapio = "Open Bar Premium",
        grupoEquipamentos = "Grupo 50 Pessoas",
        quantidadeIntegrantes = 2,
        equipe = "Marcos, Júlia",
        funcaoEquipe = "Coordenador, Bartender",
        observacao = "Cancelado pelo contratante"
    )
)
