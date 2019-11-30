package com.codebrew.encober.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.codebrew.encober.R
import com.codebrew.encober.network.common.Status
import com.codebrew.encober.ui.common.custom.LoadingDialog
import com.codebrew.encober.utils.AppConstants
import com.codebrew.encober.utils.ValidationUtils
import com.codebrew.encober.utils.extensions.handleError
import com.codebrew.encober.utils.extensions.invalidString
import com.codebrew.encober.utils.extensions.isNetworkActive
import kotlinx.android.synthetic.main.layout_welcome.*

class WelcomeFragment : Fragment() {
    companion object {
        const val TAG = "com.codebrew.encober.ui.login.WelcomeFragment"
    }

    private val viewModel by lazy { ViewModelProvider(this).get(LoginViewModel::class.java) }
    private val loadingDialog by lazy { LoadingDialog(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.layout_welcome, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
        observeChanges()
    }


    private fun setListeners() {
        btnWelcomeContinue.setOnClickListener {
            welcomeValidations()
        }
    }

    private fun welcomeValidations() {
        when {
            etWelcomeEmail.text.toString().trim().isEmpty() -> {
                invalidString(etWelcomeEmail, getString(R.string.error_empty_email_field))
            }

            !ValidationUtils.isEmailValid(etWelcomeEmail.text.toString().trim()) -> {
                invalidString(etWelcomeEmail, getString(R.string.error_wrong_email_field))
            }
            else -> {
                if (requireActivity().isNetworkActive())
                    viewModel.checkEmail(etWelcomeEmail.text.toString().trim())
            }
        }
    }


    private fun observeChanges() {
        viewModel.checkEmailRes.observe(viewLifecycleOwner, Observer { resources ->
            resources ?: return@Observer
            when (resources.status) {
                Status.SUCCESS -> {
                    loadingDialog.setLoading(false)

                    if (resources.data?.isActivate == true) {
                        val fragment = LoginFragment()
                        val bundle = Bundle()
                        bundle.putString(AppConstants.LOGIN_EMAIL, resources.data.email)
                        fragment.arguments = bundle
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.flLoginContainer, fragment)
                            .addToBackStack(LoginFragment.TAG)
                            .commit()
                    } else {
                        val fragment = SetPasswordFragment()
                        val bundle = Bundle()
                        bundle.putString(AppConstants.LOGIN_EMAIL, resources.data?.email)
                        fragment.arguments = bundle
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.flLoginContainer, fragment)
                            .addToBackStack(SetPasswordFragment.TAG)
                            .commit()
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
}