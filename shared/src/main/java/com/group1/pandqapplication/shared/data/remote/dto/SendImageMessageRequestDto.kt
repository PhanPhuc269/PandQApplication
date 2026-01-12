package com.group1.pandqapplication.shared.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO for sending an image message with URL.
 * Used when image is uploaded to Cloudinary first, then URL is sent with message.
 */
data class SendImageMessageRequestDto(
    @SerializedName("message")
    val message: String = "Image",

    @SerializedName("imageUrl")
    val imageUrl: String,

    @SerializedName("messageType")
    val messageType: String = "IMAGE"
)
