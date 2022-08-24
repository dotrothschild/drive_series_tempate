package com.dotrothschild.drive

import android.app.Application
import androidx.viewbinding.BuildConfig
import com.dotrothschild.drive.database.AppDatabase
import timber.log.Timber

class DriveApp : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }

    override fun onCreate() {
        super.onCreate()
        Timber.i("Start App file on Create")
        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String {
                    return String.format(
                        "***DriveApp***: %s: Line: %s, Method: %s",
                        super.createStackElementTag(element),
                        element.lineNumber,
                        element.methodName
                    )
                }
            })
        }
    }
}