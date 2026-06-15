package com.example.drinx.equipamentos.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.drinx.equipamentos.model.InventoryItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class InventoryViewModel : ViewModel() {

    private val database = FirebaseDatabase.getInstance()
    private val equipamentosRef = database.getReference("equipamentos")

    val items = mutableStateListOf<InventoryItem>()

    init {
        carregarItens()
    }

    private fun carregarItens() {
        equipamentosRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                items.clear()

                for (itemSnapshot in snapshot.children) {
                    val item = itemSnapshot.getValue(InventoryItem::class.java)
                    if (item != null) {
                        items.add(item)
                    }
                }

                Log.d("FIREBASE_TESTE", "Equipamentos carregados: ${items.size}")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FIREBASE_TESTE", "Erro ao carregar equipamentos: ${error.message}")
            }
        })
    }

    fun addItem(item: InventoryItem) {
        val id = equipamentosRef.push().key

        if (id != null) {
            equipamentosRef.child(id)
                .setValue(item)
                .addOnSuccessListener {
                    Log.d("FIREBASE_TESTE", "Equipamento salvo no Realtime Database")
                }
                .addOnFailureListener { erro ->
                    Log.e("FIREBASE_TESTE", "Erro ao salvar equipamento", erro)
                }
        }
    }

    fun removeItem(item: InventoryItem) {
        equipamentosRef
            .orderByChild("name")
            .equalTo(item.name)
            .get()
            .addOnSuccessListener { snapshot ->
                for (itemSnapshot in snapshot.children) {
                    itemSnapshot.ref.removeValue()
                }
            }
    }

    fun editItem(antigo: InventoryItem, novo: InventoryItem) {
        equipamentosRef
            .orderByChild("name")
            .equalTo(antigo.name)
            .get()
            .addOnSuccessListener { snapshot ->
                for (itemSnapshot in snapshot.children) {
                    itemSnapshot.ref.setValue(novo)
                }
            }
    }
}