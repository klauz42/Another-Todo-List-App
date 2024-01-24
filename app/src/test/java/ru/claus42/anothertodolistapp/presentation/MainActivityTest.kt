package ru.claus42.anothertodolistapp.presentation

import junit.framework.TestCase.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import ru.claus42.anothertodolistapp.MainApp
import ru.claus42.anothertodolistapp.rules.FirebaseInitRule

@RunWith(RobolectricTestRunner::class)
@Config(application = MainApp::class)
class MainActivityTest {

    @get:Rule
    val firebaseRule = FirebaseInitRule()

    @Test
    fun `activity should not be null`() {
        val activity: MainActivity = Robolectric.buildActivity(MainActivity::class.java)
            .create()
            .start()
            .resume()
            .get()

        assertNotNull(activity)
    }
}