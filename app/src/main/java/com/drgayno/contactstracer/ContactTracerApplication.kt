package com.drgayno.contactstracer

import androidx.multidex.MultiDexApplication
import com.drgayno.contactstracer.db.AppDatabase
import com.drgayno.contactstracer.di.allModules
import com.drgayno.contactstracer.util.Log
import com.idescout.sql.SqlScoutServer
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinComponent
import org.koin.core.context.startKoin
import com.jakewharton.threetenabp.AndroidThreeTen
import java.io.File

class ContactTracerApplication : MultiDexApplication(), KoinComponent {

    override fun onCreate() {
        super.onCreate()
        instance = this

        startKoin {
            androidContext(this@ContactTracerApplication)
            modules(allModules)
        }

        // SQLScout - Database viewer for Android Studio
        SqlScoutServer.create(this, packageName)
        AppConfig.fetchRemoteConfig()

        if (BuildConfig.DEBUG) {
            getDatabaseSize()
        }
        AndroidThreeTen.init(this);
    }

    private fun getDatabaseSize() {
        val path: String = getDatabasePath(AppDatabase.DATABASE_NAME).toString()

        val file = File(path)
        val length: Long = file.length() // File size
        Log.d("Database size: ${length / 1024} kB")
    }

    companion object {
        lateinit var instance: ContactTracerApplication private set
    }
}