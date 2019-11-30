package com.codebrew.encober.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.codebrew.encober.R
import com.codebrew.encober.models.responseModel.UserProfileData
import com.codebrew.encober.network.common.Status
import com.codebrew.encober.ui.common.custom.LoadingDialog
import com.codebrew.encober.ui.login.forgotPassword.ForgotPasswordFragment
import com.codebrew.encober.ui.login.profileSetup.ProfileSetupActivity
import com.codebrew.encober.ui.home.MainActivity
import com.codebrew.encober.utils.AppConstants
import com.codebrew.encober.utils.ValidationUtils
import com.codebrew.encober.utils.extensions.*
import com.codebrew.encober.utils.local.UserManager
import kotlinx.android.synthetic.main.layout_login.*

class LoginFragment : Fragment() {
    companion object {
        const val TAG = "com.codebrew.encober.ui.login.LoginFragment"
    }


    private val viewModel by lazy { ViewModelProvider(this).get(LoginViewModel::class.java) }
    private val loadingDialog by lazy { LoadingDialog(requireActivity()) }
    private var isPasswordVisible : Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.layout_login, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etLoginEmail.setText(arguments?.getString(AppConstants.LOGIN_EMAIL))
        observeChanges()
        setListeners()
    }

    private fun setListeners() {
        btnLogin.setOnClickListener {
            loginValidation()
        }

        etLoginPassword.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus){
                viewBelowPassword.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.active_edittext_line_color))
            }else{
                viewBelowPassword.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.main_text_color_40_alpha))
            }
        }

        ivLoginShowHidePassword.setOnClickListener {
            if(isPasswordVisible){
                etLoginPassword.transformationMethod = PasswordTransformationMethod()
                isPasswordVisible = false
                ivLoginShowHidePassword.setImageResource(R.drawable.ic_show_password)
            }else{
                etLoginPassword.transformationMethod = null
                isPasswordVisible = true
                ivLoginShowHidePassword.setImageResource(R.drawable.ic_hide_password)
            }
        }

        tvLoginForgotPassword.setOnClickListener {
            val dialog = ForgotPasswordFragment()
            val bundle = Bundle()
            bundle.putString(AppConstants.LOGIN_EMAIL,arguments?.getString(AppConstants.LOGIN_EMAIL))
            dialog.arguments = bundle
            dialog.show(childFragmentManager, ForgotPasswordFragment.TAG)
        }
    }

    private fun loginValidation() {
        etLoginEmail.error = null
        etLoginPassword.error = null


        when {
            etLoginEmail.text.toString().trim().isEmpty() -> {
                invalidString(etLoginEmail, getString(R.string.error_empty_email_field))
            }
            etLoginPassword.text.toString().trim().isEmpty() -> {
                invalidString(etLoginPassword, getString(R.string.error_empty_password_field))
            }
            !ValidationUtils.isEmailValid(etLoginEmail.text.toString().trim()) -> {
                invalidString(etLoginEmail, getString(R.string.error_wrong_email_field))
            }
            else -> {
                if (requireActivity().isNetworkActive())
                    viewModel.login(
                        etLoginEmail.text.toString().trim(),
                        etLoginPassword.text.toString().trim()
                    )

            }
        }


    }


    private fun observeChanges() {
        viewModel.loginRes.observe(viewLifecycleOwner, Observer { resources ->
            resources ?: return@Observer
            when (resources.status) {
                Status.SUCCESS -> {
                    loadingDialog.setLoading(false)
                    saveUserData(resources.data ?: UserProfileData())

                    if (resources.data?.isProfileComplete == true) {
                        activity?.finishAffinity()
                        startActivity(Intent(requireActivity(),MainActivity::class.java))
                    } else {
                        activity?.finish()
                        startActivity(Intent(requireActivity(),ProfileSetupActivity::class.java))
                    }
                }
                Status.ERROR -> {
                    loadingDialog.setLoading(false)
                    requireActivity().handleError(resources.error)
                }
                Status.LOADING -> {
                    loadingDialog.setLoading(true)
                }
            }
        })
    }

    private fun saveUserData(data: UserProfileData) {
        if (data.accessToken != null) {
            UserManager.saveAccessToken(data.accessToken)
        }

        UserManager.saveUserProfile(data)
    }

}