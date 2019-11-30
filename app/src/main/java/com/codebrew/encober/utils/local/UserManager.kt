package com.codebrew.encober.utils.local

import com.codebrew.encober.models.responseModel.UserProfileData
import com.google.android.gms.maps.model.LatLng

object UserManager {

    //Save Data
    fun saveDeviceToken(token: String) {
        PrefsManager.get().save(PrefsManager.PREF_DEVICE_TOKEN, token)
    }


    fun saveAccessToken(token: String) {
        PrefsManager.get().save(PrefsManager.PREF_ACCESS_TOKEN, token)
    }

    fun saveUserProfile(data: UserProfileData) {
        PrefsManager.get().save(PrefsManager.PREF_USER_PROFILE, data)
    }

    fun saveUserLatLng(lat: Double, long: Double) {
        PrefsManager.get().save(PrefsManager.LATITUDE, lat.toString())
        PrefsManager.get().save(PrefsManager.LONGITUDE, long.toString())
    }


    // Get Data

    fun getAccessToken(): String {
        return PrefsManager.get().getString(PrefsManager.PREF_ACCESS_TOKEN, "")
    }


    fun getDeviceToken(): String {
        return PrefsManager.get().getString(PrefsManager.PREF_DEVICE_TOKEN, "")
    }

    fun getUserProfile(): UserProfileData? {
        return PrefsManager.get()
            .getObject(PrefsManager.PREF_USER_PROFILE, UserProfileData::class.java)
    }

    fun getUserLatLng(): LatLng {
        val lat = PrefsManager.get().getString(PrefsManager.LATITUDE, "40.415363").toDouble()
        val lng = PrefsManager.get().getString(PrefsManager.LONGITUDE, "-3.707398").toDouble()
        return LatLng(lat, lng)
    }

    fun logoutUser() {
        PrefsManager.get().removeAll()
    }


    // Remove Data
    fun removeProfile() {
        PrefsManager.get().remove(PrefsManager.PREF_ACCESS_TOKEN)
        PrefsManager.get().remove(PrefsManager.PREF_USER_PROFILE)
    }

}