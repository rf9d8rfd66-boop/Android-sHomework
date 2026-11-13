package com.example.hw3.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.runtime.entryProvider
import com.example.hw3.screens.location.LocationScreen
import com.example.hw3.screens.main.MainScreen

@Composable
fun AppNavigation() {

    val backStack = remember {
        mutableStateListOf<NavKey>(MainScreenKey)
    }

    NavDisplay(
        backStack = backStack,
        onBack = {
            if (backStack.size > 1) {
                backStack.removeAt(backStack.lastIndex)
            }
        },
        entryProvider = entryProvider {

            entry<MainScreenKey> {
                MainScreen(
                    onOpenLocation = {
                        backStack.add(LocationScreenKey)
                    }
                )
            }

            entry<LocationScreenKey> {
                LocationScreen(
                    onBack = {
                        if (backStack.size > 1) {
                            backStack.removeAt(backStack.lastIndex)
                        }
                    }
                )
            }
        }
    )
}