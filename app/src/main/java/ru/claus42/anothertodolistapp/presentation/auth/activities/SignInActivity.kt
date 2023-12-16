package ru.claus42.anothertodolistapp.presentation.auth.activities

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import ru.claus42.anothertodolistapp.R
import ru.claus42.anothertodolistapp.databinding.ActivitySignInBinding
import ru.claus42.anothertodolistapp.presentation.MainActivity


class SignInActivity : AppCompatActivity() {
    private var _binding: ActivitySignInBinding? = null
    private val binding get() = _binding!!

    private val signIn: ActivityResultLauncher<Intent> =
        registerForActivityResult(FirebaseAuthUIActivityResultContract(), this::onSignInResult)

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        _binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        if (Firebase.auth.currentUser == null) {
            val signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setLogo(R.mipmap.ic_launcher)
                .setTheme(R.style.Theme_AnotherTodoListApp)
                .setAvailableProviders(
                    listOf(
                        AuthUI.IdpConfig.EmailBuilder().build(),
                    )
                )
                .build()
            signIn.launch(signInIntent)
        } else {
            goToMainActivity()
        }
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        when (result.resultCode) {
            RESULT_OK -> {
                Log.d(TAG, "Sign in successful!")
                goToMainActivity()
            }

            RESULT_CANCELED -> {
                Log.w(TAG, "Sign in canceled")
                finishAffinity()
            }

            else -> {
                val response = result.idpResponse
                if (response == null) {
                    Log.w(TAG, "Sign in error, response == null")
                } else {
                    Log.w(TAG, "Sign in error", response.error)
                }
                //todo: add error handling
                finishAffinity()
            }
        }
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object {
        private const val TAG = "SignInActivity"
    }
}