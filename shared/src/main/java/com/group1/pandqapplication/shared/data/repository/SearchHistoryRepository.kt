package com.group1.pandqapplication.shared.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.group1.pandqapplication.shared.data.remote.AppApiService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository để quản lý lịch sử tìm kiếm và gợi ý xu hướng
 */
@Singleton
class SearchHistoryRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val apiService: AppApiService
) {
    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME, Context.MODE_PRIVATE
    )
    private val gson = Gson()

    companion object {
        private const val PREFS_NAME = "search_history_prefs"
        private const val KEY_SEARCH_HISTORY = "search_history"
        private const val MAX_HISTORY_SIZE = 10
    }

    /**
     * Lấy danh sách lịch sử tìm kiếm
     */
    fun getSearchHistory(): List<String> {
        val json = prefs.getString(KEY_SEARCH_HISTORY, null) ?: return emptyList()
        val type = object : TypeToken<List<String>>() {}.type
        return try {
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * Thêm từ khóa vào lịch sử tìm kiếm
     */
    fun addSearchQuery(query: String) {
        if (query.isBlank()) return
        
        val trimmedQuery = query.trim()
        val history = getSearchHistory().toMutableList()
        
        // Xóa nếu đã tồn tại để đưa lên đầu
        history.remove(trimmedQuery)
        
        // Thêm vào đầu danh sách
        history.add(0, trimmedQuery)
        
        // Giới hạn số lượng
        val limitedHistory = history.take(MAX_HISTORY_SIZE)
        
        // Lưu lại
        saveHistory(limitedHistory)
    }

    /**
     * Xóa một từ khóa khỏi lịch sử
     */
    fun removeSearchQuery(query: String) {
        val history = getSearchHistory().toMutableList()
        history.remove(query)
        saveHistory(history)
    }

    /**
     * Xóa toàn bộ lịch sử tìm kiếm
     */
    fun clearSearchHistory() {
        prefs.edit().remove(KEY_SEARCH_HISTORY).apply()
    }

    private fun saveHistory(history: List<String>) {
        val json = gson.toJson(history)
        prefs.edit().putString(KEY_SEARCH_HISTORY, json).apply()
    }

    /**
     * Lấy danh sách xu hướng tìm kiếm từ API Backend
     */
    suspend fun getTrendingSearches(): List<String> {
        return try {
            val response = apiService.getTrendingSearches()
            if (response.isNotEmpty()) response else getFallbackTrending()
        } catch (e: Exception) {
            e.printStackTrace()
            getFallbackTrending()
        }
    }

    private fun getFallbackTrending(): List<String> {
        return listOf(
            "iPhone 15",
            "Samsung Galaxy",
            "Laptop Gaming",
            "Tai nghe Bluetooth",
            "Chuột không dây",
            "Bàn phím cơ"
        )
    }
}
