package com.codebrew.encober.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.codebrew.encober.R
import com.codebrew.encober.network.common.Status
import com.codebrew.encober.ui.common.custom.LoadingDialog
import com.codebrew.encober.ui.profile.changePassword.ChangePasswordDialogFragment
import com.codebrew.encober.ui.splash.termConditions.WebViewActivity
import com.codebrew.encober.utils.extensions.handleError
import com.codebrew.encober.utils.extensions.loadImage
import com.codebrew.encober.utils.extensions.startLandingWithClear
import com.codebrew.encober.utils.local.UserManager
import kotlinx.android.synthetic.main.dialog_delete.view.*
import kotlinx.android.synthetic.main.fragment_main_profile.*

class ProfileFragment : Fragment(), EditProfileDialogFragment.EditProfileCallback {
    override fun profileUpdated() {
        setProfileData()
    }


    companion object {
        const val TAG = "com.codebrew.vipcarts.ui.main.profile.ProfileFragment"
    }

    private val signOutViewModel by lazy { ViewModelProvider(this).get(SignOutViewModel::class.java) }
    private val loadingDialog by lazy { LoadingDialog(requireActivity()) }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_main_profile, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
        setProfileData()
        observeChanges()
    }

    private fun setListeners() {
        tvMyProfileSignOff.setOnClickListener {
            dialogToSignOut()
        }

        /*   tvMyProfileContactENCOBER.setOnClickListener {
               startActivity(Intent(requireActivity(),ChatActivity::class.java))
           }
   */
        btnMyProfileEditProfile.setOnClickListener {
            val dialog = EditProfileDialogFragment()
            dialog.setListener(this)
            dialog.show(childFragmentManager, EditProfileDialogFragment.TAG)
        }

        tvMyProfileRecoverPassword.setOnClickListener {
            ChangePasswordDialogFragment().show(
                childFragmentManager,
                ChangePasswordDialogFragment.TAG
            )
        }

        tvMyProfileTermsConditions.setOnClickListener {
            val i1 = Intent(requireActivity(), WebViewActivity::class.java)
            startActivity(i1)
        }

    }


    private fun setProfileData() {
        val userData = UserManager.getUserProfile()
        loadImage(
            requireActivity(),
            ivMyProfileDP,
            userData?.image?.thumbnail,
            userData?.image?.original
        )
        tvMyProfileName.text = userData?.firstName + " " + userData?.lastName
        tvMyProfileEmail.text = userData?.email
        tvMyProfileID.text = "Id : " + userData?.id

    }

    private fun dialogToSignOut() {
        var dialog: AlertDialog? = null
        val layout = View.inflate(requireActivity(), R.layout.dialog_delete, null)

        layout.ivDeleteDialogIcon.setImageResource(R.drawable.ic_signout_big)
        layout.tvDeleteDialogHeading.text = getString(R.string.signout_account_sub_heading)
        layout.btnDeleteDialogRemove.text = getString(R.string.yes_label_add_service_dialog)

        layout.btnDeleteDialogCancel.setOnClickListener {
            dialog?.dismiss()
        }

        layout.btnDeleteDialogRemove.setOnClickListener {
            signOutViewModel.signOut()
            dialog?.dismiss()
        }

        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(layout)
        builder.setCancelable(false)
        dialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
    }


    private fun observeChanges() {
        signOutViewModel.signOutRes.observe(viewLifecycleOwner, Observer { resources ->
            resources ?: return@Observer
            when (resources.status) {
                Status.SUCCESS -> {
                    loadingDialog.setLoading(false)
                    context?.startLandingWithClear()
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