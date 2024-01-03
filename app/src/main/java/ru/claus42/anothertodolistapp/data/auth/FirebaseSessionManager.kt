package ru.claus42.anothertodolistapp.data.auth

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import ru.claus42.anothertodolistapp.di.scopes.AppScope
import ru.claus42.anothertodolistapp.domain.authentication.SessionManager
import javax.inject.Inject


@AppScope
class FirebaseSessionManager @Inject constructor() : SessionManager {
    private val auth = Firebase.auth

    override fun isUserLoggedIn() = auth.currentUser != null
    override fun getUserid() = Firebase.auth.uid
}