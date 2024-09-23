package com.breezefieldnationalplastic.features.mylearning

import GridRVAdapter
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.breezefieldnationalplastic.DialogLoading
import com.breezefieldnationalplastic.R
import com.breezefieldnationalplastic.app.NetworkConstant
import com.breezefieldnationalplastic.app.Pref
import com.breezefieldnationalplastic.app.types.FragType
import com.breezefieldnationalplastic.app.utils.AppUtils
import com.breezefieldnationalplastic.base.presentation.BaseActivity
import com.breezefieldnationalplastic.base.presentation.BaseFragment
import com.breezefieldnationalplastic.features.dashboard.presentation.DashboardActivity
import com.breezefieldnationalplastic.features.mylearning.apiCall.LMSRepoProvider
import com.pnikosis.materialishprogress.ProgressWheel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber


class MyLearningAllVideoList : BaseFragment(), View.OnClickListener , GridRVAdapter.OnItemClickListener{
    private lateinit var mContext: Context

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
    private lateinit var gv_vdo: GridView
    lateinit var videoList: List<GridViewAllVideoModal>

    private lateinit var rv_video_view:RecyclerView
    private lateinit var progress_wheel_all_video_frag:ProgressWheel
    private lateinit var ll_video_not_found:LinearLayout
    private lateinit var ll_search_mylearning_all_video_list:LinearLayout

    var contentL : ArrayList<ContentL> = ArrayList()

