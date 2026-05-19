package ru.itis.android.homework_6

import android.app.Application
import ru.itis.android.homework_6.di.AppContainer

class MyApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}