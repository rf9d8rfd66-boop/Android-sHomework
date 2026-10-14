package com.example.hw2.model

enum class LegacyNavRoutes(
    val route: String
) {
    Profile(route = "NavProfile"),
    Auth(route = "NavAuth")
}


object NavKeys {

    object Auth {
        const val EMAIL_FIELD_VALUE = "nav_email_text_field"
        const val PASSWORD_FIELD_VALUE = "nav_password_text_field"
    }
}