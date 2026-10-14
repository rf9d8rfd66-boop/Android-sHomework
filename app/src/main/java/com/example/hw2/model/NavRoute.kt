package com.example.hw2.model

import kotlinx.serialization.Serializable

@Serializable
sealed class NavRoute {
    data object Auth : NavRoute()
    data class Profile(
        val email: String,
        val password: String
    ) : NavRoute() {
        companion object {
            val route: String = "NavProfileRoute"
        }
    }

//    data class Description(val index: Int) : NavRoute()
}





//не обьект а что то другое