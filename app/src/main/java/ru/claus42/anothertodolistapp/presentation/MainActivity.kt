package ru.claus42.anothertodolistapp.presentation

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.claus42.anothertodolistapp.R
import ru.claus42.anothertodolistapp.appComponent
import ru.claus42.anothertodolistapp.databinding.ActivityMainBinding
import ru.claus42.anothertodolistapp.di.components.ActivityComponent
import ru.claus42.anothertodolistapp.di.components.DaggerActivityComponent
import ru.claus42.anothertodolistapp.domain.authentication.SessionManager
import ru.claus42.anothertodolistapp.domain.models.UserPreferencesRepository
import ru.claus42.anothertodolistapp.presentation.auth.activities.SignInActivity
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    lateinit var activityComponent: ActivityComponent
    private lateinit var navController: NavController
    private lateinit var bottomNav: BottomNavigationView

    @Inject
    lateinit var sessionManager: SessionManager

    @Inject
    lateinit var userPreferences: UserPreferencesRepository

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

        bottomNav = findViewById(R.id.bottom_navigation)
        bottomNav.selectedItemId = R.id.destination_todo_item_details
        bottomNav.setOnItemReselectedListener { }
        bottomNav.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val delayTime = resources.getInteger(R.integer.transition_duration).toLong() / 2
            val handler = Handler(Looper.getMainLooper())

            if (destination.id == R.id.destination_todo_item_details) {
                handler.postDelayed({
                    bottomNav.visibility = View.GONE
                }, delayTime)
            } else {
                if (bottomNav.visibility == View.GONE) {
                    handler.postDelayed({
                        bottomNav.visibility = View.VISIBLE
                    }, delayTime)
                }
            }
        }
    }


    override fun onStart() {
        super.onStart()
        if (!sessionManager.isUserLoggedIn()) {
            signIn()
        } else {
            lifecycleScope.launch(Dispatchers.IO) {
                val currentUserId = userPreferences.getUserId()

                if (currentUserId.isEmpty()) {
                    sessionManager.getUserid()?.let { userPreferences.setUserId(it) } ?: {
                        with(sessionManager) {
                            Log.e(
                                TAG, "sessionManager.userId = ${getUserid()}, " +
                                        "when sessionManager.isUserLoggedIn() = ${isUserLoggedIn()}"
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        Handler(Looper.getMainLooper()).removeCallbacksAndMessages(null)

        super.onDestroy()
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
