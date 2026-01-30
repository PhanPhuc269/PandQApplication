package com.group1.pandqapplication.admin.ui.category

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.group1.pandqapplication.shared.data.remote.dto.CategoryDto
import com.group1.pandqapplication.shared.ui.theme.CategoryBackground
import com.group1.pandqapplication.shared.ui.theme.CategoryBackgroundLight
import com.group1.pandqapplication.shared.ui.theme.CategorySurface
import com.group1.pandqapplication.shared.ui.theme.CategorySurfaceLight
import com.group1.pandqapplication.shared.ui.theme.CategoryTextPrimary
import com.group1.pandqapplication.shared.ui.theme.CategoryTextPrimaryLight
import com.group1.pandqapplication.shared.ui.theme.CategoryTextSecondary
import com.group1.pandqapplication.shared.ui.theme.CategoryTextSecondaryLight
import java.util.UUID

@Composable
fun CategoryManagementScreen(
    onBackClick: () -> Unit = {},
    viewModel: CategoryViewModel = hiltViewModel()
) {
    val categories by viewModel.filteredCategories.observeAsState(emptyList())
    val isLoading by viewModel.isLoading.observeAsState(false)
    val error by viewModel.error.observeAsState()
    val successMessage by viewModel.operationSuccess.observeAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    
    // Use light theme
    val isDarkTheme = false
    val backgroundColor = if (isDarkTheme) CategoryBackground else CategoryBackgroundLight
    val surfaceColor = if (isDarkTheme) CategorySurface else CategorySurfaceLight
    val textPrimary = if (isDarkTheme) CategoryTextPrimary else CategoryTextPrimaryLight
    val textSecondary = if (isDarkTheme) CategoryTextSecondary else CategoryTextSecondaryLight

    var showAddDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedCategoryToDelete by remember { mutableStateOf<CategoryDto?>(null) }
    var selectedCategoryToEdit by remember { mutableStateOf<CategoryDto?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    // Show success message
    if (successMessage != null) {
        LaunchedEffect(Unit) {
            snackbarHostState.showSnackbar(successMessage!!)
            viewModel.clearSuccess()
        }
    }

    // Show error
    if (error != null) {
        LaunchedEffect(Unit) {
            snackbarHostState.showSnackbar(error!!)
            viewModel.clearError()
        }
    }

    Scaffold(
        containerColor = backgroundColor,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Column(modifier = Modifier.background(backgroundColor)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Back",
                            tint = textPrimary
                        )
                    }
                    Text(
                        text = "Quản lý danh mục",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary,
                        modifier = Modifier.align(Alignment.Center)
                    )
                    IconButton(
                        onClick = { showAddDialog = true },
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            tint = textPrimary
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        if (isLoading && categories.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Search Bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(surfaceColor),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .width(48.dp)
                                .height(48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = textSecondary
                            )
                        }
                        BasicTextField(
                            value = searchQuery,
                            onValueChange = {
                                searchQuery = it
                                viewModel.searchCategories(it)
                            },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            textStyle = androidx.compose.ui.text.TextStyle(
                                color = textPrimary,
                                fontSize = 16.sp
                            ),
                            decorationBox = { innerTextField ->
                                if (searchQuery.isEmpty()) {
                                    Text(
                                        text = "Tìm kiếm danh mục",
                                        color = textSecondary,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Normal
                                    )
                                }
                                innerTextField()
                            }
                        )
                    }
                }

                Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)

                // Category List
                if (categories.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Không có danh mục nào",
                            color = textSecondary,
                            fontSize = 14.sp
                        )
                    }
                } else {
                    LazyColumn {
                        val categoryTree = buildCategoryTree(categories)
                        items(categoryTree) { treeItem ->
                            CategoryItemWithMenu(
                                category = treeItem.category,
                                level = treeItem.level,
                                textPrimary = textPrimary,
                                textSecondary = textSecondary,
                                surfaceColor = surfaceColor,
                                onEdit = {
                                    selectedCategoryToEdit = treeItem.category
                                    showAddDialog = true
                                },
                                onDelete = {
                                    selectedCategoryToDelete = treeItem.category
                                    showDeleteDialog = true
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    // Dialogs
    AddEditCategoryDialog(
        isOpen = showAddDialog,
        category = selectedCategoryToEdit,
        onDismiss = {
            showAddDialog = false
            selectedCategoryToEdit = null
        },
        onSave = { name, description, imageUrl, parentId ->
            if (selectedCategoryToEdit != null) {
                viewModel.updateCategory(
                    selectedCategoryToEdit!!.id,
                    name,
                    description,
                    imageUrl,
                    parentId
                )
            } else {
                viewModel.createCategory(name, description, imageUrl, parentId)
            }
        },
        categories = categories.filter { it.id != selectedCategoryToEdit?.id }
    )

    DeleteConfirmDialog(
        isOpen = showDeleteDialog,
        categoryName = selectedCategoryToDelete?.name ?: "",
        onDismiss = {
            showDeleteDialog = false
            selectedCategoryToDelete = null
        },
        onConfirm = {
            selectedCategoryToDelete?.let {
                viewModel.deleteCategory(it.id)
            }
        }
    )
}

@Composable
fun CategoryItemWithMenu(
    category: CategoryDto,
    level: Int = 0,
    textPrimary: Color = CategoryTextPrimary,
    textSecondary: Color = CategoryTextSecondary,
    surfaceColor: Color = CategorySurface,
    onEdit: () -> Unit = {},
    onDelete: () -> Unit = {}
) {
    var showMenu by remember { mutableStateOf(false) }
    
    val indentPadding = (level * 24).dp

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp + indentPadding, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Indent indicator for child items
            if (level > 0) {
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .height(40.dp)
                        .background(textSecondary.copy(alpha = 0.3f))
                )
                Spacer(modifier = Modifier.width(12.dp))
            }
            
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(surfaceColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (level == 0) Icons.Default.Folder else Icons.Default.FolderOpen,
                        contentDescription = null,
                        tint = textPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = category.name,
                        fontSize = 16.sp,
                        fontWeight = if (level == 0) FontWeight.Normal else FontWeight.Light,
                        color = textPrimary
                    )
                    category.description?.let { description ->
                        if (description.isNotEmpty()) {
                            Text(
                                text = description,
                                fontSize = 12.sp,
                                color = textSecondary,
                                maxLines = 1
                            )
                        }
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box {
                    IconButton(
                        onClick = { showMenu = !showMenu },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreHoriz,
                            contentDescription = null,
                            tint = textPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Chỉnh sửa") },
                            onClick = {
                                onEdit()
                                showMenu = false
                            },
                            leadingIcon = {
                                Icon(Icons.Default.Edit, contentDescription = null)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Xóa") },
                            onClick = {
                                onDelete()
                                showMenu = false
                            },
                            leadingIcon = {
                                Icon(Icons.Default.Delete, contentDescription = null)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewCategoryManagementScreen() {
    CategoryManagementScreen()
}
