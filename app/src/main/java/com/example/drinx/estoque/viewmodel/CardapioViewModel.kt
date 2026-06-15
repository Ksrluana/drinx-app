package com.example.drinx.estoque.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.google.firebase.database.*

data class DrinkFirebase(
    val id: Int = 0,
    val nome: String = "",
    val desc: String = "",
    val imagemUri: String? = null
)

class CardapioViewModel : ViewModel() {

    private val database = FirebaseDatabase
        .getInstance()
        .getReference("cardapio")

    val drinks = mutableStateListOf<DrinkFirebase>()

    private var nextId = 1

    init {
        carregarDrinks()
    }

    private fun carregarDrinks() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                drinks.clear()

                snapshot.children.forEach {
                    val drink = it.getValue(DrinkFirebase::class.java)

                    drink?.let { d ->
                        drinks.add(d)

                        if (d.id >= nextId) {
                            nextId = d.id + 1
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun adicionarDrink(nome: String, desc: String, imagemUri: String?) {
        val novo = DrinkFirebase(
            id = nextId++,
            nome = nome,
            desc = desc,
            imagemUri = imagemUri
        )

        database.child(novo.id.toString()).setValue(novo)
    }

    fun atualizarDrink(drink: DrinkFirebase) {
        database.child(drink.id.toString()).setValue(drink)
    }

    fun excluirDrink(id: Int) {
        database.child(id.toString()).removeValue()
    }
}