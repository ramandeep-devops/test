package com.codebrew.encober.ui.serviceDetails

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codebrew.encober.R
import com.codebrew.encober.models.requestModel.FinalizePaymentNotificationRequest
import com.codebrew.encober.models.requestModel.SubmitReasonRequest
import com.codebrew.encober.models.requestModel.VisitorWithPopUpRequest
import com.codebrew.encober.models.responseModel.serviceDetails.ServiceDetailsData
import com.codebrew.encober.models.responseModel.serviceDetails.happenOnVisit.HappenOnVisitItem
import com.codebrew.encober.models.responseModel.serviceDetails.questionsOfVisit.QuestionOfVisitItem
import com.codebrew.encober.models.responseModel.serviceDetails.reasons.ReasonForServiceItem
import com.codebrew.encober.network.common.Status
import com.codebrew.encober.ui.base.BaseActivity
import com.codebrew.encober.ui.common.custom.LoadingDialog
import com.codebrew.encober.ui.home.start.AddRemoveServiceViewModel
import com.codebrew.encober.ui.serviceDetails.comments.CommentsFragment
import com.codebrew.encober.ui.serviceDetails.registerVisitForService.RegisterVisitFragment
import com.codebrew.encober.utils.AppConstants
import com.codebrew.encober.utils.DateUtils
import com.codebrew.encober.utils.MapUtils
import com.codebrew.encober.utils.extensions.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.bottom_sheet_happened_on_visit.*
import kotlinx.android.synthetic.main.dailog_excellent_work.view.*
import kotlinx.android.synthetic.main.dialog_delete.view.*
import kotlinx.android.synthetic.main.dialog_meet_others.view.*
import kotlinx.android.synthetic.main.dialog_result_got.*
import kotlinx.android.synthetic.main.layout_service_details.*

