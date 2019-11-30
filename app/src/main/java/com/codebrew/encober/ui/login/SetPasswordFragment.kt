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
import com.codebrew.encober.network.common.Status
import com.codebrew.encober.models.responseModel.UserProfileData
import com.codebrew.encober.ui.common.custom.LoadingDialog
import com.codebrew.encober.ui.login.profileSetup.ProfileSetupActivity
import com.codebrew.encober.utils.AppConstants
import com.codebrew.encober.utils.ValidationUtils
import com.codebrew.encober.utils.extensions.handleError
import com.codebrew.encober.utils.extensions.invalidString
import com.codebrew.encober.utils.extensions.longToast
import com.codebrew.encober.utils.local.UserManager
import kotlinx.android.synthetic.main.layout_set_password.*

class SetPasswordFragment : Fragment() {
    companion object {
        const val TAG = "com.codebrew.encober.ui.login.SetPasswordFragment"

    }

    private var isPasswordVisible : Boolean = false
    private var isConfirmPasswordVisible : Boolean = false
    private val viewModel by lazy { ViewModelProvider(this).get(LoginViewModel::class.java) }
    private val loadingDialog by lazy { LoadingDialog(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.layout_set_password, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setListeners()
        observeChanges()
    }

    private fun setListeners() {
        btnSetPassword.setOnClickListener {
            setPasswordValidation()
        }

        etSetPswdPassword.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus){
                viewBelowPassword.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.active_edittext_line_color))
            }else{
                viewBelowPassword.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.main_text_color_40_alpha))
            }
        }

        ivShowHidePassword.setOnClickListener {
            if(isPasswordVisible){
                etSetPswdPassword.transformationMethod = PasswordTransformationMethod()
                isPasswordVisible = false
                ivShowHidePassword.setImageResource(R.drawable.ic_show_password)
            }else{
                etSetPswdPassword.transformationMethod = null
                isPasswordVisible = true
                ivShowHidePassword.setImageResource(R.drawable.ic_hide_password)
            }
        }


        etSetPswdConfirmPassword.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus){
                viewBelowConfirmPassword.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.active_edittext_line_color))
            }else{
                viewBelowConfirmPassword.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.main_text_color_40_alpha))
            }
        }

        ivShowHideConfirmPassword.setOnClickListener {
            if(isConfirmPasswordVisible){
                etSetPswdConfirmPassword.transformationMethod = PasswordTransformationMethod()
                isConfirmPasswordVisible = false
                ivShowHideConfirmPassword.setImageResource(R.drawable.ic_show_password)
            }else{
                etSetPswdConfirmPassword.transformationMethod = null
                isConfirmPasswordVisible = true
                ivShowHideConfirmPassword.setImageResource(R.drawable.ic_hide_password)
            }
        }
    }


    private fun setPasswordValidation() {

        etSetPswdPassword.error = null
        etSetPswdConfirmPassword.error = null


        when {
            etSetPswdPassword.text.toString().trim().isEmpty() -> {
                invalidString(etSetPswdPassword, getString(R.string.error_empty_password_field))
            }
            etSetPswdConfirmPassword.text.toString().trim().isEmpty() -> {
                invalidString(
                    etSetPswdConfirmPassword,
                    "Please enter the Confirm Password"
                )
            }
            !ValidationUtils.isPasswordLengthValid(etSetPswdPassword.text.toString().trim()) -> {
                invalidString(etSetPswdPassword, getString(R.string.error_wrong_password_length))
            }
            etSetPswdConfirmPassword.text.toString().trim() != etSetPswdPassword.text.toString().trim() -> {
                invalidString(etSetPswdPassword, getString(R.string.error_password_not_matched))
                invalidString(etSetPswdConfirmPassword, getString(R.string.error_password_not_matched))
            }
            else -> {
                viewModel.setPassword(
                    email = arguments?.getString(AppConstants.LOGIN_EMAIL) ?: "",
                    password = etSetPswdPassword.text.toString().trim()
                )
            }
        }
    }

    private fun observeChanges() {
        viewModel.setPasswordRes.observe(viewLifecycleOwner, Observer { resources ->
            resources ?: return@Observer
            when (resources.status) {
                Status.SUCCESS -> {
                    loadingDialog.setLoading(false)
                    saveUserData(resources.data ?: UserProfileData())
                    requireContext().longToast(getString(R.string.password_saved_success_toast))
                    activity?.finish()
                    startActivity(
                        Intent(requireActivity(),
                            ProfileSetupActivity::class.java)
                    )

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