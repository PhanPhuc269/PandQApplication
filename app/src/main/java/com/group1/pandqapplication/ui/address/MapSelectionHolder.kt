package com.group1.pandqapplication.ui.address

/**
 * Simple singleton to share map selection result between screens
 */
object MapSelectionHolder {
    private var selectedAddress: String? = null
    
    fun setAddress(address: String) {
        selectedAddress = address
    }
    
    fun getAndClearAddress(): String? {
        val address = selectedAddress
        selectedAddress = null
        return address
    }
}
