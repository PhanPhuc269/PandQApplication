package com.group1.pandqapplication.shared.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTOs for ZaloPay payment API
 */
data class ZaloPayCreateOrderRequest(
    @SerializedName("amount") val amount: Long,
    @SerializedName("description") val description: String,
    @SerializedName("userId") val userId: String? = null,
    @SerializedName("orderId") val orderId: String? = null
)

data class ZaloPayCreateOrderResponse(
    @SerializedName("returnCode") val returnCode: Int,
    @SerializedName("returnMessage") val returnMessage: String?,
    @SerializedName("zpTransToken") val zpTransToken: String?,
    @SerializedName("orderUrl") val orderUrl: String?,
    @SerializedName("appTransId") val appTransId: String?
)

data class ZaloPayStatusResponse(
    @SerializedName("returnCode") val returnCode: Int,
    @SerializedName("returnMessage") val returnMessage: String?,
    @SerializedName("isProcessing") val isProcessing: Boolean?,
    @SerializedName("amount") val amount: Long?,
    @SerializedName("zpTransId") val zpTransId: String?
)
