package com.codebrew.encober.ui.myServices


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codebrew.encober.R
import com.codebrew.encober.models.responseModel.myServices.MyServiceDataItem
import com.codebrew.encober.network.common.Status
import com.codebrew.encober.ui.base.BaseActivity
import com.codebrew.encober.ui.common.custom.LoadingDialog
import com.codebrew.encober.ui.home.start.AddRemoveServiceViewModel
import com.codebrew.encober.ui.serviceDetails.ServiceDetailsActivity
import com.codebrew.encober.utils.AppConstants
import com.codebrew.encober.utils.extensions.handleError
import com.codebrew.encober.utils.extensions.isNetworkActive
import com.codebrew.encober.utils.extensions.shortToast
import kotlinx.android.synthetic.main.dialog_delete.view.*
import kotlinx.android.synthetic.main.layout_my_services.*

class MyServicesActivity : BaseActivity(), OnClickMyServiceItemCallback {
    override fun onClickMyServiceItem(data: MyServiceDataItem, type: String) {
        if (type == AppConstants.TYPE_FOR_DELETE) {
            dialogToRemoveService(data.myServiceData?.id ?: "")
        } else {
            val i1 = Intent(this,
                ServiceDetailsActivity::class.java)
            i1.putExtra(AppConstants.MAP_DATA_ITEM_ID, data.myServiceData?.id ?: "")
            startActivity(i1)

        }
    }

    override fun getLayoutResourceId(): Int = R.layout.layout_my_services

    private val adapter = MyServiceAdapter(this)
    private var type: Int = AppConstants.HOME_RADIO_COLLECTIONS
    private val viewModel by lazy { ViewModelProvider(this).get(MyServicesViewModel::class.java) }
    private val removeServiceViewModel by lazy {
        ViewModelProvider(this).get(
            AddRemoveServiceViewModel::class.java
        )
    }
    private val loadingDialog by lazy { LoadingDialog(this) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        listingApiHit()
        adapterSetup()
        setListeners()
        observeChanges()
    }


    private fun listingApiHit() {
        if (isNetworkActive()) {
            viewModel.getMyServices(type)
        }
    }

    private fun adapterSetup() {
        rvMyServices.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvMyServices.adapter = adapter
    }


    private fun setListeners() {
        tbrMyServices.setNavigationOnClickListener {
            finish()
        }

        srlMyServices.setOnRefreshListener {
            listingApiHit()
        }

        radioGroupMyServices.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                radioBtnCollection.id -> {
                    type = AppConstants.HOME_RADIO_COLLECTIONS
                }
                radioBtnSurveys.id -> {
                    type = AppConstants.HOME_RADIO_SURVEYS
                }
                radioBtnRecover.id -> {
                    type = AppConstants.HOME_RADIO_RECOVER
                }
            }
            listingApiHit()

        }
    }

    private fun observeChanges() {

        viewModel.servicesRes.observe(this, Observer { resources ->
            resources ?: return@Observer
            when (resources.status) {
                Status.SUCCESS -> {
                    if (resources.data?.size != 0) {
                        adapter.setData(resources.data ?: ArrayList())
                        vfMyServices.displayedChild = 2
                    } else {
                        vfMyServices.displayedChild = 1
                    }
                }
                Status.ERROR -> {
                    vfMyServices.displayedChild = 1
                    handleError(resources.error)
                }
                Status.LOADING -> {
                    srlMyServices.isRefreshing = false
                    vfMyServices.displayedChild = 0
                }
            }
        })


        removeServiceViewModel.addRemoveServiceRes.observe(this, Observer { resources ->
            resources ?: return@Observer
            when (resources.status) {
                Status.SUCCESS -> {
                    loadingDialog.setLoading(false)
                    shortToast(getString(R.string.service_successfully_removed_toast))
                    listingApiHit()
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


    private fun dialogToRemoveService(serviceId: String) {
        var dialog: AlertDialog? = null
        val layout = View.inflate(this, R.layout.dialog_delete, null)

        layout.tvDeleteDialogHeading.text =
            getString(R.string.delete_service_from_account_sub_heading)
        layout.btnDeleteDialogRemove.text = getString(R.string.yes_label_add_service_dialog)

        layout.btnDeleteDialogCancel.setOnClickListener {
            dialog?.dismiss()
        }

        layout.btnDeleteDialogRemove.setOnClickListener {
            removeServiceViewModel.addRemoveService(serviceId, AppConstants.TYPE_REMOVE_SERVICE)
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

    override fun onResume() {
        super.onResume()
        if (isNetworkActive()) {
            viewModel.getMyServices(type)
        }
    }
}