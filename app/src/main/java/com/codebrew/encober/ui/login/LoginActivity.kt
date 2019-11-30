package com.codebrew.encober.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.codebrew.encober.R
import com.codebrew.encober.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_welcome.*

class LoginActivity : BaseActivity() {


    override fun getLayoutResourceId(): Int = R.layout.activity_welcome

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setListeners()
        supportFragmentManager.beginTransaction()
            .add(R.id.flLoginContainer, WelcomeFragment())
            .commit()

    }

    private fun setListeners() {
        ivLoginBack.setOnClickListener {
            onBackPressed()
        }
    }


}