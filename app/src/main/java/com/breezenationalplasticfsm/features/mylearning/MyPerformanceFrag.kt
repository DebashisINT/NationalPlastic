package com.breezenationalplasticfsm.features.mylearning

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Typeface
import android.media.MediaMetadataRetriever
import android.os.Bundle
import android.os.Handler
import android.text.SpannableString
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.breezenationalplasticfsm.R
import com.breezenationalplasticfsm.app.types.FragType
import com.breezenationalplasticfsm.base.presentation.BaseFragment
import com.breezenationalplasticfsm.features.dashboard.presentation.DashboardActivity
import kotlinx.android.synthetic.main.grid_rv_adapter.view.idIVCourse
import kotlinx.android.synthetic.main.grid_rv_adapter.view.idTVCourse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MyPerformanceFrag : BaseFragment(), View.OnClickListener {
    private lateinit var ll_lms_performance: LinearLayout
    private lateinit var iv_lms_performance: ImageView
    private lateinit var tv_lms_performance: TextView

    private lateinit var ll_lms_mylearning: LinearLayout
    private lateinit var iv_lms_mylearning: ImageView
    private lateinit var tv_lms_mylearning: TextView

    private lateinit var ll_lms_leaderboard: LinearLayout
    private lateinit var iv_lms_leaderboard: ImageView
    private lateinit var tv_lms_leaderboard: TextView

    private lateinit var ll_lms_knowledgehub: LinearLayout
    private lateinit var iv_lms_knowledgehub: ImageView
    private lateinit var tv_lms_knowledgehub: TextView
    private lateinit var tv_leader_rank: TextView
    private lateinit var cv_lms_leaderboard: CardView

    private lateinit var mContext:Context

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_my_performance, container, false)
        initview(view)
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext=context
    }

    private fun initview(view: View) {

        //performance
        ll_lms_performance=view.findViewById(R.id.ll_lms_performance)
        iv_lms_performance=view.findViewById(R.id.iv_lms_performance)
        tv_lms_performance=view.findViewById(R.id.tv_lms_performance)

        //mylearning
        ll_lms_mylearning=view.findViewById(R.id.ll_lms_mylearning)
        iv_lms_mylearning=view.findViewById(R.id.iv_lms_mylearning)
        tv_lms_mylearning=view.findViewById(R.id.tv_lms_mylearning)

        //leaderboard
        ll_lms_leaderboard=view.findViewById(R.id.ll_lms_leaderboard)
        iv_lms_leaderboard=view.findViewById(R.id.iv_lms_leaderboard)
        tv_lms_leaderboard=view.findViewById(R.id.tv_lms_leaderboard)

        //knowledgehub
        ll_lms_knowledgehub=view.findViewById(R.id.ll_lms_knowledgehub)
        iv_lms_knowledgehub=view.findViewById(R.id.iv_lms_knowledgehub)
        tv_lms_knowledgehub=view.findViewById(R.id.tv_lms_knowledgehub)

        tv_leader_rank=view.findViewById(R.id.tv_leader_rank)
        cv_lms_leaderboard=view.findViewById(R.id.cv_lms_leaderboard)

        iv_lms_performance.setImageResource(R.drawable.my_performance_filled_clr)
        iv_lms_mylearning.setImageResource(R.drawable.my_learning_new)
        iv_lms_leaderboard.setImageResource(R.drawable.leaderboard_new)
        iv_lms_knowledgehub.setImageResource(R.drawable.knowledge_hub_new)
        iv_lms_leaderboard.setColorFilter(ContextCompat.getColor(mContext, R.color.black), android.graphics.PorterDuff.Mode.MULTIPLY)
        iv_lms_mylearning.setColorFilter(ContextCompat.getColor(mContext, R.color.black), android.graphics.PorterDuff.Mode.MULTIPLY)
        iv_lms_knowledgehub.setColorFilter(ContextCompat.getColor(mContext, R.color.black), android.graphics.PorterDuff.Mode.MULTIPLY)

        tv_lms_performance.setTextColor(getResources().getColor(R.color.toolbar_lms))
        tv_lms_mylearning.setTextColor(getResources().getColor(R.color.black))
        tv_lms_leaderboard.setTextColor(getResources().getColor(R.color.black))
        tv_lms_knowledgehub.setTextColor(getResources().getColor(R.color.black))

        ll_lms_performance.setOnClickListener(this)
        ll_lms_mylearning.setOnClickListener(this)
        ll_lms_leaderboard.setOnClickListener(this)
        ll_lms_knowledgehub.setOnClickListener(this)
        cv_lms_leaderboard.setOnClickListener(this)

        val fullText = tv_leader_rank.text.toString()
        val parts = fullText.split("/")

        val largeText = parts[0]
        val smallText = parts[1]
        val spannableString = SpannableString(fullText)
        // Set the size of "1000"
        spannableString.setSpan(
            RelativeSizeSpan(1.3f), // 4 times the default size
            0,
            largeText.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        // Make "1000" bold
        spannableString.setSpan(
            StyleSpan(Typeface.BOLD),
            0,
            largeText.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        // Set the size of "Contents"
        spannableString.setSpan(
            RelativeSizeSpan(1.0f), // default size
            largeText.length,
            smallText.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        tv_leader_rank.text = spannableString


        /* val rv_performance: RecyclerView = view.findViewById(R.id.rv_performance)
         rv_performance.layoutManager = LinearLayoutManager(mContext)

         val data = listOf(
             PerformanceData("The Basics of Product Knowledge Training","http://3.7.30.86:8073/Commonfolder/LMS/ContentUpload/Sell Me This Pen.mp4",  "Sales Training Basics Beginners Master","Debashis Das", 15),
             PerformanceData("The Basics of Product Knowledge Training", "http://3.7.30.86:8073/Commonfolder/LMS/ContentUpload/nature shorts video.mp4","Sales Training Basics Beginners Master", "Debashis Das" ,15)
         )
         Handler().postDelayed(Runnable {
         val adapter = PerformanceAdapter(data)
         rv_performance.adapter = adapter
         }, 1000)*/

    }

    /*data class PerformanceData(
        val videoname: String,
        val thumbnail: String,
        val title: String,
        val subtitle: String,
        val progress: Int
    )

    class PerformanceAdapter(private val courses: List<PerformanceData>) :
        RecyclerView.Adapter<PerformanceAdapter.ViewHolder>() {

        class ViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
            val titleTextView = itemView.findViewById<TextView>(R.id.tv_perform_title)
            val subtitleTextView = itemView.findViewById<TextView>(R.id.tv_perform_subtitle)
            val perform_thumbnail = itemView.findViewById<ImageView>(R.id.perform_thumbnail)
            //val authorTextView = itemView.findViewById<TextView>(R.id.perform_video_name)
            val progressBar = itemView.findViewById<ProgressBar>(R.id.progressBar)
            //val progressTextView = itemView.findViewById<TextView>(R.id.progressText)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.performance_item, parent, false)
            return ViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val course = courses[position]
            holder.titleTextView.text = course.title
            holder.subtitleTextView.text = course.subtitle
            holder.perform_thumbnail.setImageBitmap(retriveVideoFrameFromVideo(course.thumbnail))
            //holder.authorTextView.text = course.videoname
            holder.progressBar.progress = course.progress
           // holder.progressTextView.text = "${course.progress}%"
        }

        fun retriveVideoFrameFromVideo(videoPath: String?): Bitmap? {
            var def: Bitmap? = runBlocking {
                var processedBit: Bitmap? = null
                var job1 = launch(Dispatchers.Default) {
                    val mediaMetadataRetriever = MediaMetadataRetriever()
                    mediaMetadataRetriever.setDataSource(videoPath, HashMap<String, String>())
                    val bmFrame = mediaMetadataRetriever.getFrameAtTime(1000) //unit in microsecond
                    processedBit = bmFrame!!
                }
                job1.join()
                processedBit
            }

            return def

        }

        override fun getItemCount() = courses.size
    }*/



    companion object {

        fun getInstance(objects: Any): MyPerformanceFrag {
            val fragment = MyPerformanceFrag()
            return fragment
        }
    }


    override fun onClick(p0: View?) {
        when(p0?.id) {
            ll_lms_mylearning.id -> {
                (mContext as DashboardActivity).loadFragment(
                    FragType.MyLearningTopicList,
                    true,
                    ""
                )
            }

            ll_lms_leaderboard.id -> {
                (mContext as DashboardActivity).loadFragment(FragType.MyLearningFragment, true, "")
            }

            ll_lms_knowledgehub.id -> {
                (mContext as DashboardActivity).loadFragment(
                    FragType.SearchLmsKnowledgeFrag,
                    true,
                    ""
                )
            }

            ll_lms_performance.id -> {
                (mContext as DashboardActivity).loadFragment(FragType.MyPerformanceFrag, true, "")

            }

            cv_lms_leaderboard.id -> {
                (mContext as DashboardActivity).loadFragment(FragType.LeaderboardLmsFrag, true, "")
            }
        }
    }
}