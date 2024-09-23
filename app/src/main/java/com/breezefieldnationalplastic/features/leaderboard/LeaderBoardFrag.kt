package com.breezefieldnationalplastic.features.leaderboard

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.breezefieldnationalplastic.R
import com.breezefieldnationalplastic.app.NetworkConstant
import com.breezefieldnationalplastic.app.Pref
import com.breezefieldnationalplastic.app.types.FragType
import com.breezefieldnationalplastic.app.types.TopBarConfig
import com.breezefieldnationalplastic.app.utils.AppUtils
import com.breezefieldnationalplastic.app.utils.Toaster
import com.breezefieldnationalplastic.base.presentation.BaseActivity
import com.breezefieldnationalplastic.base.presentation.BaseFragment
import com.breezefieldnationalplastic.features.NewQuotation.ViewDetailsQuotFragment
import com.breezefieldnationalplastic.features.contacts.CustomData
import com.breezefieldnationalplastic.features.contacts.GenericDialog
import com.breezefieldnationalplastic.features.contacts.GenericDialogWithOutSearch
import com.breezefieldnationalplastic.features.contacts.SchedulerAddFormFrag
import com.breezefieldnationalplastic.features.dashboard.presentation.DashboardActivity
import com.breezefieldnationalplastic.features.leaderboard.api.BranchData
import com.breezefieldnationalplastic.features.leaderboard.api.LeaderboardBranchData
import com.breezefieldnationalplastic.features.leaderboard.api.LeaderboardRepoProvider
import com.breezefieldnationalplastic.features.leaderboard.api.OverallUserListData
import com.breezefieldnationalplastic.features.leaderboard.api.SubBranchData
import com.breezefieldnationalplastic.widgets.AppCustomEditText
import com.breezefieldnationalplastic.widgets.AppCustomTextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pnikosis.materialishprogress.ProgressWheel
import de.hdodenhof.circleimageview.CircleImageView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.row_shop_list_ma.view.iv_row_shop_list_ma_pointer

class LeaderBoardFrag : BaseFragment(), View.OnClickListener{
    private val firstList: ArrayList<OverallUserListData> =ArrayList()
    private val secondList: ArrayList<OverallUserListData> = ArrayList()
    var idMultiSubBranchL:String =""
    private var subBranch_list: ArrayList<SubBranchData> = ArrayList()
    private var subBranch_listTemp: ArrayList<SubBranchData> = ArrayList()
    private var mFilterbranchData: ArrayList<BranchData> =ArrayList()
    private lateinit var constraintLayoutBTN: ConstraintLayout
    private lateinit var constraintLayout: ConstraintLayout
    private lateinit var main_const: ConstraintLayout
    private lateinit var bt_overall: TextView
    private lateinit var bt_own: TextView
    private lateinit var tv_nointernet: TextView
    private lateinit var tv_ownrank: TextView
    private lateinit var tv_ownname: TextView
    private lateinit var tv_ownscore: TextView
    private lateinit var tv_totalscoreA: TextView
    private lateinit var tv_totalscoreAV: TextView
    private lateinit var tv_totalscoreRev: TextView
    private lateinit var tv_totalscoreAOr: TextView
    private lateinit var tv_totalscoreAActivities: TextView
    private lateinit var headerOfAllEmployee: TextView
    private lateinit var iv_filter: ImageView
    private lateinit var iv_ownimg: CircleImageView
    private lateinit var iv_OwnTag: ImageView
    private lateinit var rv_leaderboard: RecyclerView
    private lateinit var cardView_own: ConstraintLayout
    private lateinit var topStick_bar: ConstraintLayout
    private lateinit var own_part_background: ConstraintLayout
    private lateinit var ll_no_data_root_leader: ConstraintLayout
    private lateinit var overall_firstTo_thirdrank: ConstraintLayout
    private lateinit var progress_wheel: ProgressWheel
    private lateinit var mLeaderBoardAdapter : LeaderBoardAdapter
    private var filterDialog: Dialog? = null
    private lateinit var mContext: Context
    private var str_filterBranchID:String = ""
    private var str_filterSubBranchID:String = ""
    private var str_filterBranchIDTemp:String = ""
    private var str_filterSubBranchIDTemp:String = ""
    private var str_filterDurationID:String = ""
    private var flag:String = "M"
    private var ownclick:Boolean = false
    private var isFromManuOwn:Boolean = true
    private var isFromManuOver:Boolean = true
    private var isOverallOwnSelected:Boolean = true        // if isOverallOwnSelected =true then overall is 1 else own 1
    private var subBranchList: ArrayList<BranchData> = ArrayList()
    private var respBranchData: LeaderboardBranchData = LeaderboardBranchData()
    private lateinit var tv_header: TextView
    private lateinit var ll_subBranch: LinearLayout
    private lateinit var ll_durationFilter: LinearLayout
    private lateinit var ll_durationFilter2: LinearLayout
    private lateinit var ll_branchFilter: LinearLayout
    private lateinit var tv_subBranch: TextView
    private lateinit var tv_headbranch: TextView
    private lateinit var tv_durationown: TextView
    private lateinit var tv_filterDoneOwn: TextView
    private lateinit var tv_nodata: TextView
    private lateinit var tv_firstPname: TextView
    private lateinit var tv_firstPscore: TextView
    private lateinit var tv_secondPscore: TextView
    private lateinit var tv_secondPname: TextView
    private lateinit var tv_thirdPname: TextView
    private lateinit var tv_thirdPscore: TextView
    private lateinit var iv_close: ImageView
    private lateinit var iv_ownimg2: ImageView
    private lateinit var iv_empty_data: ImageView
    private lateinit var iv_hand_ani: ImageView
    private lateinit var iv_firstP: ImageView
    private lateinit var iv_secondP: ImageView
    private lateinit var iv_thirdP: ImageView
    private lateinit var view_bar: View
    private lateinit var tv_empty_page_msg_head: TextView
    private lateinit var tv_empty_page_msg: TextView
    private lateinit var adapterSubBranchName:AdapterSubBranchName


