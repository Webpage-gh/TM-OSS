package com.rk.taskmanager.oss

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.room.Room
import com.rk.taskmanager.oss.data.AppDatabase
import com.rk.taskmanager.oss.settings.SettingsRoutes

class TaskManager : Application() {
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "apps.db"
                )
                    .createFromAsset("databases/apps.db")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }

        private lateinit var instance: TaskManager
        val application get() = instance

        fun requireContext(): Context {
            if (::instance.isInitialized.not()) {
                throw IllegalStateException("Application not initialized")
            } else {
                return instance.applicationContext
            }
        }

        fun getContext(): Context {
            return requireContext()
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        com.rk.commons.application = this

        // Pro version initialization removed - open source version only
    }
}
