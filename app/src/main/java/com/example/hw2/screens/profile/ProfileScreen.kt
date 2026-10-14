package com.example.hw2.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import coil.compose.AsyncImage
import com.example.hw2.R
import com.example.hw2.utils.Consts

@Composable
fun ProfileScreen(
    email: String,
    password: String,
    generatedPassword: String,
    onBack: () -> Unit
) {
    ProfileContent(email, password, generatedPassword, onBack)
}


@Composable
private fun ProfileContent(
    email: String,
    password: String,
    generatedPassword: String,
    onBack: () -> Unit
) {
    var passwordVisible by rememberSaveable {
        mutableStateOf(false)

    }

    val clipboardManager = LocalClipboardManager.current

    var passwordCopied by rememberSaveable {
        mutableStateOf(false)
    }

    val isPasswordCorrect = password == generatedPassword
    val imageUrl = if (isPasswordCorrect) {
        Consts.SUCCESS_IMAGE_URL
    } else {
        Consts.ERROR_IMAGE_URL
    }

    val imageDescription = if (isPasswordCorrect) {
       stringResource(R.string.success_image_description)

    } else {
        stringResource(R.string.error_image_description)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Consts.SCREEN_PADDING),

        horizontalAlignment = Alignment.CenterHorizontally,

        verticalArrangement = Arrangement.Center

    ) {

        Text(

            text = password

        )

        Spacer(modifier = Modifier.height(Consts.LARGE_SPACER))

        Text(

            text = stringResource(

                R.string.profile_email,

                email

            )

        )

        Spacer(modifier = Modifier.height(Consts.LARGE_SPACER))

        AsyncImage(
            model = imageUrl,
            contentDescription = imageDescription,
            modifier = Modifier.size(Consts.IMAGE_SIZE)
        )

        Spacer(modifier = Modifier.height(Consts.EXTRA_LARGE_SPACER))

        Text(

            text = if (isPasswordCorrect) {

                stringResource(R.string.password_correct)
            } else {
                stringResource(R.string.password_incorrect)
            }

        )

        Spacer(modifier = Modifier.height(Consts.LARGE_SPACER))

        OutlinedTextField(
            value = generatedPassword,
            onValueChange = {},
            readOnly = true,
            label = {
                Text(
                    text = stringResource(R.string.generated_password_label)
                )

            },

            visualTransformation = if (passwordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()

            },

            trailingIcon = {
                TextButton(

                    onClick = {

                        passwordVisible = !passwordVisible

                    }

                ) {

                    Text(

                        text = if (passwordVisible) {

                            stringResource(R.string.hide_password)
                        } else {

                            stringResource(R.string.show_password)
                        }

                    )

                }

            },

            singleLine = true,

            modifier = Modifier.fillMaxWidth()

        )

        Spacer(modifier = Modifier.height(Consts.SMALL_SPACER))

        Button(
            onClick = {
                clipboardManager.setText(
                    AnnotatedString(generatedPassword)
                )
                passwordCopied = true
            },
            modifier = Modifier.fillMaxWidth()

        ) {
            Text(
                text = if (passwordCopied) {
                    stringResource(R.string.password_copied)
                } else {
                    stringResource(R.string.copy_password)
                }
            )
        }


        Spacer(modifier = Modifier.height(Consts.EXTRA_LARGE_SPACER))

        Button(

            onClick = onBack,

            modifier = Modifier.fillMaxWidth()

        ) {

            Text(
                text = stringResource(R.string.back_button)
            )


        }


    }
}