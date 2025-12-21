package com.group1.pandqapplication.shared.data.remote.dto

data class CategoryDto(
    val id: String,
    val name: String,
    val iconUrl: String?  // Nullable to handle null from API
)
