package com.codebrew.encober.ui.login.profileSetup

import android.os.Bundle
import com.codebrew.encober.R
import com.codebrew.encober.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_profile_setup.*

class ProfileSetupActivity : BaseActivity() {

    override fun getLayoutResourceId(): Int = R.layout.activity_profile_setup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        stepProgressView(1)

        supportFragmentManager.beginTransaction()
            .add(
                R.id.flProfileSetupContainer,
                PersonalInformationFragment(),
                PersonalInformationFragment.TAG
            )
            .commit()

        tbrProfile.setNavigationOnClickListener {
            onBackPressed()
        }

    }

    fun stepProgressView(fragNo: Int) {
        chktvProfilePersonalInfo.isChecked = false
        chktvProfileMyAddress.isChecked = false
//        chktvProfileBankData.isChecked = false

        when (fragNo) {
            1 -> {
                chktvProfilePersonalInfo.isChecked = true
            }

            2 -> {
                chktvProfilePersonalInfo.isChecked = true
                chktvProfileMyAddress.isChecked = true
            }

//            3 -> {
//                chktvProfilePersonalInfo.isChecked = true
//                chktvProfileMyAddress.isChecked = true
//                chktvProfileBankData.isChecked = true
//            }
        }
    }


}