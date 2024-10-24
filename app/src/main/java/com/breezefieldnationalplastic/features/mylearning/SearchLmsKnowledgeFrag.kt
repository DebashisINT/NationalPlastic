package com.breezefieldnationalplastic.features.mylearning

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.breezefieldnationalplastic.R
import com.breezefieldnationalplastic.app.NetworkConstant
import com.breezefieldnationalplastic.app.Pref
import com.breezefieldnationalplastic.app.types.FragType
import com.breezefieldnationalplastic.app.utils.AppUtils
import com.breezefieldnationalplastic.base.presentation.BaseActivity
import com.breezefieldnationalplastic.base.presentation.BaseFragment
import com.breezefieldnationalplastic.features.dashboard.presentation.DashboardActivity
import com.breezefieldnationalplastic.features.mylearning.apiCall.LMSRepoProvider
import com.breezefieldnationalplastic.widgets.AppCustomEditText
import com.pnikosis.materialishprogress.ProgressWheel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class SearchLmsKnowledgeFrag : BaseFragment() , View.OnClickListener, LmsSearchAdapter.OnItemClickListener{
    private lateinit var mContext:Context
    lateinit var gv_search: RecyclerView
    lateinit var tv_next_afterSearch_lms: LinearLayout
    lateinit var ll_my_learning_topic_list: LinearLayout
    lateinit var courseList: List<LmsSearchData>
    lateinit var sortedCourseList: List<LmsSearchData>
    lateinit var lmsSearchAdapter: LmsSearchAdapter

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
    lateinit var progress_wheel: ProgressWheel

    lateinit var ll_search: LinearLayout
    lateinit var ll_voice: LinearLayout
    lateinit var et_search: AppCustomEditText
    private var  suffixText:String = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext=context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_search_lms, container, false)
        (context as Activity).requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        initView(view)
        return view
    }

    private fun initView(view: View) {
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        gv_search = view.findViewById(R.id.gv_search)
        tv_next_afterSearch_lms = view.findViewById(R.id.tv_next_afterSearch_lms)
        progress_wheel = view.findViewById(R.id.ll_frag_search_lms_search)
        ll_my_learning_topic_list = view.findViewById(R.id.ll_my_learning_topic_list)
        progress_wheel.stopSpinning()

        ll_search = view.findViewById(R.id.ll_frag_sch_add_template_root)
        et_search = view.findViewById(R.id.et_frag_contacts_search)
        ll_voice = view.findViewById(R.id.iv_frag_sched_add_form_template_dropDown)
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

        iv_lms_performance.setImageResource(R.drawable.performance_insights)
        iv_lms_mylearning.setImageResource(R.drawable.open_book_lms_)
        iv_lms_knowledgehub.setImageResource(R.drawable.all_topics_selected)

        tv_lms_performance.setTextColor(getResources().getColor(R.color.black))
        tv_lms_mylearning.setTextColor(getResources().getColor(R.color.black))
        tv_lms_leaderboard.setTextColor(getResources().getColor(R.color.black))
        tv_lms_knowledgehub.setTextColor(getResources().getColor(R.color.toolbar_lms))


        if (AppUtils.isOnline(mContext)) {
            ll_my_learning_topic_list.visibility =View.VISIBLE
            //Get All topics API calling -0027570
            getTopicL()
        }else{
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
            ll_my_learning_topic_list.visibility =View.INVISIBLE

        }
        //Code start for auto search functionality
        et_search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!et_search.text.toString().trim().equals("")) {
                    progress_wheel.spin()
                    doAsync {
                        progress_wheel.stopSpinning()
                        if (courseList.size > 0) {
                            var tempSearchL = courseList.filter {
                                it.courseName.contains(
                                    et_search.text.toString().trim(), ignoreCase = true
                                )
                            }
                            uiThread {
                                progress_wheel.stopSpinning()
                                if (tempSearchL.size > 0) {
                                    gv_search.visibility = View.VISIBLE
                                    setTopicAdapter(tempSearchL)
                                } else {
                                    gv_search.visibility = View.GONE
                                }
                            }
                        }
                    }
                }else{
                    doAsync {
                        if (courseList.size > 0) {
                            var tempSearchL = courseList
                            uiThread {
                                progress_wheel.stopSpinning()

                                setTopicAdapter(tempSearchL)

                            }
                        }
                    }
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        //Code end for auto search functionality

        ll_lms_performance.setOnClickListener(this)
        ll_lms_mylearning.setOnClickListener(this)
        ll_lms_leaderboard.setOnClickListener(this)
        ll_lms_knowledgehub.setOnClickListener(this)
        ll_search.setOnClickListener(this)
        ll_voice.setOnClickListener(this)

    }

    fun getTopicL() {
        progress_wheel.spin()
        try {
            val repository = LMSRepoProvider.getTopicList()
            BaseActivity.compositeDisposable.add(
                repository.getTopics("0")
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ result ->
                        val response = result as TopicListResponse
                        if (response.status == NetworkConstant.SUCCESS) {
                            courseList = ArrayList<LmsSearchData>()
                            sortedCourseList = ArrayList<LmsSearchData>()
                            for (i in 0..response.topic_list.size - 1) {
                                if (response.topic_list.get(i).video_count!= 0) {
                                    sortedCourseList = sortedCourseList + LmsSearchData(
                                        response.topic_list.get(i).topic_id.toString(),
                                        response.topic_list.get(i).topic_name,
                                        response.topic_list.get(i).video_count,
                                        response.topic_list.get(i).topic_parcentage,
                                        response.topic_list.get(i).topic_sequence
                                    )
                                    //code start by Puja date 25.09.2024 mantis - 0027716
                                    // Sorting the topic list by topic_sequence
                                    courseList = sortedCourseList.sortedBy { it.topic_sequence }
                                    //code end by Puja date 25.09.2024 mantis - 0027716
                                }
                            }
                            progress_wheel.stopSpinning()
                            setTopicAdapter(courseList)

                        } else {
                            progress_wheel.stopSpinning()
                            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_data_found))
                        }
                    }, { error ->
                        progress_wheel.stopSpinning()
                        (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                    })
            )
        }
        catch (ex: Exception) {
            ex.printStackTrace()
            progress_wheel.stopSpinning()
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
        }
    }

    fun setTopicAdapter(list:List<LmsSearchData>) {
        progress_wheel.stopSpinning()
        gv_search.visibility =View.VISIBLE
        //val layoutManager = FlexboxLayoutManager(mContext)
        //layoutManager.flexDirection = FlexDirection.COLUMN
        //layoutManager.justifyContent = JustifyContent.FLEX_END
        //gv_search.layoutManager = FlexboxLayoutManager(mContext)
        lmsSearchAdapter = LmsSearchAdapter(mContext, list, FragType.SearchLmsKnowledgeFrag, this)
        gv_search.adapter = lmsSearchAdapter

        tv_next_afterSearch_lms.setOnClickListener {

        }
    }


    companion object {
        fun getInstance(objects: Any): SearchLmsKnowledgeFrag {
            val fragment = SearchLmsKnowledgeFrag()
            return fragment
        }
    }

    override fun onItemClick(item: LmsSearchData) {
        if (lmsSearchAdapter.getSelectedPosition() == RecyclerView.NO_POSITION) {
            Toast.makeText(mContext, "Please select one topic", Toast.LENGTH_SHORT).show()
        } else {

            //code start for Selected topic wise content page redirection - 0027570
            val selectedItem = item
            VideoPlayLMS.previousFrag = FragType.SearchLmsFrag.toString()
            VideoPlayLMS.loadedFrom = "SearchLmsKnowledgeFrag"
            Pref.videoCompleteCount = "0"
            (mContext as DashboardActivity).loadFragment(FragType.AllTopicsWiseContents, true, selectedItem.searchid+"~"+selectedItem.courseName)
            //code end for Selected topic wise content page redirection - 0027570
        }
    }

    override fun onClick(p0: View?) {

        when (p0?.id) {
            //My topics page redirection -> Assigned to user mantis - 0027573
            ll_lms_mylearning.id -> {
                (mContext as DashboardActivity).loadFragment(
                    FragType.SearchLmsFrag,
                    true,
                    ""
                )
            }
            //All topics page redirection mantis - 0027570
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

            //Code start for Search functionality manually click on search
            ll_search.id -> {
                AppUtils.hideSoftKeyboard(mContext as DashboardActivity)
                if (!et_search.text.toString().trim().equals("")) {
                    progress_wheel.spin()
                    doAsync {
                        progress_wheel.stopSpinning()
                        if (courseList.size > 0) {
                            var tempSearchL = courseList.filter {
                                it.courseName.contains(
                                    et_search.text.toString().trim(), ignoreCase = true
                                )
                            }
                            uiThread {
                                progress_wheel.stopSpinning()
                                if (tempSearchL.size > 0) {
                                    gv_search.visibility = View.VISIBLE
                                    setTopicAdapter(tempSearchL)
                                } else {
                                    gv_search.visibility = View.GONE
                                }
                            }
                        }
                    }
                }
            }
            //Code end for Search functionality manually click on search

            //code start for through voice assistance search in assigned topic list
            ll_voice.id ->{
                suffixText = et_search.text.toString().trim()
                startVoiceInput()
            }
            //code end for through voice assistance search in assigned topic list
        }

    }

    private fun startVoiceInput() {
        val intent: Intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"en-US")
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US")
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hello, How can I help you?")
        try {
            startActivityForResult(intent, 7009)
        } catch (a: ActivityNotFoundException) {
            a.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 7009) {
            try {
                val result = data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                var t = result!![0]
                if (suffixText.length > 0 && !suffixText.equals("")) {
                    var setFullText = suffixText + t
                    et_search.setText(suffixText + t)
                    et_search.setSelection(setFullText.length);
                } else {
                    var SuffixPostText = t + et_search.text.toString()
                    et_search.setText(SuffixPostText)
                    et_search.setSelection(SuffixPostText.length);
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

}