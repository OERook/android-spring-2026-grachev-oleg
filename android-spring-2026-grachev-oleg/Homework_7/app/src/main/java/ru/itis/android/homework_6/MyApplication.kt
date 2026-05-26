package ru.itis.android.homework_6

import android.app.Application
import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp
import ru.itis.android.homework_6.util.UserIdProvider
import javax.inject.Inject

@HiltAndroidApp
class MyApplication : Application() {

    @Inject lateinit var userIdProvider: UserIdProvider

    override fun onCreate() {
        super.onCreate()
        val userId = userIdProvider.getOrCreate()
        FirebaseCrashlytics.getInstance().apply {
            isCrashlyticsCollectionEnabled = true
            setUserId(userId)
            setCustomKey("user_id", userId)
        }
        Log.d("MyApplication", "User id: $userId")
    }
}
