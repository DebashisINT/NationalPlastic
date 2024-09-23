package com.breezefieldnationalplastic.features.mylearning

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.breezefieldnationalplastic.CustomStatic
import com.breezefieldnationalplastic.R
import com.breezefieldnationalplastic.app.NetworkConstant
import com.breezefieldnationalplastic.app.Pref
import com.breezefieldnationalplastic.app.types.FragType
import com.breezefieldnationalplastic.app.utils.AppUtils
import com.breezefieldnationalplastic.base.presentation.BaseActivity
import com.breezefieldnationalplastic.base.presentation.BaseFragment
import com.breezefieldnationalplastic.features.dashboard.presentation.DashboardActivity
import com.breezefieldnationalplastic.features.mylearning.apiCall.LMSRepoProvider
import com.modigoldbreeze.features.mylearning.MyLearningProgressAdapter
import com.pnikosis.materialishprogress.ProgressWheel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import timber.log.Timber


class MyTopicsWiseContents : BaseFragment() , View.OnClickListener , MyLearningProgressAdapter.OnItemClickListener{

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
    lateinit var et_search: EditText
    private var  suffixText:String = ""
    private lateinit var rv_mylearning_progress: RecyclerView
    private lateinit var ll_frag_search_root: LinearLayout
    private lateinit var ll_continue_learning: LinearLayout
    private lateinit var ll_no_data: LinearLayout
    private lateinit var final_dataL: ArrayList<LarningList>
    var contentL: ArrayList<ContentL> = ArrayList()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext=context
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_my_topics_wise_contents, container, false)
        (context as Activity).requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        initView(view)
        return view
    }


    companion object {
        var topic_id: String = ""
        var topic_name: String = ""
        var previousFrag: String = ""
        fun getInstance(objects: Any): MyTopicsWiseContents {
            val myTopicsWiseContents = MyTopicsWiseContents()

            if (!TextUtils.isEmpty(objects.toString())) {
                val parts = objects.toString().split("~")
                topic_id = parts[0]
                topic_name = parts[1]
            } else {
                topic_id = ""
                topic_name = ""
            }
            println("tag_topic_id" + topic_id)

            return myTopicsWiseContents
        }
    }

    private fun initView(view: View) {
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        // println("tag_myLearningVideoL"+myLearningVideoL)

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

        iv_lms_performance.setImageResource(R.drawable.performance_insights)
        iv_lms_mylearning.setImageResource(R.drawable.my_topics_selected)
        iv_lms_knowledgehub.setImageResource(R.drawable.set_of_books_lms)

        tv_lms_performance.setTextColor(getResources().getColor(R.color.black))
        tv_lms_mylearning.setTextColor(getResources().getColor(R.color.toolbar_lms))
        tv_lms_leaderboard.setTextColor(getResources().getColor(R.color.black))
        tv_lms_knowledgehub.setTextColor(getResources().getColor(R.color.black))

        rv_mylearning_progress = view.findViewById(R.id.rv_mylearning_progress)
        et_search = view.findViewById(R.id.et_frag_learning_search)
        ll_voice = view.findViewById(R.id.iv_frag_spk)
        ll_frag_search_root = view.findViewById(R.id.ll_frag_search_root)
        progress_wheel = view.findViewById(R.id.ll_frag_my_learning_progress_wheel)
        ll_no_data = view.findViewById(R.id.ll_no_data)
        ll_continue_learning = view.findViewById(R.id.ll_continue_learning)

        progress_wheel.stopSpinning()

        //API calling for selected topic wise content fetching
        getMyLarningInfoAPI()

        //Code start for Searching functionality for selected topic wise content
        et_search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            @SuppressLint("SuspiciousIndentation")
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //setLearningData(final_dataL)
                var tempSearchL = contentL.filter { it.content_title.contains(et_search.text.toString().trim(), ignoreCase = true) || it.content_description.contains(et_search.text.toString().trim(), ignoreCase = true)  } as ArrayList<ContentL>
                progress_wheel.stopSpinning()
                if(tempSearchL.size>0){
                    rv_mylearning_progress.visibility =View.VISIBLE
                    ll_continue_learning.visibility =View.VISIBLE
                    ll_no_data.visibility =View.GONE
                    setLearningData(tempSearchL,topic_name)
                }else{
                    rv_mylearning_progress.visibility =View.GONE
                    ll_continue_learning.visibility =View.GONE
                    ll_no_data.visibility =View.VISIBLE
                }
            }
            override fun afterTextChanged(s: Editable) {}
        })
        //Code end for Searching functionality for selected topic wise content

        ll_lms_performance.setOnClickListener(this)
        ll_lms_mylearning.setOnClickListener(this)
        ll_lms_leaderboard.setOnClickListener(this)
        ll_lms_knowledgehub.setOnClickListener(this)
        ll_voice.setOnClickListener(this)
        ll_frag_search_root.setOnClickListener(this)

    }

    private fun getMyLarningInfoAPI() {
        try {
            if(topic_id.equals("")){
                topic_id = CustomStatic.TOPIC_SEL
            }
            progress_wheel.visibility = View.VISIBLE
            Timber.d("deleteImei call" + AppUtils.getCurrentDateTime())
            val repository = LMSRepoProvider.getTopicList()
            BaseActivity.compositeDisposable.add(
                repository.getTopicsWiseVideo(Pref.user_id!!, topic_id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ result ->
                        val response = result as VideoTopicWiseResponse
                        if (response.status == NetworkConstant.SUCCESS) {
                            progress_wheel.visibility = View.GONE

                            try {
                                if (response.content_list != null && response.content_list.size > 0) {
                                    var temp = response.content_list.distinctBy { it.content_id.toString() }
                                    contentL = temp as  ArrayList<ContentL>
                                    // Sort the content list by content_play_sequence
                                    val sortedList = contentL.sortedBy { it.content_play_sequence.toInt() }.toCollection(ArrayList())
                                    Log.d("sortedList", "" + sortedList)
                                    setLearningData(sortedList ,topic_name)
                                } else {
                                    Toast.makeText(mContext, "No video found", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            } catch (ex: Exception) {
                                ex.printStackTrace()
                            }
                        } else {
                            progress_wheel.visibility = View.GONE
                            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_data_found))

                        }
                    }, { error ->
                        println("errortopicwiselist"+error.message)
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

    private fun setLearningData(finalDatal: ArrayList<ContentL>, topic_name: String) {

        rv_mylearning_progress.layoutManager = LinearLayoutManager(mContext)
        val adapter = MyLearningProgressAdapter(mContext, finalDatal,topic_name, this)
        rv_mylearning_progress.adapter = adapter
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
                (mContext as DashboardActivity).loadFragment(
                    FragType.PerformanceInsightPage,
                    true,
                    ""
                )
            }
            //Code start for search click functionality from contentlist
            ll_frag_search_root.id -> {
                AppUtils.hideSoftKeyboard(mContext as DashboardActivity)
                progress_wheel.spin()
                doAsync {
                    var tempSearchL = contentL.filter { it.content_title.contains(et_search.text.toString().trim(), ignoreCase = true) || it.content_description.contains(et_search.text.toString().trim(), ignoreCase = true)  } as ArrayList<ContentL>
                    uiThread {
                        progress_wheel.stopSpinning()
                        if(tempSearchL.size>0){
                            rv_mylearning_progress.visibility =View.VISIBLE
                            ll_continue_learning.visibility =View.VISIBLE
                            ll_no_data.visibility =View.GONE
                            setLearningData(tempSearchL, topic_name)
                        }else{
                            rv_mylearning_progress.visibility =View.GONE
                            ll_continue_learning.visibility =View.GONE
                            ll_no_data.visibility =View.VISIBLE
                        }
                    }
                }
            }
            //Code end for search click functionality from contentlist

            //code start for through voice assistance search in assigned topic list
            ll_voice.id ->{
                suffixText = et_search.text.toString().trim()
                startVoiceInput()
            }
            //code end for through voice assistance search in assigned topic list

        }
    }

    fun updateList(){
        getMyLarningInfoAPI()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 7009){
            try{
                val result = data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                var t= result!![0]
                if(suffixText.length>0 && !suffixText.equals("")){
                    var setFullText = suffixText+t
                    et_search.setText(suffixText+t)
                    et_search.setSelection(setFullText.length);
                }else{
                    var SuffixPostText = t+et_search.text.toString()
                    et_search.setText(SuffixPostText)
                    et_search.setSelection(SuffixPostText.length);
                }
            }
            catch (ex:Exception) {
                ex.printStackTrace()
            }
        }
    }

    //Code start for Redirect to Video play screen as selected content
    override fun onItemClick(item: ContentL , position: Int) {
        val store_topic_id = topic_id
        val store_topic_name = topic_name
        val store_content_id = item.content_id
        //getwiselistAPIcalling(store_topic_id.toInt(),store_content_id.toInt(),store_topic_name)
        VideoPlayLMS.loadedFrom = "MyTopicsWiseContents"
        CustomStatic.VideoPosition = position
        Pref.videoCompleteCount = "0"
        (mContext as DashboardActivity).loadFragment(FragType.VideoPlayLMS, true, topic_id +"~"+ topic_name/*+"~"+position*/)

    }
        //Code end for Redirect to Video play screen as selected content
}