package com.codebrew.encober.ui.login.forgotPassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.codebrew.encober.R
import com.codebrew.encober.network.common.Status
import com.codebrew.encober.ui.common.custom.LoadingDialog
import com.codebrew.encober.utils.AppConstants
import com.codebrew.encober.utils.extensions.handleError
import com.codebrew.encober.utils.extensions.shortToast
import kotlinx.android.synthetic.main.layout_forgot_password.*

class ForgotPasswordFragment : DialogFragment() {

    companion object {
        const val TAG = "com.codebrew.vipcarts.ui.login.forgotPassword.ForgotPasswordFragment"
    }

    private val viewModel by lazy { ViewModelProvider(this).get(ForgotPasswordViewModel::class.java) }
    private val loadingDialog by lazy { LoadingDialog(requireActivity()) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_forgot_password, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etForgotPasswordEmail.setText(arguments?.getString(AppConstants.LOGIN_EMAIL))

        setListeners()
        observeChanges()

    }

    private fun setListeners() {

        tbrForgotPassword.setNavigationOnClickListener {
            dismiss()
        }

        btnForgotPasswordContinue.setOnClickListener {
            viewModel.forgotPassword(etForgotPasswordEmail.text.toString())
        }
    }


    private fun observeChanges() {
        viewModel.forgotPasswordRes.observe(viewLifecycleOwner, Observer { resources ->
            resources ?: return@Observer
            when (resources.status) {
                Status.SUCCESS -> {
                    loadingDialog.setLoading(false)
                    requireActivity().shortToast(getString(R.string.forgot_password_success_toast))
                    dismiss()
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