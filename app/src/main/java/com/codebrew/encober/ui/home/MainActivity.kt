package com.codebrew.encober.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.*
import android.location.Location
import android.os.Bundle
import android.os.IBinder
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.codebrew.encober.R
import com.codebrew.encober.ui.base.BaseActivity
import com.codebrew.encober.ui.myVideos.MyVideosFragment
import com.codebrew.encober.ui.notifications.NotificationsFragment
import com.codebrew.encober.ui.profile.ProfileFragment
import com.codebrew.encober.ui.home.start.StartFragment
import com.codebrew.encober.utils.CommonPagerAdapter
import com.codebrew.encober.utils.local.UserManager
import com.codebrew.encober.utils.location.GpsUtils
import com.codebrew.encober.utils.location.LocationUpdatesService
import com.codebrew.encober.utils.PermissionUtils
import com.codebrew.encober.utils.extensions.shortToast
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import permissions.dispatcher.*





@RuntimePermissions
class MainActivity : BaseActivity() {


    // The BroadcastReceiver used to listen from broadcasts from the service.
    private var myReceiver: MyReceiver? = null

    // A reference to the service used to get location updates.
    private var mService: LocationUpdatesService? = null

    // Tracks the bound state of the service.
    private var mBound = false

    private inner class MyReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val location =
                intent.getParcelableExtra<Location>(LocationUpdatesService.EXTRA_LOCATION)
            if (location != null) {
                UserManager.saveUserLatLng(location.latitude, location.longitude)
            } else {
                shortToast("location is null")
            }
        }
    }

    // Monitors the state of the connection to the service.
    private val mServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as LocationUpdatesService.LocalBinder
            mService = binder.service
            mBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            mService = null
            mBound = false
        }
    }


    override fun getLayoutResourceId(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        myReceiver = MyReceiver()
        onLocationAccessWithPermissionCheck()

        setupBottomTabs()
    }

    private fun setupBottomTabs() {

        val list = ArrayList<Fragment>()

        list.add(StartFragment())
        list.add(MyVideosFragment())
        list.add(NotificationsFragment())
        list.add(ProfileFragment())

        val pagerAdapter = CommonPagerAdapter(supportFragmentManager,list)
        viewPagerMain.adapter = pagerAdapter

        tabLayoutMain.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPagerMain.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })
        viewPagerMain.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayoutMain))

    }

    @SuppressLint("NeedOnRequestPermissionsResult")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }


    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun onLocationAccess() {
        mService?.requestLocationUpdates()
        GpsUtils(this).turnGPSOn {
        }


    }

    @OnShowRationale(Manifest.permission.ACCESS_FINE_LOCATION)
    fun showLocationRationale(request: PermissionRequest) {
        request.proceed()
    }

    @OnNeverAskAgain(Manifest.permission.ACCESS_FINE_LOCATION)
    fun onNeverAskAgainRationale() {
        PermissionUtils.showAppSettingsDialog(
            this,
            R.string.location_permission
        )
    }


    override fun onStart() {
        super.onStart()
        // Bind to the service. If the service is in foreground mode, this signals to the service
        // that since this activity is in the foreground, the service can exit foreground mode.
        bindService(
            Intent(this, LocationUpdatesService::class.java), mServiceConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver(
            myReceiver as BroadcastReceiver,
            IntentFilter(LocationUpdatesService.ACTION_BROADCAST)
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 10001) {
                mService?.requestLocationUpdates()
            }
        }
    }

    override fun onDestroy() {
        unbindService(mServiceConnection)
        mService?.removeLocationUpdates()
        super.onDestroy()
    }
}