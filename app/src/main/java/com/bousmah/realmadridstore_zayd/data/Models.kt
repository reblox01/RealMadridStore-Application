package com.bousmah.realmadridstore_zayd.data

import android.graphics.Bitmap

data class Product(
    val id: String,
    val name: String,
    val price: Double,
    val category: String,
    val imageUrl: String,
    val storeUrl: String,
    val isFavorite: Boolean = false
)

data class CartItem(
    val product: Product,
    var quantity: Int = 1
)

data class Store(
    val name: String,
    val address: String,
    val openingHours: String,
    val latitude: Double,
    val longitude: Double
)

data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val image: Bitmap? = null,
    val timestamp: Long = System.currentTimeMillis()
)
