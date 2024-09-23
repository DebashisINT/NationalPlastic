package com.breezefieldnationalplastic.features.orderITC

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.breezefieldnationalplastic.R
import com.breezefieldnationalplastic.features.orderITC.CommonProductCatagory
import com.breezefieldnationalplastic.features.orderITC.OrderOptiCatagoryOnClick
import com.breezefieldnationalplastic.widgets.AppCustomEditText
import com.breezefieldnationalplastic.widgets.AppCustomTextView

class ProductCatagoryDialog: DialogFragment() {
    private lateinit var header: AppCustomTextView
    private lateinit var close: ImageView
    private lateinit var rv_catagory: RecyclerView
    private  var adapter: ProductCatagoryDialogAdapter? = null
    private lateinit var mContext: Context
    private lateinit  var et_search: AppCustomEditText

    companion object{
        private lateinit var onSelectItem: (CommonProductCatagory) -> Unit
        private var mList: ArrayList<CommonProductCatagory>? = null
        private var headerStr: String = ""

        fun newInstance(gList: ArrayList<CommonProductCatagory>,headerStr:String, function: (CommonProductCatagory) -> Unit): ProductCatagoryDialog {
            val dialogFragment = ProductCatagoryDialog()
            ProductCatagoryDialog.mList = gList
            ProductCatagoryDialog.onSelectItem = function
            return dialogFragment
        }
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        dialog?.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCanceledOnTouchOutside(true)
        dialog?.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val v = inflater.inflate(R.layout.dialog_order_catagory, container, false)

        isCancelable = false

        initView(v)
        initTextChangeListener()
        return v
    }

    private fun initView(v: View){
        header=v.findViewById(R.id.tv_dialog_order_catagory_list_header)
        close=v.findViewById(R.id.iv_dialog_order_catagory_list_close_icon)
        rv_catagory=v.findViewById(R.id.rv_dialog_order_catagory_list)
        et_search=v!!.findViewById(R.id.et_dialog_order_catagory_search)
        rv_catagory.layoutManager = LinearLayoutManager(mContext)

        header.text=headerStr

        adapter=ProductCatagoryDialogAdapter(mContext,ProductCatagoryDialog.mList!!,object :
            OrderOptiCatagoryOnClick {
            override fun catagoryListOnClick(objSel: CommonProductCatagory) {
                dismiss()
                onSelectItem(objSel)
            }
        })
        rv_catagory.adapter=adapter

        close.apply {
            visibility = View.VISIBLE
            setOnClickListener {
                dismiss()
            }
        }
    }

    private fun initTextChangeListener() {
        et_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                adapter!!.getFilter().filter(et_search.text.toString().trim())
            }
        })
    }

}