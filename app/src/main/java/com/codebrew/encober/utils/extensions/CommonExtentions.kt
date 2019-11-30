package com.codebrew.encober.utils.extensions

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.codebrew.encober.R
import com.codebrew.encober.utils.local.PrefsManager


private const val SECOND_MILLIS = 1000
private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
private const val DAY_MILLIS = 24 * HOUR_MILLIS




fun Long.timeAgo(): String? = getTimeAgo(this)

fun getTimeAgo(timeF: Long): String? {
    var time = timeF
    if (time < 1000000000000L) {
        time *= 1000
    }

    val now = System.currentTimeMillis()
    if (time > now || time <= 0) {
        return null
    }
    val diff = now - time
    return when {
        diff < MINUTE_MILLIS -> "just now"
        diff < 2 * MINUTE_MILLIS -> "a minute ago"
        diff < 50 * MINUTE_MILLIS -> "${diff / MINUTE_MILLIS} minutes ago"
        diff < 90 * MINUTE_MILLIS -> "an hour ago"
        diff < 24 * HOUR_MILLIS -> "${diff / HOUR_MILLIS} hours ago"
        diff < 48 * HOUR_MILLIS -> "yesterday"
        else -> "${diff / DAY_MILLIS} days ago"
    }
}

fun getAccessToken(): String {
    val accessToken = PrefsManager.get().getString(PrefsManager.PREF_ACCESS_TOKEN, "")
    return if (accessToken.isBlank()) {
        "Bearer"
    } else {
        "Bearer $accessToken"
    }
}

fun loadImage(
    context: Context?,
    ivUser: ImageView,
    thumbnailImage: String?,
    originalImage: String?
) {
    Glide.with(context as Context).load(originalImage).diskCacheStrategy(DiskCacheStrategy.ALL)
        .centerCrop()
        .placeholder(R.color.colorPrimaryDark)
        .thumbnail(Glide.with(context).load(thumbnailImage).override(100, 100)).into(ivUser)
}

