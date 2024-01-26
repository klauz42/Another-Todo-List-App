package ru.claus42.anothertodolistapp.presentation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.claus42.anothertodolistapp.R
import ru.claus42.anothertodolistapp.appComponent
import ru.claus42.anothertodolistapp.databinding.ActivityMainBinding
import ru.claus42.anothertodolistapp.di.components.ActivityComponent
import ru.claus42.anothertodolistapp.di.components.DaggerActivityComponent
import ru.claus42.anothertodolistapp.domain.authentication.SessionManager
import ru.claus42.anothertodolistapp.domain.models.TodoItemRepository
import ru.claus42.anothertodolistapp.domain.models.UserPreferencesRepository
import ru.claus42.anothertodolistapp.presentation.auth.activities.SignInActivity
import ru.claus42.anothertodolistapp.utils.Constants.Notifications.NO_INTERNET_NOTIFICATION_CHANNEL_ID
import ru.claus42.anothertodolistapp.utils.Constants.Notifications.NO_INTERNET_NOTIFICATION_CHANNEL_RES_ID
import java.net.InetAddress
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    lateinit var activityComponent: ActivityComponent
    private lateinit var navController: NavController
    private lateinit var bottomNav: BottomNavigationView

    @Inject
    lateinit var sessionManager: SessionManager

    @Inject
    lateinit var userPreferences: UserPreferencesRepository

    @Inject
    lateinit var repository: TodoItemRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        activityComponent = DaggerActivityComponent.builder()
            .appComponent(appComponent)
            .activity(this).build()
        activityComponent.inject(this)

        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        setupBottomNavigation()
        setOnDestinationChangedListener()
    }

    private fun setupBottomNavigation() {
        bottomNav = findViewById<BottomNavigationView?>(R.id.bottom_navigation).apply {
            selectedItemId = R.id.destination_todo_item_details

            setupWithNavController(navController)

            setOnItemSelectedListener { menuItem ->
                when (navController.currentDestination?.id) {
                    R.id.destination_todo_item_details,
                    R.id.destination_search_sort_options,
                    R.id.destination_search_filter_options -> {
                        navController.popBackStack()
                    }
                }

                NavigationUI.onNavDestinationSelected(menuItem, navController)
            }
        }
    }

    private fun setOnDestinationChangedListener() {
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
            sync()
        }
    }

    override fun onDestroy() {
        Handler(Looper.getMainLooper()).removeCallbacksAndMessages(null)
        _binding = null

        super.onDestroy()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun sync() = lifecycleScope.launch(Dispatchers.IO) {
        if (isInternetAvailable()) {
            cancelNoInternetNotification()
            Log.i(TAG, "Internet is available, starting sync")
            try {
                repository.syncLocalWithRemote()
            } catch (e: Exception) {
                showNoInternetNotification(
                    getString(
                        R.string.synchronization_error,
                        e.message
                    )
                )
            }
        } else {
            showNoInternetNotification(getString(R.string.internet_is_not_available_msg))
        }
    }

    private suspend fun isInternetAvailable(): Boolean = withContext(Dispatchers.IO) {
        try {
            val address = InetAddress.getByName("google.com")
            return@withContext !address.equals("")
        } catch (e: Exception) {
            Log.w(TAG, "Internet is not available: ${e.message}")
        }

        return@withContext false
    }

    private fun signIn() {
        val signInIntent = Intent(this, SignInActivity::class.java)
        startActivity(signInIntent)
        finish()
        return
    }

    fun signOut() {
        sessionManager.signOut()

        lifecycleScope.launch(Dispatchers.IO) {
            repository.clearRepository()
        }

        val signInIntent = Intent(this, SignInActivity::class.java)
        startActivity(signInIntent)
        finish()
    }

    private fun showNoInternetNotification(text: String) {
        val builder = NotificationCompat.Builder(
            this,
            NO_INTERNET_NOTIFICATION_CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(getString(applicationInfo.labelRes))
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(NO_INTERNET_NOTIFICATION_CHANNEL_RES_ID, builder.build())
        }
    }

    private fun cancelNoInternetNotification() {
        with(NotificationManagerCompat.from(this)) {
            cancel(NO_INTERNET_NOTIFICATION_CHANNEL_RES_ID)
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
