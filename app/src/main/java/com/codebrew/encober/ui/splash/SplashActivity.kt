package com.codebrew.encober.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.codebrew.encober.R
import com.codebrew.encober.ui.base.BaseActivity
import com.codebrew.encober.ui.home.MainActivity
import com.codebrew.encober.ui.splash.adScreen.DemoScreenFragment
import com.codebrew.encober.utils.extensions.visible
import com.codebrew.encober.utils.local.PrefsManager
import com.codebrew.encober.utils.local.UserManager
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : BaseActivity() {
    override fun getLayoutResourceId(): Int = R.layout.activity_splash
    private var delayHandler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PrefsManager.initialize(applicationContext)

        delayHandler = Handler()
        delayHandler?.postDelayed(mRunnable, 3000)
    }

    private fun setListeners() {
        btnSplashLogin.setOnClickListener {
            DemoScreenFragment.start(this)
        }
    }


    private val mRunnable: Runnable = Runnable {
        if (!isFinishing) {
            val token = PrefsManager.get().getString(PrefsManager.PREF_ACCESS_TOKEN, "")

            if (token.isNotEmpty()) {
                val userData = UserManager.getUserProfile()
                if (userData?.isProfileComplete == true) {
                    finish()
                    startActivity(Intent(this,MainActivity::class.java))
                } else {
                    btnSplashLogin.visible()
                    setListeners()
                }
            } else {
                btnSplashLogin.visible()
                setListeners()
            }
        }
    }


    override fun onDestroy() {

        if (delayHandler != null) {
            delayHandler?.removeCallbacks(mRunnable)
        }
        super.onDestroy()
    }
}

