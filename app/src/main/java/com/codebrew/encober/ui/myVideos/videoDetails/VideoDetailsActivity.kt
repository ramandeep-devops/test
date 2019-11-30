package com.codebrew.encober.ui.myVideos.videoDetails

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.codebrew.encober.R
import com.codebrew.encober.models.responseModel.videosData.VideoListingItem
import com.codebrew.encober.utils.AppConstants
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.layout_video_details.*


class VideoDetailsActivity : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener {

    companion object {
        private const val RECOVERY_DIALOG_REQUEST = 1
    }

    private lateinit var videoItem: VideoListingItem



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_video_details)

        ivVideoDetailMain.initialize(getString(R.string.google_api_key), this)

        videoItem = intent?.getParcelableExtra(AppConstants.MY_VIDEO_ITEM) ?: VideoListingItem()
        setViews(videoItem)

        setListeners()
    }


    private fun setViews(data: VideoListingItem) {
        tvVideoDetailHeading.text = data.videoName
        tvVideoDetailDescription.text = data.description
    }

    private fun setListeners() {
        ivVideoDetailBack.setOnClickListener {
            finish()
        }
    }

    override fun onInitializationSuccess(p0: YouTubePlayer.Provider?, player: YouTubePlayer?, wasRestored: Boolean) {
        if (!wasRestored) {
            player?.loadVideo(videoItem.videoUrl)
        }
    }

    override fun onInitializationFailure(
        p0: YouTubePlayer.Provider?,
        errorReason: YouTubeInitializationResult?
    ) {

        if (errorReason?.isUserRecoverableError == true) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show()
        } else {
            val errorMessage =
                String.format(getString(R.string.error_player), errorReason.toString())
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            ivVideoDetailMain.initialize(getString(R.string.google_api_key), this)
        }
    }

}