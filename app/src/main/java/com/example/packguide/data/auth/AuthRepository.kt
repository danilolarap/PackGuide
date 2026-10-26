package com.example.packguide.data.auth

import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64

sealed interface AuthResult {
    data object Success : AuthResult
    data object InvalidCredentials : AuthResult
    data object UsernameTaken : AuthResult
    data object StorageError : AuthResult
}

class AuthRepository(private val userAccountDao: UserAccountDao) {
    suspend fun register(username: String, password: String): AuthResult {
        val normalizedUsername = username.trim()
        return runCatching {
            if (userAccountDao.findByUsername(normalizedUsername) != null) {
                return@runCatching AuthResult.UsernameTaken
            }
            val salt = ByteArray(SALT_SIZE).also(SecureRandom()::nextBytes)
            userAccountDao.insert(
                UserAccount(
                    username = normalizedUsername,
                    passwordHash = derivePasswordHash(password, salt),
                    passwordSalt = Base64.getEncoder().encodeToString(salt)
                )
            )
            AuthResult.Success
        }.getOrElse { error ->
            if (error.javaClass.simpleName.contains("SQLiteConstraintException")) AuthResult.UsernameTaken
            else AuthResult.StorageError
        }
    }

    suspend fun login(username: String, password: String): AuthResult {
        val account = runCatching { userAccountDao.findByUsername(username.trim()) }
            .getOrElse { return AuthResult.StorageError }
            ?: return AuthResult.InvalidCredentials

        val salt = runCatching { Base64.getDecoder().decode(account.passwordSalt) }
            .getOrElse { return AuthResult.StorageError }
        val actual = runCatching { derivePasswordHash(password, salt) }
            .getOrElse { return AuthResult.StorageError }
        return if (MessageDigest.isEqual(actual.toByteArray(), account.passwordHash.toByteArray())) {
            AuthResult.Success
        } else {
            AuthResult.InvalidCredentials
        }
    }

    private fun derivePasswordHash(password: String, salt: ByteArray): String {
        val spec = javax.crypto.spec.PBEKeySpec(password.toCharArray(), salt, ITERATIONS, HASH_BITS)
        return try {
            Base64.getEncoder().encodeToString(
                javax.crypto.SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                    .generateSecret(spec).encoded
            )
        } finally {
            spec.clearPassword()
        }
    }

    private companion object {
        const val SALT_SIZE = 16
        const val ITERATIONS = 120_000
        const val HASH_BITS = 256
    }
}
