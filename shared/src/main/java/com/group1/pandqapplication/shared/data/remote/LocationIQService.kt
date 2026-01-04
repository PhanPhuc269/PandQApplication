package com.group1.pandqapplication.shared.data.remote

import com.group1.pandqapplication.shared.data.remote.dto.ReverseGeocodeResponse
import com.group1.pandqapplication.shared.data.remote.dto.SearchLocationResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationIQService {
    
    @GET("reverse.php")
    suspend fun reverseGeocode(
        @Query("key") apiKey: String,
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("format") format: String = "json",
        @Query("addressdetails") addressDetails: Int = 1
    ): ReverseGeocodeResponse

    @GET("search.php")
    suspend fun searchLocation(
        @Query("key") apiKey: String,
        @Query("q") query: String,
        @Query("format") format: String = "json",
        @Query("addressdetails") addressDetails: Int = 1,
        @Query("limit") limit: Int = 5
    ): List<SearchLocationResponse>
}