    override fun onAttach(context: Context) {
        super.onAttach(context)
        println("onAttach")
        mContext = context
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        println("onCreateView")
        val view = inflater.inflate(R.layout.fragment_leader_board, container, false)
        initView(view)
        return view
    }
    private fun initView(view: View) {
        constraintLayoutBTN = view.findViewById(R.id.constraintLayoutBTN)
        progress_wheel = view.findViewById(R.id.progress_wheel_frag_leaderboard)
        ll_no_data_root_leader = view.findViewById(R.id.ll_no_data_root_leader)
        overall_firstTo_thirdrank = view.findViewById(R.id.overall_firstTo_thirdrank)
        constraintLayout = view.findViewById(R.id.constraintLayout)
        main_const = view.findViewById(R.id.main_const)
        bt_overall = view.findViewById(R.id.bt_overall)
        bt_own = view.findViewById(R.id.bt_own)
        iv_filter = view.findViewById(R.id.iv_filter)
        topStick_bar = view.findViewById(R.id.topStick_bar)
        own_part_background = view.findViewById(R.id.own_part_background)
        rv_leaderboard = view.findViewById(R.id.rv_leaderboard)
        cardView_own = view.findViewById(R.id.cardView_own)
        tv_nointernet = view.findViewById(R.id.tv_nointernet)
        tv_ownrank = view.findViewById(R.id.tv_ownrank)
        tv_ownname = view.findViewById(R.id.tv_ownname)
        tv_ownscore = view.findViewById(R.id.tv_ownscore)
        tv_totalscoreA = view.findViewById(R.id.tv_totalscoreA)
        tv_totalscoreAV = view.findViewById(R.id.tv_totalscoreAV)
        tv_totalscoreRev = view.findViewById(R.id.tv_totalscoreRev)
        tv_totalscoreAOr = view.findViewById(R.id.tv_totalscoreAOr)
        tv_totalscoreAActivities = view.findViewById(R.id.tv_totalscoreAActivities)
        iv_ownimg = view.findViewById(R.id.iv_ownimg)
        iv_OwnTag = view.findViewById(R.id.iv_OwnTag)
        view_bar = view.findViewById(R.id.view_bar)
        tv_empty_page_msg_head = view.findViewById(R.id.tv_empty_page_msg_head)
        tv_empty_page_msg = view.findViewById(R.id.tv_empty_page_msg)
        iv_empty_data = view.findViewById(R.id.iv_empty_data)
        iv_ownimg2 = view.findViewById(R.id.iv_ownimg2)
        tv_nodata = view.findViewById(R.id.tv_nodata)
        headerOfAllEmployee = view.findViewById(R.id.headerOfAllEmployee)
        iv_firstP = view.findViewById(R.id.iv_firstP)
        iv_secondP = view.findViewById(R.id.iv_secondP)
        iv_thirdP = view.findViewById(R.id.iv_thirdP)
        tv_firstPname = view.findViewById(R.id.tv_firstPname)
        tv_firstPscore = view.findViewById(R.id.tv_firstPscore)
        tv_secondPscore = view.findViewById(R.id.tv_secondPscore)
        tv_secondPname = view.findViewById(R.id.tv_secondPname)
        tv_thirdPname = view.findViewById(R.id.tv_thirdPname)
        tv_thirdPscore = view.findViewById(R.id.tv_thirdPscore)
        iv_hand_ani = view.findViewById(R.id.iv_hand_ani)
        filterDialog = Dialog(mContext)
        filterDialog!!.setCancelable(true)
        filterDialog!!.setCanceledOnTouchOutside(false)
        filterDialog!!.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        filterDialog!!.setContentView(R.layout.filter_of_leaderboard)
        tv_header = filterDialog!!.findViewById(R.id.tv_header) as TextView
        ll_branchFilter = filterDialog!!.findViewById(R.id.ll_branchFilter) as LinearLayout
        ll_subBranch = filterDialog!!.findViewById(R.id.ll_subBranch) as LinearLayout
        tv_subBranch = filterDialog!!.findViewById(R.id.tv_subBranch) as TextView
        ll_durationFilter = filterDialog!!.findViewById(R.id.ll_durationFilter) as LinearLayout
        ll_durationFilter2 = filterDialog!!.findViewById(R.id.ll_durationFilter2) as LinearLayout
        tv_headbranch = filterDialog!!.findViewById(R.id.tv_headbranch) as TextView
        tv_durationown = filterDialog!!.findViewById(R.id.tv_durationown) as TextView
        iv_close = filterDialog!!.findViewById(R.id.iv_close) as ImageView
        tv_filterDoneOwn = filterDialog!!.findViewById(R.id.tv_filterDoneOwn) as TextView
       // ll_subBranch.isEnabled=true
       // ll_no_data_root_leader.visibility = View.VISIBLE

        if (ownclick) {
                filterDialog!!.dismiss()
                idMultiSubBranchL =""
                ownAPI()

        }else{
                filterDialog!!.dismiss()
                idMultiSubBranchL =""
                overAllAPI()
        }

        overall_firstTo_thirdrank.visibility = View.VISIBLE

        if (AppUtils.isOnline(mContext)) {
            constraintLayout.visibility=View.VISIBLE
            tv_nointernet.visibility=View.INVISIBLE
            bt_overall.setOnClickListener(this)
            bt_own.setOnClickListener(this)
            iv_filter.setOnClickListener(this)
            tv_filterDoneOwn.setOnClickListener(this)
            bt_overall.setText("Overall")
            bt_own.setText("Own")
            bt_overall.setBackgroundResource(R.drawable.attached_image_rounded_bg)
            context?.getColor(R.color.white)?.let { bt_overall.setTextColor(it) }
            context?.getColor(R.color.black)?.let { bt_own.setTextColor(it) }
            rv_leaderboard.visibility = View.VISIBLE
            bt_own.setBackgroundResource(0)
            topStick_bar.setBackgroundColor(0x00000000)

        }
        else{
            constraintLayout.visibility=View.INVISIBLE
            tv_nointernet.visibility=View.VISIBLE
        }
        if (AppUtils.isOnline(mContext)) {
            getHeadBranchList(tv_headbranch, tv_subBranch)
        }else{
            tv_nointernet.visibility=View.VISIBLE
        }
        ll_branchFilter.performClick()

        //test code
      //  getHeadBranchListNew()
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LeaderBoardFrag().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.bt_overall ->{
                ownclick=false
                isOverallOwnSelected = true
                setToolbar()
                bt_overall.setBackgroundResource(R.drawable.attached_image_rounded_bg)
                bt_own.setBackgroundResource(0)
                topStick_bar.setBackgroundColor(0x00000000)
                //topStick_bar.setBackgroundColor(Color.parseColor("#3d30d7"));
                context?.getColor(R.color.white)?.let { bt_overall.setTextColor(it) }
                context?.getColor(R.color.black)?.let { bt_own.setTextColor(it) }
                rv_leaderboard.visibility=View.VISIBLE
                cardView_own.visibility=View.INVISIBLE
                own_part_background.visibility=View.INVISIBLE
                (mContext as DashboardActivity).setTopBarTitle("Leaderboard")
                iv_hand_ani.visibility = View.GONE

                if (!str_filterSubBranchID.equals("") || isFromManuOwn == true) {
                    overAllAPI()
                    iv_empty_data.visibility = View.INVISIBLE
                    tv_nodata.visibility = View.INVISIBLE
                }
                else{
                    rv_leaderboard.visibility = View.INVISIBLE
                    ll_no_data_root_leader.visibility = View.VISIBLE
                    overall_firstTo_thirdrank.visibility = View.INVISIBLE
                    iv_empty_data.visibility = View.INVISIBLE
                    tv_nodata.visibility = View.INVISIBLE
                }
            }
            R.id.bt_own ->{
                progress_wheel.spin()
                ownclick=true
                isOverallOwnSelected = false
                setToolbar()
                bt_own.setBackgroundResource(R.drawable.attached_image_rounded_bg)
                //topStick_bar.setBackgroundColor(0x00000000)
                topStick_bar.setBackgroundColor(Color.parseColor("#3d30d7"));
                tv_ownscore.text ="NA"
                own_part_background.setBackgroundResource(R.drawable.leaderbackground)
                bt_overall.setBackgroundResource(0)
                context?.getColor(R.color.white)?.let { bt_own.setTextColor(it) }
                context?.getColor(R.color.black)?.let { bt_overall.setTextColor(it) }
                rv_leaderboard.visibility=View.GONE
               // cardView_own.visibility=View.VISIBLE
                own_part_background.visibility=View.VISIBLE
                ll_no_data_root_leader.visibility = View.INVISIBLE
                overall_firstTo_thirdrank.visibility =View.GONE
                view_bar.visibility =View.GONE
                tv_ownrank.visibility =View.GONE
                tv_ownname.text = Pref.user_name

                val param = iv_ownimg.layoutParams as ViewGroup.MarginLayoutParams
                param.setMargins(0,25,0,0)
                iv_ownimg.layoutParams = param

                val param1 = tv_ownname.layoutParams as ViewGroup.MarginLayoutParams
                param1.setMargins(0,25,0,0)
                tv_ownname.layoutParams = param1

                Glide.with(mContext)
                    .load(Pref.profile_img)
                    .apply(RequestOptions.placeholderOf(R.drawable.user_blank).error(R.drawable.user_blank))
                    .into(iv_ownimg)
                if (!str_filterSubBranchID.equals("") || isFromManuOwn == true) {
                    ownAPI()
                    iv_empty_data.visibility = View.INVISIBLE
                    tv_nodata.visibility = View.INVISIBLE
                    iv_hand_ani.visibility = View.GONE
                }
                else{
                    cardView_own.visibility =View.INVISIBLE
                    iv_empty_data.visibility = View.VISIBLE
                    tv_nodata.visibility = View.VISIBLE
                    iv_hand_ani.visibility = View.VISIBLE

                    Glide.with(mContext)
                        .load(R.drawable.icon_pointer_gif)
                        .into(iv_hand_ani)
                }
                Handler().postDelayed(Runnable {
                    progress_wheel.stopSpinning()
                }, 2000)

            }
            R.id.iv_filter ->{


                tv_header.text = "Filter"
                val params: WindowManager.LayoutParams =
                    filterDialog!!.getWindow()!!.getAttributes() // change this to your dialog.
                params.y = -440 // Here is the param to set your dialog position. Same with params.x
                filterDialog!!.getWindow()?.setAttributes(params)
                filterDialog!!.show()

               // getHeadBranchList(tv_headbranch,tv_subBranch)

                Handler().postDelayed(Runnable {
                    if(respBranchData.status == NetworkConstant.SUCCESS){
                        mFilterbranchData.clear()
                        mFilterbranchData = respBranchData.branch_list.clone() as ArrayList<BranchData>
                        if(mFilterbranchData.size>0){
                            var headBranchAll = respBranchData.branch_list.filter { it.branch_head.equals("All",ignoreCase = true) }.first()
                            subBranchList =(mFilterbranchData.filter { it.branch_head_id == headBranchAll.branch_head_id }).first().sub_branch as ArrayList<BranchData>
                            subBranch_list =headBranchAll.sub_branch

                            var genericL : ArrayList<CustomData> = ArrayList()
                            for(i in 0..mFilterbranchData.size-1){
                                genericL.add(CustomData(mFilterbranchData.get(i).branch_head_id.toString(),mFilterbranchData.get(i).branch_head))
                            }
                            GenericDialog.newInstance("Head Branch",genericL as ArrayList<CustomData>){
                                str_filterBranchID = it.id
                                tv_headbranch.setText(it.name)
                                // ll_subBranch.isEnabled =true
                                subBranchList =mFilterbranchData.filter { it.branch_head_id == str_filterBranchID.toInt() } as ArrayList<BranchData>
                                for(i in 0..subBranchList.size-1){
                                    subBranch_list = subBranchList.get(i).sub_branch
                                    if (subBranch_list.size > 0) {
                                        tv_subBranch.text = subBranch_list.get(0).value
                                       // str_filterSubBranchID = subBranch_list.get(0).id.toString()
                                    }else{
                                        tv_subBranch.text = ""
                                        str_filterSubBranchID=""
                                    }
                                }

                            }/*.show((mContext as DashboardActivity).supportFragmentManager, "")*/
                        }else{
                            Toaster.msgShort(mContext, "No Branch Found")
                        }
                    }
                    else{
                        (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                    }
                    if (tv_headbranch.text.equals("") || tv_headbranch.hint.equals("Head Branch")){
                        // ll_subBranch.isEnabled =false
                    }
                    else{
                        //  ll_subBranch.isEnabled =true
                    }

                }, 1500)

                iv_close.setOnClickListener {
                     str_filterBranchID = str_filterBranchIDTemp
                     str_filterSubBranchID = str_filterSubBranchIDTemp
                    println("subBranch_listTemp--"+subBranch_listTemp.size)
                    subBranch_list = subBranch_listTemp
                    println("subBranch_list--"+subBranch_list.size)
                     filterDialog!!.dismiss()
                }

                //branch filter
                ll_branchFilter.setOnClickListener {
                    ll_branchFilter.isEnabled = false
                        if(respBranchData.status == NetworkConstant.SUCCESS){

                        //mFilterbranchData.clear()
                        mFilterbranchData = respBranchData.branch_list.clone() as ArrayList<BranchData>
                        if(mFilterbranchData.size>0){
                            var genericL : ArrayList<CustomData> = ArrayList()
                                for(i in 0..mFilterbranchData.size-1){
                                    genericL.add(CustomData(mFilterbranchData.get(i).branch_head_id.toString(),mFilterbranchData.get(i).branch_head))
                            }
                            GenericDialog.newInstance("Head Branch",genericL as ArrayList<CustomData>){
                                str_filterBranchID = it.id
                                tv_headbranch.setText(it.name)
                                tv_subBranch.text = ""
                                str_filterSubBranchID=""
                                subBranchList =mFilterbranchData.filter { it.branch_head_id == str_filterBranchID.toInt() } as ArrayList<BranchData>
                                for(i in 0..subBranchList.size-1){
                                    subBranch_list = subBranchList.get(i).sub_branch
                                    if (subBranch_list.size > 0) {
                                       // tv_subBranch.text = subBranch_list.get(0).value
                                       // str_filterSubBranchID = subBranch_list.get(0).id.toString()
                                    }else{
                                        tv_subBranch.text = ""
                                        str_filterSubBranchID=""
                                    }
                                }

                            }.show((mContext as DashboardActivity).supportFragmentManager, "")
                        }else{
                            Toaster.msgShort(mContext, "No Branch Found")
                        }
                    }
                    else{
                        (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                    }

                    Handler().postDelayed(Runnable {
                        ll_branchFilter.isEnabled = true
                    }, 1000)
                }
                //Sub branch filter
                ll_subBranch.setOnClickListener {
                    println("subBranch_list>>>"+subBranch_list.size)
                    ll_subBranch.isEnabled = false
                    Handler().postDelayed(Runnable {
                        if (subBranch_list.size > 0) {
                            getSubBranchList(tv_subBranch, subBranch_list)
                        } else {
                            showNoDataDialog()
                        }
                    },500)
                    Handler().postDelayed(Runnable {
                        ll_subBranch.isEnabled = true
                    },1000)
                }
                //duration filter
                ll_durationFilter2.setOnClickListener {
                    ll_durationFilter2.isEnabled = false
                    var mFilterbranchData = ArrayList<LeaderBoardFilterOnDurationData>()
                    mFilterbranchData.add(LeaderBoardFilterOnDurationData(5,"MTD"))
                    mFilterbranchData.add(LeaderBoardFilterOnDurationData(5,"Overall"))
                    if(mFilterbranchData.size>0){
                        var genericL : ArrayList<CustomData> = ArrayList()
                        for(i in 0..mFilterbranchData.size-1){
                            genericL.add(CustomData(mFilterbranchData.get(i).id.toString(),mFilterbranchData.get(i).name.toString()))
                        }
                        GenericDialogWithOutSearch.newInstance("Duration",genericL as ArrayList<CustomData>){
                            str_filterDurationID = it.id
                            tv_durationown.setText(it.name)

                            if (it.name.equals("MTD")){
                                flag = "M"
                            }
                            if (it.name.equals("Overall")){
                                flag = "O"
                            }
                        }.show((mContext as DashboardActivity).supportFragmentManager, "")
                    }else{
                        Toaster.msgShort(mContext, "No Duration Found")
                    }

                    Handler().postDelayed(Runnable {
                        ll_durationFilter2.isEnabled = true
                    },1000)
                }

            }

        }
        tv_filterDoneOwn.setOnClickListener {
            str_filterBranchIDTemp = str_filterBranchID
            str_filterSubBranchIDTemp = str_filterSubBranchID
            println("subBranch_list--"+subBranch_list.size)
            subBranch_listTemp = subBranch_list
            println("subBranch_listTemp--"+subBranch_listTemp.size)
            if (isOverallOwnSelected==true){
                isFromManuOver=false
            }else{
                isFromManuOwn=false
            }
            if (ownclick) {
                if (!str_filterSubBranchID.equals("")) {
                    filterDialog!!.dismiss()
                    ownAPI()

                }
                else{
                    showNoDataDialog()
                }
            }else{
                if (!str_filterSubBranchID.equals("")) {
                    filterDialog!!.dismiss()
                    overAllAPI()
                }
                else{
                    showNoDataDialog()
                }
            }

        }
    }

    private fun showNoDataDialog() {
        val simpleDialog = Dialog(mContext)
        simpleDialog.setCancelable(true)
        simpleDialog.setCanceledOnTouchOutside(false)
        simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        simpleDialog.setContentView(R.layout.dialog_yes_no_3)
        val dialogHeader = simpleDialog.findViewById(R.id.dialog_cancel_order_header_TV) as AppCustomTextView
        dialogHeader.text = mContext.applicationContext.getString(R.string.branch_select)
        val dialog_yes_no_headerTV = simpleDialog.findViewById(R.id.dialog_yes_no_headerTV) as AppCustomTextView
        dialog_yes_no_headerTV.text = "Hi "+Pref.user_name!!+"!"
        val dialogYes = simpleDialog.findViewById(R.id.tv_dialog_yes_no_yes) as AppCustomTextView
        dialogYes.setOnClickListener({ view ->
            simpleDialog.cancel()
        })
        simpleDialog.show()
    }

    private fun overAllAPI() {
        progress_wheel.spin()
        val repository = LeaderboardRepoProvider.provideLeaderboardbranchRepository()
        BaseActivity.compositeDisposable.add(
            repository.overAllAPI(Pref.user_id!!,"All",str_filterSubBranchID,flag)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    progress_wheel.stopSpinning()
                    if(result.status == NetworkConstant.SUCCESS){
                        topStick_bar.setBackgroundColor(Color.parseColor("#3d30d7"));
                        rv_leaderboard.visibility = View.VISIBLE
                        overall_firstTo_thirdrank.visibility = View.VISIBLE
                        view_bar.visibility = View.VISIBLE
                        var mLeaderBoardData = result.user_list
                        // Split the list based on ranks
                        val firstRankL = mLeaderBoardData.filter { it.position == 1 }
                        val secondRankL = mLeaderBoardData.filter { it.position == 2 }
                        val thirdRankL = mLeaderBoardData.filter { it.position == 3 }
                        val fourthToTenthRank = mLeaderBoardData.filter { it.position > 3 }

                        if (firstRankL.size>0) {
                            Glide.with(mContext)
                                .load(firstRankL.get(0).profile_pictures_url)
                                .apply(
                                    RequestOptions.placeholderOf(R.drawable.user_blank)
                                        .error(R.drawable.user_blank)
                                )
                                .into(iv_firstP)

                            tv_firstPname.text = firstRankL.get(0).user_name
                            tv_firstPscore.text = firstRankL.get(0).totalscore.toString()
                        }else{
                            Glide.with(mContext)
                                .load(R.drawable.user_blank)
                                .apply(
                                    RequestOptions.placeholderOf(R.drawable.user_blank)
                                        .error(R.drawable.user_blank)
                                )
                                .into(iv_firstP)

                            tv_firstPname.text = "NA"
                            tv_firstPscore.text = "NA"
                        }
                        if (secondRankL.size>0) {
                            Glide.with(mContext)
                                .load(secondRankL.get(0).profile_pictures_url)
                                .apply(
                                    RequestOptions.placeholderOf(R.drawable.user_blank)
                                        .error(R.drawable.user_blank)
                                )
                                .into(iv_secondP)
                            tv_secondPname.text = secondRankL.get(0).user_name.toString()
                            tv_secondPscore.text = secondRankL.get(0).totalscore.toString()
                        }else{
                            Glide.with(mContext)
                                .load(R.drawable.user_blank)
                                .apply(
                                    RequestOptions.placeholderOf(R.drawable.user_blank)
                                        .error(R.drawable.user_blank)
                                )
                                .into(iv_secondP)
                            tv_secondPname.text = "NA"
                            tv_secondPscore.text = "NA"
                        }
                        if (thirdRankL.size>0) {
                            Glide.with(mContext)
                                .load(thirdRankL.get(0).profile_pictures_url)
                                .apply(
                                    RequestOptions.placeholderOf(R.drawable.user_blank)
                                        .error(R.drawable.user_blank)
                                )
                                .into(iv_thirdP)
                            tv_thirdPname.text = thirdRankL.get(0).user_name
                            tv_thirdPscore.text = thirdRankL.get(0).totalscore.toString()
                        }
                        else{
                            Glide.with(mContext)
                                .load(R.drawable.user_blank)
                                .apply(
                                    RequestOptions.placeholderOf(R.drawable.user_blank)
                                        .error(R.drawable.user_blank)
                                )
                                .into(iv_thirdP)
                            tv_thirdPname.text = "NA"
                            tv_thirdPscore.text = "NA"
                        }
                        mLeaderBoardAdapter = LeaderBoardAdapter(fourthToTenthRank as ArrayList<OverallUserListData>,mContext)
                        rv_leaderboard.layoutManager = LinearLayoutManager(context)
                        rv_leaderboard.setHasFixedSize(true)
                        rv_leaderboard.adapter = mLeaderBoardAdapter
                        mLeaderBoardAdapter.notifyDataSetChanged()
                        headerOfAllEmployee.text = "All Employees("+result.user_list.size.toString()+")"
                        ll_no_data_root_leader.visibility = View.INVISIBLE
                        iv_empty_data.visibility = View.INVISIBLE
                        tv_nodata.visibility = View.INVISIBLE

                    }else{
                        rv_leaderboard.visibility = View.INVISIBLE
                        view_bar.visibility = View.INVISIBLE
                        ll_no_data_root_leader.visibility = View.VISIBLE
                        overall_firstTo_thirdrank.visibility = View.INVISIBLE
                        iv_empty_data.visibility = View.INVISIBLE
                        tv_nodata.visibility = View.INVISIBLE
                        topStick_bar.setBackgroundColor(0x00000000)
                        (mContext as DashboardActivity).showSnackMessage(result.message.toString())
                    }
                }, { error ->
                    error.printStackTrace()
                    ll_no_data_root_leader.visibility = View.INVISIBLE
                    progress_wheel.stopSpinning()
                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                })
        )
    }

    private fun ownAPI() {
        progress_wheel.spin()
        val repository = LeaderboardRepoProvider.provideLeaderboardbranchRepository()
        BaseActivity.compositeDisposable.add(
            repository.ownDatalist(Pref.user_id!!,"All",/*str_filterSubBranchID*/ idMultiSubBranchL,flag)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->

                    Handler().postDelayed(Runnable {
                        if (result.status == NetworkConstant.SUCCESS) {
                            cardView_own.visibility = View.VISIBLE
                            iv_ownimg2.visibility =View.VISIBLE
                            iv_hand_ani.visibility = View.GONE

                            // tv_ownrank.visibility =View.VISIBLE

                            if (result.position != null || result.position == 0) {
                                tv_ownrank.text = "#" + result.position.toString()
                            }
                            if (result.user_name != null) {
                               // tv_ownname.text = result.user_name.toString()
                                tv_ownname.text = result.user_name
                            }
                            else{
                                tv_ownname.text = Pref.user_name.toString()
                            }
                            if (result.totalscore != null || result.totalscore == 0) {
                                tv_ownscore.text = result.totalscore.toString()
                                tv_ownscore.visibility =View.VISIBLE
                            }
                                if (result.attendance != null || result.attendance == 0) {
                                        tv_totalscoreA.text = result.attendance.toString()
                                    }
                                    if (result.new_visit != null || result.new_visit == 0) {
                                        tv_totalscoreAV.text = result.new_visit.toString()
                                    }
                                    if (result.revisit != null || result.revisit == 0) {
                                        tv_totalscoreRev.text = result.revisit.toString()
                                    }
                                    if (result.order != null || result.order == 0) {
                                        tv_totalscoreAOr.text = result.order.toString()
                                    }
                                    if (result.activities != null || result.activities == 0) {
                                        tv_totalscoreAActivities.text = result.activities.toString()
                                    }

                            if (result.position == 1) {
                                iv_OwnTag.visibility = View.VISIBLE
                                tv_ownrank.visibility =View.GONE
                                val param = iv_ownimg.layoutParams as ViewGroup.MarginLayoutParams
                                param.setMargins(0,75,0,0)
                                iv_ownimg.layoutParams = param

                                val param1 = tv_ownname.layoutParams as ViewGroup.MarginLayoutParams
                                param1.setMargins(0,60,0,0)
                                tv_ownname.layoutParams = param1

                                iv_OwnTag.setBackgroundResource(R.drawable.first_icon);
                            } else if (result.position == 2) {
                                iv_OwnTag.visibility = View.VISIBLE
                                tv_ownrank.visibility =View.GONE
                                val param = iv_ownimg.layoutParams as ViewGroup.MarginLayoutParams
                                param.setMargins(0,25,0,0)
                                iv_ownimg.layoutParams = param

                                val param1 = tv_ownname.layoutParams as ViewGroup.MarginLayoutParams
                                param1.setMargins(0,85,0,0)
                                tv_ownname.layoutParams = param1

                                iv_OwnTag.setBackgroundResource(R.drawable.second_icon)
                            } else if (result.position==3) {
                                iv_OwnTag.visibility = View.VISIBLE
                                tv_ownrank.visibility =View.GONE
                                val param = iv_ownimg.layoutParams as ViewGroup.MarginLayoutParams
                                param.setMargins(0,75,0,0)
                                iv_ownimg.layoutParams = param

                                val param1 = tv_ownname.layoutParams as ViewGroup.MarginLayoutParams
                                param1.setMargins(0,60,0,0)
                                tv_ownname.layoutParams = param1

                                iv_OwnTag.setBackgroundResource(R.drawable.third_icon)
                            } else {
                                iv_OwnTag.visibility = View.INVISIBLE
                                tv_ownrank.visibility =View.VISIBLE
                                val param = iv_ownimg.layoutParams as ViewGroup.MarginLayoutParams
                                param.setMargins(0,0,0,0)
                                iv_ownimg.layoutParams = param

                                val param1 = tv_ownname.layoutParams as ViewGroup.MarginLayoutParams
                                param1.setMargins(0,140,0,0)
                                tv_ownname.layoutParams = param1


                            }
                            iv_empty_data.visibility = View.INVISIBLE
                            tv_nodata.visibility = View.INVISIBLE
                        }
                        else {
                            if (result.status.equals("205")) {
                                cardView_own.visibility = View.INVISIBLE
                                iv_OwnTag.visibility = View.INVISIBLE
                                tv_nodata.visibility = View.VISIBLE
                                iv_ownimg2.visibility =View.VISIBLE
                                tv_ownrank.visibility =View.GONE
                                tv_ownscore.text ="NA"
                                tv_ownrank.text = "NA"
                                tv_ownname.text = Pref.user_name.toString()
                                iv_hand_ani.visibility = View.VISIBLE
                                (mContext as DashboardActivity).showSnackMessage(result.message.toString())

                                val param = iv_ownimg.layoutParams as ViewGroup.MarginLayoutParams
                                param.setMargins(0,25,0,0)
                                iv_ownimg.layoutParams = param

                                val param1 = tv_ownname.layoutParams as ViewGroup.MarginLayoutParams
                                param1.setMargins(0,105,0,0)
                                tv_ownname.layoutParams = param1

                            } else {
                                (mContext as DashboardActivity).showSnackMessage(result.message.toString())
                            }
                            iv_empty_data.visibility = View.VISIBLE
                            tv_nodata.visibility = View.VISIBLE
                            iv_ownimg2.visibility =View.VISIBLE
                            iv_OwnTag.visibility = View.INVISIBLE
                            tv_ownscore.text ="NA"
                            tv_ownrank.text ="NA"
                            tv_ownname.text = Pref.user_name.toString()
                            iv_hand_ani.visibility = View.VISIBLE

                        }
                    },1000)
                    Handler().postDelayed(Runnable {
                        progress_wheel.stopSpinning()
                    },1500)
                }, { error ->
                    error.printStackTrace()
                    progress_wheel.stopSpinning()
                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                })
        )
    }

    private fun getHeadBranchList(
        tv_headbranch: TextView,
        tv_subBranch: TextView
    ) {
            progress_wheel.spin()
            val repository = LeaderboardRepoProvider.provideLeaderboardbranchRepository()
            BaseActivity.compositeDisposable.add(
                repository.branchlist(Pref.session_token.toString())
                    .observeOn(AndroidSchedulers.mainThread())

                    .subscribeOn(Schedulers.io())
                    .subscribe({ result ->
                         respBranchData = result as LeaderboardBranchData
                         progress_wheel.stopSpinning()
                         if (respBranchData.branch_list.size > 0) {
                             tv_headbranch.text = respBranchData.branch_list.get(0).branch_head
                             str_filterBranchID = respBranchData.branch_list.get(0).branch_head_id.toString()
                           //  tv_subBranch.text = respBranchData.branch_list.get(0).sub_branch.get(0).value
                           //  str_filterSubBranchID =
                                 respBranchData.branch_list.get(0).sub_branch.get(0).id.toString()

                         }else{
                             tv_headbranch.text=""
                             str_filterBranchID=""
                             tv_subBranch.text = ""
                             str_filterSubBranchID=""

                         }
                        // overAllAPI()
                    }, { error ->
                        error.printStackTrace()
                        progress_wheel.stopSpinning()
                        (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                    })
            )
    }


    private fun getSubBranchList(tv_subBranch: TextView, subBranch_list: ArrayList<SubBranchData>) {
       /* var genericL : ArrayList<CustomData> = ArrayList()
        for(i in 0..subBranch_list.size-1){
                genericL.add(CustomData(subBranch_list.get(i).id.toString(),subBranch_list.get(i).value))

        }
        GenericDialog.newInstance("Branch",genericL as ArrayList<CustomData>){
            str_filterSubBranchID = it.id
            tv_subBranch.setText(it.name)
        }.show((mContext as DashboardActivity).supportFragmentManager, "")*/

        val subBranchDialog = Dialog(mContext)
        subBranchDialog.setCanceledOnTouchOutside(false)
        subBranchDialog.setCancelable(false)
        subBranchDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        subBranchDialog.setContentView(R.layout.dialog_leaderboard_subbranch_select)
        val rvSubBranchL = subBranchDialog.findViewById(R.id.rv_dialog_subBranch_list) as RecyclerView
        val tvHeader = subBranchDialog.findViewById(R.id.tv_dialog_subBranch_sel_header) as TextView
        val submit = subBranchDialog.findViewById(R.id.tv_dialog_subBranch_submit) as TextView
        val et_subBranchNameSearch =
            subBranchDialog.findViewById(R.id.et_dialog_subBranch_search) as AppCustomEditText
        val subBranch_selectAll =
            subBranchDialog.findViewById(R.id.cb_dialog_subBranch_select_all) as CheckBox
        val iv_close =
            subBranchDialog.findViewById(R.id.iv_dialog_generic_list_close_icon) as ImageView

        var totalLSize = subBranch_list.size
        var totalLTickSize = subBranch_list.filter { it.isTick }.size
        if (totalLSize == totalLTickSize){
            subBranch_selectAll.isChecked = true
            subBranch_selectAll.setText("Deselect All")
        }else{
            subBranch_selectAll.isChecked = false
            subBranch_selectAll.setText("Select All")
        }
        var subBranch_listTemp : ArrayList<SubBranchData> = ArrayList()
        adapterSubBranchName = AdapterSubBranchName(mContext,subBranch_list,object :AdapterSubBranchName.OnSubBrCLick{
            override fun onTick(obj: SubBranchData, isTick: Boolean) {
                if (isTick) {
                    subBranch_listTemp.add(obj)
                   /* var finalSUbBrL = subBranch_list.filter { it.isTick }
                    try {
                        var subBrNameL = finalSUbBrL.map { it.value }
                        var nameL = ""
                        for(i in 0..subBrNameL.size-1){
                            nameL = nameL + subBrNameL.get(i)+","
                        }
                        if (nameL.endsWith(",")) {
                            nameL = nameL.substring(0, nameL.length - 1);
                        }
                        tv_subBranch.text = nameL
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    try {
                        var subBrIdL = subBranch_list.filter { it.isTick }.map { it.id.toString() }
                        idMultiSubBranchL = ""
                        for (i in 0.. subBrIdL.size-1){
                            idMultiSubBranchL = idMultiSubBranchL+subBrIdL.get(i)+","
                        }
                        if (idMultiSubBranchL.endsWith(",")) {
                            idMultiSubBranchL = idMultiSubBranchL.substring(0, idMultiSubBranchL.length - 1);
                        }
                        str_filterSubBranchID = idMultiSubBranchL
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }*/
                } else {
                    subBranch_listTemp.remove(obj)
                    /* var finalSUbBrL = subBranch_list.filter { it.isTick }
                     try {
                         var subBrNameL = finalSUbBrL.map { it.value }
                         var nameL = ""
                         for(i in 0..subBrNameL.size-1){
                             nameL = nameL + subBrNameL.get(i)+","
                         }
                         if (nameL.endsWith(",")) {
                             nameL = nameL.substring(0, nameL.length - 1);
                         }
                         tv_subBranch.text = nameL
                     } catch (e: Exception) {
                         e.printStackTrace()
                     }
                     try {
                         var subBrIdL = subBranch_list.filter { it.isTick }.map { it.id.toString() }
                         idMultiSubBranchL = ""
                         for (i in 0.. subBrIdL.size-1){
                             idMultiSubBranchL = idMultiSubBranchL+subBrIdL.get(i)+","
                         }
                         if (idMultiSubBranchL.endsWith(",")) {
                             idMultiSubBranchL = idMultiSubBranchL.substring(0, idMultiSubBranchL.length - 1);
                         }
                         str_filterSubBranchID = idMultiSubBranchL
                     } catch (e: Exception) {
                         e.printStackTrace()
                     }*/
                }
            }


        }, {
            it
        })

        submit.setOnClickListener {
           var tickSize = subBranch_list.filter { it.isTick }.size
            if (tickSize==0){
                Toast.makeText(mContext, "Please select atleast one contact", Toast.LENGTH_SHORT).show()
            }
            else{
                var finalSUbBrL = subBranch_list.filter { it.isTick }
                try {
                    var subBrNameL = finalSUbBrL.map { it.value }
                    var nameL = ""
                    for(i in 0..subBrNameL.size-1){
                        nameL = nameL + subBrNameL.get(i)+","
                    }
                    if (nameL.endsWith(",")) {
                        nameL = nameL.substring(0, nameL.length - 1);
                    }
                    tv_subBranch.text = nameL
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                try {
                    var subBrIdL = subBranch_list.filter { it.isTick }.map { it.id.toString() }
                    idMultiSubBranchL = ""
                    for (i in 0.. subBrIdL.size-1){
                        idMultiSubBranchL = idMultiSubBranchL+subBrIdL.get(i)+","
                    }
                    if (idMultiSubBranchL.endsWith(",")) {
                        idMultiSubBranchL = idMultiSubBranchL.substring(0, idMultiSubBranchL.length - 1);
                    }
                    str_filterSubBranchID = idMultiSubBranchL
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                subBranchDialog.cancel()
            }
            var finalSUbBrL = subBranch_list.filter { it.isTick }
            try {
                var subBrNameL = finalSUbBrL.map { it.value }
                var nameL = ""
                for(i in 0..subBrNameL.size-1){
                    nameL = nameL + subBrNameL.get(i)+","
                }
                if (nameL.endsWith(",")) {
                    nameL = nameL.substring(0, nameL.length - 1);
                }
                tv_subBranch.text = nameL
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                var subBrIdL = subBranch_list.filter { it.isTick }.map { it.id.toString() }
                idMultiSubBranchL = ""
                for (i in 0.. subBrIdL.size-1){
                    idMultiSubBranchL = idMultiSubBranchL+subBrIdL.get(i)+","
                }
                if (idMultiSubBranchL.endsWith(",")) {
                    idMultiSubBranchL = idMultiSubBranchL.substring(0, idMultiSubBranchL.length - 1);
                }
                str_filterSubBranchID = idMultiSubBranchL
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


        subBranch_selectAll.setOnCheckedChangeListener { compoundButton, b ->
            println("tag_selectAllCheck")
            if (compoundButton.isChecked) {
                adapterSubBranchName.selectAll()
                subBranch_selectAll.setText("Deselect All")
            } else {
                adapterSubBranchName.deselectAll()
                subBranch_selectAll.setText("Select All")
            }
        }
        et_subBranchNameSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                adapterSubBranchName!!.getFilter().filter(et_subBranchNameSearch.text.toString().trim())
            }
        })

        iv_close.setOnClickListener {
            for (i in 0..subBranch_listTemp.size-1){
                subBranch_list.filter { it.id == subBranch_listTemp.get(i).id }.first().isTick=false
            }
            subBranchDialog.dismiss()
        }

        rvSubBranchL.adapter = adapterSubBranchName
        subBranchDialog.show()
    }
    fun setToolbar(){
        if (!ownclick) {
            (mContext as DashboardActivity).setTopBarTitle("Leaderboard")
            (mContext as DashboardActivity).setTopBarVisibility(TopBarConfig.LEADERBOARD)
        }else{
            (mContext as DashboardActivity).setTopBarTitle("Leaderboard (Me)")
            (mContext as DashboardActivity).setTopBarVisibility(TopBarConfig.LEADERBOARD_OWN)

        }
    }

    override fun onResume() {
        super.onResume()
        println("onResume")
    }

    override fun onStart() {
        super.onStart()
        println("onStart")

    }

    override fun onDestroy() {
        super.onDestroy()
        println("onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        println("onDetach")
    }

    override fun onPause() {
        super.onPause()
        println("onPause")
    }

    override fun onStop() {
        super.onStop()
        println("onStop")
    }
}