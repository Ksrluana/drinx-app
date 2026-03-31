package com.example.drinx.equipamentos.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.drinx.R
import com.example.drinx.equipamentos.model.InventoryItem

class InventoryViewModel : ViewModel() {

    val items = mutableStateListOf(
        InventoryItem(
            name = "Faca de Chef", quantity = 10,
            material = "Aço Inox", fragility = "Resistente",
            iconRes = R.drawable.ic_utensilios, categoryName = "Utensílios"
        ),
        InventoryItem(
            name = "Taça de Vinho", quantity = 24,
            material = "Cristal", fragility = "Extra frágil",
            iconRes = R.drawable.ic_copos, categoryName = "Copos"
        )
    )

    fun addItem(item: InventoryItem) {
        items.add(item)
    }

    fun removeItem(item: InventoryItem) {
        items.remove(item)
    }

    fun editItem(antigo: InventoryItem, novo: InventoryItem) {
        val index = items.indexOf(antigo)
        if (index != -1) {
            items[index] = novo
        }
    }
}
