package com.breezenationalplasticfsm.features.pendinglocationinout

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.breezenationalplasticfsm.R
import com.breezenationalplasticfsm.app.AppDatabase
import com.breezenationalplasticfsm.app.Pref
import com.breezenationalplasticfsm.app.domain.AddShopDBModelEntity
import com.breezenationalplasticfsm.app.utils.AppUtils
import com.breezenationalplasticfsm.base.presentation.BaseFragment
import com.breezenationalplasticfsm.features.NewQuotation.ViewAllQuotListFragment
import com.breezenationalplasticfsm.features.commondialogsinglebtn.AddFeedbackSingleBtnDialog
import com.breezenationalplasticfsm.features.dashboard.presentation.DashboardActivity
import com.breezenationalplasticfsm.features.shopFeedbackHistory.ShopFeedbackHisFrag
import com.breezenationalplasticfsm.widgets.AppCustomTextView
import com.pnikosis.materialishprogress.ProgressWheel
import kotlinx.android.synthetic.main.inflate_nearby_shops.view.*

class PendingOutLocationFrag: BaseFragment(),
    View.OnClickListener  {
    private lateinit var mContext: Context
    private lateinit var tv_shop_name: AppCustomTextView
    private lateinit var tv_shop_address: AppCustomTextView
    private lateinit var tv_phone_number: AppCustomTextView
    private lateinit var tv_shop_type: AppCustomTextView
    private lateinit var visit_TV: AppCustomTextView
    private lateinit var ll_root: LinearLayout
    private lateinit var visit_icon: ImageView
    private lateinit var shop_image_view: ImageView

    private var shopIdStr = ""
    private var shopNameStr = ""
    private var shopContactStr = ""
    private lateinit var tv_no_data: AppCustomTextView
    private lateinit var progress_wheel: ProgressWheel

    private var feedbackDialog: AddFeedbackSingleBtnDialog? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.frag_pending_out_location, container, false)
        initView(view)
        return view
    }

    @SuppressLint("RestrictedApi")
    private fun initView(view: View) {
        tv_shop_name =  view.findViewById(R.id.myshop_name_TV)
        tv_shop_address = view.findViewById(R.id.myshop_address_TV)
        tv_phone_number = view.findViewById(R.id.tv_shop_contact_no)
        tv_shop_type = view.findViewById(R.id.myshop_Type_TV)
        visit_TV = view.findViewById(R.id.frag_pending_visit_TV)
        ll_root = view.findViewById(R.id.ll_frag_pending_in_out_root)
        visit_icon = view.findViewById(R.id.visit_icon)
        tv_no_data = view.findViewById(R.id.tv_no_data)
        progress_wheel= view.findViewById(R.id.progress_wheel)
        shop_image_view = view.findViewById(R.id.shop_IV)

        visit_TV.setOnClickListener(this)
        visit_icon.setOnClickListener(this)
        progress_wheel.stopSpinning()

        doWork()

    }

    fun doWork(){
        var objL =  AppDatabase.getDBInstance()!!.shopActivityDao().getDurationCalculatedVisitedShopForADay(AppUtils.getCurrentDateForShopActi(), false)
        if(objL.size>0){
            var obj = objL.get(0)
            tv_shop_name.text  = obj.shop_name
            shopNameStr =  obj.shop_name.toString()
            tv_shop_address.text = obj.shop_address
            val shop = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopByIdN(obj.shopid)
            tv_phone_number.text  = shop.ownerContactNumber
            shopContactStr = shop.ownerContactNumber.toString()

            var shopNameByID=""
            try{
                shopNameByID = AppDatabase.getDBInstance()!!.shopTypeDao().getShopNameById(shop.type)
            }catch (ex:Exception){
                shopNameByID = "N.A"
            }
            tv_shop_type.text = shopNameByID

            val drawable = TextDrawable.builder()
                .buildRoundRect(obj.shop_name!!.toUpperCase().take(1), ColorGenerator.MATERIAL.randomColor, 120)
            shop_image_view.setImageDrawable(drawable)

            visit_TV.text = "Out Location"
            visit_TV.setTextColor(ContextCompat.getColor(mContext, R.color.red))
            visit_icon.setColorFilter(ContextCompat.getColor(mContext,R.color.red),android.graphics.PorterDuff.Mode.SRC_IN)

            shopIdStr = shop.shop_id

        }else{
            ll_root.visibility=View.GONE
            tv_no_data.visibility = View.VISIBLE
        }
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.visit_icon , R.id.frag_pending_visit_TV ->{

                feedbackDialog = AddFeedbackSingleBtnDialog.getInstance(shopNameStr + "\n" + shopContactStr, getString(R.string.confirm_revisit), shopIdStr, object : AddFeedbackSingleBtnDialog.OnOkClickListener {
                    override fun onOkClick(mFeedback: String, mNextVisitDate: String, filePath: String, mapproxValue: String, mprosId: String,sel_extraContNameStr:String,sel_extraContPhStr:String,filePathNW:String) {
                        AppDatabase.getDBInstance()!!.shopActivityDao().updateFeedbackVisitdate(mFeedback,mNextVisitDate, shopIdStr, AppUtils.getCurrentDateForShopActi())
                        proceed()
                    }

                    override fun onCloseClick(mfeedback: String,sel_extraContNameStr :String,sel_extraContPhStr : String) {
                        //
                        proceed()
                    }

                    override fun onClickCompetitorImg() {
                        //
                    }
                })
                feedbackDialog?.show((mContext as DashboardActivity).supportFragmentManager, "AddFeedbackSingleBtnDialog")

            }
        }

    }

    fun proceed(){
        progress_wheel.spin()
        AppUtils.endShopDuration(shopIdStr,mContext)

        var obL = AppDatabase.getDBInstance()!!.shopActivityDao().getShopForDay(shopIdStr, AppUtils.getCurrentDateForShopActi())
        progress_wheel.stopSpinning()
        val simpleDialog = Dialog(mContext)
        simpleDialog.setCancelable(false)
        simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        simpleDialog.setContentView(R.layout.dialog_ok)

        try {
            simpleDialog.setCancelable(true)
            simpleDialog.setCanceledOnTouchOutside(false)
            val dialogName = simpleDialog.findViewById(R.id.tv_dialog_ok_name) as AppCustomTextView
            val dialogCross = simpleDialog.findViewById(R.id.tv_dialog_ok_cancel) as ImageView
            dialogName.text = AppUtils.hiFirstNameText()
            dialogCross.setOnClickListener {
                simpleDialog.cancel()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val dialogHeader = simpleDialog.findViewById(R.id.dialog_yes_header_TV) as AppCustomTextView
        dialogHeader.text = "Your spend duration for the outlet ${obL.get(0).shop_name} is ${obL.get(0).duration_spent}"
        val dialogYes = simpleDialog.findViewById(R.id.tv_dialog_yes) as AppCustomTextView
        dialogYes.setOnClickListener({ view ->
            simpleDialog.cancel()
            doWork()
        })
        simpleDialog.show()
    }

}