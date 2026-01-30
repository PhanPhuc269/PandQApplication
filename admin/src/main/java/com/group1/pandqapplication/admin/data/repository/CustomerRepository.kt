package com.group1.pandqapplication.admin.data.repository

import com.group1.pandqapplication.admin.data.remote.AdminApiService
import com.group1.pandqapplication.admin.data.remote.dto.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomerRepository @Inject constructor(
    private val apiService: AdminApiService
) {
    fun getCustomers(
        page: Int = 0,
        size: Int = 20,
        search: String? = null,
        tier: CustomerTier? = null,
        status: AccountStatus? = null
    ): Flow<Result<CustomerListResponse>> = flow {
        try {
            val response = apiService.getCustomers(
                page = page,
                size = size,
                search = search,
                tier = tier?.name,
                status = status?.name
            )
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    fun getCustomerDetail(id: String): Flow<Result<CustomerDetailDto>> = flow {
        try {
            val response = apiService.getCustomerDetail(id)
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    fun updateCustomerStatus(id: String, status: AccountStatus): Flow<Result<Unit>> = flow {
        try {
            apiService.updateCustomerStatus(id, UpdateStatusRequest(status))
            emit(Result.success(Unit))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    fun getCustomerStats(): Flow<Result<CustomerStatsDto>> = flow {
        try {
            val response = apiService.getCustomerStats()
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}
