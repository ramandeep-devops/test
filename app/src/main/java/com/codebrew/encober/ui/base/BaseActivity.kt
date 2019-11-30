package com.codebrew.encober.ui.base

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.codebrew.encober.utils.LocaleHelper
import com.codebrew.encober.utils.local.PrefsManager

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResourceId())
    }

    protected abstract fun getLayoutResourceId():Int

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase, PrefsManager.PREF_LANGUAGE))
    }

}
