package com.codebrew.encober.ui.profile

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.codebrew.encober.R
import com.codebrew.encober.models.requestModel.ProfileUpdateRequest
import com.codebrew.encober.models.responseModel.UserProfileData
import com.codebrew.encober.network.common.Status
import com.codebrew.encober.ui.common.ImageUploadViewModel
import com.codebrew.encober.ui.common.custom.LoadingDialog
import com.codebrew.encober.ui.login.profileSetup.ProfileSetupViewModel
import com.codebrew.encober.ui.login.profileSetup.onRequestPermissionsResult
import com.codebrew.encober.utils.*
import com.codebrew.encober.utils.PermissionUtils
import com.codebrew.encober.utils.extensions.*
import com.codebrew.encober.utils.local.UserManager
import com.codebrew.encober.utils.media.GetSampledImage
import com.codebrew.encober.utils.media.MediaPicker
import com.merge.utils.FileUtils
import kotlinx.android.synthetic.main.layout_edit_profile.*
import permissions.dispatcher.*
import java.io.File
import java.util.*


@RuntimePermissions
class EditProfileDialogFragment : DialogFragment(), DateUtils.OnDateSelectedListener {


    companion object {
        const val TAG = "com.codebrew.vipcarts.ui.main.profile.EditProfileDialogFragment"
    }

