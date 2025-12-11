package com.group1.pandqapplication.data.repository

import com.group1.pandqapplication.data.remote.ApiService
import com.group1.pandqapplication.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MyRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : MyRepository {
    override suspend fun getMessage(): Flow<Result<String>> = flow {
        emit(Result.Loading)
        try {
            val response = apiService.getMessage()
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Unknown Error", e))
        }
    }
}
