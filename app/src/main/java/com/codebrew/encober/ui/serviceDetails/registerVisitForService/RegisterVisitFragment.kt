package com.codebrew.encober.ui.serviceDetails.registerVisitForService

import android.app.DatePickerDialog
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.codebrew.encober.R
import com.codebrew.encober.models.requestModel.FinalizePaymentNotificationRequest
import com.codebrew.encober.network.common.Status
import com.codebrew.encober.ui.common.ImageUploadViewModel
import com.codebrew.encober.ui.common.custom.LoadingDialog
import com.codebrew.encober.utils.ApiConstants
import com.codebrew.encober.utils.AppConstants
import com.codebrew.encober.utils.DateUtils
import com.codebrew.encober.utils.RetrofitUtils
import com.codebrew.encober.utils.extensions.handleError
import com.codebrew.encober.utils.extensions.invalidString
import com.codebrew.encober.utils.extensions.isNetworkActive
import com.codebrew.encober.utils.extensions.shortToast
import kotlinx.android.synthetic.main.layout_register.*
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*


class RegisterVisitFragment : Fragment() {

    companion object {
        const val TAG =
            "com.codebrew.vipcarts.ui.main.start.serviceDetails.registerVisitForService.RegisterVisitFragment"
    }


    private val loadingDialog by lazy { LoadingDialog(requireActivity()) }
    private val viewModel by lazy { ViewModelProvider(this).get(RegisterVisitViewModel::class.java) }
    private val imageViewModel by lazy { ViewModelProvider(this).get(ImageUploadViewModel::class.java) }
    private var signFile: File? = null
    private lateinit var callback: RegisterVisitCallback
    private lateinit var paymentNotificationReq: FinalizePaymentNotificationRequest


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        paymentNotificationReq =
            arguments?.getParcelable(AppConstants.FINALIZE_PAYMENT_REQUEST)
                ?: FinalizePaymentNotificationRequest()

        setListeners()
        observeChanges()

    }

    private fun setListeners() {
        tbrRegister.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        tvRegisterPromiseDate.setOnClickListener {
            openDateDialog()
        }

        btnClearSignature.setOnClickListener {
            signaturePadRegister.clear()
            signFile = null
        }

        btnRegisterVisit.setOnClickListener {
            validations()
        }

    }

    private fun validations() {
        etRegisterAmount.error = null

        when {
            tvRegisterPromiseDate.text.toString().trim().isEmpty() -> {
                requireActivity().shortToast(getString(R.string.promise_date_empty_error))
            }
            etRegisterAmount.text.toString().trim().isEmpty() -> {
                invalidString(etRegisterAmount, getString(R.string.agreed_amount_empty_error))
            }
            signaturePadRegister.isEmpty -> {
                requireActivity().shortToast(getString(R.string.signature_of_promiser_error))
            }
            else -> {
                val res = imageFileForBitmap(
                    signaturePadRegister.transparentSignatureBitmap,
                    String.format("Firma_%d", System.currentTimeMillis())
                )
                paymentNotificationReq.agreedAmount = etRegisterAmount.text.toString().trim().toDouble()

                if (res) {
                    imageViewModel.uploadImage(
                        RetrofitUtils.createMultiPart(signFile!!, ApiConstants.PARAM_IMAGE)
                    )
                } else {
                    requireContext().shortToast(getString(R.string.sign_saved_failure_toast))
                }
            }
        }

    }


    private fun observeChanges() {
        imageViewModel.imageUploadRes.observe(viewLifecycleOwner, Observer { resources ->
            resources ?: return@Observer
            when (resources.status) {
                Status.SUCCESS -> {
                    loadingDialog.setLoading(false)

                    paymentNotificationReq.SignatureOriginal = resources.data?.original
                    paymentNotificationReq.SignatureThumbnail = resources.data?.thumbnail

                    if (requireActivity().isNetworkActive()) {
                        viewModel.finalizePaymentNotification(paymentNotificationReq)
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

        viewModel.finalizePaymentRes.observe(viewLifecycleOwner, Observer { resources ->
            resources ?: return@Observer
            when (resources.status) {
                Status.SUCCESS -> {
                    loadingDialog.setLoading(false)
                    activity?.onBackPressed()
                    callback.registerVisitComplete()
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


    private fun imageFileForBitmap(bitmap: Bitmap, name: String): Boolean {
        var result = false
        val filesDir = context?.filesDir
        signFile = File(filesDir, "$name.jpeg")
        val os: OutputStream
        try {
            os = FileOutputStream(signFile!!)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
            os.flush()
            os.close()
            result = true
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return result
    }


    private fun openDateDialog() {

        val c = Calendar.getInstance()
        val yearCal = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        val dpd = DatePickerDialog(
            requireActivity(),
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->

                val mCurrentTimeSelected = Calendar.getInstance()
                mCurrentTimeSelected.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                mCurrentTimeSelected.set(Calendar.MONTH, monthOfYear)
                mCurrentTimeSelected.set(Calendar.YEAR, year)

                tvRegisterPromiseDate.text =
                    DateUtils.getFormatFromDate(mCurrentTimeSelected.time, "dd/MM/yyyy")
                paymentNotificationReq.promiseDate = mCurrentTimeSelected.timeInMillis

            },
            yearCal, month, day
        )


        dpd.datePicker.minDate = System.currentTimeMillis()
        dpd.show()
    }

    fun setListener(callback: RegisterVisitCallback) {
        this.callback = callback
    }


    interface RegisterVisitCallback {
        fun registerVisitComplete()
    }
}