package com.breezenationalplasticfsm.features.nearbyshops.presentation

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.PopupWindow
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.breezenationalplasticfsm.R
import com.breezenationalplasticfsm.app.domain.ProspectEntity
import com.breezenationalplasticfsm.app.utils.Toaster
import com.breezenationalplasticfsm.widgets.AppCustomTextView

class UpdateShopStatusDialog: DialogFragment(), View.OnClickListener {
    private lateinit var mContext: Context

    private lateinit var dialogHeader: AppCustomTextView
    private lateinit var dialogCancel: AppCustomTextView
    private lateinit var selType: AppCustomTextView
    private lateinit var dialogOk: AppCustomTextView
    private lateinit var tv_ds_type_dropdown: AppCustomTextView
    private lateinit var cv_ds_type_main: CardView

    private  var selectedTypeID: String = ""
    private  var selectedTypeName: String = ""

    private var dsTypePopupWindow: PopupWindow? = null

    val dsList = ArrayList<String>()




    companion object {

        private lateinit var mHeader: String
        private lateinit var mBody: String
        private lateinit var mLeftBtn: String
        private lateinit var mRightBtn: String
        private lateinit var mSelType: String
        private lateinit var muserIdForTypeUpdate: String
        private var mIsCancelable: Boolean = true
        private lateinit var mListener: OnDSButtonClickListener

        fun getInstance(header: String, leftCancel: String, rightOk: String, isCancelable: Boolean,selectedType:String, usrId:String,bodyText:String="",listener: OnDSButtonClickListener): UpdateShopStatusDialog {
            val cardFragment = UpdateShopStatusDialog()
            mHeader = header
            mLeftBtn = leftCancel
            mRightBtn = rightOk
            mIsCancelable = isCancelable
            mSelType=selectedType
            muserIdForTypeUpdate=usrId
            mListener = listener
            mBody = bodyText
            return cardFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        dialog?.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        dialog?.window!!.setBackgroundDrawableResource(R.drawable.rounded_corner_white_bg)
        val v = inflater.inflate(R.layout.dialog_fragment_shop_update_status, container, false)
        isCancelable = mIsCancelable
        initView(v)
        dsList.add("Active")
        dsList.add("Inactive")
        return v
    }

    private fun initView(v: View) {
        dialogHeader = v.findViewById(R.id.dialog_header_TV)
        dialogCancel = v.findViewById(R.id.cancel_TV)
        dialogOk = v.findViewById(R.id.ok_TV)
        dialogOk.isSelected = true
        tv_ds_type_dropdown= v.findViewById(R.id.tv_ds_type_dropdown)
        cv_ds_type_main= v.findViewById(R.id.cv_ds_type_main)
        selType= v.findViewById(R.id.tv_dialog_frag_add_update_ds_selected_type)

        dialogOk.text=mRightBtn
        dialogCancel.text= mLeftBtn

        dialogHeader.text="Select status for\n"+mHeader
        //tv_ds_type_dropdown.hint = "Select Shop Status"
        tv_ds_type_dropdown.hint = mBody
        selType.text=mSelType
        tv_ds_type_dropdown.setOnClickListener(this)
        dialogCancel.setOnClickListener(this)
        dialogOk.setOnClickListener(this)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }


    interface OnDSButtonClickListener {
        fun onLeftClick()
        fun onRightClick(status:String)
    }

    override fun onClick(v: View?) {
        when (v!!.id){
            R.id.tv_ds_type_dropdown ->{
                if (dsTypePopupWindow != null && dsTypePopupWindow?.isShowing!!)
                    dsTypePopupWindow?.dismiss()
                else {
                    if (dsList == null || dsList!!.size == 0) {
                        Toaster.msgShort(mContext, getString(R.string.no_data_available))
                        return
                    }
                    callShopUpdateTypeDropDownPopUp()
                }
            }
            R.id.cancel_TV ->{
                mListener.onLeftClick()
                dismiss()
            }
            R.id.ok_TV ->{
                mListener.onRightClick(tv_ds_type_dropdown.text.toString())
                dismiss()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun callShopUpdateTypeDropDownPopUp() {

        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?
        val customView = inflater!!.inflate(R.layout.popup_meeting_type, null)

        dsTypePopupWindow = PopupWindow(customView, resources.getDimensionPixelOffset(R.dimen._220sdp), RelativeLayout.LayoutParams.WRAP_CONTENT)
        val rv_ds_type_list = customView.findViewById(R.id.rv_meeting_type_list) as RecyclerView
        rv_ds_type_list.layoutManager = LinearLayoutManager(mContext)

        dsTypePopupWindow?.elevation = 200f
        dsTypePopupWindow?.isFocusable = true
        dsTypePopupWindow?.update()

        rv_ds_type_list.adapter = ShopUpdateStatusAdapter(mContext, dsList,object:ShopStatusListner{
            override fun getStatusInfoOnLick(pos: String) {
                tv_ds_type_dropdown.text = pos
                selectedTypeName=pos
                dsTypePopupWindow?.dismiss()
            }
        })

        if (dsTypePopupWindow != null && !dsTypePopupWindow?.isShowing!!) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                cv_ds_type_main.post(Runnable {
                    dsTypePopupWindow?.showAsDropDown(tv_ds_type_dropdown, resources.getDimensionPixelOffset(R.dimen._10sdp), resources.getDimensionPixelOffset(R.dimen._20sdp), Gravity.BOTTOM)
                })
            } else {
                dsTypePopupWindow?.showAsDropDown(tv_ds_type_dropdown, tv_ds_type_dropdown.width - tv_ds_type_dropdown?.width!!, 0)
            }
        }
    }




}