package com.example.hw3.screens.main

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.hw3.notifications.NotificationHandler
import com.example.hw3.notifications.NotificationModel
import com.example.hw3.notifications.NotificationType
import com.example.hw3.permissions.PermissionHandler
import com.example.hw3.utils.Consts



@Composable
fun MainScreen(
    onOpenLocation: () -> Unit
) {
    val context = LocalContext.current

    var notificationId by remember {
        mutableStateOf("")
    }

    var activeNotifications by remember {
        mutableStateOf(emptyList<NotificationModel>())
    }

    var title by remember { mutableStateOf("") }
    var text by remember { mutableStateOf("") }
    var isOngoing by remember { mutableStateOf(false) }

    var selectedType by remember {
        mutableStateOf(NotificationType.DEFAULT)
    }

    var hasPermission by remember {
        mutableStateOf(PermissionHandler.hasNotificationPermission(context))
    }

    var permissionChecked by remember {
        mutableStateOf(false)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission = granted
        permissionChecked = true
    }

    fun updateActiveNotifications() {
        activeNotifications =
            NotificationHandler(context).getActiveNotifications()
    }

    LaunchedEffect(Unit) {
        NotificationHandler(context).createNotificationChannels()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            !hasPermission &&
            !permissionChecked
        ) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Уведомления",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (!hasPermission) {

            Text(
                text = "Для работы приложения необходимо разрешение на отправку уведомлений."
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    PermissionHandler.openNotificationSettings(context)
                }
            ) {
                Text("Открыть настройки")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    hasPermission =
                        PermissionHandler.hasNotificationPermission(context)
                }
            ) {
                Text("Проверить разрешение")
            }

        } else {

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Заголовок уведомления") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Текст уведомления") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isOngoing,
                    onCheckedChange = { isOngoing = it }
                )

                Text("Нельзя удалить уведомление")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text("Важность уведомления")


            var expanded by remember {
                mutableStateOf(false)
            }

            Box {
                Button(
                    onClick = { expanded = true }
                ) {
                    Text(selectedType.name)
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    NotificationType.values().forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type.name) },
                            onClick = {
                                selectedType = type
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (title.isNotBlank() && text.isNotBlank()) {

                        updateActiveNotifications()

                        val newId = (activeNotifications.maxOfOrNull { it.id }
                            ?: (Consts.NOTIFICATION_ID_START - 1)) + 1

                        val notification = NotificationModel(
                            id = newId,
                            title = title,
                            text = text,
                            type = selectedType,
                            isOngoing = isOngoing
                        )

                        NotificationHandler(context).showNotification(notification)

                        updateActiveNotifications()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Отправить уведомление")
            }


            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Управление уведомлениями",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Активные ID: ${
                    activeNotifications.joinToString { it.id.toString() }
                }"
            )

            OutlinedTextField(
                value = notificationId,
                onValueChange = { notificationId = it },
                label = { Text("ID уведомления") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    val id = notificationId.toIntOrNull()

                    if (id != null && title.isNotBlank() && text.isNotBlank()) {
                        val notification = NotificationModel(
                            id = id,
                            title = title,
                            text = text,
                            type = selectedType,
                            isOngoing = isOngoing
                        )

                        NotificationHandler(context)
                            .updateNotification(notification)

                        updateActiveNotifications()
                    }
                },
                enabled = activeNotifications.isNotEmpty() &&
                        notificationId.toIntOrNull() != null &&
                        activeNotifications.any { it.id == notificationId.toIntOrNull() } &&
                        title.isNotBlank() &&
                        text.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Изменить уведомление")
            }

            Button(
                onClick = {
                    NotificationHandler(context).cancelAllNotifications()
                    updateActiveNotifications()
                    notificationId = ""
                },
                enabled = activeNotifications.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Удалить все уведомления")
            }


            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onOpenLocation,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Открыть экран геолокации")
            }
        }
    }
}