package com.breezefieldnationalplastic.features.mylearning

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.breezefieldnationalplastic.R
import com.breezefieldnationalplastic.base.presentation.BaseFragment
import com.downloader.Progress
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.pnikosis.materialishprogress.ProgressWheel

class BookmarkPlayFrag: BaseFragment() {
    private lateinit var mContext: Context
    private lateinit var progress_wheel:ProgressWheel
    private lateinit var style_player_bookmark: StyledPlayerView
    lateinit var exoPlayer: ExoPlayer
    lateinit var mediaSource: MediaSource

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    companion object{
        var play_url:String=""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.frag_bookmark_play, container, false)
        initView(view)
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        return view
    }


    private fun initView(view: View) {
        style_player_bookmark = view.findViewById(R.id.style_player_bookmark)
        progress_wheel = view.findViewById(R.id.progress_wheel_bookmark_play)
        progress_wheel.stopSpinning()
        playVid()
    }

    fun playVid(){
        exoPlayer = ExoPlayer.Builder(mContext)
            .setRenderersFactory(
                DefaultRenderersFactory(mContext).setEnableDecoderFallback(
                    true
                )
            ).setSeekForwardIncrementMs(10000L)
            .setSeekBackIncrementMs(10000L).build()

        exoPlayer.seekTo(0)
        exoPlayer.repeatMode = Player.REPEAT_MODE_OFF

        val dataSourceFactory = DefaultDataSource.Factory(mContext)

        mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(Uri.parse(play_url)))
        exoPlayer.setMediaSource(mediaSource)
        exoPlayer.prepare()

        exoPlayer.playWhenReady = true
        exoPlayer.play()

        style_player_bookmark.visibility = View.VISIBLE
        style_player_bookmark.player = exoPlayer

        exoPlayer.addListener(object :Player.Listener{
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                super.onPlayerStateChanged(playWhenReady, playbackState)
                when(playbackState){
                    Player.STATE_READY -> {
                        progress_wheel.stopSpinning()
                    }
                    Player.STATE_BUFFERING -> {
                        progress_wheel.spin()
                    }
                }
            }
        })
    }

    override fun onPause() {
        super.onPause()
        println("tag_bookm onPause")
        try {
            exoPlayer.stop()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}