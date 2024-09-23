package com.breezefieldnationalplastic.features.mylearning

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.breezefieldnationalplastic.R
import com.breezefieldnationalplastic.app.AppDatabase
import com.breezefieldnationalplastic.app.NetworkConstant
import com.breezefieldnationalplastic.app.Pref
import com.breezefieldnationalplastic.app.domain.AddShopDBModelEntity
import com.breezefieldnationalplastic.app.types.FragType
import com.breezefieldnationalplastic.base.BaseResponse
import com.breezefieldnationalplastic.base.presentation.BaseActivity
import com.breezefieldnationalplastic.base.presentation.BaseFragment
import com.breezefieldnationalplastic.features.dashboard.presentation.DashboardActivity
import com.breezefieldnationalplastic.features.mylearning.apiCall.LMSRepoProvider
import com.breezefieldnationalplastic.widgets.AppCustomTextView
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.pnikosis.materialishprogress.ProgressWheel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class BookmarkFrag: BaseFragment()  {

    private lateinit var response: BookmarkFetchResponse
    private lateinit var mContext: Context
    private lateinit var rvList:RecyclerView
    private lateinit var iv_no_data:ImageView
    private lateinit var adapterBookmarked:AdapterBookmarkedprivate

    private lateinit var progress_wheel:ProgressWheel



    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.frag_bookmark, container, false)
        (mContext as Activity).requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        initView(view)
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        return view
    }

    private fun initView(view: View) {

        rvList = view.findViewById(R.id.rv_frag_bookmark)
        progress_wheel = view.findViewById(R.id.progress_wheel_bookmark)
        iv_no_data = view.findViewById(R.id.iv_no_data)

        progress_wheel.stopSpinning()
        getBookmarked()
    }

    private fun getBookmarked(){
        try {
            progress_wheel.spin()
            val repository = LMSRepoProvider.getTopicList()
            BaseActivity.compositeDisposable.add(
                repository.getBookmarkedApiCall(Pref.user_id.toString())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ result ->
                        response = result as BookmarkFetchResponse
                        if (response.status == NetworkConstant.SUCCESS) {
                            rvList.visibility = View.VISIBLE
                            iv_no_data.visibility = View.GONE
                            progress_wheel.stopSpinning()
                            (mContext as DashboardActivity).setTopBarTitle("Saved Contents : (${response.bookmark_list.size})")
                            showData(response.bookmark_list)

                            try {
                                (mContext as DashboardActivity).updateBookmarkCnt()
                            } catch (e: Exception) {
                                Pref.CurrentBookmarkCount = 0
                            }
                        } else {
                            rvList.visibility = View.GONE
                            iv_no_data.visibility = View.VISIBLE
                            progress_wheel.stopSpinning()
                            try {
                                (mContext as DashboardActivity).updateBookmarkCnt()
                            } catch (e: Exception) {
                                Pref.CurrentBookmarkCount = 0
                            }

                            (mContext as DashboardActivity).setTopBarTitle("Saved Contents")

                        }
                    }, { error ->
                        error.printStackTrace()
                        rvList.visibility = View.GONE
                        iv_no_data.visibility = View.GONE
                        progress_wheel.stopSpinning()
                    })
            )
        } catch (ex: Exception) {
            ex.printStackTrace()
            rvList.visibility = View.GONE
            iv_no_data.visibility = View.GONE
            progress_wheel.stopSpinning()
        }
    }

    fun showData(bookmark_list:ArrayList<VidBookmark>){
        var filterL = bookmark_list.distinctBy { it.content_id.toString() }
        adapterBookmarked = AdapterBookmarkedprivate(mContext,filterL as ArrayList<VidBookmark>,object :AdapterBookmarkedprivate.OnClick{
            override fun onClick(obj: VidBookmark) {
                BookmarkPlayFrag.play_url = obj.content_url
                (mContext as DashboardActivity).loadFragment(FragType.BookmarkPlayFrag, true, "")
            }

            override fun onDelClick(obj: VidBookmark) {
                obj.isBookmarked = "0"
                bookmarkDelApi(obj)

                /*val simpleDialog = Dialog(mContext)
                simpleDialog.setCancelable(false)
                simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                simpleDialog.setContentView(R.layout.dialog_yes_no)
                val dialogHeader = simpleDialog.findViewById(R.id.dialog_cancel_order_header_TV) as AppCustomTextView
                val dialog_yes_no_headerTV = simpleDialog.findViewById(R.id.dialog_yes_no_headerTV) as AppCustomTextView
                //dialog_yes_no_headerTV.text = "Hi "+Pref.user_name?.substring(0, Pref.user_name?.indexOf(" ")!!)+"!"
                dialog_yes_no_headerTV.text = "Hi "+Pref.user_name!!+"!"
                dialogHeader.text = "Are you sure ?"
                val dialogYes = simpleDialog.findViewById(R.id.tv_dialog_yes_no_yes) as AppCustomTextView
                val dialogNo = simpleDialog.findViewById(R.id.tv_dialog_yes_no_no) as AppCustomTextView
                dialogYes.setOnClickListener({ view ->
                    simpleDialog.cancel()
                    bookmarkDelApi(obj)
                })
                dialogNo.setOnClickListener({ view ->
                    simpleDialog.cancel()
                })
                simpleDialog.show()*/


            }
        })
        rvList.adapter = adapterBookmarked
    }

    private fun bookmarkDelApi(obj:VidBookmark){
        var apiObj : BookmarkResponse = BookmarkResponse()
        apiObj.user_id = Pref.user_id.toString()
        apiObj.topic_id = obj.topic_id
        apiObj.topic_name = obj.topic_name
        apiObj.content_id = obj.content_id
        apiObj.content_name = obj.content_name
        apiObj.content_desc = obj.content_desc
        apiObj.content_bitmap = obj.content_bitmap
        apiObj.content_url = obj.content_url
        apiObj.addBookmark = obj.isBookmarked

        try {
            val repository = LMSRepoProvider.getTopicList()
            BaseActivity.compositeDisposable.add(
                repository.bookmarkApiCall(apiObj)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ result ->
                        var response = result as BaseResponse
                        if (response.status == NetworkConstant.SUCCESS) {
                            getBookmarked()
                        } else {

                        }
                    }, { error ->
                        error.printStackTrace()
                    })
            )
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun updateToolbar() {
        super.onResume()
        try {
            (mContext as DashboardActivity).setTopBarTitle("Saved Contents : (${response.bookmark_list.size})")

        }catch (ex:Exception){
            ex.printStackTrace()
            (mContext as DashboardActivity).setTopBarTitle("Saved Contents")

        }

        try {
            (mContext as DashboardActivity).updateBookmarkCnt()
        } catch (e: Exception) {
            Pref.CurrentBookmarkCount = 0
        }
    }
}