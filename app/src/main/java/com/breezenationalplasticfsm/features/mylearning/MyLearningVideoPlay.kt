package com.breezenationalplasticfsm.features.mylearning

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.text.TextUtils
import android.text.format.DateFormat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.breezenationalplasticfsm.R
import com.breezenationalplasticfsm.app.FileUtils
import com.breezenationalplasticfsm.app.NetworkConstant
import com.breezenationalplasticfsm.app.Pref
import com.breezenationalplasticfsm.app.types.FragType
import com.breezenationalplasticfsm.app.utils.AppUtils
import com.breezenationalplasticfsm.base.BaseResponse
import com.breezenationalplasticfsm.base.presentation.BaseActivity
import com.breezenationalplasticfsm.base.presentation.BaseFragment
import com.breezenationalplasticfsm.features.dashboard.presentation.DashboardActivity
import com.breezenationalplasticfsm.features.location.LocationWizard
import com.breezenationalplasticfsm.features.mylearning.apiCall.LMSRepoProvider
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.pnikosis.materialishprogress.ProgressWheel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.apache.commons.lang3.time.DurationFormatUtils
import timber.log.Timber
import java.io.File
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale


class MyLearningVideoPlay : BaseFragment(), View.OnClickListener {
    private lateinit var mContext: Context
    private lateinit var playerView: PlayerView
    private lateinit var progress_wheel: ProgressWheel
    private lateinit var simpleExoPlayer: SimpleExoPlayer
    private lateinit var mediaDataSourceFactory: DataSource.Factory
    private lateinit var ll_vdo_ply_like: LinearLayout
    private lateinit var iv_vdo_ply_like: ImageView
    private lateinit var ll_vdo_ply_cmmnt: LinearLayout
    private lateinit var ll_vdo_ply_share: LinearLayout
    private lateinit var ll_frag_video_play_comments: LinearLayout
    private lateinit var iv_frag_video_comment_hide: ImageView
    private lateinit var et_frag_video_comment: EditText
    private lateinit var iv_frag_video_comment_save: ImageView
    var video_watch_completed = false
    private lateinit var cmtAdapter: AdapterComment
    private lateinit var rvComments: RecyclerView
    var commentL: ArrayList<CommentL> = ArrayList()
    var like_flag = false
    var seek_dragging = false
    var data_store_LMS_CONTENT_INFO: LMS_CONTENT_INFO = LMS_CONTENT_INFO()

