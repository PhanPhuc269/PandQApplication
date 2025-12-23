package com.group1.pandqapplication.shared.data.remote.dto

data class AddressDto(
    val id: String,
    val userId: String,
    val receiverName: String,
    val phone: String,
    val detailAddress: String,
    val ward: String,
    val district: String,
    val city: String,
    val isDefault: Boolean
)

data class CreateAddressRequest(
    val userId: String,
    val receiverName: String,
    val phone: String,
    val detailAddress: String,
    val ward: String,
    val district: String,
    val city: String,
    val isDefault: Boolean = false
)

data class UpdateAddressRequest(
    val receiverName: String,
    val phone: String,
    val detailAddress: String,
    val ward: String,
    val district: String,
    val city: String,
    val isDefault: Boolean = false
)
