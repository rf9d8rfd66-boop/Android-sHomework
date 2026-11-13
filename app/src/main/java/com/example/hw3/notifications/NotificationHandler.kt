package com.example.hw3.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.hw3.MainActivity
import com.example.hw3.R
import com.example.hw3.permissions.PermissionHandler
import com.example.hw3.utils.Consts

class NotificationHandler(
    private val context: Context
) {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channels = listOf(
                NotificationChannel(
                    Consts.NOTIFICATION_CHANNEL_MIN,
                    Consts.NOTIFICATION_CHANNEL_MIN_NAME,
                    NotificationManager.IMPORTANCE_MIN
                ),
                NotificationChannel(
                    Consts.NOTIFICATION_CHANNEL_DEFAULT,
                    Consts.NOTIFICATION_CHANNEL_DEFAULT_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
                ),
                NotificationChannel(
                    Consts.NOTIFICATION_CHANNEL_HIGH,
                    Consts.NOTIFICATION_CHANNEL_HIGH_NAME,
                    NotificationManager.IMPORTANCE_HIGH
                )
            )

            notificationManager.createNotificationChannels(channels)
        }
    }

    fun showNotification(notification: NotificationModel) {


        val channelId = when (notification.type) {
            NotificationType.MIN -> Consts.NOTIFICATION_CHANNEL_MIN
            NotificationType.DEFAULT -> Consts.NOTIFICATION_CHANNEL_DEFAULT
            NotificationType.HIGH -> Consts.NOTIFICATION_CHANNEL_HIGH
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra("notification_title", notification.title)
            putExtra("notification_text", notification.text)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            notification.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(notification.title)
            .setContentText(notification.text)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(notification.text)
            )
            .setContentIntent(pendingIntent)
            .setAutoCancel(!notification.isOngoing)
            .setOngoing(notification.isOngoing)
            .setPriority(
                when (notification.type) {
                    NotificationType.MIN -> NotificationCompat.PRIORITY_MIN
                    NotificationType.DEFAULT -> NotificationCompat.PRIORITY_DEFAULT
                    NotificationType.HIGH -> NotificationCompat.PRIORITY_HIGH
                }
            )

        notificationManager.notify(notification.id, builder.build())
    }

    fun updateNotification(notification: NotificationModel) {
        showNotification(notification)
    }

    fun cancelNotification(id: Int) {
        notificationManager.cancel(id)
    }

    fun cancelAllNotifications() {
        notificationManager.cancelAll()
    }

    fun getActiveNotifications(): List<NotificationModel> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            notificationManager.activeNotifications.filter { it.tag == null}
                .mapNotNull { active ->
                val extras = active.notification.extras

                NotificationModel(
                    id = active.id,
                    title = extras.getCharSequence("android.title")?.toString() ?: "",
                    text = extras.getCharSequence("android.text")?.toString() ?: "",
                    type = NotificationType.DEFAULT,
                    isOngoing = active.isOngoing
                    )
                }
            } else {
                emptyList()
            }
        }
    }