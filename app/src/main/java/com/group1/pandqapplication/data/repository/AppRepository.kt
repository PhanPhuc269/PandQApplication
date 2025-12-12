package com.group1.pandqapplication.data.repository

import com.group1.pandqapplication.util.Result
import kotlinx.coroutines.flow.Flow

interface AppRepository {
    suspend fun initializeApp(): Flow<Result<Boolean>>
}
