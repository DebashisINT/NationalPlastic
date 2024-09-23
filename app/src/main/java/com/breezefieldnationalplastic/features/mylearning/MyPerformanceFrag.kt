package com.breezefieldnationalplastic.features.mylearning

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.text.SpannableString
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.breezefieldnationalplastic.CustomStatic
import com.breezefieldnationalplastic.R
import com.breezefieldnationalplastic.app.NetworkConstant
import com.breezefieldnationalplastic.app.Pref
import com.breezefieldnationalplastic.app.types.FragType
import com.breezefieldnationalplastic.app.utils.AppUtils
import com.breezefieldnationalplastic.app.utils.Toaster
import com.breezefieldnationalplastic.base.presentation.BaseActivity
import com.breezefieldnationalplastic.base.presentation.BaseFragment
import com.breezefieldnationalplastic.features.contacts.CustomDataLms
import com.breezefieldnationalplastic.features.dashboard.presentation.DashboardActivity
import com.breezefieldnationalplastic.features.mylearning.apiCall.LMSRepoProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.Duration

class MyPerformanceFrag : BaseFragment(), View.OnClickListener {
    private lateinit var totalTimeString: String
    private lateinit var averageTimeString: String
    private lateinit var totalTimeString1: String
    private lateinit var averageTimeString1: String
    private lateinit var ll_lms_performance: LinearLayout
    private lateinit var iv_lms_performance: ImageView
    private lateinit var tv_lms_performance: TextView

    private lateinit var ll_lms_mylearning: LinearLayout
    private lateinit var iv_lms_mylearning: ImageView
    private lateinit var tv_lms_mylearning: TextView

    private lateinit var ll_lms_leaderboard: LinearLayout
    private lateinit var ll_my_performance: LinearLayout
    private lateinit var iv_lms_leaderboard: ImageView
    private lateinit var tv_lms_leaderboard: TextView

    private lateinit var ll_lms_knowledgehub: LinearLayout
    private lateinit var ll_filter: LinearLayout
    private lateinit var iv_lms_knowledgehub: ImageView
    private lateinit var tv_lms_knowledgehub: TextView
    private lateinit var tv_leader_rank: TextView
    private lateinit var tv_hour_of_learning: TextView
    private lateinit var tv_filter_topic_name: TextView
    private lateinit var avg_hr_of_lrng: TextView
    private lateinit var cv_lms_leaderboard: CardView

    private lateinit var mContext:Context
    lateinit var courseList: List<LmsSearchData>
    var str_filtertopicID: String=""
    var str_filtertopicname: String=""
    var str_filtertopicParcentage: Int=0

