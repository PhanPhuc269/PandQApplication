package com.group1.pandqapplication.ui.chat

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Holds product context temporarily for chat divider display.
 * This is shared across the entire app as a Singleton.
 */
@Singleton
class ProductContextHolder @Inject constructor() {
    var productId: String = ""
    var productName: String = ""
    var productImage: String = ""
    var productPrice: String = ""
    
    fun setContext(id: String, name: String, image: String, price: String) {
        android.util.Log.d("ProductContextHolder", "setContext: id=$id, name=$name, image=$image, price=$price")
        this.productId = id
        this.productName = name
        this.productImage = image
        this.productPrice = price
    }
    
    fun clear() {
        this.productId = ""
        this.productName = ""
        this.productImage = ""
        this.productPrice = ""
    }
}
