package com.group1.pandqapplication.shared.data.repository

import com.group1.pandqapplication.shared.data.remote.dto.AddressDto
import com.group1.pandqapplication.shared.data.remote.dto.CreateAddressRequest
import com.group1.pandqapplication.shared.data.remote.dto.UpdateAddressRequest

interface AddressRepository {
    suspend fun getAddressesByUserId(userId: String): Result<List<AddressDto>>
    suspend fun getAddressById(id: String): Result<AddressDto>
    suspend fun createAddress(request: CreateAddressRequest): Result<AddressDto>
    suspend fun updateAddress(id: String, request: UpdateAddressRequest): Result<AddressDto>
    suspend fun deleteAddress(id: String): Result<Unit>
    suspend fun setDefaultAddress(id: String): Result<Unit>
}
