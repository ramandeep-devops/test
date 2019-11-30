package com.codebrew.encober.ui.login.yourDetails

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.codebrew.encober.R
import com.codebrew.encober.ui.login.profileSetup.ProfileSetupActivity
import com.codebrew.encober.utils.extensions.longToast
import com.codebrew.encober.utils.local.UserManager
import kotlinx.android.synthetic.main.fragment_your_details.*

class YourDetailsFragment : Fragment() {
    companion object {
        private const val TAG = "com.codebrew.encober.ui.loginSignup.signup.YourDetailsFragment"

        fun start(context: AppCompatActivity) {
            context.supportFragmentManager.beginTransaction()
                .replace(
                    android.R.id.content,
                    YourDetailsFragment(),
                    YourDetailsFragment::class.java.name
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
        inflater.inflate(R.layout.fragment_your_details, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
        setViews()

        tvYourDetailsTerms.text = termsSpanning(
            getString(
                R.string.accept_terms_and_conditions_label_details_page
            )
            , getString(R.string.terms_and_conditions_label_adscreen)
        )
    }

    private fun setViews() {
        val userData = UserManager.getUserProfile()
        tvYourDetailsName.text = userData?.firstName + userData?.lastName
        tvYourDetailsAddress.text = userData?.address
        tvYourDetailsEmail.text = userData?.email
        tvYourDetailsPhone.text = userData?.countryCode + " " + userData?.phoneNumber
    }

    private fun setListeners() {
        btnYourDetailsContinue.setOnClickListener {
            if (chkboxYourDetailsTerms.isChecked) {

                activity?.finish()
                startActivity(Intent(requireActivity(), ProfileSetupActivity::class.java))

            } else {
                requireActivity().longToast(getString(R.string.please_accept_terms_and_conditions))
            }
        }
    }


    private fun termsSpanning(fullStr: String, boldStr: String): SpannableString {
        val spanStr = SpannableString(fullStr)

        val clickSpan1 = object : ClickableSpan() {
            override fun onClick(widget: View) {
                requireActivity().longToast(getString(R.string.terms_and_conditions_label_adscreen))
            }
        }
        spanStr.setSpan(
            clickSpan1, spanStr.indexOf(boldStr),
            spanStr.indexOf(boldStr) + boldStr.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spanStr.setSpan(
            StyleSpan(Typeface.BOLD), spanStr.indexOf(boldStr),
            spanStr.indexOf(boldStr) + boldStr.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spanStr.setSpan(
            ForegroundColorSpan(
                ContextCompat.getColor(
                    tvYourDetailsTerms.context,
                    R.color.colorPrimaryDark
                )
            ), spanStr.indexOf(boldStr),
            spanStr.indexOf(boldStr) + boldStr.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spanStr
    }
}