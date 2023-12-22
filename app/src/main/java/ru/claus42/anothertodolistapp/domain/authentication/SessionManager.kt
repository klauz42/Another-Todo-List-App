package ru.claus42.anothertodolistapp.domain.authentication

interface SessionManager {
    fun isLoggedIn(): Boolean
}