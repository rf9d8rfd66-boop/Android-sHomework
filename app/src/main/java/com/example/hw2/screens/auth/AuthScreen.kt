package com.example.hw2.screens.auth

import android.util.Patterns
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.example.hw2.R
import com.example.hw2.utils.Consts


@Composable
fun AuthScreen(
    onButtonClick:(String, String) -> Unit
) {
    AuthContent(onButtonClick)
}


@Composable
private fun AuthContent(
    onButtonClick:(String, String) -> Unit
) {
    var email by rememberSaveable {
        mutableStateOf("")
    }

    var password by rememberSaveable {
        mutableStateOf("")
    }

    var emailError by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    var passwordError by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    val emailEmptyError = stringResource(R.string.email_empty_error)

    val passwordEmptyError = stringResource(R.string.password_empty_error)

    val emailInvalidError = stringResource(R.string.email_invalid_error)

    val passwordInvalidError = stringResource(R.string.password_invalid_error)

    fun submit() {
        emailError = null
        passwordError = null

        if (email.isBlank()) {
            emailError = emailEmptyError

        } else if (
            !Patterns.EMAIL_ADDRESS
                .matcher(email)
                .matches()
        ) {
            emailError = emailInvalidError
        }

        if (password.isBlank()) {
            passwordError = passwordEmptyError

        } else if (

            password.length < 8 ||
            password.none {
                it in 'A'..'Z' || it in 'a'..'z'
            } ||
            password.none {
                it in '0'..'9'
            }

        ) {
            passwordError = passwordInvalidError
        }

        if (
            emailError == null &&
            passwordError == null
        ) {

            onButtonClick.invoke(email, password)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(Consts.SCREEN_PADDING),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = stringResource(R.string.auth_title))
        Spacer(modifier = Modifier.height(Consts.EXTRA_LARGE_SPACER))
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = null
            },
            label = {
                Text(text = stringResource(R.string.email_label))
            },
            isError = emailError != null,
            supportingText = {
                emailError?.let {
                    Text(it)
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(Consts.MEDIUM_SPACER)
        )

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = null
            },
            label = {
                Text(text = stringResource(R.string.password_label))
            },
            isError = passwordError != null,
            supportingText = {
                passwordError?.let {
                    Text(it)
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    submit()
                }
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(Consts.EXTRA_LARGE_SPACER)
        )

        Button(
            onClick = {
               submit()
            }
        ) {
            Text(text = stringResource(R.string.login_button))
        }

    }

}