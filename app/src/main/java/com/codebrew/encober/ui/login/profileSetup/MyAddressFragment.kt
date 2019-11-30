package com.codebrew.encober.ui.login.profileSetup

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.codebrew.encober.R
import com.codebrew.encober.models.requestModel.ProfileUpdateRequest
import com.codebrew.encober.models.responseModel.UserProfileData
import com.codebrew.encober.network.common.Status
import com.codebrew.encober.ui.common.custom.LoadingDialog
import com.codebrew.encober.ui.home.MainActivity
import com.codebrew.encober.utils.AppConstants
import com.codebrew.encober.utils.extensions.handleError
import com.codebrew.encober.utils.extensions.invalidString
import com.codebrew.encober.utils.extensions.isNetworkActive
import com.codebrew.encober.utils.extensions.shortToast
import com.codebrew.encober.utils.local.UserManager
import kotlinx.android.synthetic.main.fragment_my_address.*


class MyAddressFragment : Fragment() {
    companion object {
        const val TAG = "com.codebrew.encober.ui.login.profileUpdate.MyAddressFragment"

    }

    private val loadingDialog by lazy { LoadingDialog(requireActivity()) }
    private val viewModel by lazy { ViewModelProvider(this).get(ProfileSetupViewModel::class.java) }
    private lateinit var profileUpdateReq: ProfileUpdateRequest


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_my_address, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as ProfileSetupActivity).stepProgressView(2)

        profileUpdateReq = arguments?.getParcelable(AppConstants.PROFILE_UPDATE_REQUEST_DATA)
            ?: ProfileUpdateRequest()


        setListeners()
        observeChanges()

    }

    private fun setListeners() {
        btnMyAddressNext.setOnClickListener {
            validations()
        }

        etMyAddressPostalCode.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                val code = s.toString()
                when {
                    code.trim().isEmpty() || code.trim().length in 1..4 -> etMyAddressPostalCode.error =
                        null
                    else -> viewModel.getPostalCodeDetails(code.toDouble())
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
            }
        })


    }

    private fun observeChanges() {
        viewModel.postalCodeRes.observe(viewLifecycleOwner, Observer { resources ->
            resources ?: return@Observer
            when (resources.status) {
                Status.SUCCESS -> {
                    etMyAddressPostalCode.error = null
                    etMyAddressState.setText(resources.data?.state)
                    etMyAddressCity.setText(resources.data?.city)
                    etMyAddressDelegation.setText(resources.data?.municipality)
                    loadingDialog.setLoading(false)
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

        viewModel.profileUpdateRes.observe(viewLifecycleOwner, Observer { resources ->
            resources ?: return@Observer
            when (resources.status) {
                Status.SUCCESS -> {
                    loadingDialog.setLoading(false)
                    UserManager.saveUserProfile(resources.data ?: UserProfileData())
                    requireActivity().shortToast(getString(R.string.profile_successfully_updated_toast))
                    activity?.finishAffinity()
                    startActivity(Intent(requireActivity(),MainActivity::class.java))

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


    private fun validations() {
        etMyAddressStreet.error = null
        etMyAddressExteriorNo.error = null
        etMyAddressInteriorNo.error = null
        etMyAddressPostalCode.error = null
        etMyAddressState.error = null
        etMyAddressCity.error = null
        etMyAddressDelegation.error = null

        when {
            etMyAddressStreet.text.toString().trim().isEmpty() -> {
                invalidString(etMyAddressStreet, getString(R.string.error_empty_street_field))
            }

            etMyAddressExteriorNo.text.toString().trim().isEmpty() -> {
                invalidString(etMyAddressExteriorNo, getString(R.string.error_empty_exterior_no_field))
            }

            etMyAddressInteriorNo.text.toString().trim().isEmpty() -> {
                invalidString(etMyAddressInteriorNo, getString(R.string.error_empty_interior_no_field))
            }

            etMyAddressPostalCode.text.toString().trim().isEmpty() -> {
                invalidString(etMyAddressPostalCode, getString(R.string.error_empty_postal_code_field))
            }

            etMyAddressState.text.toString().trim().isEmpty() -> {
                invalidString(etMyAddressState, getString(R.string.empty_state_error))
            }

            etMyAddressCity.text.toString().trim().isEmpty() -> {
                invalidString(etMyAddressCity, getString(R.string.empty_city_error))
            }

            etMyAddressDelegation.text.toString().trim().isEmpty() -> {
                invalidString(etMyAddressCity, getString(R.string.empty_city_error))
            }
            else -> {
                profileUpdateApiHit()
            }
        }
    }

    private fun profileUpdateApiHit() {
        profileUpdateReq.street = etMyAddressStreet.text.toString().trim()
        profileUpdateReq.outsideNo = etMyAddressExteriorNo.text.toString().trim()
        profileUpdateReq.interiorNo = etMyAddressInteriorNo.text.toString().trim()
        profileUpdateReq.postalCode = etMyAddressPostalCode.text.toString().trim()
        profileUpdateReq.state = etMyAddressState.text.toString().trim()
        profileUpdateReq.city = etMyAddressCity.text.toString().trim()
        profileUpdateReq.delegationOrMunicipality = etMyAddressDelegation.text.toString().trim()

        if (requireActivity().isNetworkActive()) {
            viewModel.profileUpdate(profileUpdateReq)
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as ProfileSetupActivity).stepProgressView(2)
    }

}