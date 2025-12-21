package com.group1.pandqapplication.shared.data.remote.dto

data class CategoryDto(
    val id: String,
    val name: String,
    val description: String?,
    val imageUrl: String?,
    val parentId: String?
)
