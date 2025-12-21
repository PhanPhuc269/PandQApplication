package com.group1.pandqapplication.shared.data.remote.dto

data class PaginationResponseDto<T>(
    val data: List<T>,
    val meta: Meta?,
    val code: String?,
    val message: String?
)

data class Meta(
    val timestamp: String?,
    val pagination: Pagination?
)

data class Pagination(
    val page: Int,
    val size: Int,
    val total: Long,
    val totalPages: Int
) {
    val hasMore: Boolean
        get() = page < totalPages - 1
}
