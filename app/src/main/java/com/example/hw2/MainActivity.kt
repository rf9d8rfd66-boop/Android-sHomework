package com.example.hw2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.hw2.model.LegacyNavRoutes
import com.example.hw2.model.NavKeys
import com.example.hw2.model.NavRoute
import com.example.hw2.screens.auth.AuthScreen
import com.example.hw2.screens.profile.ProfileScreen
import com.example.hw2.utils.generatePassword

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()
            val generatedPassword = remember {
                generatePassword()
            }


            NavHost(
                navController = navController,
                startDestination = LegacyNavRoutes.Auth.route
            ) {

                composable(
                    route = LegacyNavRoutes.Auth.route
                ) {entry ->
                    AuthScreen() {
                        emailFieldValue, passwordFieldValue ->
                        navController.navigate(
                            LegacyNavRoutes.Profile.route.plus("?${NavKeys.Auth.EMAIL_FIELD_VALUE}=${emailFieldValue}&${NavKeys.Auth.PASSWORD_FIELD_VALUE}=${passwordFieldValue}")
                        )
                    }
                }

                composable (
                    route = LegacyNavRoutes.Profile.route + "?${NavKeys.Auth.EMAIL_FIELD_VALUE}={firstArg}&${NavKeys.Auth.PASSWORD_FIELD_VALUE}={secondArg}",
                    arguments = listOf(
                        navArgument(name = "firstArg") {
                            type = NavType.StringType
                            nullable = false
                            defaultValue = "Error data"
                        },
                        navArgument(name = "secondArg") {
                            type = NavType.StringType
                            nullable = false
                            defaultValue = "Error data"
                        }
                    )
                ) {entry ->
                    val emailFieldValue = entry.arguments?.getString("firstArg") ?: "Data is null"
                    val passwordFieldValue = entry.arguments?.getString("secondArg") ?: "Data is null"
                    ProfileScreen(
                        email = emailFieldValue,
                        password = passwordFieldValue,
                        generatedPassword = generatedPassword,
                        onBack = {
                            navController.popBackStack()
                        }
                    )
                }


            }


        }
    }
}
