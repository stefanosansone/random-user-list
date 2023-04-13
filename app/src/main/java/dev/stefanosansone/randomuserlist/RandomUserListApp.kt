package dev.stefanosansone.randomuserlist

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RandomUserListApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}