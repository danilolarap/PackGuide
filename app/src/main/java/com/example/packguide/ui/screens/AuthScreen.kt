package com.example.packguide.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.packguide.data.auth.AuthRepository
import com.example.packguide.data.auth.AuthResult
import com.example.packguide.data.auth.AuthValidation
import com.example.packguide.ui.components.AsciiButton
import com.example.packguide.ui.components.DashedDivider
import com.example.packguide.ui.components.HudHeader
import com.example.packguide.ui.components.LedIndicator
import com.example.packguide.ui.theme.AccentGreenLed
import com.example.packguide.ui.theme.AccentRedLed
import com.example.packguide.ui.theme.BackgroundLight
import com.example.packguide.ui.theme.BorderSlate
import com.example.packguide.ui.theme.CardSurfaceLight
import com.example.packguide.ui.theme.PrimaryBlack
import com.example.packguide.ui.theme.SecondarySlate
import com.example.packguide.ui.theme.TechMonospaceFontFamily
import com.example.packguide.ui.theme.Typography
import kotlinx.coroutines.launch

@Composable
fun AuthScreen(repository: AuthRepository, onAuthenticated: () -> Unit) {
    var isRegistering by rememberSaveable { mutableStateOf(false) }
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var confirmPasswordVisible by rememberSaveable { mutableStateOf(false) }
    var error by rememberSaveable { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize().background(BackgroundLight).imePadding()
            .verticalScroll(rememberScrollState()).padding(horizontal = 24.dp, vertical = 28.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            LedIndicator(color = AccentRedLed, isPulsing = true)
            Spacer(Modifier.width(10.dp))
            Text("PACKGUIDE // ACCESS", style = Typography.labelMedium, color = SecondarySlate)
        }
        Spacer(Modifier.height(24.dp))
        HudHeader(
            title = if (isRegistering) "CREAR CUENTA" else "INICIAR SESIÓN",
            sysCode = "SYS // IDENTIDAD LOCAL",
            statusText = "[ SINCRONIZACIÓN OFF ]",
            ledColor = AccentRedLed
        )
        Spacer(Modifier.height(20.dp))
        DashedDivider()
        Spacer(Modifier.height(22.dp))

        Column(
            modifier = Modifier.fillMaxWidth().background(CardSurfaceLight, RoundedCornerShape(2.dp))
                .border(1.dp, BorderSlate, RoundedCornerShape(2.dp)).padding(20.dp)
        ) {
            Text("[ CREDENCIALES ]", style = Typography.labelMedium, color = PrimaryBlack)
            Spacer(Modifier.height(18.dp))
            AuthField(value = username, onValueChange = { username = it; error = null }, label = "USUARIO")
            Spacer(Modifier.height(12.dp))
            AuthField(
                value = password,
                onValueChange = { password = it; error = null },
                label = "CONTRASEÑA",
                secret = true,
                isPasswordVisible = passwordVisible,
                onPasswordVisibilityChange = { passwordVisible = it }
            )
            if (isRegistering) {
                Spacer(Modifier.height(12.dp))
                AuthField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it; error = null },
                    label = "CONFIRMAR CONTRASEÑA",
                    secret = true,
                    isPasswordVisible = confirmPasswordVisible,
                    onPasswordVisibilityChange = { confirmPasswordVisible = it }
                )
            }
            if (error != null) {
                Spacer(Modifier.height(12.dp))
                Text(error.orEmpty(), style = Typography.bodyMedium, color = AccentRedLed)
            }
            Spacer(Modifier.height(20.dp))
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                AsciiButton(
                    text = if (isRegistering) "[ REGISTRAR ]" else "[ ENTRAR ]",
                    onClick = {
                        val cleanUser = username.trim()
                        error = AuthValidation.usernameError(cleanUser)
                            ?: AuthValidation.passwordError(password)
                            ?: if (isRegistering && password != confirmPassword) {
                                "Las contraseñas no coinciden."
                            } else null
                        if (error == null) {
                            isLoading = true
                            scope.launch {
                                val result = if (isRegistering) repository.register(cleanUser, password)
                                else repository.login(cleanUser, password)
                                isLoading = false
                                when (result) {
                                    AuthResult.Success -> onAuthenticated()
                                    AuthResult.InvalidCredentials -> error = "Usuario o contraseña incorrectos."
                                    AuthResult.UsernameTaken -> error = "Ese usuario ya está registrado."
                                    AuthResult.StorageError -> error = "No se pudo acceder a la base local. Intenta de nuevo."
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    isAccentRed = true,
                    enabled = !isLoading
                )
                if (isLoading) CircularProgressIndicator(Modifier.size(20.dp), color = CardSurfaceLight, strokeWidth = 2.dp)
            }
            Spacer(Modifier.height(10.dp))
            AsciiButton(
                text = if (isRegistering) "[ YA TENGO CUENTA ]" else "[ CREAR CUENTA ]",
                onClick = { isRegistering = !isRegistering; error = null },
                modifier = Modifier.fillMaxWidth(),
                isPrimary = false,
                enabled = !isLoading
            )
        }
        Spacer(Modifier.height(18.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            LedIndicator(color = AccentGreenLed, isPulsing = false)
            Spacer(Modifier.width(8.dp))
            Text("CUENTA ALMACENADA EN ESTE DISPOSITIVO", style = Typography.labelSmall, color = SecondarySlate)
        }
    }
}

@Composable
private fun AuthField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    secret: Boolean = false,
    isPasswordVisible: Boolean = false,
    onPasswordVisibilityChange: (Boolean) -> Unit = {}
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label, fontFamily = TechMonospaceFontFamily) },
        singleLine = true,
        visualTransformation = if (secret && !isPasswordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(
            keyboardType = if (secret) KeyboardType.Password else KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        trailingIcon = if (secret) {
            {
                TextButton(onClick = { onPasswordVisibilityChange(!isPasswordVisible) }) {
                    Text(if (isPasswordVisible) "OCULTAR" else "VER", style = Typography.labelSmall)
                }
            }
        } else null,
        shape = RoundedCornerShape(2.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = PrimaryBlack,
            unfocusedBorderColor = BorderSlate,
            focusedLabelColor = PrimaryBlack,
            cursorColor = AccentRedLed
        )
    )
}
