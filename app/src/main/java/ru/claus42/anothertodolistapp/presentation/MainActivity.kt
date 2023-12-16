package ru.claus42.anothertodolistapp.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import ru.claus42.anothertodolistapp.R
import ru.claus42.anothertodolistapp.appComponent
import ru.claus42.anothertodolistapp.databinding.ActivityMainBinding
import ru.claus42.anothertodolistapp.di.components.ActivityComponent
import ru.claus42.anothertodolistapp.di.components.DaggerActivityComponent
import ru.claus42.anothertodolistapp.presentation.auth.activities.SignInActivity


class MainActivity : AppCompatActivity() {

    lateinit var activityComponent: ActivityComponent
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        activityComponent = DaggerActivityComponent.builder()
            .appComponent(appComponent)
            .activity(this).build()
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    override fun onStart() {
        super.onStart()
        signIn()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun signIn() {
        val auth = Firebase.auth
        if (auth.currentUser == null) {
            val signInIntent = Intent(this, SignInActivity::class.java)
            startActivity(signInIntent)
            finish()
            return
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
