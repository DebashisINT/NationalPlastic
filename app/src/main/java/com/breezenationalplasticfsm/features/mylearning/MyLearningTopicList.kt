package com.breezenationalplasticfsm.features.mylearning

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.breezenationalplasticfsm.CustomStatic
import com.breezenationalplasticfsm.R
import com.breezenationalplasticfsm.app.NetworkConstant
import com.breezenationalplasticfsm.app.Pref
import com.breezenationalplasticfsm.app.types.FragType
import com.breezenationalplasticfsm.app.utils.AppUtils
import com.breezenationalplasticfsm.base.presentation.BaseActivity
import com.breezenationalplasticfsm.base.presentation.BaseFragment
import com.breezenationalplasticfsm.features.dashboard.presentation.DashboardActivity
import com.breezenationalplasticfsm.features.mylearning.apiCall.LMSRepoProvider
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.pnikosis.materialishprogress.ProgressWheel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import timber.log.Timber

class MyLearningTopicList : BaseFragment(), View.OnClickListener , LmsSearchAdapter.OnItemClickListener{

    private lateinit var mContext: Context
    lateinit var progress_wheel: ProgressWheel

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

    lateinit var ll_voice: LinearLayout
    lateinit var ll_search: LinearLayout

    lateinit var et_search: EditText
    private var  suffixText:String = ""

    private lateinit var gv_search: RecyclerView
    private lateinit var ll_frag_search_root: LinearLayout
    private lateinit var ll_continue_learning: LinearLayout
    private lateinit var ll_no_data: LinearLayout
    private lateinit var final_dataL: ArrayList<LarningList>
    lateinit var courseList: ArrayList<LmsSearchData>
    lateinit var courseListF: ArrayList<LmsSearchData>
    lateinit var tv_next_afterSearch_lms: LinearLayout
    lateinit var lmsSearchAdapter: LmsSearchAdapter

