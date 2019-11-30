package com.codebrew.encober.ui.splash.termConditions

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.codebrew.encober.R
import com.codebrew.encober.network.common.Status
import com.codebrew.encober.ui.base.BaseActivity
import com.codebrew.encober.utils.extensions.gone
import com.codebrew.encober.utils.extensions.handleError
import com.codebrew.encober.utils.extensions.visible
import kotlinx.android.synthetic.main.layout_term_condition.*


class WebViewActivity : BaseActivity(){

    private val viewModel by lazy { ViewModelProvider(this).get(TermConditionViewModel::class.java) }

    override fun getLayoutResourceId(): Int = R.layout.layout_term_condition

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getTermCondition()
        setListeners()
        observeChanges()
    }


    private fun observeChanges() {
        viewModel.termConditionRes.observe(this, Observer { resources ->
            resources ?: return@Observer
            when (resources.status) {
                Status.SUCCESS -> {
                    pgbTermCondition.gone()
                    webViewTermCondition.visible()
                    webViewTermCondition.loadData(resources.data?.text, "text/html", "charset=UTF-8")
                }
                Status.ERROR -> {
                    pgbTermCondition.gone()
                    webViewTermCondition.visible()
                    handleError(resources.error)
                }
                Status.LOADING -> {
                    pgbTermCondition.visible()
                }
            }
        })
    }

    private fun setListeners(){
        tbrTermCondition.setNavigationOnClickListener {
            finish()
        }

    }
}