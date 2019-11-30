package com.codebrew.encober.ui.profile.changePassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.codebrew.encober.R
import com.codebrew.encober.models.requestModel.ChangePasswordRequest
import com.codebrew.encober.network.common.Status
import com.codebrew.encober.ui.common.custom.LoadingDialog
import com.codebrew.encober.utils.extensions.handleError
import com.codebrew.encober.utils.extensions.isNetworkActive
import com.codebrew.encober.utils.extensions.shortToast
import kotlinx.android.synthetic.main.layout_change_password.*

class ChangePasswordDialogFragment : DialogFragment() {

    companion object {
        const val TAG =
            "com.codebrew.vipcarts.ui.main.profile.changePassword.ChangePasswordDialogFragment"
    }


    private val viewModel by lazy { ViewModelProvider(this).get(ChangePasswordViewModel::class.java) }
    private val loadingDialog by lazy { LoadingDialog(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_change_password, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
        observeChanges()
    }

    private fun setListeners() {
        tbrChangePassword.setNavigationOnClickListener {
            dismiss()
        }

        btnChangePasswordSave.setOnClickListener {
            validations()
        }

    }

    private fun validations() {
        when {
            etCurrentPassword.text.toString().trim().isEmpty() -> {
                etCurrentPassword.requestFocus()
                etCurrentPassword.error = getString(R.string.current_password_empty_error)
            }
            etNewPassword.text.toString().trim().isEmpty() -> {
                etNewPassword.requestFocus()
                etNewPassword.error = getString(R.string.new_password_empty_error)
            }
            etConfirmPassword.text.toString().trim().isEmpty() -> {
                etConfirmPassword.requestFocus()
                etConfirmPassword.error = getString(R.string.confirm_password_empty_error)
            }
            etNewPassword.text.toString().trim() == etCurrentPassword.text.toString().trim() -> {
                etNewPassword.requestFocus()
                etNewPassword.error = getString(R.string.current_new_password__error)
            }
            etNewPassword.text.toString().trim() != etConfirmPassword.text.toString().trim() -> {
                etConfirmPassword.requestFocus()
                etConfirmPassword.error = getString(R.string.password_matching_error)
            }
            else -> {
                val request = ChangePasswordRequest(
                    etCurrentPassword.text.toString().trim(),
                    etNewPassword.text.toString().trim()
                )

                if (requireActivity().isNetworkActive()) {
                    viewModel.changePassword(request)
                }

            }
        }
    }


    private fun observeChanges() {
        viewModel.changePasswordRes.observe(viewLifecycleOwner, Observer { resources ->
            resources ?: return@Observer
            when (resources.status) {
                Status.SUCCESS -> {
                    loadingDialog.setLoading(false)
                    requireActivity().shortToast(getString(R.string.change_password_success_toast))
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
