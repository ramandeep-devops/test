package com.codebrew.encober.ui.home.start

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.LocationManager
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codebrew.encober.R
import com.codebrew.encober.models.responseModel.UserProfileData
import com.codebrew.encober.models.responseModel.start.MapData
import com.codebrew.encober.models.responseModel.start.ServiceListItem
import com.codebrew.encober.network.common.Status
import com.codebrew.encober.ui.common.custom.LoadingDialog
import com.codebrew.encober.ui.myServices.MyServicesActivity
import com.codebrew.encober.ui.serviceDetails.ServiceDetailsActivity
import com.codebrew.encober.utils.AppConstants
import com.codebrew.encober.utils.extensions.*
import com.codebrew.encober.utils.local.UserManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.dialog_delete.view.*
import kotlinx.android.synthetic.main.fragment_main_start.*
import kotlinx.android.synthetic.main.item_home_collection.view.*
import kotlinx.android.synthetic.main.marker_dollar_start.view.*


class StartFragment : Fragment(), OnMapReadyCallback, OnClickMapDataListCallback {
    override fun onClickMapListItem(data: ServiceListItem, type: String) {
        if (type == AppConstants.TYPE_FOR_ADD_SERVICE) {
            dialogToAddService(data)
        } else {
            openTaskDetails(data.id ?: "")
        }
    }

    companion object {
        const val TAG = "com.codebrew.encober.ui.main.start.StartFragment"
    }

    private val viewModel by lazy { ViewModelProvider(this).get(StartViewModel::class.java) }
    private val addServiceViewModel by lazy { ViewModelProvider(this).get(AddRemoveServiceViewModel::class.java) }
    private val loadingDialog by lazy { LoadingDialog(requireActivity()) }
    private var locationManager: LocationManager? = null
    private lateinit var gMap: GoogleMap
    private val userLoc: LatLng = UserManager.getUserLatLng()
    private var center: LatLng = userLoc
    private var searchType: Int = AppConstants.HOME_RADIO_COLLECTIONS
    private var mapData: MapData? = null
    private val mapDataAdapter = MapDataListAdapter(this)
    private var bottomDialog: AlertDialog? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_main_start, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapViewStartFragment.onCreate(savedInstanceState)
        mapViewStartFragment.getMapAsync(this)

