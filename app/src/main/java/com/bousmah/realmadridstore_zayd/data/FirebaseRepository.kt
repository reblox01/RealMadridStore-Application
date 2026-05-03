package com.bousmah.realmadridstore_zayd.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    suspend fun saveProducts(products: List<Product>) {
        val batch = firestore.batch()
        products.forEach { product ->
            val docRef = firestore.collection("products").document(product.id)
            batch.set(docRef, product)
        }
        batch.commit().await()
    }

    suspend fun getAllProducts(): List<Product> {
        return try {
            val snapshot = firestore.collection("products").get().await()
            snapshot.toObjects(Product::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun saveOrder(cartItems: List<CartItem>, total: Double) {
        val userId = auth.currentUser?.uid ?: "anonymous"
        val order = mapOf(
            "userId" to userId,
            "items" to cartItems,
            "total" to total,
            "timestamp" to System.currentTimeMillis()
        )
        firestore.collection("orders").add(order).await()
    }

    suspend fun updateWishlist(wishlist: Set<String>) {
        val userId = auth.currentUser?.uid ?: return
        firestore.collection("wishlists").document(userId)
            .set(mapOf("productIds" to wishlist.toList()))
            .await()
    }

    suspend fun getWishlist(): Set<String> {
        val userId = auth.currentUser?.uid ?: return emptySet()
        return try {
            val doc = firestore.collection("wishlists").document(userId).get().await()
            (doc.get("productIds") as? List<String>)?.toSet() ?: emptySet()
        } catch (e: Exception) {
            emptySet()
        }
    }
}
