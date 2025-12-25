package com.group1.pandqapplication.shared.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.group1.pandqapplication.shared.data.local.model.GuestCart
import com.group1.pandqapplication.shared.data.local.model.GuestCartItem
import com.group1.pandqapplication.shared.data.remote.dto.AddToCartRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.firstOrNull
import java.util.UUID

private val Context.guestCartDataStore: DataStore<Preferences> by preferencesDataStore(name = "guest_cart")

interface GuestCartRepository {
    fun getGuestCart(): Flow<GuestCart>
    suspend fun addToGuestCart(productId: String, productName: String, quantity: Int, price: Double, imageUrl: String? = null)
    suspend fun removeFromGuestCart(productId: String)
    suspend fun updateGuestCartItemQuantity(productId: String, quantity: Int)
    suspend fun clearGuestCart()
    suspend fun getGuestCartItemsForMerge(): List<AddToCartRequest>
}

class GuestCartRepositoryImpl(private val context: Context) : GuestCartRepository {

    private val guestCartKey = stringPreferencesKey("guest_cart_data")
    private val gson = Gson()

    override fun getGuestCart(): Flow<GuestCart> {
        return context.guestCartDataStore.data.map { preferences ->
            val cartJson = preferences[guestCartKey] ?: "{\"items\":[]}"
            try {
                gson.fromJson(cartJson, GuestCart::class.java)
            } catch (e: Exception) {
                GuestCart()
            }
        }
    }

    override suspend fun addToGuestCart(
        productId: String,
        productName: String,
        quantity: Int,
        price: Double,
        imageUrl: String?
    ) {
        context.guestCartDataStore.edit { preferences ->
            val cartJson = preferences[guestCartKey] ?: "{\"items\":[]}"
            val currentCart = try {
                gson.fromJson(cartJson, GuestCart::class.java)
            } catch (e: Exception) {
                GuestCart()
            }

            val updatedItems = currentCart.items.toMutableList()
            val existingItem = updatedItems.find { it.productId == productId }

            if (existingItem != null) {
                val index = updatedItems.indexOf(existingItem)
                updatedItems[index] = existingItem.copy(quantity = existingItem.quantity + quantity)
            } else {
                updatedItems.add(
                    GuestCartItem(
                        productId = productId,
                        productName = productName,
                        quantity = quantity,
                        price = price,
                        imageUrl = imageUrl
                    )
                )
            }

            val updatedCart = GuestCart(items = updatedItems)
            preferences[guestCartKey] = gson.toJson(updatedCart)
        }
    }

    override suspend fun removeFromGuestCart(productId: String) {
        context.guestCartDataStore.edit { preferences ->
            val cartJson = preferences[guestCartKey] ?: "{\"items\":[]}"
            val currentCart = try {
                gson.fromJson(cartJson, GuestCart::class.java)
            } catch (e: Exception) {
                GuestCart()
            }

            val updatedItems = currentCart.items.filter { it.productId != productId }
            val updatedCart = GuestCart(items = updatedItems)
            preferences[guestCartKey] = gson.toJson(updatedCart)
        }
    }

    override suspend fun updateGuestCartItemQuantity(productId: String, quantity: Int) {
        context.guestCartDataStore.edit { preferences ->
            val cartJson = preferences[guestCartKey] ?: "{\"items\":[]}"
            val currentCart = try {
                gson.fromJson(cartJson, GuestCart::class.java)
            } catch (e: Exception) {
                GuestCart()
            }

            val updatedItems = currentCart.items.map { item ->
                if (item.productId == productId) {
                    if (quantity > 0) {
                        item.copy(quantity = quantity)
                    } else {
                        null
                    }
                } else {
                    item
                }
            }.filterNotNull()

            val updatedCart = GuestCart(items = updatedItems)
            preferences[guestCartKey] = gson.toJson(updatedCart)
        }
    }

    override suspend fun clearGuestCart() {
        context.guestCartDataStore.edit { preferences ->
            preferences[guestCartKey] = gson.toJson(GuestCart())
        }
    }

    override suspend fun getGuestCartItemsForMerge(): List<AddToCartRequest> {
        return try {
            val guestCart = context.guestCartDataStore.data
                .map { preferences ->
                    val cartJson = preferences[guestCartKey] ?: "{\"items\":[]}"
                    gson.fromJson(cartJson, GuestCart::class.java)
                }
                .firstOrNull() ?: GuestCart()
            
            guestCart.items.map { item ->
                AddToCartRequest(
                    userId = "",
                    productId = item.productId,
                    quantity = item.quantity
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