        init()
        observeChanges()
        setListeners()
    }

    private fun init() {

        val userData = UserManager.getUserProfile()
        switchStartTop.isChecked = userData?.isActive == true
        setSwitchData(userData?.isActive ?: false)

        rvMapDataList.layoutManager =
            LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
        rvMapDataList.adapter = mapDataAdapter

    }

    private fun setListeners() {

        switchStartTop.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.activeInactiveAccount(true)
                ivStartListIcon.setImageResource(R.drawable.ic_map_list_blue)
                ivStartMapIcon.setImageResource(R.drawable.ic_map_white)
            } else {
                viewModel.activeInactiveAccount(false)
            }
        }

        ivStartListIcon.setOnClickListener {
            srlMapDataList.visible()
            mapViewStartFragment.gone()
            ivStartListIcon.setImageResource(R.drawable.ic_map_list_white)
            ivStartMapIcon.setImageResource(R.drawable.ic_map_blue)

        }

        btnStartContinue.setOnClickListener {
            startActivity(Intent(requireActivity(), MyServicesActivity::class.java))
        }

        srlMapDataList.setOnRefreshListener {
            mapApiHit()
        }

        ivStartMapIcon.setOnClickListener {
            srlMapDataList.gone()
            mapViewStartFragment.visible()
            ivStartListIcon.setImageResource(R.drawable.ic_map_list_blue)
            ivStartMapIcon.setImageResource(R.drawable.ic_map_white)
        }

        radioGroupTopStart.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                radioBtnStartCollection.id -> {
                    searchType = AppConstants.HOME_RADIO_COLLECTIONS
                }
                radioBtnStartSurveys.id -> {
                    searchType = AppConstants.HOME_RADIO_SURVEYS
                }
                radioBtnStartRecover.id -> {
                    searchType = AppConstants.HOME_RADIO_RECOVER
                }
            }
            mapApiHit()
        }
    }


    private fun observeChanges() {
        viewModel.activeInactiveRes.observe(viewLifecycleOwner, Observer { resources ->
            resources ?: return@Observer
            when (resources.status) {
                Status.SUCCESS -> {
                    loadingDialog.setLoading(false)
                    UserManager.saveUserProfile(resources.data ?: UserProfileData())
                    setSwitchData(resources.data?.isActive ?: false)
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


        viewModel.mapServiceDataRes.observe(viewLifecycleOwner, Observer { resources ->
            resources ?: return@Observer
            when (resources.status) {
                Status.SUCCESS -> {
                    loadingDialog.setLoading(false)
                    mapData = resources.data ?: MapData()
                    if (resources.data?.listing?.size != 0) {
                        mapDataAdapter.setData(resources.data?.listing ?: ArrayList())
                        vfMapDataList.displayedChild = 2
                    } else {
                        vfMapDataList.displayedChild = 1
                    }
                    setMarkers(resources.data ?: MapData())
                }
                Status.ERROR -> {
                    loadingDialog.setLoading(false)
                    vfMapDataList.displayedChild = 1
                    requireActivity().handleError(resources.error)
                }
                Status.LOADING -> {
                    srlMapDataList.isRefreshing = false
                    loadingDialog.setLoading(true)
                }
            }
        })

        addServiceViewModel.addRemoveServiceRes.observe(viewLifecycleOwner, Observer { resources ->
            resources ?: return@Observer
            when (resources.status) {
                Status.SUCCESS -> {
                    loadingDialog.setLoading(false)
                    requireActivity().longToast(getString(R.string.service_added_to_account_toast))
                    mapApiHit()
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

    private fun setSwitchData(isActive: Boolean) {
        if (isActive) {
            tvStartInactiveState.invisible()
            srlMapDataList.invisible()
            mapViewStartFragment.visible()
            radioGroupTopStart.visible()
            rlStartMapMenu.visible()
        } else {
            tvStartInactiveState.visible()
            srlMapDataList.invisible()
            mapViewStartFragment.invisible()
            radioGroupTopStart.invisible()
            rlStartMapMenu.invisible()
        }
    }

    override fun onMapReady(it: GoogleMap?) {
        if (it != null) {
            gMap = it
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 14.0f))
        }

        locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        clearMapAndSetCurrentLoc()
        mapApiHit()

        gMap.setOnMarkerClickListener { marker: Marker ->
            val markerData = mapData?.listing?.find { it.id == marker.snippet }
            if (markerData != null) {
                showDetailsBottomSheet(markerData)
            } else {
                requireContext().longToast(getString(R.string.your_location_toast))
            }
            true
        }
    }

    private fun mapApiHit() {
        val map = HashMap<String, Any>()
        map["lat"] = center.latitude
        map["lng"] = center.longitude
        map["type"] = searchType

        if (requireActivity().isNetworkActive())
            viewModel.getMapServiceData(map)
    }


    private fun setMarkers(obj: MapData) {
        clearMapAndSetCurrentLoc()
        val data = obj.listing
        data?.forEach {
            try {
                val latLng = LatLng(it.lat!!, it.lng!!)
                val commission = (it.servicePrice?.times(it.commissionPer!!))?.div(100)
                gMap.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .title(it.servicePrice.toString())
                        .snippet(it.id)
                        .icon(BitmapDescriptorFactory.fromBitmap(convertViewToDrawable(commission.toString())))
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }


    private fun clearMapAndSetCurrentLoc() {
        gMap.clear()
        gMap.addMarker(MarkerOptions().position(userLoc).title(getString(R.string.your_location_toast)))
            ?.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_user_marker))
    }


    private fun convertViewToDrawable(price: String): Bitmap {
        val view = View.inflate(context, R.layout.marker_dollar_start, null)
        view.tvMarkerPrice.text = "$ $price"
        when (searchType) {
            AppConstants.HOME_RADIO_COLLECTIONS -> {
                view.ivMarkerIcon.setImageResource(R.drawable.ic_dollar_marker_yellow)
            }
            AppConstants.HOME_RADIO_SURVEYS -> {
                view.ivMarkerIcon.setImageResource(R.drawable.ic_file_yellow)

            }
            AppConstants.HOME_RADIO_RECOVER -> {
                view.ivMarkerIcon.setImageResource(R.drawable.ic_screen)

            }
        }
        val spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        view.measure(spec, spec)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        val b = Bitmap.createBitmap(
            view.measuredWidth, view.measuredHeight,
            Bitmap.Config.ARGB_8888
        )
        val c = Canvas(b)
        c.translate((-view.scrollX).toFloat(), (-view.scrollY).toFloat())
        view.draw(c)
        return b
    }


    private fun showDetailsBottomSheet(obj: ServiceListItem) {
        val alertLayout = View.inflate(context, R.layout.item_home_collection, null)


        alertLayout.tvItemCollectionTitle?.text = obj.title
        alertLayout.tvItemCollectionName?.text = obj.serviceName
        val commission = (obj.servicePrice?.times(obj.commissionPer!!))?.div(100).toString()
        alertLayout.btnItemCollectionCommission.text = "Commission $$commission"
        alertLayout.tvItemCollectionAddress?.text = obj.address

        alertLayout.ivItemCollectionUserIcon?.setOnClickListener {
            dialogToAddService(obj)
        }

        alertLayout.btnItemCollectionCommission.setOnClickListener {
            openTaskDetails(obj.id ?: "")
            bottomDialog?.dismiss()
        }

        val slideDown = AnimationUtils.loadAnimation(activity, R.anim.slide_up_in)
        alertLayout.startAnimation(slideDown)

        val builder = AlertDialog.Builder(context as Context, R.style.FullWidthDialog)
        builder.setView(alertLayout)
        builder.setCancelable(false)
        bottomDialog = builder.create()
        bottomDialog?.setCanceledOnTouchOutside(true)
        bottomDialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        bottomDialog?.window?.setGravity(Gravity.BOTTOM)
        if (activity?.isFinishing == false)
            bottomDialog?.show()
    }

    private fun dialogToAddService(data: ServiceListItem) {
        var dialog: AlertDialog? = null
        val layout = View.inflate(context, R.layout.dialog_delete, null)

        layout.ivDeleteDialogIcon.setImageResource(R.drawable.ic_add_service)
        layout.tvDeleteDialogHeading.text = getString(R.string.add_service_to_account_sub_heading)
        layout.btnDeleteDialogRemove.text = getString(R.string.yes_label_add_service_dialog)


        layout.btnDeleteDialogCancel.setOnClickListener {
            dialog?.dismiss()
        }

        layout.btnDeleteDialogRemove.setOnClickListener {
            addServiceViewModel.addRemoveService(data.id ?: "", AppConstants.TYPE_ADD_SERVICE)
            dialog?.dismiss()
            if (bottomDialog?.isShowing == true)
                bottomDialog?.dismiss()
        }

        val builder = AlertDialog.Builder(context as Context)
        builder.setView(layout)
        builder.setCancelable(false)
        dialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        if (activity?.isFinishing == false)
            dialog.show()
    }

    private fun openTaskDetails(id: String) {
        val i1 = Intent(
            requireActivity(),
            ServiceDetailsActivity::class.java
        )
        i1.putExtra(AppConstants.MAP_DATA_ITEM_ID, id)
        startActivity(i1)

    }


    override fun onResume() {
        super.onResume()
        mapViewStartFragment.onResume()
        mapApiHit()
    }

    override fun onPause() {
        super.onPause()
        mapViewStartFragment.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapViewStartFragment.onLowMemory()
    }


    override fun onDestroy() {
        super.onDestroy()
        if (mapViewStartFragment != null)
            mapViewStartFragment.onDestroy()
    }
}