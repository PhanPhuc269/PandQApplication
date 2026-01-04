package com.group1.pandqapplication.shared.data.remote.dto

/**
 * Generic wrapper for paginated API responses
 * Matches backend PaginationResponseDto structure
 */
data class PaginationResponseDto<T>(
    val data: List<T>,
    val meta: MetaResponseDto?,
    val code: String?,
    val message: String?
)

data class MetaResponseDto(
    val timestamp: String?,
    val pagination: PaginationMetaDto?
)

data class PaginationMetaDto(
    val page: Int,
    val size: Int,
    val total: Long,
    val totalPages: Int = 0
) {
    val hasMore: Boolean
        get() = page < totalPages - 1
}
