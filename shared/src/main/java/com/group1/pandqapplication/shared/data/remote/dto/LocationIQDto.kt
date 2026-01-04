package com.group1.pandqapplication.shared.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ReverseGeocodeResponse(
    @SerializedName("place_id") val placeId: String?,
    @SerializedName("lat") val lat: String?,
    @SerializedName("lon") val lon: String?,
    @SerializedName("display_name") val displayName: String?,
    @SerializedName("address") val address: AddressComponents?
)

data class AddressComponents(
    @SerializedName("house_number") val houseNumber: String?,
    @SerializedName("road") val road: String?,
    @SerializedName("suburb") val suburb: String?,
    @SerializedName("city") val city: String?,
    @SerializedName("state") val state: String?,
    @SerializedName("postcode") val postcode: String?,
    @SerializedName("country") val country: String?,
    @SerializedName("quarter") val quarter: String?,
    @SerializedName("neighbourhood") val neighbourhood: String?
)

data class SearchLocationResponse(
    @SerializedName("place_id") val placeId: String?,
    @SerializedName("lat") val lat: String?,
    @SerializedName("lon") val lon: String?,
    @SerializedName("display_name") val displayName: String?,
    @SerializedName("address") val address: AddressComponents?
)