    private val viewModel by lazy { ViewModelProvider(this).get(ProfileSetupViewModel::class.java) }
    private val imageViewModel by lazy { ViewModelProvider(this).get(ImageUploadViewModel::class.java) }
    private var selectedImage: File? = null
    private var getSampledImage: GetSampledImage? = null
    private val mediaPicker by lazy { MediaPicker(this) }
    private val loadingDialog by lazy { LoadingDialog(requireActivity()) }
    private val editProfileReq = ProfileUpdateRequest()
    private lateinit var callback: EditProfileCallback


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_edit_profile, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViews()
        setListeners()
        observeChanges()

    }


    private fun setViews() {
        val userData = UserManager.getUserProfile()

        editProfileReq.thumbnail = userData?.image?.thumbnail
        editProfileReq.original = userData?.image?.original
        editProfileReq.dob = userData?.dob

        tvEditProfileEmail.text = userData?.email
        tvEditProfileID.text = "ID : " +userData?.id

        loadImage(
            requireActivity(),
            ivEditProfileDp,
            userData?.image?.thumbnail,
            userData?.image?.original
        )
        etEditProfileFirstName.setText(userData?.firstName)
        etEditProfileLastName.setText(userData?.lastName)
        tvEditProfileDOB.text = DateUtils.getFormatFromMillis(userData?.dob, "dd/MM/yyy")
        etEditProfileStreet.setText(userData?.street)
        etEditProfileExteriorNo.setText(userData?.outsideNo)
        etEditProfileInteriorNo.setText(userData?.interiorNo)
        etEditProfilePostalCode.setText(userData?.postalCode)
        etEditProfileState.setText(userData?.state)
        etEditProfileCity.setText(userData?.city)
        etEditProfileDelegation.setText(userData?.delegationOrMunicipality)
    }

    private fun setListeners() {

        tbrEditProfile.setNavigationOnClickListener {
            dismiss()
        }

        ivEditDp.setOnClickListener {
            showMediaPickerWithPermissionCheck()
        }

        imageUpload()


        tvEditProfileDOB.setOnClickListener {
            DateUtils.openDOBDateDialog(tvEditProfileDOB.context, this)
        }

        btnEditProfileSave.setOnClickListener {
            validations()
        }


        etEditProfilePostalCode.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                val code = s.toString()
                when {
                    code.trim().isEmpty() || code.trim().length in 1..4 -> {
                        etEditProfilePostalCode.error = null
                    }
                    else -> {
                        if (requireActivity().isNetworkActive())
                            viewModel.getPostalCodeDetails(code.toDouble())
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
            }
        })

    }


    private fun imageUpload() {

        mediaPicker.setImagePickerListener { imageFile ->
            if (imageFile.length() < AppConstants.MAXIMUM_IMAGE_SIZE) {
                getSampledImage?.removeListener()
                getSampledImage?.cancel(true)

                getSampledImage = GetSampledImage()
                getSampledImage?.setListener { sampledImage ->
                    selectedImage = sampledImage
                    Glide.with(requireActivity()).load(sampledImage).into(ivEditProfileDp)
                }

                val imageDirectory = FileUtils.getAppCacheDirectoryPath(requireActivity())
                getSampledImage?.sampleImage(imageFile.absolutePath, imageDirectory, 600)
            } else {
                AppToast.longToast(requireActivity(), R.string.message_select_smaller_image)
                return@setImagePickerListener
            }


        }
    }



    private fun validations() {

        etEditProfileFirstName.error = null
        etEditProfileLastName.error = null
        etEditProfileStreet.error = null
        etEditProfileExteriorNo.error = null
        etEditProfileInteriorNo.error = null
        etEditProfilePostalCode.error = null
        etEditProfileState.error = null
        etEditProfileCity.error = null
        etEditProfileDelegation.error = null


        when {
            etEditProfileFirstName.text.toString().trim().isEmpty() -> {
                invalidString(etEditProfileFirstName, getString(R.string.error_empty_first_name))
            }

            etEditProfileLastName.text.toString().trim().isEmpty() -> {
                invalidString(
                    etEditProfileLastName,
                    getString(R.string.error_empty_last_name)
                )
            }

            etEditProfileStreet.text.toString().trim().isEmpty() -> {
                invalidString(etEditProfileStreet, getString(R.string.error_empty_street_field))
            }

            etEditProfileExteriorNo.text.toString().trim().isEmpty() -> {
                invalidString(
                    etEditProfileExteriorNo,
                    getString(R.string.error_empty_exterior_no_field)
                )
            }

            etEditProfileInteriorNo.text.toString().trim().isEmpty() -> {
                invalidString(
                    etEditProfileInteriorNo,
                    getString(R.string.error_empty_interior_no_field)
                )
            }

            etEditProfilePostalCode.text.toString().trim().isEmpty() -> {
                invalidString(
                    etEditProfilePostalCode,
                    getString(R.string.error_empty_postal_code_field)
                )
            }

            etEditProfileState.text.toString().trim().isEmpty() -> {
                invalidString(etEditProfileState, getString(R.string.empty_state_error))
            }

            etEditProfileCity.text.toString().trim().isEmpty() -> {
                invalidString(etEditProfileCity, getString(R.string.empty_city_error))
            }

            etEditProfileDelegation.text.toString().trim().isEmpty() -> {
                invalidString(etEditProfileDelegation, getString(R.string.empty_city_error))
            }
            else -> {
                if (selectedImage != null) {
                    imageViewModel.uploadImage(
                        RetrofitUtils.createMultiPart(
                            selectedImage!!,
                            ApiConstants.PARAM_IMAGE
                        )
                    )
                } else {
                    profileUpdateApiHit()
                }
            }
        }
    }

    private fun profileUpdateApiHit() {

        editProfileReq.firstName = etEditProfileFirstName.text.toString().trim()
        editProfileReq.lastName = etEditProfileLastName.text.toString().trim()
        editProfileReq.street = etEditProfileStreet.text.toString().trim()
        editProfileReq.outsideNo = etEditProfileExteriorNo.text.toString().trim()
        editProfileReq.interiorNo = etEditProfileInteriorNo.text.toString().trim()
        editProfileReq.postalCode = etEditProfilePostalCode.text.toString().trim()
        editProfileReq.city = etEditProfileState.text.toString().trim()
        editProfileReq.delegationOrMunicipality = etEditProfileCity.text.toString().trim()
        editProfileReq.state = etEditProfileDelegation.text.toString().trim()


        if (requireActivity().isNetworkActive()) {
            viewModel.profileUpdate(editProfileReq)
        }
    }


    private fun observeChanges() {
        viewModel.postalCodeRes.observe(viewLifecycleOwner, Observer { resources ->
            resources ?: return@Observer
            when (resources.status) {
                Status.SUCCESS -> {
                    etEditProfilePostalCode.error = null
                    etEditProfileState.setText(resources.data?.state)
                    etEditProfileCity.setText(resources.data?.city)
                    etEditProfileDelegation.setText(resources.data?.municipality)
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
                    callback.profileUpdated()
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


        imageViewModel.imageUploadRes.observe(viewLifecycleOwner, Observer { resources ->
            resources ?: return@Observer
            when (resources.status) {
                Status.SUCCESS -> {
                    loadingDialog.setLoading(false)
                    editProfileReq.original = resources.data?.original
                    editProfileReq.thumbnail = resources.data?.thumbnail
                    profileUpdateApiHit()
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



    override fun dateTimeSelected(dateCal: Calendar) {
        tvEditProfileDOB.text = DateUtils.getFormatFromDate(dateCal.time, "dd/MM/yyyy")
        editProfileReq.dob = dateCal.timeInMillis
    }


    @NeedsPermission(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun showMediaPicker() {
        mediaPicker.show()
    }

    @OnShowRationale(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun cameraStorageRationale(request: PermissionRequest) {
        PermissionUtils.showRationalDialog(
            requireActivity(),
            R.string.permission_rationale_camera_storage,
            request
        )
    }

    @OnPermissionDenied(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun cameraStorageDenied() {
        activity?.longToast(R.string.permission_denied_camera_storage)
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun cameraStorageNeverAsk() {
        PermissionUtils.showAppSettingsDialog(
            this,
            R.string.permission_never_ask_camera_storage,
            AppConstants.REQ_CODE_APP_SETTINGS
        )
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mediaPicker.onActivityResult(requestCode, resultCode, data)

    }


    fun setListener(callback: EditProfileCallback) {
        this.callback = callback
    }


    interface EditProfileCallback {
        fun profileUpdated()
    }

}