    lateinit var responseDataL: ArrayList<LarningList>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_search_lms, container, false)
        initView(view)
        return view
    }

    private fun initView(view: View) {
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);


        //performance
        ll_lms_performance = view.findViewById(R.id.ll_lms_performance)
        iv_lms_performance = view.findViewById(R.id.iv_lms_performance)
        tv_lms_performance = view.findViewById(R.id.tv_lms_performance)

        //mylearning
        ll_lms_mylearning = view.findViewById(R.id.ll_lms_mylearning)
        iv_lms_mylearning = view.findViewById(R.id.iv_lms_mylearning)
        tv_lms_mylearning = view.findViewById(R.id.tv_lms_mylearning)

        //leaderboard
        ll_lms_leaderboard = view.findViewById(R.id.ll_lms_leaderboard)
        iv_lms_leaderboard = view.findViewById(R.id.iv_lms_leaderboard)
        tv_lms_leaderboard = view.findViewById(R.id.tv_lms_leaderboard)

        //knowledgehub
        ll_lms_knowledgehub = view.findViewById(R.id.ll_lms_knowledgehub)
        iv_lms_knowledgehub = view.findViewById(R.id.iv_lms_knowledgehub)
        tv_lms_knowledgehub = view.findViewById(R.id.tv_lms_knowledgehub)

        iv_lms_mylearning.setImageResource(R.drawable.my_learning_filled_clr)
        iv_lms_leaderboard.setImageResource(R.drawable.leaderboard_new)
        iv_lms_performance.setImageResource(R.drawable.my_performance_new)
        iv_lms_knowledgehub.setImageResource(R.drawable.knowledge_hub_new)
        iv_lms_performance.setColorFilter(ContextCompat.getColor(mContext, R.color.black), android.graphics.PorterDuff.Mode.MULTIPLY)
        iv_lms_leaderboard.setColorFilter(ContextCompat.getColor(mContext, R.color.black), android.graphics.PorterDuff.Mode.MULTIPLY)
        iv_lms_knowledgehub.setColorFilter(ContextCompat.getColor(mContext, R.color.black), android.graphics.PorterDuff.Mode.MULTIPLY)

        tv_lms_performance.setTextColor(getResources().getColor(R.color.black))
        tv_lms_mylearning.setTextColor(getResources().getColor(R.color.toolbar_lms))
        tv_lms_leaderboard.setTextColor(getResources().getColor(R.color.black))
        tv_lms_knowledgehub.setTextColor(getResources().getColor(R.color.black))

        gv_search = view.findViewById(R.id.gv_search)
        tv_next_afterSearch_lms = view.findViewById(R.id.tv_next_afterSearch_lms)
        progress_wheel = view.findViewById(R.id.ll_frag_search_lms_search)
        ll_search = view.findViewById(R.id.ll_frag_sch_add_template_root)
        et_search = view.findViewById(R.id.et_frag_contacts_search)
        ll_voice = view.findViewById(R.id.iv_frag_sched_add_form_template_dropDown)

        progress_wheel.stopSpinning()
        ll_search.setOnClickListener(this)
        ll_voice.setOnClickListener(this)

        ll_lms_performance.setOnClickListener(this)
        ll_lms_mylearning.setOnClickListener(this)
        ll_lms_leaderboard.setOnClickListener(this)
        ll_lms_knowledgehub.setOnClickListener(this)


        getMyLarningTopicListInfoAPI()

        et_search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //AppUtils.hideSoftKeyboard(mContext as DashboardActivity)
                setTopicAdapter(courseList.distinct())
            }

            override fun afterTextChanged(s: Editable) {}
        })

    }

    private fun getMyLarningTopicListInfoAPI() {
        progress_wheel.spin()
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
                                    courseList = (courseList + LmsSearchData(
                                        response.topic_list.get(i).topic_id.toString(),
                                        response.topic_list.get(i).topic_name,
                                        response.topic_list.get(i).video_count
                                    )) as ArrayList<LmsSearchData>
                                }
                            }
                            (mContext as DashboardActivity).setTopBarTitle("My Learning")
                            progress_wheel.stopSpinning()
                            setTopicAdapter(courseList)
                        }else{
                            progress_wheel.stopSpinning()
                            (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))

                        }
                    }, { error ->
                        progress_wheel.stopSpinning()
                        (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                    })
            )
        } catch (ex: Exception) {
            ex.printStackTrace()
            progress_wheel.stopSpinning()
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
        }
    }

    fun setTopicAdapter(list:List<LmsSearchData>) {
        gv_search.visibility =View.VISIBLE
        val layoutManager = FlexboxLayoutManager(mContext)
        layoutManager.flexDirection = FlexDirection.COLUMN
        layoutManager.justifyContent = JustifyContent.FLEX_END
        gv_search.layoutManager = FlexboxLayoutManager(mContext)
        lmsSearchAdapter = LmsSearchAdapter(mContext, list, this)
        gv_search.adapter = lmsSearchAdapter

        tv_next_afterSearch_lms.setOnClickListener {
            if (lmsSearchAdapter.getSelectedPosition() == RecyclerView.NO_POSITION) {
                Toast.makeText(mContext, "Please select one topic", Toast.LENGTH_SHORT).show()
            } else {
                val selectedItem = list[lmsSearchAdapter.getSelectedPosition()]
                VideoPlayLMS.previousFrag = FragType.SearchLmsFrag.toString()
                CustomStatic.TOPIC_SEL = selectedItem.searchid
                (mContext as DashboardActivity).loadFragment(FragType.SearchLmsLearningFrag, true, selectedItem.searchid+"~"+selectedItem.courseName)
            }
        }
    }


    companion object {
        fun getInstance(objects: Any): MyLearningTopicList {
            val fragment = MyLearningTopicList()
            return fragment
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            ll_lms_mylearning.id -> {
                (mContext as DashboardActivity).loadFragment(
                    FragType.MyLearningTopicList,
                    true,
                    ""
                )
            }

            ll_lms_leaderboard.id -> {
                (mContext as DashboardActivity).loadFragment(
                    FragType.MyLearningFragment,
                    true,
                    ""
                )
            }

            ll_lms_knowledgehub.id -> {
                (mContext as DashboardActivity).loadFragment(
                    FragType.SearchLmsKnowledgeFrag,
                    true,
                    ""
                )
            }

            ll_lms_performance.id -> {
                (mContext as DashboardActivity).loadFragment(
                    FragType.MyPerformanceFrag,
                    true,
                    ""
                )
            }

            ll_search.id -> {
                AppUtils.hideSoftKeyboard(mContext as DashboardActivity)
                if (!et_search.text.toString().trim().equals("")) {
                    progress_wheel.spin()
                    doAsync {
                        var tempSearchL = courseList.distinct().filter { it.courseName.contains(et_search.text.toString().trim(), ignoreCase = true) }
                        uiThread {
                            progress_wheel.stopSpinning()
                            if(tempSearchL.size>0){
                                gv_search.visibility =View.VISIBLE
                                setTopicAdapter(tempSearchL)
                            }else{
                                gv_search.visibility =View.GONE
                            }
                        }
                    }
                }
            }

            ll_voice.id ->{
                suffixText = et_search.text.toString().trim()
               // startVoiceInput()
            }
        }
    }

    override fun onItemClick(item: LmsSearchData) {


    }
}