package ru.claus42.anothertodolistapp

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

class NoAuthTestRunner : AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader?, className: String?, context: Context?): Application {
        return super.newApplication(cl, NoAuthTestMainApp::class.java.name, context)
    }
}