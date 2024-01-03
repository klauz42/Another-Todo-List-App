package ru.claus42.anothertodolistapp.data.auth

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import ru.claus42.anothertodolistapp.R
import ru.claus42.anothertodolistapp.di.scopes.ActivityScope
import ru.claus42.anothertodolistapp.domain.authentication.Authenticator
import ru.claus42.anothertodolistapp.domain.models.entities.AuthResult
import java.lang.ref.WeakReference
import javax.inject.Inject


@ActivityScope
class FirebaseAuthenticator @Inject constructor(
    hostSignInActivity: AppCompatActivity,
) : Authenticator {

    private val hostActivityReference = WeakReference(hostSignInActivity)

    private var onSignInCallback: ((AuthResult) -> Unit)? = null

    private var signInResultLauncher: ActivityResultLauncher<Intent>? = null

    override fun setOnSignInCallback(callback: (result: AuthResult) -> Unit) {
        this.onSignInCallback = callback
    }

    override fun startSignInActivity() {
        signInResultLauncher =
            hostActivityReference.get()
                ?.registerForActivityResult(FirebaseAuthUIActivityResultContract(), ::onResult)

        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setLogo(R.mipmap.ic_launcher)
            .setTheme(R.style.Theme_AnotherTodoListApp)
            .setAvailableProviders(
                listOf(
                    AuthUI.IdpConfig.GoogleBuilder().build(),
                    AuthUI.IdpConfig.EmailBuilder().build(),
                )
            )
            .build()

        signInResultLauncher?.launch(signInIntent)
    }

    private fun onResult(result: FirebaseAuthUIAuthenticationResult) {
        val authResult = when (result.resultCode) {
            RESULT_OK -> AuthResult.Success
            RESULT_CANCELED -> AuthResult.Cancelled
            else -> AuthResult.Error(result.idpResponse?.error)
        }
        onSignInCallback?.invoke(authResult)
    }
}