    private lateinit var tv_durationown: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_my_performance, container, false)
        (mContext as Activity).requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        initview(view)

        if (AppUtils.isOnline(mContext)) {
            getTopicL()
            getMyLarningInfoAPI("0", "All")
            tv_filter_topic_name.text = "All"

        }
        else{
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
        }
        return view
    }

    fun getTopicL() {
        try {
            Timber.d("deleteImei call" + AppUtils.getCurrentDateTime())
            val repository = LMSRepoProvider.getTopicList()
            BaseActivity.compositeDisposable.add(
                repository.getTopics(Pref.user_id!!)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ result ->
                        val response = result as TopicListResponse
                        if (response.status == NetworkConstant.SUCCESS) {
                            courseList = ArrayList<LmsSearchData>()
                            for (i in 0..response.topic_list.size - 1) {
                                if (response.topic_list.get(i).video_count!= 0) {
                                    courseList = courseList + LmsSearchData(
                                        response.topic_list.get(i).topic_id.toString(),
                                        response.topic_list.get(i).topic_name,
                                        response.topic_list.get(i).video_count,
                                        response.topic_list.get(i).topic_parcentage
                                    )
                                }
                            }
                        }else{

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

    private fun getMyLarningInfoAPI(str_filtertopicID: String, str_filtertopicname: String) {
        try {
            Timber.d("deleteImei call" + AppUtils.getCurrentDateTime())
            val repository = LMSRepoProvider.getTopicList()
            BaseActivity.compositeDisposable.add(
                repository.getMyLraningInfo(
                    Pref.user_id!!
                )
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ result ->
                        val response = result as MyLarningListResponse
                        if (response.status == NetworkConstant.SUCCESS) {
                            try {
                                if (response.learning_content_info_list != null && response.learning_content_info_list.size > 0) {

                                    if (!str_filtertopicID.equals("0") && !str_filtertopicname.equals("All")) {

                                        var topic_name_wise_filter =
                                            response.learning_content_info_list.filter { it.topic_name.equals(str_filtertopicname) && it.topic_id.equals(str_filtertopicID.toInt()) }

                                        println("topic_name_wise_filter: $topic_name_wise_filter")

                                        val totalDuration1 = topic_name_wise_filter
                                            .map { stringToDuration(it.WatchedDuration) }
                                            .reduce { acc, duration -> acc.plus(duration) }

                                        println("totalDuration1: $totalDuration1")


                                        totalTimeString1 = String.format(
                                            "%02d:%02d:%02d",
                                            totalDuration1.toHours(),
                                            totalDuration1.toMinutes() % 60,
                                            totalDuration1.seconds % 60
                                        )

                                        val averageSeconds1 =
                                            totalDuration1.seconds / topic_name_wise_filter.size
                                        // Convert back to Duration
                                        val averageDuration1 = Duration.ofSeconds(averageSeconds1)

                                        // Format as HH:mm:ss or whatever format you prefer
                                        averageTimeString1 = String.format(
                                            "%02d:%02d:%02d",
                                            averageDuration1.toHours(),
                                            averageDuration1.toMinutes() % 60,
                                            averageDuration1.seconds % 60
                                        )

                                        println("Average Watched Duration1: $averageTimeString1")
                                        println("Total  Watched Duration1: $totalTimeString1")

                                        avg_hr_of_lrng.text = averageTimeString1
                                        tv_hour_of_learning.text = totalTimeString1
                                    }
                                    else{
                                        val totalDuration = response.learning_content_info_list
                                            .map { stringToDuration(it.WatchedDuration) }
                                            .reduce { acc, duration -> acc.plus(duration) }

                                        totalTimeString = String.format(
                                            "%02d:%02d:%02d",
                                            totalDuration.toHours(),
                                            totalDuration.toMinutes() % 60,
                                            totalDuration.seconds % 60
                                        )

                                        val averageSeconds = totalDuration.seconds / response.learning_content_info_list.size
                                        // Convert back to Duration
                                        val averageDuration = Duration.ofSeconds(averageSeconds)

                                        // Format as HH:mm:ss or whatever format you prefer
                                        averageTimeString = String.format(
                                            "%02d:%02d:%02d",
                                            averageDuration.toHours(),
                                            averageDuration.toMinutes() % 60,
                                            averageDuration.seconds % 60
                                        )

                                        println("Average Watched Duration: $averageTimeString")
                                        println("Total  Watched Duration: $totalTimeString")

                                        avg_hr_of_lrng.text = averageTimeString
                                        tv_hour_of_learning.text = totalTimeString

                                    }

                                } else {

                                }
                            } catch (ex: Exception) {
                                ex.printStackTrace()
                            }
                        } else {
                            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_data_found))

                        }
                    }, { error ->
                        println("errortopicwiselist"+error.message)
                        (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                    })
            )
        } catch (ex: Exception) {
            ex.printStackTrace()
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
        }
    }

    fun stringToDuration(timeString: String): Duration {
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        val localTime = LocalTime.parse(timeString, timeFormatter)
        return Duration.ofHours(localTime.hour.toLong())
            .plusMinutes(localTime.minute.toLong())
            .plusSeconds(localTime.second.toLong())
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext=context
    }

    private fun initview(view: View) {

        //performance
        ll_lms_performance=view.findViewById(R.id.ll_lms_performance)
        ll_my_performance=view.findViewById(R.id.ll_my_performance)
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
        ll_filter=view.findViewById(R.id.ll_filter)
        tv_hour_of_learning=view.findViewById(R.id.tv_hour_of_learning)
        avg_hr_of_lrng=view.findViewById(R.id.avg_hr_of_lrng)
        tv_filter_topic_name=view.findViewById(R.id.tv_filter_topic_name)

        iv_lms_performance.setImageResource(R.drawable.performance_insights_checked)
        iv_lms_mylearning.setImageResource(R.drawable.open_book_lms_)
        iv_lms_knowledgehub.setImageResource(R.drawable.set_of_books_lms)
        tv_lms_performance.setTextColor(getResources().getColor(R.color.toolbar_lms))
        tv_lms_mylearning.setTextColor(getResources().getColor(R.color.black))
        tv_lms_leaderboard.setTextColor(getResources().getColor(R.color.black))
        tv_lms_knowledgehub.setTextColor(getResources().getColor(R.color.black))

        overAllAPI()

        ll_lms_performance.setOnClickListener(this)
        ll_lms_mylearning.setOnClickListener(this)
        ll_lms_leaderboard.setOnClickListener(this)
        ll_lms_knowledgehub.setOnClickListener(this)
        cv_lms_leaderboard.setOnClickListener(this)
        ll_filter
            .setOnClickListener(this)

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

        if (AppUtils.isOnline(mContext)) {
            ll_my_performance.visibility =View.VISIBLE

        }else{
            ll_my_performance.visibility =View.INVISIBLE
        }

    }

    private fun overAllAPI() {
        val repository = LMSRepoProvider.getTopicList()
        BaseActivity.compositeDisposable.add(
            repository.overAllAPI(Pref.user_id!!,"","M")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    if(result.status == NetworkConstant.SUCCESS){
                        // Get sublist starting from index 3 to the end of the list
                        val ownObj = result.user_list.filter { it.user_id == Pref.user_id!!.toInt() }.first()
                        tv_leader_rank.text = ownObj.position.toString()+"/"+result.user_list.size
                    }else{
                        (mContext as DashboardActivity).showSnackMessage(result.message.toString())
                    }
                }, { error ->
                    error.printStackTrace()
                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                })
        )
    }

    companion object {

        fun getInstance(objects: Any): MyPerformanceFrag {
            val fragment = MyPerformanceFrag()
            return fragment
        }
    }


    override fun onClick(p0: View?) {
        when(p0?.id) {
            //My topics page redirection -> Assigned to user mantis - 0027573
            ll_lms_mylearning.id -> {
                (mContext as DashboardActivity).loadFragment(
                    FragType.SearchLmsFrag,
                    true,
                    ""
                )
            }
            //All topics page redirection  mantis - 0027570
            ll_lms_knowledgehub.id -> {
                (mContext as DashboardActivity).loadFragment(
                    FragType.SearchLmsKnowledgeFrag,
                    true,
                    ""
                )
            }
            //Performance Insight page redirection
            ll_lms_performance.id -> {
                (mContext as DashboardActivity).loadFragment(FragType.PerformanceInsightPage, true, "")

            }
            //LMS leaderboard page redirection mantis - 0027571
            cv_lms_leaderboard.id -> {
                (mContext as DashboardActivity).loadFragment(FragType.LeaderboardLmsFrag, true, "")
            }
            //Filter functionality
            ll_filter.id -> {
                genericDialogOfTopicList()
            }
        }
    }

    private fun genericDialogOfTopicList() {

        if(courseList.size>0){
            val allOption = LmsSearchData( "0","All")
            val updatedCourseList = courseList.toMutableList()
            updatedCourseList.add(0, allOption)


            var genericL : ArrayList<CustomDataLms> = ArrayList()
            for(i in 0..updatedCourseList.size-1){
                genericL.add(CustomDataLms(updatedCourseList.get(i).searchid.toString(),updatedCourseList.get(i).courseName.toString(),updatedCourseList.get(i).topic_parcentage))
            }
            GenericDialogLMS.newInstance("Topic",genericL as ArrayList<CustomDataLms>){
                str_filtertopicID = it.id
                str_filtertopicname = it.name
                str_filtertopicParcentage = it.topic_parcentage

                tv_filter_topic_name.setText(it.name)
                if (str_filtertopicname.toString().equals("All")){
                    avg_hr_of_lrng.text = averageTimeString
                    tv_hour_of_learning.text = totalTimeString
                }else{
                    if (str_filtertopicParcentage!=0) {
                        getMyLarningInfoAPI(str_filtertopicID, str_filtertopicname)
                    }
                    else{
                        tv_hour_of_learning.text = "00:00:00"
                        avg_hr_of_lrng.text = "00:00:00"
                    }
                }

            }.show((mContext as DashboardActivity).supportFragmentManager, "")
        }else{
            Toaster.msgShort(mContext, "No Duration Found")
        }

    }
}