package ru.claus42.anothertodolistapp.domain.models.entities

sealed class AuthResult {
    data object Success : AuthResult()
    data object Cancelled : AuthResult()
    data class Error(val error: Exception?) : AuthResult()
}