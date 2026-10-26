package com.example.packguide.data.auth

object AuthValidation {
    fun usernameError(username: String): String? = when {
        username.trim().length < MIN_USERNAME_LENGTH -> "El usuario debe tener al menos 3 caracteres."
        username.trim().length > MAX_USERNAME_LENGTH -> "El usuario no puede superar 24 caracteres."
        !USERNAME_PATTERN.matches(username.trim()) -> "Usa letras, números, punto, guion o guion bajo."
        else -> null
    }

    fun passwordError(password: String): String? = when {
        password.isEmpty() -> "La contraseña no puede estar vacía."
        password.length > MAX_PASSWORD_LENGTH -> "La contraseña no puede superar 45 caracteres."
        !password.all(Char::isLetterOrDigit) -> "Usa solo letras y números en la contraseña."
        else -> null
    }

    private const val MIN_USERNAME_LENGTH = 3
    private const val MAX_USERNAME_LENGTH = 24
    private const val MAX_PASSWORD_LENGTH = 45
    private val USERNAME_PATTERN = Regex("[A-Za-z0-9._-]+")
}
