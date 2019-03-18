package app.com.android.ihsteachers.main.activity

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import app.com.android.ihsteachers.R
import android.widget.MediaController
import android.widget.ProgressBar
import android.widget.VideoView



class VideoViewActivity : AppCompatActivity() {

    var progressbar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_view)

        val videoView: VideoView = findViewById(R.id.videoViewOnline)
        progressbar = findViewById(R.id.progressBarVideoLoad)

        val bundle = intent.extras
        if(bundle!=null)
        {
            val vidUrl = bundle.getString("VIDEO_URL")

            val mc = MediaController(this)
            mc.setAnchorView(videoView)
            mc.setMediaPlayer(videoView)
            val video = Uri.parse(vidUrl)
            videoView.setMediaController(mc)
            videoView.setVideoURI(video)
            videoView.requestFocus()
            videoView.setOnPreparedListener {
                progressbar!!.visibility = View.GONE
                videoView.start()
            }

        }

    }
}
