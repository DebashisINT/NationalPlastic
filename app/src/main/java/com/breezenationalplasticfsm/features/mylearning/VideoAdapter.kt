package com.breezenationalplasticfsm.features.mylearning

import android.content.Context
import android.net.Uri
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.breezenationalplasticfsm.R
import com.breezenationalplasticfsm.app.Pref
import com.breezenationalplasticfsm.app.utils.AppUtils
import com.breezenationalplasticfsm.features.location.LocationWizard
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import kotlinx.android.synthetic.main.list_video.view.iv_list_video
import kotlinx.android.synthetic.main.list_video.view.progress_wheel
import kotlinx.android.synthetic.main.list_video.view.stylplayerView
import kotlinx.android.synthetic.main.list_video.view.tvDescrip
import kotlinx.android.synthetic.main.list_video.view.tvTitle
import org.apache.commons.lang3.time.DurationFormatUtils
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Base64
import java.util.Date
import java.util.Locale
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class VideoAdapter(var viewPager2: ViewPager2,
                   var context: Context,
                   var videos: ArrayList<ContentL>,
                   var topic_id:String,
                   var topic_name:String,
                   var content_position:Int,
                   var ll_vdo_ply_like: LinearLayout,
                   var ll_vdo_ply_cmmnt: LinearLayout,
                   var ll_vdo_ply_share: LinearLayout,
                   var iv_vdo_ply_like: ImageView,
                   var videoPreparedListener: VideoAdapter.OnVideoPreparedListener,
                   var lastVideoCompleteListener: OnLastVideoCompleteListener) :
    RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    private var position_: Long = 0
    private var duration: Long = 0
    private var percentageWatched: Long = 0
    private var starttime: Long = 0
    private var endTime: Long = 0
    var currentVideoUri: Uri? = null

    val questionSubmit = LmsQuestionAnswerSet.question_submit
    val question_submit_content_id = LmsQuestionAnswerSet.question_submit_content_id

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_video, parent, false)
        return VideoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return videos.size
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bindItems(context,videos,videoPreparedListener)
    }

    interface OnVideoPlaybackStateChangedListener {
        fun onVideoPlaybackStateChanged(position: Long, duration: Long)
    }

    private var onVideoPlaybackStateChangedListener: OnVideoPlaybackStateChangedListener? = null

    inner class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var exoPlayer: ExoPlayer
        lateinit var mediaSource: MediaSource
        var video_watch_completed = false
        var like_flag = false
        var seek_dragging = false
        private val savedContentIds = mutableSetOf<Int>()

        fun bindItems(context: Context, videos_:ArrayList<ContentL>, listner : OnVideoPreparedListener){

            if (videos_.get(absoluteAdapterPosition).content_url.contains(".mp4", ignoreCase = true)) {
                setVideoPath(videos_.get(absoluteAdapterPosition).content_url, absoluteAdapterPosition ,listner)
                if (videos_.get(absoluteAdapterPosition).content_url.contains("http")) {
                    setVideoPath(videos_.get(absoluteAdapterPosition).content_url, absoluteAdapterPosition ,listner)
                } else {
                    setVideoPath("http://3.7.30.86:8073" + videos_.get(absoluteAdapterPosition).content_url, absoluteAdapterPosition ,listner)
                }
            } else {
                setGIF()
            }

            val model = videos[absoluteAdapterPosition]
            itemView.tvTitle.text = model.content_title
            itemView.tvDescrip.text = model.content_description

            println("video_adapter like_flag"+videos.get(position).like_flag)
            println("video_adapter isLiked"+videos.get(position).isLiked)

            if (model.content_watch_length!="" && model.content_watch_completed==false) {

                val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
                val time = LocalTime.parse(model.content_watch_length, formatter)
                val milliseconds = time.toSecondOfDay() * 1000L
                println("Milliseconds: $milliseconds")
                exoPlayer.seekTo(milliseconds)
            }else{
                exoPlayer.seekTo(0)

            }

            /*  ll_vdo_ply_like.setOnClickListener {
                  if (like_flag) {
                      iv_vdo_ply_like.setImageResource(R.drawable.like_white)
                      like_flag = false
                      videos.get(position).isLiked = false
                      videoPreparedListener.onLikeClick(false)
                  } else {
                      like_flag = true
                      iv_vdo_ply_like.setImageResource(R.drawable.heart_red)
                      videos.get(position).isLiked = true
                      videoPreparedListener.onLikeClick(true)
                  }

              }

              if(videos.get(position).*//*isLiked*//* like_flag == true){
                iv_vdo_ply_like.setImageResource(R.drawable.heart_red)
            }else{
                iv_vdo_ply_like.setImageResource(R.drawable.like_white)
            }*/

            ll_vdo_ply_share.setOnClickListener {
                openShareIntents(videos[position])
            }

            /*//suman 17-07-2024
            ll_vdo_ply_cmmnt.setOnClickListener {
                listner.onCommentCLick(videos[absoluteAdapterPosition])
            }*/
        }

        fun setGIF() {
            itemView.progress_wheel.stopSpinning()
            itemView.iv_list_video.visibility = View.GONE
            itemView.stylplayerView.visibility = View.GONE

            videoPreparedListener.onNonVideo()
            itemView.iv_list_video.visibility = View.VISIBLE
            itemView.stylplayerView.visibility = View.GONE
        }

        private fun setVideoPath(
            contentUrl: String,
            position: Int,
            listner: OnVideoPreparedListener
        ) {

            try {
                // exoPlayer.clearMediaItems()
                exoPlayer.release()

            } catch (e: Exception) {
                e.printStackTrace()
            }

            itemView.progress_wheel.stopSpinning()
            println("tag_vodeo_position_state pos : $adapterPosition")
            exoPlayer = ExoPlayer.Builder(context)
                .setRenderersFactory(
                    DefaultRenderersFactory(context).setEnableDecoderFallback(
                        true
                    )
                ).setSeekForwardIncrementMs(10000L)
                .setSeekBackIncrementMs(10000L).build()

            exoPlayer.addListener(object : Player.Listener {
                override fun onPlayerError(error: PlaybackException) {
                    super.onPlayerError(error)
                    Toast.makeText(context, "Can't play this video", Toast.LENGTH_SHORT).show()
                }

                override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {


                    /*if (playbackState == PlaybackStateCompat.STATE_FAST_FORWARDING) {
                        seek_dragging = true
                        println("tag_puja STATE_FAST_FORWARDING  $seek_dragging")
                    }
                    if (playbackState == PlaybackStateCompat.STATE_REWINDING) {
                        seek_dragging = false
                        println("tag_puja STATE_FAST_FORWARDING  $seek_dragging")
                    }*/

                    position_ = exoPlayer.currentPosition //Returns the playback position in the current content
                    duration = exoPlayer.duration //Returns the duration of the current content

                    onVideoPlaybackStateChangedListener?.onVideoPlaybackStateChanged(exoPlayer.currentPosition, exoPlayer.duration)

                    percentageWatched = (100 * position_ / duration)
                    if (percentageWatched.toInt() == 100) {
                        video_watch_completed = true
                    } else {
                        video_watch_completed = false
                    }
                    println("tag_percentageWatched_after  $video_watch_completed")

                    if (playWhenReady && playbackState == Player.STATE_READY) {
                        starttime = System.currentTimeMillis()
                        convertDate(starttime.toString(), "hh:mm:ss a")
                        convertTo24HourFormat(convertDate(starttime.toString(), "hh:mm:ss a"))
                        println(
                            "Playback started at: " + convertTo24HourFormat(
                                convertDate(
                                    starttime.toString(),
                                    "hh:mm:ss a"
                                )
                            )
                        )
                    }

                    if (position_ >0 && playbackState!=Player.STATE_BUFFERING){

                        println("currentDateandTime"+AppUtils.getCurrentDateyymmdd())

                        val comment_list: ArrayList<CommentL> = ArrayList()

                        val data_end_LMS_CONTENT_INFO = LMS_CONTENT_INFO(
                            Pref.user_id!!,
                            topic_id.toInt(),
                            topic_name,
                            videos.get(position).content_id.toInt(),
                            videos.get(position).like_flag,
                            0,
                            0,
                            DurationFormatUtils.formatDuration(duration, "HH:mm:ss"),
                            DurationFormatUtils.formatDuration(position_, "HH:mm:ss"),
                            "",
                            "",
                            video_watch_completed,
                            AppUtils.getCurrentDateTimeNew(),
                            convertTo24HourFormat(convertDate(starttime.toString(), "hh:mm:ss a")),
                            convertTo24HourFormat(convertDate(endTime.toString(),"hh:mm:ss a")),
                            DurationFormatUtils.formatDuration(position_, "HH:mm:ss"),
                            AppUtils.getCurrentDateTimeNew(),
                            "Mobile",
                            "Android",
                            LocationWizard.getNewLocationName(context, Pref.current_latitude.toDouble(), Pref.current_longitude.toDouble()),
                            exoPlayer.playbackParameters.speed.toString(),
                            percentageWatched.toString(),
                            0,
                            0,
                            false,
                            comment_list
                        )

                        listner.onContentInfoAPICalling(data_end_LMS_CONTENT_INFO)

                    }

                    when (playbackState) {

                        Player.STATE_ENDED -> {
                            println("tag_video_position_state  state ended ")

                            endTime = System.currentTimeMillis()
                            convertDate(endTime.toString(),"hh:mm:ss a")
                            convertTo24HourFormat(convertDate(endTime.toString(),"hh:mm:ss a"))
                            println("Playback ended at: "+convertTo24HourFormat(convertDate(endTime.toString(),"hh:mm:ss a")))
                            val comment_list: ArrayList<CommentL> = ArrayList()

                            val data_end_LMS_CONTENT_INFO = LMS_CONTENT_INFO(
                                Pref.user_id!!,
                                topic_id.toInt(),
                                topic_name,
                                videos.get(position).content_id.toInt(),
                                like_flag,
                                0,
                                0,
                                DurationFormatUtils.formatDuration(duration, "HH:mm:ss"),
                                DurationFormatUtils.formatDuration(position_, "HH:mm:ss"),
                                "",
                                "",
                                video_watch_completed,
                                AppUtils.getCurrentDateTimeNew(),
                                convertTo24HourFormat(convertDate(starttime.toString(), "hh:mm:ss a")),
                                convertTo24HourFormat(convertDate(endTime.toString(),"hh:mm:ss a")),
                                DurationFormatUtils.formatDuration(position_, "HH:mm:ss"),
                                AppUtils.getCurrentDateTimeNew(),
                                "Mobile",
                                "Android",
                                LocationWizard.getNewLocationName(context, Pref.current_latitude.toDouble(), Pref.current_longitude.toDouble()),
                                exoPlayer.playbackParameters.speed.toString(),
                                percentageWatched.toString(),
                                0,
                                0,
                                false,
                                comment_list
                            )

                            listner.onContentInfoAPICalling(data_end_LMS_CONTENT_INFO)

                        }

                        Player.STATE_READY -> {
                            println("tag_vodeo_position_state  state ready ")
                            val starttime = System.currentTimeMillis()
                            convertDate(starttime.toString(),"yyyy-MM-dd hh:mm:ss");
                            println("Playback start at: "+convertDate(starttime.toString(),"yyyy-MM-dd hh:mm:ss"))
                        }
                        Player.STATE_BUFFERING -> {
                            println("tag_vodeo_position_state  state buffering ")
                        }
                        Player.STATE_IDLE -> {
                            println("tag_vodeo_position_state  state idle ")
                        }
                    }

                    if (playbackState == Player.STATE_BUFFERING) {
                        println("tag_vodeo_position_state  state STATE_BUFFERING ")
                        itemView.progress_wheel.spin()
                    }
                    else if (playbackState == ExoPlayer.STATE_READY ){
                        //else if (playbackState == Player.STATE_READY) {
                        println("tag_content_last_view position_  >>>>"+position_)

                        itemView.progress_wheel.stopSpinning()
                    }
                    else if (playbackState != Player.STATE_BUFFERING && playbackState == Player.STATE_ENDED /*&& position != videos.size - 1*/) {
                        viewPager2.setCurrentItem(viewPager2.currentItem + 1, true)
                        try {
                            val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                            val contentIdsString = sharedPreferences.getString("saved_content_ids", "")
                            val savedContentIds = SavedContentIds()
                            savedContentIds.content_id = contentIdsString!!.split(",").filter { it.isNotEmpty() }.map { it.toInt() }.let { it.toCollection(LinkedHashSet(it)) }
                            println("seek_dragging"+seek_dragging)
                            println("contentIdsString"+contentIdsString)
                            println("question_submit"+LmsQuestionAnswerSet.question_submit)
                            println("question_submit savedContentIds_content_id"+savedContentIds.content_id)

                            var qList: ArrayList<QuestionL> = ArrayList()
                            try {
                                if(videos.get(position).question_list!=null){
                                    if(videos.get(position).question_list.size>0){
                                        qList = videos.get(position).question_list
                                    }
                                }
                            } catch (e: Exception) {
                                qList = ArrayList()
                            }

                            if (qList.size>0 ) {
                                println("seek_dragging >>>  "+seek_dragging)
                                if (exoPlayer.playbackParameters.speed != 2.0.toFloat() && !seek_dragging /*&& !LmsQuestionAnswerSet.question_submit*/ ) {
                                    println("qqqqqqq"+savedContentIds.content_id)
                                    println("question_submit_content_id"+LmsQuestionAnswerSet.question_submit_content_id)
                                    println("zzzzzzz"+videos.get(absoluteAdapterPosition).content_id)
                                    if (!savedContentIds.content_id.contains(videos.get(absoluteAdapterPosition).content_id.toInt()) ) {
                                        //if (LmsQuestionAnswerSet.question_submit_content_id != videos.get(absoluteAdapterPosition).content_id.toInt()) {

                                        listner.onQuestionAnswerSetPageLoad(videos.get(position).question_list.clone() as ArrayList<QuestionL>,absoluteAdapterPosition)

                                    }
                                    else{

                                    }
                                }
                            }
                            else{
                                if (position == videos.size - 1)
                                    lastVideoCompleteListener.onLastVideoComplete()
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onPositionDiscontinuity(
                    oldPosition: Player.PositionInfo,
                    newPosition: Player.PositionInfo,
                    reason: Int
                ) {
                    super.onPositionDiscontinuity(oldPosition, newPosition, reason)

                    println("tag_seek seekRewind ${oldPosition.positionMs} ${newPosition.positionMs} $reason")
                    println("tag_seek seekRewind_"+ ((newPosition.positionMs / 1000).toInt() - (oldPosition.positionMs / 1000).toInt()))

                    if(((newPosition.positionMs / 1000).toInt() - (oldPosition.positionMs / 1000).toInt()) >= 10 ){
                        seek_dragging = true
                        println("tag_seek seek ${oldPosition.positionMs} ${newPosition.positionMs} $reason")
                    }else{
                        seek_dragging = false
                    }
                }
            })

            itemView.iv_list_video.visibility = View.GONE
            itemView.stylplayerView.visibility = View.VISIBLE

            exoPlayer.seekTo(0)
            exoPlayer.repeatMode = Player.REPEAT_MODE_OFF

            val dataSourceFactory = DefaultDataSource.Factory(context)

            mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(Uri.parse(contentUrl)))
            exoPlayer.setMediaSource(mediaSource)
            exoPlayer.prepare()

            itemView.progress_wheel.visibility = View.GONE
            if (absoluteAdapterPosition == 0) {
                exoPlayer.playWhenReady = true
                exoPlayer.play()
                itemView.progress_wheel.stopSpinning()
            }
            /*if (position == viewPager2.currentItem+2) {
                // If so, start playback
                exoPlayer.playWhenReady = true
                exoPlayer.play()
            }*/

            println("tag_posss absoluteAdapterPosition:$absoluteAdapterPosition")

            videoPreparedListener.onVideoPrepared(ExoPlayerItem(exoPlayer, absoluteAdapterPosition))
            itemView.stylplayerView.player = exoPlayer

        }

        fun convertDate(dateInMilliseconds: String, dateFormat: String?): String {
            return DateFormat.format(dateFormat, dateInMilliseconds.toLong()).toString()
        }

        private fun convertTo24HourFormat(time12Hour: String): String {
            val inputFormat = SimpleDateFormat("hh:mm:ss a", Locale.getDefault())
            val outputFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            val date = inputFormat.parse(time12Hour)
            return outputFormat.format(date)
        }

        fun openShareIntents(contentL: ContentL) {
            /*try {
                videoPreparedListener.onShareClick(contentL)
                return

                *//*lateinit var manager : DownloadManager
                manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                var uuri : Uri = Uri.parse(contentL.content_url)
                val request = DownloadManager.Request(uuri)
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                val reference = manager.enqueue(request)*//*

                *//*lateinit var videoFile: InputStream
                var videoFileUrl = "https://file-examples-com.github.io/uploads/2017/04/file_example_MP4_480_1_5MG.mp4"
                // download the video to videoFile
                val sharingIntent = Intent(Intent.ACTION_SEND)
                sharingIntent.type = "video/mp4"
                sharingIntent.putExtra(Intent.EXTRA_STREAM, uuri)
                context.startActivity(Intent.createChooser(sharingIntent,"Share Video"))*//*

                *//* val password = "123"
                 val salt = ByteArray(16) // 128 bits
                 SecureRandom().nextBytes(salt)

                 val keyGenerator = KeyGenerator.getInstance("PBKDF2WithHmacSHA1")
                 val spec = PBEKeySpec(password.toCharArray(), salt, 1000, 256) // 256 bits
                 val secretKeyBytes = keyGenerator.generateKey()
                 val secretKey = SecretKeySpec(secretKeyBytes.encoded, "AES")

                 val encryptedUrl = encryptUrl(contentL.content_url, secretKey)
                 val originalUrl = decryptUrl(encryptedUrl, secretKey)*//*

                println("originalUrl+++"+contentL.content_url)
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_TEXT, contentL.content_url)
                ContextCompat.startActivity(
                    context,
                    Intent.createChooser(
                        intent,
                        context.getString(R.string.app_name)
                    ), null
                )

            } catch (e: Exception) {
                e.printStackTrace()
            }*/
        }

        fun encryptUrl(url: String, secretKey: SecretKeySpec): String {
            val cipher = Cipher.getInstance("AES")
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)

            val urlBytes = url.toByteArray(Charsets.UTF_8)
            val encryptedBytes = cipher.doFinal(urlBytes)

            return Base64.getEncoder().encodeToString(encryptedBytes)
        }

        fun decryptUrl(encryptedUrl: String, secretKey: SecretKeySpec): String {
            val cipher = Cipher.getInstance("AES")
            cipher.init(Cipher.DECRYPT_MODE, secretKey)

            val encryptedBytes = Base64.getDecoder().decode(encryptedUrl)
            val decryptedBytes = cipher.doFinal(encryptedBytes)

            return String(decryptedBytes, Charsets.UTF_8)
        }
    }

    interface OnVideoPreparedListener {
        fun onVideoPrepared(exoPlayerItem: ExoPlayerItem)
        fun onNonVideo()
        fun onContentInfoAPICalling(obj: LMS_CONTENT_INFO)
        fun onCommentCLick(obj: ContentL)
        fun onShareClick(obj: ContentL)
        fun onQuestionAnswerSetPageLoad(obj: ArrayList<QuestionL>,position:Int)
        fun onLikeClick(isLike:Boolean)
    }
    interface OnLastVideoCompleteListener {
        fun onLastVideoComplete()
    }
}
