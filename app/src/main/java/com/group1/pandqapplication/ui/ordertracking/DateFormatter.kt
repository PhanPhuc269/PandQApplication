package com.group1.pandqapplication.ui.ordertracking

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DateFormatter {
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm")
    private val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    fun formatDateTime(dateTime: LocalDateTime?): String {
        return if (dateTime != null) {
            dateTime.format(dateTimeFormatter)
        } else {
            "Không xác định"
        }
    }

    fun formatDate(dateTime: LocalDateTime?): String {
        return if (dateTime != null) {
            dateTime.format(dateFormatter)
        } else {
            "Không xác định"
        }
    }

    fun formatTime(dateTime: LocalDateTime?): String {
        return if (dateTime != null) {
            dateTime.format(timeFormatter)
        } else {
            "Không xác định"
        }
    }
}
