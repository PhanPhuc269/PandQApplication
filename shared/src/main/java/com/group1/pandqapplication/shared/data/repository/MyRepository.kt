package com.group1.pandqapplication.shared.data.repository

import com.group1.pandqapplication.shared.util.Result
import kotlinx.coroutines.flow.Flow

interface MyRepository {
    suspend fun getMessage(): Flow<Result<String>>
}
