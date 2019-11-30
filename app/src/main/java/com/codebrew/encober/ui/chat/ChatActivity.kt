package com.codebrew.encober.ui.chat

import android.os.Bundle
import com.codebrew.encober.R
import com.codebrew.encober.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity :BaseActivity(){
    override fun getLayoutResourceId(): Int = R.layout.activity_chat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setListeners()
    }

    private fun setListeners(){
        tbrChat.setNavigationOnClickListener {
            finish()
        }

    }

}