package com.example.hw3

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.hw3.navigation.AppNavigation
import com.example.hw3.ui.theme.HW3Theme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            HW3Theme {
                AppNavigation()
            }
        }

        // Приложение открылось через уведомление
        showNotificationToast(intent, true)
    }

    override fun onNewIntent(intent: android.content.Intent) {
        super.onNewIntent(intent)
        setIntent(intent)

        // Приложение уже было запущено
        showNotificationToast(intent, false)
    }

    private fun showNotificationToast(
        intent: android.content.Intent?,
        isColdStart: Boolean
    ) {
        val message = if (isColdStart) {
            intent?.getStringExtra("notification_title")
        } else {
            intent?.getStringExtra("notification_text")
        }

        if (!message.isNullOrEmpty()) {
            Toast.makeText(
                this,
                message,
                Toast.LENGTH_LONG
            ).show()
        }
    }
}