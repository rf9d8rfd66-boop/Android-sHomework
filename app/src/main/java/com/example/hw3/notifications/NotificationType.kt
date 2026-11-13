package com.example.hw3.notifications

import android.app.NotificationManager

enum class NotificationType(
    val importance: Int
) {
    MIN(NotificationManager.IMPORTANCE_MIN),
    DEFAULT(NotificationManager.IMPORTANCE_DEFAULT),
    HIGH(NotificationManager.IMPORTANCE_HIGH)
}