class ServiceDetailsActivity : BaseActivity(), OnClickDialogCallback,
    RegisterVisitFragment.RegisterVisitCallback {
    override fun registerVisitComplete() {
        dialogAfterVisit()
    }

    override fun onClickReason(data: ReasonForServiceItem) {
        reasonItem = data
    }

    override fun onClickHappenOnVisit(data: HappenOnVisitItem) {
        happenItem = data
    }

    override fun onClickQuestionOfVisit(data: QuestionOfVisitItem) {
        questionItem = data
    }

    private val viewModel by lazy { ViewModelProvider(this).get(ServiceDetailsViewModel::class.java) }
    private val removeViewModel by lazy { ViewModelProvider(this).get(AddRemoveServiceViewModel::class.java) }
    private val loadingDialog by lazy { LoadingDialog(this) }
    private lateinit var serviceData: ServiceDetailsData
    private var reasonItem: ReasonForServiceItem? = null
    private var happenItem: HappenOnVisitItem? = null
    private var questionItem: QuestionOfVisitItem? = null
    private val paymentNotificationReq = FinalizePaymentNotificationRequest()


    override fun getLayoutResourceId(): Int = R.layout.layout_service_details

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val itemId = intent.getStringExtra(AppConstants.MAP_DATA_ITEM_ID) ?: ""

        if (isNetworkActive()) {
            viewModel.serviceDetails(itemId)
        }


        setListeners()
        observeChanges()
    }

    private fun setListeners() {
        tbrServiceDetails.setNavigationOnClickListener {
            finish()
        }

        ivServiceDetailsDelete.setOnClickListener {
            dialogToAddRemoveService(false)
        }

        ivServiceDetailsAdd.setOnClickListener {
            dialogToAddRemoveService(true)
        }

        btnServiceDetailsSeeMap.setOnClickListener {
            MapUtils.openGoogleMaps(this, serviceData.lat.toString(), serviceData.lng.toString())
        }

        btnServiceDetailsSeeAll.setOnClickListener {
            val fragment =
                CommentsFragment()
            val bundle = Bundle()
            bundle.putString(AppConstants.SERVICE_ID, serviceData.id)
            fragment.arguments = bundle

            supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, fragment, CommentsFragment::class.java.name)
                .addToBackStack(CommentsFragment.TAG)
                .commitAllowingStateLoss()
        }

        tvServiceDetailsPaymentNotifications.setOnClickListener {
            if (isNetworkActive())
                viewModel.happenOnVisit(serviceData.id ?: "")

            /**/
        }

        btnServiceDetailsRegisterVisit.setOnClickListener {
            if (isNetworkActive())
                viewModel.reasonForService(serviceData.id ?: "")
        }
    }


    private fun setViews() {
        tvServiceDetailsHeading.text = serviceData.title
        tvServiceDetailsName.text = serviceData.serviceName
        tvServiceDetailsPercentage.text = serviceData.serviceName.toString()
        tvServiceDetailsAddress.text = serviceData.address
        tvServiceDetailsPhone.text = "(" + serviceData.countryCode + ")  " + serviceData.phoneNumber
        tvServiceDetailsMobile.text =
            "(" + serviceData.countryCode + ")  " + serviceData.phoneNumber


        if (serviceData.userCommentsData != null) {
            tvServiceDetailsLastVisitResult.text = serviceData.userCommentsData?.questionEn
            tvServiceDetailsLastVisit.text = DateUtils.getFormatFromMillis(
                serviceData.userCommentsData?.createdAt,
                "dd MMM yyyy"
            )
        }

        if (serviceData.isAdded == true) {
            ivServiceDetailsAdd.gone()
            ivServiceDetailsDelete.visible()
            llRegisterVisit.visible()
        } else {
            ivServiceDetailsDelete.gone()
            llRegisterVisit.gone()
            ivServiceDetailsAdd.visible()
        }

    }


    private fun observeChanges() {
        viewModel.serviceDetailsRes.observe(this, Observer { resources ->
            resources ?: return@Observer
            when (resources.status) {
                Status.SUCCESS -> {
                    loadingDialog.setLoading(false)
                    if (resources.data != null) {
                        serviceData = resources.data

                        setViews()

                    } else {
                        longToast(getString(R.string.no_detail_found_toast))
                        finish()
                    }
                }
                Status.ERROR -> {
                    loadingDialog.setLoading(false)
                    handleError(resources.error)
                }
                Status.LOADING -> {
                    loadingDialog.setLoading(true)
                }
            }
        })


        viewModel.reasonForServiceRes.observe(this, Observer { resources ->
            resources ?: return@Observer
            when (resources.status) {
                Status.SUCCESS -> {
                    loadingDialog.setLoading(false)

                    if (resources.data?.count == 0) {
                        shortToast(getString(R.string.no_reason_found_error_service_details))
                    } else {
                        reasonItem = resources.data?.listing?.get(0) ?: ReasonForServiceItem()
                        reasonForServiceBottomSheet(resources.data?.listing ?: ArrayList())
                    }
                }
                Status.ERROR -> {
                    loadingDialog.setLoading(false)
                    handleError(resources.error)
                }
                Status.LOADING -> {
                    loadingDialog.setLoading(true)
                }
            }
        })

        viewModel.happenOnVisitRes.observe(this, Observer { resources ->
            resources ?: return@Observer
            when (resources.status) {
                Status.SUCCESS -> {
                    loadingDialog.setLoading(false)
                    if (resources.data?.count == 0) {
                        shortToast(getString(R.string.no_comment_found_error))
                    } else {
                        happenItem = resources.data?.listing?.get(0) ?: HappenOnVisitItem()
                        happenOnVisitBottomSheet(resources.data?.listing ?: ArrayList())
                    }
                }
                Status.ERROR -> {
                    loadingDialog.setLoading(false)
                    handleError(resources.error)
                }
                Status.LOADING -> {
                    loadingDialog.setLoading(true)
                }
            }
        })


        viewModel.questionOfVisitRes.observe(this, Observer { resources ->
            resources ?: return@Observer
            when (resources.status) {
                Status.SUCCESS -> {
                    loadingDialog.setLoading(false)
                    if (resources.data?.count == 0) {
                        shortToast(getString(R.string.no_question_found_toast))
                    } else {
                        questionItem = resources.data?.listing?.get(0) ?: QuestionOfVisitItem()
                        questionOfVisitDialog(resources.data?.listing ?: ArrayList())
                    }
                }
                Status.ERROR -> {
                    loadingDialog.setLoading(false)
                    handleError(resources.error)
                }
                Status.LOADING -> {
                    loadingDialog.setLoading(true)
                }
            }
        })

        viewModel.submitReasonRes.observe(this, Observer { resources ->
            resources ?: return@Observer
            when (resources.status) {
                Status.SUCCESS -> {
                    loadingDialog.setLoading(false)
                }
                Status.ERROR -> {
                    loadingDialog.setLoading(false)
                    handleError(resources.error)
                }
                Status.LOADING -> {
                    loadingDialog.setLoading(true)
                }
            }
        })


        viewModel.visitorPopUpRes.observe(this, Observer { resources ->
            resources ?: return@Observer
            when (resources.status) {
                Status.SUCCESS -> {
                    loadingDialog.setLoading(false)
                    when (resources.data?.order) {
                        1 -> dialogMeetOthers(true)
                        4 -> dialogMeetOthers(false)
                        5 -> dialogAfterVisit()
                    }
                }
                Status.ERROR -> {
                    loadingDialog.setLoading(false)
                    handleError(resources.error)
                }
                Status.LOADING -> {
                    loadingDialog.setLoading(true)
                }
            }
        })



        removeViewModel.addRemoveServiceRes.observe(this, Observer { resources ->
            resources ?: return@Observer
            when (resources.status) {
                Status.SUCCESS -> {
                    loadingDialog.setLoading(false)
                    if (resources.data?.isAdded == true) {
                        shortToast(getString(R.string.service_added_to_account_toast))
                    } else {
                        shortToast(getString(R.string.service_remove_success_toast))
                    }
                    finish()

                }
                Status.ERROR -> {
                    loadingDialog.setLoading(false)
                    handleError(resources.error)
                }
                Status.LOADING -> {
                    loadingDialog.setLoading(true)
                }
            }
        })


    }


    private fun dialogToAddRemoveService(isAdded: Boolean) {
        var dialog: AlertDialog? = null
        val layout = View.inflate(this, R.layout.dialog_delete, null)


        if (isAdded) {
            layout.ivDeleteDialogIcon.setImageResource(R.drawable.ic_add_service)
            layout.tvDeleteDialogHeading.text =
                getString(R.string.add_service_to_account_sub_heading)
        } else {
            layout.ivDeleteDialogIcon.setImageResource(R.drawable.ic_delete_big)
            layout.tvDeleteDialogHeading.text =
                getString(R.string.delete_service_from_account_sub_heading)
        }

        layout.btnDeleteDialogRemove.text = getString(R.string.yes_label_add_service_dialog)

        layout.btnDeleteDialogCancel.setOnClickListener {
            dialog?.dismiss()
        }

        layout.btnDeleteDialogRemove.setOnClickListener {
            if (isNetworkActive())
                if (isAdded) {
                    removeViewModel.addRemoveService(
                        serviceData.id ?: "",
                        AppConstants.TYPE_ADD_SERVICE
                    )
                } else {
                    removeViewModel.addRemoveService(
                        serviceData.id ?: "",
                        AppConstants.TYPE_REMOVE_SERVICE
                    )
                }
            dialog?.dismiss()
        }

        val builder = AlertDialog.Builder(this)
        builder.setView(layout)
        builder.setCancelable(false)
        dialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
    }


    private fun happenOnVisitBottomSheet(reasonList: ArrayList<HappenOnVisitItem>) {
        val dialog = BottomSheetDialog(this, R.style.AppBottomSheetDialogTheme)
        dialog.setContentView(R.layout.bottom_sheet_happened_on_visit)

        dialog.rvHappenedOnVisit?.layoutManager =
            LinearLayoutManager(this@ServiceDetailsActivity, RecyclerView.VERTICAL, false)
        dialog.rvHappenedOnVisit?.adapter = HappenOnVisitAdapter(reasonList, this)
        dialog.btnHappenedOnVisitContinue?.setOnClickListener {
            nextStepHappenOnVisit()
            dialog.dismiss()
        }

        dialog.setCanceledOnTouchOutside(true)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setGravity(Gravity.BOTTOM)
        dialog.show()
    }

    private fun nextStepHappenOnVisit() {
        when (happenItem?.order) {

            1 -> {
                // Address not located
                visitorPopUpApi(1)
            }
            2, 3 -> {
                if (isNetworkActive())
                    viewModel.questionsOfVisit(happenItem?.id ?: "", happenItem?.order ?: 2)
            }
            4 -> {
                // con vecino
                visitorPopUpApi(4)
            }
            5 -> {
                // bajo pureta
                visitorPopUpApi(5)
            }
        }
    }

    private fun visitorPopUpApi(orderNo: Int) {
        val request = VisitorWithPopUpRequest(
            serviceId = serviceData.id,
            order = orderNo
        )

        if (isNetworkActive())
            viewModel.visitorPopUp(request)
    }

    private fun questionOfVisitDialog(questionList: ArrayList<QuestionOfVisitItem>) {
        val dialog = Dialog(this, R.style.FullWidthDialog)
        dialog.setContentView(R.layout.dialog_result_got)
        dialog.rvResultGot?.layoutManager =
            LinearLayoutManager(this@ServiceDetailsActivity, RecyclerView.VERTICAL, false)
        dialog.rvResultGot?.adapter = QuestionOfVisitAdapter(questionList, this)

        dialog.btnResultGotContinue?.setOnClickListener {
            paymentNotificationReq.questionEn = questionItem?.questionEn
            paymentNotificationReq.questionSp = questionItem?.questionSp
            paymentNotificationReq.serviceId = serviceData.id

            openRegisterVisitFragment()
            dialog.dismiss()
        }

        dialog.setCanceledOnTouchOutside(true)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
    }

    private fun openRegisterVisitFragment() {
        val fragment = RegisterVisitFragment()

        val bundle = Bundle()
        bundle.putParcelable(AppConstants.FINALIZE_PAYMENT_REQUEST, paymentNotificationReq)
        fragment.arguments = bundle

        fragment.setListener(this)
        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, fragment, RegisterVisitFragment::class.java.name)
            .addToBackStack(RegisterVisitFragment.TAG)
            .commitAllowingStateLoss()
    }


    private fun reasonForServiceBottomSheet(reasonList: ArrayList<ReasonForServiceItem>) {
        val dialog = BottomSheetDialog(this, R.style.AppBottomSheetDialogTheme)
        dialog.setContentView(R.layout.bottom_sheet_happened_on_visit)


        dialog.rvHappenedOnVisit?.layoutManager =
            LinearLayoutManager(this@ServiceDetailsActivity, RecyclerView.VERTICAL, false)
        dialog.rvHappenedOnVisit?.adapter = ReasonForServiceAdapter(reasonList, this)

        dialog.btnHappenedOnVisitContinue?.setOnClickListener {
            submitReasonApiHit()
            dialog.dismiss()
        }

        dialog.setCanceledOnTouchOutside(true)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setGravity(Gravity.BOTTOM)
        dialog.show()
    }

    private fun submitReasonApiHit() {
        val req = SubmitReasonRequest(
            serviceId = serviceData.id,
            questionEn = reasonItem?.questionEn,
            questionSp = reasonItem?.questionSp
        )
        if (isNetworkActive())
            viewModel.submitReason(req)
    }

    private fun dialogAfterVisit() {
        var dialog: AlertDialog? = null
        val layout = View.inflate(this, R.layout.dailog_excellent_work, null)

        layout.btnExcellentDialog.setOnClickListener {
            dialog?.dismiss()
            finish()
        }

        val builder = AlertDialog.Builder(this)
        builder.setView(layout)
        builder.setCancelable(false)
        dialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
    }


    private fun dialogMeetOthers(isNoAddress: Boolean) {
        var dialog: AlertDialog? = null
        val layout = View.inflate(this, R.layout.dialog_meet_others, null)

        if (isNoAddress) {
            layout.tvMeetOtherHeading.text = getString(R.string.not_located_heading)
            layout.tvMeetOtherSubHeading.text = getString(R.string.not_located_sub_heading)
        }


        layout.btnMeetOtherContinue.setOnClickListener {
            dialog?.dismiss()
            finish()
        }

        val builder = AlertDialog.Builder(this)
        builder.setView(layout)
        builder.setCancelable(false)
        dialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
    }

}