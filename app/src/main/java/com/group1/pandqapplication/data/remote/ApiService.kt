package com.group1.pandqapplication.data.remote

import retrofit2.http.GET

interface ApiService {
    @GET("endpoint")
    suspend fun getMessage(): String
}
