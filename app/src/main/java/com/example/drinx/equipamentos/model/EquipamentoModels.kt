package com.example.drinx.equipamentos.model

import androidx.annotation.DrawableRes

data class InventoryCategory(
    val title: String,
    val description: String,
    @DrawableRes val iconRes: Int
)

data class InventoryItem(
    val name: String,
    val quantity: Int,
    val material: String,
    val fragility: String,
    @DrawableRes val iconRes: Int?,
    val categoryName: String
)
