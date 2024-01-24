package ru.claus42.anothertodolistapp.rules

import androidx.test.platform.app.InstrumentationRegistry
import com.google.firebase.FirebaseApp
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class FirebaseInitRule : TestRule {
    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                if (FirebaseApp.getApps(InstrumentationRegistry.getInstrumentation().targetContext).isEmpty()) {
                    FirebaseApp.initializeApp(InstrumentationRegistry.getInstrumentation().targetContext)
                }

                base.evaluate()
            }
        }
    }
}