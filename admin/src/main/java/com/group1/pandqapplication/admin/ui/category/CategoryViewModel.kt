package com.group1.pandqapplication.admin.ui.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group1.pandqapplication.shared.data.remote.dto.CategoryCreateRequest
import com.group1.pandqapplication.shared.data.remote.dto.CategoryDto
import com.group1.pandqapplication.shared.data.remote.dto.CategoryUpdateRequest
import com.group1.pandqapplication.shared.data.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _categories = MutableLiveData<List<CategoryDto>>()
    val categories: LiveData<List<CategoryDto>> = _categories

    private val _filteredCategories = MutableLiveData<List<CategoryDto>>()
    val filteredCategories: LiveData<List<CategoryDto>> = _filteredCategories

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _selectedCategory = MutableLiveData<CategoryDto?>()
    val selectedCategory: LiveData<CategoryDto?> = _selectedCategory

    private val _operationSuccess = MutableLiveData<String?>()
    val operationSuccess: LiveData<String?> = _operationSuccess

    private var allCategories = listOf<CategoryDto>()

    init {
        loadCategories()
    }

    fun loadCategories() {
        viewModelScope.launch {
            _isLoading.postValue(true)
            _error.postValue(null)
            try {
                categoryRepository.getAllCategories().onSuccess { categoriesList ->
                    allCategories = categoriesList
                    _categories.postValue(categoriesList)
                    _filteredCategories.postValue(categoriesList)
                    _error.postValue(null)
                }.onFailure { exception ->
                    _error.postValue(exception.message ?: "Failed to load categories")
                }
            } catch (e: Exception) {
                _error.postValue(e.message ?: "An error occurred")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun getCategoryById(id: UUID) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                categoryRepository.getCategoryById(id).onSuccess { category ->
                    _selectedCategory.postValue(category)
                }.onFailure { exception ->
                    _error.postValue(exception.message ?: "Failed to load category")
                }
            } catch (e: Exception) {
                _error.postValue(e.message ?: "An error occurred")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun createCategory(
        name: String,
        description: String?,
        imageUrl: String?,
        parentId: UUID?
    ) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            _error.postValue(null)
            try {
                val request = CategoryCreateRequest(
                    name = name,
                    description = description.takeIf { !it.isNullOrBlank() },
                    imageUrl = imageUrl.takeIf { !it.isNullOrBlank() },
                    parentId = parentId
                )
                categoryRepository.createCategory(request).onSuccess { newCategory ->
                    allCategories = allCategories + newCategory
                    _categories.postValue(allCategories)
                    _operationSuccess.postValue("Danh mục đã được tạo thành công")
                    loadCategories()
                }.onFailure { exception ->
                    _error.postValue(exception.message ?: "Failed to create category")
                }
            } catch (e: Exception) {
                _error.postValue(e.message ?: "An error occurred")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun updateCategory(
        id: UUID,
        name: String,
        description: String?,
        imageUrl: String?,
        parentId: UUID?
    ) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            _error.postValue(null)
            try {
                val request = CategoryUpdateRequest(
                    name = name,
                    description = description.takeIf { !it.isNullOrBlank() },
                    imageUrl = imageUrl.takeIf { !it.isNullOrBlank() },
                    parentId = parentId
                )
                categoryRepository.updateCategory(id, request).onSuccess { updatedCategory ->
                    allCategories = allCategories.map { if (it.id == id) updatedCategory else it }
                    _categories.postValue(allCategories)
                    _operationSuccess.postValue("Danh mục đã được cập nhật thành công")
                    loadCategories()
                }.onFailure { exception ->
                    _error.postValue(exception.message ?: "Failed to update category")
                }
            } catch (e: Exception) {
                _error.postValue(e.message ?: "An error occurred")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun deleteCategory(id: UUID) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            _error.postValue(null)
            try {
                categoryRepository.deleteCategory(id).onSuccess {
                    allCategories = allCategories.filter { it.id != id }
                    _categories.postValue(allCategories)
                    _operationSuccess.postValue("Danh mục đã được xóa thành công")
                    loadCategories()
                }.onFailure { exception ->
                    _error.postValue(exception.message ?: "Failed to delete category")
                }
            } catch (e: Exception) {
                _error.postValue(e.message ?: "An error occurred")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun searchCategories(query: String) {
        if (query.isEmpty()) {
            _filteredCategories.postValue(allCategories)
        } else {
            val filtered = allCategories.filter { category ->
                category.name.contains(query, ignoreCase = true) ||
                (category.description?.contains(query, ignoreCase = true) == true)
            }
            _filteredCategories.postValue(filtered)
        }
    }

    fun clearError() {
        _error.postValue(null)
    }

    fun clearSuccess() {
        _operationSuccess.postValue(null)
    }

    fun clearSelectedCategory() {
        _selectedCategory.postValue(null)
    }
}