    companion object {

        var obj_my_learning: ArrayList<ContentL> = ArrayList<ContentL>()
        var store_topic_id: String = ""
        var store_topic_id_: String = ""
        var store_topic_name: String = ""
        var store_topic_name_: String = ""
        var previousFrag: String = ""
        fun getInstance(objects: Any): MyLearningVideoPlay {
            val myLearningVideoPlay = MyLearningVideoPlay()

            if (objects!=null) {
                val parts = objects.toString().split("~")
                obj_my_learning = objects as ArrayList<ContentL>

                store_topic_id = parts[1]
                store_topic_name = parts[2]

                store_topic_id_ = extractNumber(store_topic_id)
                store_topic_name_ = extractPhrase(store_topic_name)

            }else{
                store_topic_id=""
                store_topic_name=""
            }
                //obj_my_learning = objects as ContentL

            println("tag_objects" + objects)
            println("tag_objects store_topic_id" + store_topic_id_)
            println("tag_objects store_topic_name" + store_topic_name_)
            return myLearningVideoPlay
        }

        private fun extractNumber(storeTopicId: String): String {
            // Split the string by commas and trim the result
            val parts = storeTopicId.split(",").map { it.trim() }
            // Find the part that is a valid number and convert it to Int
            return parts.first { it.isNotEmpty() && it.all { char -> char.isDigit() } }
        }

        fun extractPhrase(input: String): String {
            // Split the string by the comma
            val parts = input.split(",")
            // Get the second part, trim it to remove leading/trailing spaces, and remove the closing bracket
            val phrase = parts[1].trim().removeSuffix("]")
            return phrase
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater!!.inflate(R.layout.fragment_my_learning_video_play, container, false)
        initView(view)
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        return view
    }

    private fun initView(view: View) {
        playerView = view.findViewById(R.id.playerView_my_learning)
        progress_wheel =view. findViewById(R.id.progress_wheel_my_learning)
        progress_wheel.stopSpinning()
        ll_vdo_ply_like = view.findViewById(R.id.ll_vdo_ply_like)
        iv_vdo_ply_like = view.findViewById(R.id.iv_vdo_ply_like)
        ll_vdo_ply_cmmnt = view.findViewById(R.id.ll_vdo_ply_cmmnt)
        ll_vdo_ply_share = view.findViewById(R.id.ll_vdo_ply_share)
        ll_frag_video_play_comments = view.findViewById(R.id.ll_frag_video_play_comments)
        iv_frag_video_comment_hide = view.findViewById(R.id.iv_frag_video_comment_hide)
        rvComments = view.findViewById(R.id.rv_frag_video_play_comment)
        et_frag_video_comment = view.findViewById(R.id.et_frag_video_comment)
        iv_frag_video_comment_save = view.findViewById(R.id.iv_frag_video_comment_save)


        ll_frag_video_play_comments.visibility = View.GONE

        ll_vdo_ply_cmmnt.setOnClickListener(this)
        iv_frag_video_comment_hide.setOnClickListener(this)
        ll_vdo_ply_like.setOnClickListener(this)
        ll_vdo_ply_share.setOnClickListener(this)
        iv_frag_video_comment_save.setOnClickListener(this)
    }

    public override fun onStart() {
        super.onStart()

        if (Util.SDK_INT > 23)
            initializePlayer()
    }

    private fun initializePlayer() {
        //simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this)
        simpleExoPlayer =  SimpleExoPlayer.Builder(mContext).build()

        mediaDataSourceFactory = DefaultDataSourceFactory(mContext, Util.getUserAgent(mContext, "mediaPlayerSample"))
        val mediaSource = ProgressiveMediaSource.Factory(mediaDataSourceFactory).createMediaSource(
            MediaItem.fromUri(Uri.parse(obj_my_learning!!.get(0)!!.content_url)))


        //simpleExoPlayer.playWhenReady = mMicroLearning?.play_when_ready!!
        val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        val time = LocalTime.parse(obj_my_learning.get(0).content_watch_length, formatter)
        val milliseconds = time.toSecondOfDay() * 1000L
        println("Milliseconds: $milliseconds")
        if (obj_my_learning.get(0).content_watch_completed == true) {
            simpleExoPlayer.seekTo(0)
        }else{
            simpleExoPlayer.seekTo(milliseconds)
        }
        simpleExoPlayer.prepare(mediaSource, false, false)
        simpleExoPlayer.playWhenReady = true


        playerView.setShutterBackgroundColor(Color.TRANSPARENT)
        playerView.player = simpleExoPlayer
        playerView.requestFocus()


        if(obj_my_learning.get(0)./*isLiked*/ like_flag == true){
            iv_vdo_ply_like.setImageResource(R.drawable.heart_red)
        }else{
            iv_vdo_ply_like.setImageResource(R.drawable.like_white)
        }

        simpleExoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(@Player.State state: Int) {

                when(state){
                    Player.STATE_BUFFERING -> {
                        progress_wheel.spin()
                        println("STATE_BUFFERINGMyLearningVideo")

                    }
                    Player.STATE_READY -> {
                        progress_wheel.stopSpinning()
                        println("STATE_READYMyLearningVideo")

                    }
                    Player.STATE_IDLE -> {
                        println("STATE_IDLEMyLearningVideo")

                    }
                    Player.STATE_ENDED -> {
                        println("STATE_ENDEDMyLearningVideo")
                        val sharedPreferences = mContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                        val contentIdsString = sharedPreferences.getString("saved_content_ids", "")
                        val savedContentIds = SavedContentIds()
                        savedContentIds.content_id = contentIdsString!!.split(",").filter { it.isNotEmpty() }.map { it.toInt() }.let { it.toCollection(LinkedHashSet(it)) }
                        var qList: ArrayList<QuestionL> = ArrayList()
                        try {
                            if(obj_my_learning.get(0).question_list!=null){
                                if(obj_my_learning.get(0).question_list.size>0){
                                    qList = obj_my_learning.get(0).question_list
                                }
                            }
                        } catch (e: Exception) {
                            qList = ArrayList()
                        }

                        if (qList.size>0 ) {

                            println("seek_dragging >>>  "+seek_dragging)
                            if (simpleExoPlayer.playbackParameters.speed != 2.0.toFloat() && !seek_dragging /*&& !LmsQuestionAnswerSet.question_submit*/) {
                                //println("qqqqqqq"+savedContentIds.content_id)
                                //println("question_submit_content_id"+LmsQuestionAnswerSet.question_submit_content_id)
                                //println("zzzzzzz"+videos.get(absoluteAdapterPosition).content_id)
                                if (!savedContentIds.content_id.contains(obj_my_learning.get(0).content_id.toInt())) {
                                    //if (LmsQuestionAnswerSet.question_submit_content_id != videos.get(absoluteAdapterPosition).content_id.toInt()) {

                                    onQuestionAnswerSetPageLoad(obj_my_learning.get(0).question_list.clone() as ArrayList<QuestionL>,0)

                                }
                                else{

                                }
                            }
                        }
                        else{
                            (mContext as DashboardActivity).onBackPressed()
                        }
                    }
                }
            }

            override fun onPositionDiscontinuity(
                oldPosition: Player.PositionInfo,
                newPosition: Player.PositionInfo,
                reason: Int
            ) {
                super.onPositionDiscontinuity(oldPosition, newPosition, reason)
                if(((newPosition.positionMs / 1000).toInt() - (oldPosition.positionMs / 1000).toInt()) > 10 ){
                    seek_dragging = true
                    println("tag_seek seek ${oldPosition.positionMs} ${newPosition.positionMs} $reason")
                }else{
                    seek_dragging = false
                }
            }

            override fun onPlayerError(error: PlaybackException) {
                error?.printStackTrace()
            }
        })
    }

    private fun onQuestionAnswerSetPageLoad(questionLS: ArrayList<QuestionL>, position: Int) {

        println("tag_value_contentLSize"+questionLS.size)
        println("tag_value_position"+position)

        (context as DashboardActivity).loadFragment(
            FragType.LmsQuestionAnswerSet,
            true,
            questionLS/*+"~"+lastvideo*/
        )
    }

    private fun saveContentWiseInfo(obj: LMS_CONTENT_INFO) {
        try {
            progress_wheel.visibility = View.VISIBLE
            Timber.d("saveContentWiseInfo call" + obj)
            val repository = LMSRepoProvider.getTopicList()
            BaseActivity.compositeDisposable.add(
                repository.saveContentInfoApi(obj)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ result ->
                        val response = result as BaseResponse
                        if (response.status == NetworkConstant.SUCCESS) {
                            progress_wheel.visibility = View.GONE
                        } else {
                            progress_wheel.visibility = View.GONE
                            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_data_found))

                        }
                    }, { error ->
                        progress_wheel.visibility = View.GONE
                        (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                    })
            )
        } catch (ex: Exception) {
            ex.printStackTrace()
            progress_wheel.visibility = View.GONE
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
        }
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

    override fun onResume() {
        super.onResume()
        println("onResumeMyLearningVideo")
        if (Util.SDK_INT <= 23)
            initializePlayer()
    }

    fun onAPICalling() {
        //super.onDetach()
        println("onDetachMyLearningVideo")
        simpleExoPlayer?.apply {
            var position_ = simpleExoPlayer.currentPosition //Returns the playback position in the current content
            var duration = simpleExoPlayer.duration //Returns the duration of the current content
            var percentageWatched = (100 * position_ / duration)

         //   println("tag_content_lengthMyLarning"+DurationFormatUtils.formatDuration(duration, "HH:mm:ss"))
         //   println("tag_content_watch_lengthMyLarning"+DurationFormatUtils.formatDuration(position_, "HH:mm:ss"))
         //    println("tag_WatchPercentageMyLarning"+percentageWatched)
            release()
        }

        val comment_list: ArrayList<CommentL> = ArrayList()

        var position_ = simpleExoPlayer.currentPosition //Returns the playback position in the current content
        var duration = simpleExoPlayer.duration //Returns the duration of the current content
        var percentageWatched = (100 * position_ / duration)

        if (duration>0 && position_>0) {

            if (percentageWatched.toInt() == 100) {
                video_watch_completed = true
            } else {
                video_watch_completed = false
            }

            val endTime = System.currentTimeMillis()
            convertDate(endTime.toString(), "hh:mm:ss a")
            convertTo24HourFormat(convertDate(endTime.toString(), "hh:mm:ss a"))

            data_store_LMS_CONTENT_INFO = LMS_CONTENT_INFO(
                Pref.user_id!!,
                store_topic_id_.toInt(),
                store_topic_name_,
                obj_my_learning.get(0).content_id.toInt(),
                like_flag,
                0,
                0,
                DurationFormatUtils.formatDuration(duration, "HH:mm:ss"),
                DurationFormatUtils.formatDuration(position_, "HH:mm:ss"),
                AppUtils.getCurrentDateyymmdd(),
                AppUtils.getCurrentDateyymmdd(),
                video_watch_completed,
                AppUtils.getCurrentDateTimeNew(),
                "",
                convertTo24HourFormat(convertDate(endTime.toString(), "hh:mm:ss a")),
                DurationFormatUtils.formatDuration(position_, "HH:mm:ss"),
                DurationFormatUtils.formatDuration(position_, "HH:mm:ss"),
                "Mobile",
                "Android",
                LocationWizard.getNewLocationName(
                    mContext,
                    Pref.current_latitude.toDouble(),
                    Pref.current_longitude.toDouble()
                ),
                simpleExoPlayer.playbackParameters.speed.toString(),
                percentageWatched.toString(),
                0,
                0,
                false,
                comment_list
            )

            saveContentWiseInfo(data_store_LMS_CONTENT_INFO)
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        println("onDestroyMyLearningVideo")
        simpleExoPlayer?.apply {
            release()
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {

            ll_vdo_ply_cmmnt.id -> {

                onCommentClick(obj_my_learning.get(0).content_id)
            }

            iv_frag_video_comment_hide.id -> {
                ll_frag_video_play_comments.visibility = View.GONE
            }

            ll_vdo_ply_like.id -> {
                if (like_flag) {
                    iv_vdo_ply_like.setImageResource(R.drawable.like_white)
                    like_flag = false
                    obj_my_learning.get(0).isLiked = false
                    //obj_my_learning.filter { it.content_id.equals(obj_my_learning.get(0).content_id) }.first()./*isLiked*/ like_flag = false
                } else {
                    like_flag = true
                    iv_vdo_ply_like.setImageResource(R.drawable.heart_red)
                    obj_my_learning.get(0).isLiked = true
                    //obj_my_learning.filter { it.content_id.equals(obj_my_learning.get(0).content_id) }.first()./*isLiked*/ like_flag = true
                }
            }

            ll_vdo_ply_share.id -> {
                try {
                    val Download_Uri = Uri.parse(obj_my_learning.get(0).content_url)
                    val request = DownloadManager.Request(Download_Uri)
                    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                    request.setAllowedOverRoaming(false)
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    request.setTitle("Downloading")
                    request.setDescription("Downloading File")
                    var dir = "${AppUtils.getCurrentDateTime().replace(" ", "").replace("-", "").replace(":", "")}.mp4"
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, dir)

                    val downloadManager = mContext!!.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager?
                    var downloadID = downloadManager!!.enqueue(request) // enqueue puts the download request in the queue.

                    Handler().postDelayed(Runnable {
                        if (downloadID != null) {
                            try {
                                val fileDownloadedUri: Uri =
                                    downloadManager.getUriForDownloadedFile(downloadID)
                                val fileDownloadedPath =
                                    FileUtils.getPath(context, fileDownloadedUri)

                                val shareIntent = Intent(Intent.ACTION_SEND)
                                val fileUrl = Uri.parse(fileDownloadedPath)
                                val file = File(fileUrl.path)
                                //val uri = Uri.fromFile(file)
                                val uri: Uri = FileProvider.getUriForFile(
                                    mContext,
                                    mContext!!.applicationContext.packageName.toString() + ".provider",
                                    file
                                )
                                shareIntent.type = "image/png"
                                shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
                                startActivity(Intent.createChooser(shareIntent, "Share using"));
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }, 3100)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        }
    }

    fun onCommentClick(content_id: String) {

        commentAPICalling(content_id)

        println("onCommentCLick++++" + content_id)

        iv_frag_video_comment_save.setOnClickListener {
            AppUtils.hideSoftKeyboard(mContext as DashboardActivity)
            var obj: CommentL = CommentL()
            obj.topic_id = store_topic_id_
            obj.content_id = content_id
            obj.commented_user_id = Pref.user_id.toString()
            obj.commented_user_name = Pref.user_name.toString()
            obj.comment_id = Pref.user_id+"_"+AppUtils.getCurrentDateTime()
            obj.comment_description = et_frag_video_comment.text.toString()
            obj.comment_date_time = AppUtils.getCurrentDateTime()
            commentL.add(obj)

            et_frag_video_comment.setText("")

            data_store_LMS_CONTENT_INFO.user_id = Pref.user_id.toString()
            data_store_LMS_CONTENT_INFO.comment_list = ArrayList()
            data_store_LMS_CONTENT_INFO.comment_list.add(obj)

            saveContentWiseInfoFOrComment(data_store_LMS_CONTENT_INFO)
        }
    }

    private fun commentAPICalling(content_id: String) {
        try {
            Timber.d("deleteImei call" + AppUtils.getCurrentDateTime())
            val repository = LMSRepoProvider.getTopicList()
            BaseActivity.compositeDisposable.add(
                repository.getCommentInfo(store_topic_id_,content_id )
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ result ->
                        val response = result as MyCommentListResponse
                        try {
                            if (response.status == NetworkConstant.SUCCESS) {
                                commentL = ArrayList<CommentL>()
                                ll_frag_video_play_comments.visibility = View.VISIBLE
                                loadCommentData(response.comment_list.filter { it.content_id.toString().equals(content_id.toString()) } as ArrayList<CommentL>)
                            }else{
                                //(mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                                var blankL : ArrayList<CommentL> = ArrayList()
                                loadCommentData(blankL)
                            }
                        } catch (e: Exception) {
                            var blankL : ArrayList<CommentL> = ArrayList()
                            loadCommentData(blankL)
                        }
                    }, { error ->
                        (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                    })
            )
        } catch (ex: Exception) {
            ex.printStackTrace()
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
        }
    }

    private fun loadCommentData(comL: ArrayList<CommentL>) {
        ll_frag_video_play_comments.visibility = View.VISIBLE
        try {
            if(comL.size>0) {
                cmtAdapter = AdapterComment(mContext, comL)
                rvComments.adapter = cmtAdapter
                rvComments.smoothScrollToPosition(comL.size-1)
            }else{
                if (cmtAdapter != null) {
                    cmtAdapter.clear() // Clear the adapter's data
                    cmtAdapter.notifyDataSetChanged()
                } else {
                    cmtAdapter = AdapterComment(mContext, comL)
                    rvComments.adapter = cmtAdapter
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        /*rvComments.visibility = View.VISIBLE
        cmtAdapter = AdapterComment(mContext, comL)
        rvComments.adapter = cmtAdapter*/
    }

    private fun saveContentWiseInfoFOrComment(obj: LMS_CONTENT_INFO) {
        try {
            progress_wheel.visibility = View.VISIBLE
            Timber.d("saveContentWiseInfo call" + obj)
            val repository = LMSRepoProvider.getTopicList()
            BaseActivity.compositeDisposable.add(
                repository.saveContentInfoApi(obj)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ result ->
                        val response = result as BaseResponse
                        if (response.status == NetworkConstant.SUCCESS) {
                            progress_wheel.visibility = View.GONE
                            onCommentClick(obj.content_id.toString())
                        } else {
                            progress_wheel.visibility = View.GONE
                            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_data_found))

                        }
                    }, { error ->
                        progress_wheel.visibility = View.GONE
                        (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                    })
            )
        } catch (ex: Exception) {
            ex.printStackTrace()
            progress_wheel.visibility = View.GONE
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
        }
    }
}