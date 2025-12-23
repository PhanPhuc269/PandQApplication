package com.group1.pandqapplication.shared.data.repository

import com.group1.pandqapplication.shared.data.remote.AppApiService
import com.group1.pandqapplication.shared.data.remote.dto.AddressDto
import com.group1.pandqapplication.shared.data.remote.dto.CreateAddressRequest
import com.group1.pandqapplication.shared.data.remote.dto.UpdateAddressRequest
import javax.inject.Inject

class AddressRepositoryImpl @Inject constructor(
    private val apiService: AppApiService
) : AddressRepository {

    override suspend fun getAddressesByUserId(userId: String): Result<List<AddressDto>> {
        return try {
            val response = apiService.getAddressesByUserId(userId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAddressById(id: String): Result<AddressDto> {
        return try {
            val response = apiService.getAddressById(id)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createAddress(request: CreateAddressRequest): Result<AddressDto> {
        return try {
            val response = apiService.createAddress(request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateAddress(id: String, request: UpdateAddressRequest): Result<AddressDto> {
        return try {
            val response = apiService.updateAddress(id, request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteAddress(id: String): Result<Unit> {
        return try {
            apiService.deleteAddress(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
