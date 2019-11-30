package com.codebrew.encober

import android.app.Application
import com.codebrew.encober.utils.local.PrefsManager

class EncoberApp : Application(){
    override fun onCreate() {
    super.onCreate()
    PrefsManager.initialize(this)
}
}