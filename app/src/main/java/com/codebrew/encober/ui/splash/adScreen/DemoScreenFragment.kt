package com.codebrew.encober.ui.splash.adScreen

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.codebrew.encober.R
import com.codebrew.encober.ui.login.LoginActivity
import com.codebrew.encober.ui.splash.termConditions.WebViewActivity
import kotlinx.android.synthetic.main.fragment_ad_screen.*

class DemoScreenFragment : Fragment() {
    companion object {
        const val TAG = "com.codebrew.encober.ui.splash.adScreen.DemoScreenFragment"

        fun start(context: AppCompatActivity) {
            context.supportFragmentManager.beginTransaction()
                .replace(
                    android.R.id.content,
                    DemoScreenFragment(),
                    DemoScreenFragment::class.java.name
                )
                .addToBackStack(TAG)
                .commitAllowingStateLoss()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_ad_screen, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewPager()
        setListener()
    }

    private fun setupViewPager() {
        viewPagerAdScreen.adapter = DemoScreenViewPager(requireActivity())
        circleIndicatorAdScreen.setViewPager(viewPagerAdScreen)
    }

    private fun setListener() {
        btnAdScreenLogin.setOnClickListener {
            startActivity(
                Intent(requireActivity(),
                    LoginActivity::class.java)
            )
        }

        tvAdScreenTermCondition.setOnClickListener {
            val i1 = Intent(requireActivity(), WebViewActivity::class.java)
            startActivity(i1)
        }

        parentPanel.setOnClickListener{
        }
    }

}