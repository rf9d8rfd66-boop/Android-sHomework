package com.example.hw3.screens.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat

@Composable
fun LocationScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current

    var locationText by remember {
        mutableStateOf("Местоположение ещё не получено")
    }

    fun getLocation() {
        val locationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val finePermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarsePermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val location = if (finePermission) {
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                ?: locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        } else if (coarsePermission) {
            locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        } else {
            null
        }

        locationText = if (location != null) {
            "Широта: ${location.latitude}\nДолгота: ${location.longitude}"
        } else {
            "Не удалось получить координаты. Возможно, местоположение ещё неизвестно."
        }
    }

    val locationPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->

            val fineGranted =
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true

            val coarseGranted =
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

            if (fineGranted || coarseGranted) {
                getLocation()
            } else {
                locationText = "Для получения координат необходимо разрешение."
            }
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Геолокация",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = locationText)

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val fineGranted = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

                val coarseGranted = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

                if (fineGranted || coarseGranted) {
                    getLocation()
                } else {
                    locationPermissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }
            }
        ) {
            Text("Получить координаты")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = onBack) {


            Text("Назад")
        }
    }
}