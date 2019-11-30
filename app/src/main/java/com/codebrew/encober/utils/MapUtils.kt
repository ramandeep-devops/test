package com.codebrew.encober.utils

import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import java.util.*
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin


object MapUtils {


    fun distanceBetweenLatLong(start: LatLng, destination: LatLng): Double {
        val theta = start.longitude - destination.longitude
        var dist = sin(deg2rad(start.latitude))*
                sin(deg2rad(destination.latitude)) +
                (cos(deg2rad(start.latitude))
                * cos(deg2rad(destination.latitude))
                * cos(deg2rad(theta)))
        dist = acos(dist)
        dist = rad2deg(dist)
        dist *= 60.0 * 1.1515
        return dist
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }



    fun getAddressFromLatLong(latLong: LatLng,context: Context): Address {
        var address : Address? = null
        try {
            val geoCoder = Geocoder(context, Locale.getDefault())
            val addresses = geoCoder.getFromLocation(latLong.latitude, latLong.longitude, 1)
            address = addresses[0]
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return address!!
    }

    fun getVisibleRegionRadius(gMap : GoogleMap): Double {
        val bounds = gMap.projection.visibleRegion.latLngBounds
        val northeast = bounds.northeast
        val southwest = bounds.southwest
        return distanceBetweenLatLong(northeast,southwest)

    }



    // Enter your project googleMap key here
    fun getGoogleStaticMapUrl(latitude: String, longitude: String): String
            = "https://maps.googleapis.com/maps/api/staticmap?center=$latitude,$longitude&zoom=8&size=300x300&scale=2&markers=color:yellow%7C$latitude,$longitude&key=AIzaSyAnE6Xv13FD1jANdHGOL5xzp04at6D3RMw"


    fun openGoogleMaps(context: Context, latitude: String, longitude: String)
    {
        val intentUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=$latitude,$longitude")
        val mapIntent = Intent(Intent.ACTION_VIEW, intentUri)

        // Open in google maps app if available
        mapIntent.`package` = "com.google.android.apps.maps"
        if (mapIntent.resolveActivity(context.packageManager) != null)
        {
            context.startActivity(mapIntent)
        }
        else
        {
            // If google maps is not available, then open in browser
            mapIntent.`package` = null
            if (mapIntent.resolveActivity(context.packageManager) != null)
            {
                context.startActivity(mapIntent)
            }
        }
    }



}