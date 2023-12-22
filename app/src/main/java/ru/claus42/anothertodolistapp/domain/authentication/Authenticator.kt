package ru.claus42.anothertodolistapp.domain.authentication

import ru.claus42.anothertodolistapp.domain.models.entities.AuthResult

interface Authenticator {
    fun startSignInActivity()
    fun setOnSignInCallback(callback: (result: AuthResult) -> Unit)
    fun isCallbackNull(): Boolean
}