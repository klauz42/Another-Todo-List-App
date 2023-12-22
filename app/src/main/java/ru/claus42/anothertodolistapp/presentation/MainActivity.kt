package ru.claus42.anothertodolistapp.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import ru.claus42.anothertodolistapp.R
import ru.claus42.anothertodolistapp.appComponent
import ru.claus42.anothertodolistapp.databinding.ActivityMainBinding
import ru.claus42.anothertodolistapp.di.components.ActivityComponent
import ru.claus42.anothertodolistapp.di.components.DaggerActivityComponent
import ru.claus42.anothertodolistapp.domain.authentication.SessionManager
import ru.claus42.anothertodolistapp.presentation.auth.activities.SignInActivity
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    lateinit var activityComponent: ActivityComponent
    private lateinit var navController: NavController

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        activityComponent = DaggerActivityComponent.builder()
            .appComponent(appComponent)
            .activity(this).build()
        activityComponent.inject(this)

        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    override fun onStart() {
        super.onStart()
        if (!sessionManager.isLoggedIn()) {
            signIn()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun signIn() {
        val signInIntent = Intent(this, SignInActivity::class.java)
        startActivity(signInIntent)
        finish()
        return
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
