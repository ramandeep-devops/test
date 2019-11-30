package com.codebrew.encober.ui.login.profileSetup

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.codebrew.encober.R
import com.codebrew.encober.models.requestModel.ProfileUpdateRequest
import com.codebrew.encober.network.common.Status
import com.codebrew.encober.ui.common.ImageUploadViewModel
import com.codebrew.encober.ui.common.custom.LoadingDialog
import com.codebrew.encober.utils.*
import com.codebrew.encober.utils.PermissionUtils
import com.codebrew.encober.utils.extensions.gone
import com.codebrew.encober.utils.extensions.handleError
import com.codebrew.encober.utils.extensions.longToast
import com.codebrew.encober.utils.extensions.shortToast
import com.codebrew.encober.utils.media.GetSampledImage
import com.codebrew.encober.utils.media.MediaPicker
import com.merge.utils.FileUtils
import kotlinx.android.synthetic.main.fragment_personal_information.*
import permissions.dispatcher.*
import java.io.File
import java.util.*

@RuntimePermissions
class PersonalInformationFragment : Fragment(), DateUtils.OnDateSelectedListener {

    companion object {
        const val TAG = "com.codebrew.encober.ui.login.profileUpdate.PersonalInformationFragment"
    }

    private val profileUpdateReq = ProfileUpdateRequest()
    private val loadingDialog by lazy { LoadingDialog(requireActivity()) }
    private val imageViewModel by lazy { ViewModelProvider(this).get(ImageUploadViewModel::class.java) }
    private var selectedImage: File? = null
    private var getSampledImage: GetSampledImage? = null
    private val mediaPicker by lazy { MediaPicker(this) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_personal_information, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        (activity as ProfileSetupActivity).stepProgressView(1)
        setListeners()
        observeChanges()

    }

    private fun observeChanges() {
        imageViewModel.imageUploadRes.observe(viewLifecycleOwner, Observer { resources ->
            resources ?: return@Observer
            when (resources.status) {
                Status.SUCCESS -> {
                    loadingDialog.setLoading(false)
                    profileUpdateReq.original = resources.data?.original
                    profileUpdateReq.thumbnail = resources.data?.thumbnail
                    replaceFragment()
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

    private fun setListeners() {
        btnPersonalInfoNext.setOnClickListener {
            when {
                tvPersonalInfoDOB.text.isEmpty() -> {
                    requireActivity().shortToast(getString(R.string.error_empty_DOB_field))
                }
                selectedImage == null -> {
                    requireActivity().shortToast(getString(R.string.error_empty_image))
                }
                else -> {
                    imageViewModel.uploadImage(
                        RetrofitUtils.createMultiPart(selectedImage!!, ApiConstants.PARAM_IMAGE)
                    )
                }
            }
        }

        tvPersonalInfoDOB.setOnClickListener {
            DateUtils.openDOBDateDialog(tvPersonalInfoDOB.context, this)
        }

        ivPersonalInfoDp.setOnClickListener {
            showMediaPickerWithPermissionCheck()
        }
        imageUpload()
    }

    private fun imageUpload() {

        mediaPicker.setImagePickerListener { imageFile ->
            if (imageFile.length() < AppConstants.MAXIMUM_IMAGE_SIZE) {
                getSampledImage?.removeListener()
                getSampledImage?.cancel(true)

                getSampledImage = GetSampledImage()
                getSampledImage?.setListener { sampledImage ->
                    selectedImage = sampledImage
                    Glide.with(requireActivity()).load(sampledImage).into(ivPersonalInfoDp)
                    tvAddImage.gone()
                }

                val imageDirectory = FileUtils.getAppCacheDirectoryPath(requireActivity())
                getSampledImage?.sampleImage(imageFile.absolutePath, imageDirectory, 600)
            } else {
                AppToast.longToast(requireActivity(), R.string.message_select_smaller_image)
                return@setImagePickerListener
            }


        }
    }

    private fun replaceFragment() {
        val fragment = MyAddressFragment()
        val bundle = Bundle()
        bundle.putParcelable(AppConstants.PROFILE_UPDATE_REQUEST_DATA, profileUpdateReq)
        fragment.arguments = bundle
        parentFragmentManager.beginTransaction()
            .replace(R.id.flProfileSetupContainer, fragment)
            .addToBackStack(MyAddressFragment.TAG)
            .commit()
    }

    override fun dateTimeSelected(dateCal: Calendar) {
        tvPersonalInfoDOB.text = DateUtils.getFormatFromDate(dateCal.time, "dd/MM/yyyy")
        profileUpdateReq.dob = dateCal.timeInMillis
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


    override fun onResume() {
        super.onResume()
        (activity as ProfileSetupActivity).stepProgressView(1)
    }


}