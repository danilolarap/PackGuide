package com.example.packguide.data.auth

object AuthValidation {
    fun usernameError(username: String): String? = when {
        username.trim().length < MIN_USERNAME_LENGTH -> "El usuario debe tener al menos 3 caracteres."
        username.trim().length > MAX_USERNAME_LENGTH -> "El usuario no puede superar 24 caracteres."
        !USERNAME_PATTERN.matches(username.trim()) -> "Usa letras, números, punto, guion o guion bajo."
        else -> null
    }

    fun passwordError(password: String): String? =
        if (password.length < MIN_PASSWORD_LENGTH) "La contraseña debe tener al menos 8 caracteres." else null

    private const val MIN_USERNAME_LENGTH = 3
    private const val MAX_USERNAME_LENGTH = 24
    private const val MIN_PASSWORD_LENGTH = 8
    private val USERNAME_PATTERN = Regex("[A-Za-z0-9._-]+")
}
