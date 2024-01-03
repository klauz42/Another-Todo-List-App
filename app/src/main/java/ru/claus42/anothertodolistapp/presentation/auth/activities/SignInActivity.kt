package ru.claus42.anothertodolistapp.presentation.auth.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import ru.claus42.anothertodolistapp.appComponent
import ru.claus42.anothertodolistapp.databinding.ActivitySignInBinding
import ru.claus42.anothertodolistapp.di.components.ActivityComponent
import ru.claus42.anothertodolistapp.di.components.DaggerActivityComponent
import ru.claus42.anothertodolistapp.domain.authentication.Authenticator
import ru.claus42.anothertodolistapp.domain.authentication.SessionManager
import ru.claus42.anothertodolistapp.domain.models.entities.AuthResult
import ru.claus42.anothertodolistapp.presentation.MainActivity
import javax.inject.Inject


class SignInActivity : AppCompatActivity() {
    private var _binding: ActivitySignInBinding? = null
    private val binding get() = _binding!!

    private lateinit var activityComponent: ActivityComponent

    @Inject
    lateinit var sessionManager: SessionManager

    @Inject
    lateinit var authenticator: Authenticator

    override fun onCreate(savedInstanceState: Bundle?) {
        activityComponent = DaggerActivityComponent.builder()
            .appComponent(appComponent)
            .activity(this).build()
        activityComponent.inject(this)

        super.onCreate(savedInstanceState)

        _binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authenticator.setOnSignInCallback {
            onSignInResult(it)
        }

        if (!sessionManager.isUserLoggedIn()) {
            authenticator.startSignInActivity()
        } else {
            goToMainActivity()
        }
    }

    private fun onSignInResult(result: AuthResult) {
        when (result) {
            AuthResult.Success -> {
                Log.d(TAG, "Sign in successful!")
                goToMainActivity()
            }

            AuthResult.Cancelled -> {
                Log.w(TAG, "Sign in canceled")
                finishAffinity()
            }

            is AuthResult.Error -> {
                Log.w(TAG, "Sign in error", result.error)
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