    companion object {
        var topic_id: String = ""
        fun getInstance(objects: Any): MyLearningAllVideoList {
            val myLearningAllVideoList = MyLearningAllVideoList()
            if (!TextUtils.isEmpty(objects.toString())) {
                topic_id=objects.toString()
            }else{
                topic_id = ""
            }
            println("tag_topic_id"+ topic_id)
            return myLearningAllVideoList
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext=context
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater!!.inflate(R.layout.fragment_my_learning_all_video_list, container, false)
        initView(view)

        return view
    }

    private fun initView(view: View) {
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

        rv_video_view=view.findViewById(R.id.rv_video_view)
        progress_wheel_all_video_frag=view.findViewById(R.id.progress_wheel_all_video_frag)
        ll_video_not_found=view.findViewById(R.id.ll_video_not_found)
        ll_search_mylearning_all_video_list=view.findViewById(R.id.ll_search_mylearning_all_video_list)

        iv_lms_mylearning.setImageResource(R.drawable.my_learning_filled_clr)

      /*  videoList = ArrayList<GridViewAllVideoModal>()

        videoList = videoList + GridViewAllVideoModal("Top Free Courses | ProductManagement | Product Manager", "http://3.7.30.86:8073/Commonfolder/LMS/ContentUpload/Sell Me This Pen.mp4")
        videoList = videoList + GridViewAllVideoModal("Top 3 Degrees to become a Product Manager", "http://3.7.30.86:8073/Commonfolder/LMS/ContentUpload/nature shorts video.mp4")
        videoList = videoList + GridViewAllVideoModal("Salary of Product Manager", "http://3.7.30.86:8073/Commonfolder/LMS/ContentUpload/Sell These 5 things To Become Rich.mp4")
        videoList = videoList + GridViewAllVideoModal("How to Transition to ProductManagement?!", "http://3.7.30.86:8073/Commonfolder/LMS/ContentUpload/The GOLDEN Rule Of Selling.mp4")
        videoList = videoList + GridViewAllVideoModal("How to Transition to ProductManagement?!", "http://3.7.30.86:8073/Commonfolder/LMS/ContentUpload/Sales ki mol baat Basics of Sales.mp4")
        videoList = videoList + GridViewAllVideoModal("How to Transition to ProductManagement?!", "http://3.7.30.86:8073/Commonfolder/LMS/ContentUpload/How to approach Sales Management.mp4")
        videoList = videoList + GridViewAllVideoModal("How to Transition to ProductManagement?!", "http://3.7.30.86:8073/Commonfolder/LMS/ContentUpload/Attendance Marking.mp4")
*/
            //val courseAdapter = GridRVAllVideoAdapter(courseList = videoList, mContext)
        //val courseAdapter = VideoGridAdapter(mContext, videoList)

        contentL = ArrayList()

        getVideoTopicWise()
        progress_wheel_all_video_frag.spin()


        /*lifecycleScope.launch(Dispatchers.Main) {
            DialogLoading.show((mContext as DashboardActivity).supportFragmentManager, "")
            val courseAdapter = GridRVAdapter(mContext, videoList , this@MyLearningAllVideoList)
            rv_video_view.setLayoutManager(GridLayoutManager(mContext, 2))
            rv_video_view.adapter = courseAdapter
            progress_wheel_all_video_frag.stopSpinning()
        }.invokeOnCompletion {
            DialogLoading.dismiss()
        }*/

        //gv_vdo.adapter = courseAdapter

        /*gv_vdo.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->

            (mContext as DashboardActivity).loadFragment(FragType.VideoPlayLMS, true, "")
        }*/

        ll_lms_performance.setOnClickListener(this)
        ll_lms_mylearning.setOnClickListener(this)
        ll_lms_leaderboard.setOnClickListener(this)
        ll_lms_knowledgehub.setOnClickListener(this)

    }

    private fun getVideoTopicWise() {
        try {
            progress_wheel_all_video_frag.visibility = View.VISIBLE
            Timber.d("deleteImei call" + AppUtils.getCurrentDateTime()+"Puja" +topic_id)
            val repository = LMSRepoProvider.getTopicList()
            BaseActivity.compositeDisposable.add(
                repository.getTopicsWiseVideo(Pref.user_id!!,topic_id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ result ->
                        val response = result as VideoTopicWiseResponse
                        if (response.status == NetworkConstant.SUCCESS) {
                            progress_wheel_all_video_frag.visibility = View.GONE
                            try {
                                if (response.content_list!=null && response.content_list.size>0) {
                                    ll_video_not_found.visibility =View.GONE
                                    rv_video_view.visibility =View.VISIBLE
                                    ll_search_mylearning_all_video_list.visibility =View.VISIBLE

                                    contentL = response.content_list
                                    // Sort the content list by content_play_sequence
                                    val sortedList = contentL.sortedBy { it.content_play_sequence }
                                        .toCollection(ArrayList())
                                    Log.d("sortedList", "" + sortedList)

                                    lifecycleScope.launch(Dispatchers.Main) {
                                        DialogLoading.show((mContext as DashboardActivity).supportFragmentManager, "")
                                        val courseAdapter = GridRVAdapter(mContext, contentL , this@MyLearningAllVideoList)
                                        rv_video_view.setLayoutManager(GridLayoutManager(mContext, 2))
                                        rv_video_view.adapter = courseAdapter
                                        progress_wheel_all_video_frag.stopSpinning()
                                    }.invokeOnCompletion {
                                        DialogLoading.dismiss()
                                    }

                                }else{
                                    Toast.makeText(mContext, "No video found", Toast.LENGTH_SHORT).show()
                                    ll_video_not_found.visibility =View.VISIBLE
                                    rv_video_view.visibility =View.GONE
                                    ll_search_mylearning_all_video_list.visibility =View.GONE

                                }
                            }catch (ex:Exception){
                                ex.printStackTrace()
                            }
                        }else{
                            progress_wheel_all_video_frag.visibility = View.GONE
                            ll_video_not_found.visibility =View.VISIBLE
                            ll_search_mylearning_all_video_list.visibility =View.GONE
                            rv_video_view.visibility =View.GONE
                            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_data_found))

                        }
                    }, { error ->
                        progress_wheel_all_video_frag.visibility = View.GONE
                        ll_video_not_found.visibility =View.GONE
                        ll_search_mylearning_all_video_list.visibility =View.GONE
                        rv_video_view.visibility =View.GONE

                        (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                    })
            )
        } catch (ex: Exception) {
            ex.printStackTrace()
            progress_wheel_all_video_frag.visibility = View.GONE
            ll_video_not_found.visibility =View.GONE
            ll_search_mylearning_all_video_list.visibility =View.GONE
            rv_video_view.visibility =View.GONE

            (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
        }

    }


    override fun onClick(p0: View?) {
        when(p0?.id){

            ll_lms_mylearning.id -> {
                (mContext as DashboardActivity).loadFragment(FragType.SearchLmsFrag, true, "")
            }

            ll_lms_leaderboard.id -> {
                (mContext as DashboardActivity).loadFragment(FragType.MyLearningFragment, true, "")
            }

            ll_lms_knowledgehub.id -> {
                (mContext as DashboardActivity).loadFragment(FragType.SearchLmsKnowledgeFrag, true, "")
            }

            ll_lms_performance.id -> {
                (mContext as DashboardActivity).loadFragment(FragType.PerformanceInsightPage, true, "")
            }
        }
    }

    override fun onItemClick(item: ContentL) {
        //Toast.makeText(mContext, "Clicked: ${item.videoName}", Toast.LENGTH_SHORT).show()
       // (mContext as DashboardActivity).loadFragment(FragType.VideoPlayLMS, true, "")
    }

}