package com.group1.pandqapplication.admin.ui.category

import com.group1.pandqapplication.shared.data.remote.dto.CategoryDto
import java.util.UUID

/**
 * Data class để represent category item trong tree structure
 */
data class CategoryTreeItem(
    val category: CategoryDto,
    val level: Int = 0,  // 0 = root, 1 = child, 2 = grandchild, etc.
    val parentId: UUID? = null
)

/**
 * Convert flat list of categories vào tree structure
 * Sắp xếp parent trước, sau đó children
 */
fun buildCategoryTree(categories: List<CategoryDto>): List<CategoryTreeItem> {
    val result = mutableListOf<CategoryTreeItem>()
    val processedIds = mutableSetOf<UUID>()
    
    fun addCategoryAndChildren(categoryId: UUID?, level: Int = 0) {
        // Find categories with this parentId
        categories
            .filter { it.parentId == categoryId && it.id !in processedIds }
            .forEach { category ->
                result.add(CategoryTreeItem(category, level, categoryId))
                processedIds.add(category.id)
                // Recursively add children
                addCategoryAndChildren(category.id, level + 1)
            }
    }
    
    // Start with root categories (parentId = null)
    addCategoryAndChildren(null, 0)
    
    // Add any remaining categories that weren't processed
    categories
        .filter { it.id !in processedIds }
        .forEach { category ->
            result.add(CategoryTreeItem(category, 0, null))
            processedIds.add(category.id)
            addCategoryAndChildren(category.id, 1)
        }
    
    return result
}
