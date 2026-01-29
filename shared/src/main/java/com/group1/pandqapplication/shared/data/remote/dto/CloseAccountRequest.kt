package com.group1.pandqapplication.shared.data.remote.dto

data class CloseAccountRequest(
    val email: String,
    val reason: String? = null
)
