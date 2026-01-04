package com.group1.pandqapplication.shared.data.remote.dto

import com.google.gson.annotations.SerializedName
import java.util.UUID

data class CategoryDto(
    @SerializedName("id")
    val id: UUID,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("imageUrl")
    val imageUrl: String? = null,
    @SerializedName("parentId")
    val parentId: UUID? = null,
    @SerializedName("subCategories")
    val subCategories: List<CategoryDto>? = null
)

data class CategoryCreateRequest(
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("imageUrl")
    val imageUrl: String? = null,
    @SerializedName("parentId")
    val parentId: UUID? = null
)

data class CategoryUpdateRequest(
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("imageUrl")
    val imageUrl: String? = null,
    @SerializedName("parentId")
    val parentId: UUID? = null
)
