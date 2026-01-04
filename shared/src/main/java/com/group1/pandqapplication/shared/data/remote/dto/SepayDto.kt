package com.group1.pandqapplication.shared.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTOs for SePay payment API (VietQR)
 */
data class SepayCreateQRRequest(
    @SerializedName("amount") val amount: Long,
    @SerializedName("description") val description: String? = null,
    @SerializedName("orderId") val orderId: String? = null
)

data class SepayCreateQRResponse(
    @SerializedName("returnCode") val returnCode: Int,
    @SerializedName("returnMessage") val returnMessage: String?,
    @SerializedName("qrDataUrl") val qrDataUrl: String?,  // URL to QR code image
    @SerializedName("qrCode") val qrCode: String?,        // Raw QR content
    @SerializedName("transactionId") val transactionId: String?,
    @SerializedName("bankAccount") val bankAccount: String?,
    @SerializedName("bankCode") val bankCode: String?,
    @SerializedName("accountName") val accountName: String?,
    @SerializedName("amount") val amount: Long?,
    @SerializedName("content") val content: String?       // Payment content
)

data class SepayStatusResponse(
    @SerializedName("returnCode") val returnCode: Int,
    @SerializedName("returnMessage") val returnMessage: String?,
    @SerializedName("isPaid") val isPaid: Boolean?,
    @SerializedName("amount") val amount: Long?,
    @SerializedName("transactionDate") val transactionDate: String?
)
