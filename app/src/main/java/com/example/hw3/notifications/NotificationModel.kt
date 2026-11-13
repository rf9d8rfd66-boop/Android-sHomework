package com.example.hw3.notifications

data class NotificationModel(
    val id: Int,
    val title: String,
    val text: String,
    val type: NotificationType,
    val isOngoing: Boolean
)