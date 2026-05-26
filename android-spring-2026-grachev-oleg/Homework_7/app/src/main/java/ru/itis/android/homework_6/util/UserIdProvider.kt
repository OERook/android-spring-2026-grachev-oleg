package ru.itis.android.homework_6.util

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Persisted random UUID used as the Crashlytics user id key.
 * Injected via Hilt and consumed by the start screen ViewModel — this is the
 * "dynamic argument generated at app start" required by Homework_7.
 */
@Singleton
class UserIdProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getOrCreate(): String {
        prefs.getString(KEY_USER_ID, null)?.let { return it }
        val newId = UUID.randomUUID().toString()
        prefs.edit().putString(KEY_USER_ID, newId).apply()
        return newId
    }

    companion object {
        private const val PREFS_NAME = "app_user_prefs"
        private const val KEY_USER_ID = "user_id"
    }
}
