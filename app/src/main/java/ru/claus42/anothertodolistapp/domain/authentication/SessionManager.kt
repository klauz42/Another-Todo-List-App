package ru.claus42.anothertodolistapp.domain.authentication

interface SessionManager {
    fun isUserLoggedIn(): Boolean
    fun getUserId(): String?
    fun getUserEmail(): String?
    fun signOut()
}