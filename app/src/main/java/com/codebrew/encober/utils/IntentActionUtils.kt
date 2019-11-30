package com.codebrew.encober.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import com.codebrew.encober.R
import android.content.ActivityNotFoundException





object IntentActionUtils {
    fun dialPhone(context: Context, phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null))
        context.startActivity(intent)
    }

    fun sendEmail(context: Context, email: String) {
        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.data = Uri.parse("mailto:$email")
        context.startActivity(Intent.createChooser(emailIntent, context.getString(R.string.send_via)))
    }

    fun sendWhatsapp(context: Context, phoneNumber: String) {
        val installed = isWhatsappInstalled(context)

        if (installed) {
            val uri = Uri.parse("smsto:$phoneNumber")
            val i = Intent(Intent.ACTION_SENDTO, uri)
            i.putExtra("sms_body", "Hi")
            i.setPackage("com.whatsapp")
            context.startActivity(i)
        }else{
            val webIntent = Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=com.whatsapp"))
            context.startActivity(webIntent)
        }
    }

    fun openUrl(context: Context, url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }


    private fun isWhatsappInstalled(context: Context): Boolean {
        val pm = context.packageManager
        return try {
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    fun openYoutubeVideo(context: Context, url: String) {
        val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$url"))
        val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=$url"))

        try {
            context.startActivity(appIntent)
        } catch (ex: ActivityNotFoundException) {
            context.startActivity(webIntent)
        }

    }
}