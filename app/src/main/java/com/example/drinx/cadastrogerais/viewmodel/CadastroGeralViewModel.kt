package com.example.drinx.cadastrogerais.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.drinx.cadastrogerais.model.*
import com.google.firebase.database.*

class CadastroGeralViewModel : ViewModel() {

    private val database = FirebaseDatabase
        .getInstance()
        .getReference("cadastrogerais")

    val pessoas = mutableStateListOf<Pessoa>()

    private var nextId = 1

    init {
        carregarPessoas()
    }

    private fun carregarPessoas() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                pessoas.clear()

                snapshot.children.forEach {
                    val pessoaFirebase = it.getValue(PessoaFirebase::class.java)

                    pessoaFirebase?.let { p ->
                        val pessoa = Pessoa(
                            id = p.id,
                            tipoDocumento = TipoDocumento.valueOf(p.tipoDocumento),
                            documento = p.documento,
                            nome = p.nome,
                            email = p.email,
                            contato = p.contato,
                            endereco = p.endereco,
                            flag = FlagPessoa.valueOf(p.flag),
                            situacao = SituacaoPessoa.valueOf(p.situacao),
                            observacao = p.observacao
                        )

                        pessoas.add(pessoa)

                        if (pessoa.id >= nextId) {
                            nextId = pessoa.id + 1
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun adicionarPessoa(pessoa: Pessoa) {
        val novaPessoa = pessoa.copy(id = nextId++)

        val firebasePessoa = PessoaFirebase(
            id = novaPessoa.id,
            tipoDocumento = novaPessoa.tipoDocumento.name,
            documento = novaPessoa.documento,
            nome = novaPessoa.nome,
            email = novaPessoa.email,
            contato = novaPessoa.contato,
            endereco = novaPessoa.endereco,
            flag = novaPessoa.flag.name,
            situacao = novaPessoa.situacao.name,
            observacao = novaPessoa.observacao
        )

        database
            .child(novaPessoa.id.toString())
            .setValue(firebasePessoa)
    }

    fun atualizarPessoa(pessoa: Pessoa) {
        val firebasePessoa = PessoaFirebase(
            id = pessoa.id,
            tipoDocumento = pessoa.tipoDocumento.name,
            documento = pessoa.documento,
            nome = pessoa.nome,
            email = pessoa.email,
            contato = pessoa.contato,
            endereco = pessoa.endereco,
            flag = pessoa.flag.name,
            situacao = pessoa.situacao.name,
            observacao = pessoa.observacao
        )

        database
            .child(pessoa.id.toString())
            .setValue(firebasePessoa)
    }

    fun excluirPessoa(id: Int) {
        database
            .child(id.toString())
            .removeValue()
    }
}