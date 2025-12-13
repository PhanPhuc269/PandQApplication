package com.group1.pandqapplication.shared.data.repository

import com.group1.pandqapplication.shared.util.Result
import kotlinx.coroutines.flow.Flow

interface AppRepository {
    suspend fun initializeApp(): Flow<Result<Boolean>>
}
