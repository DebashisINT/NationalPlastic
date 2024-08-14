package com.breezenationalplasticfsm.features.performanceAPP

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
import com.breezenationalplasticfsm.R
import com.breezenationalplasticfsm.app.domain.AddShopDBModelEntity
import com.breezenationalplasticfsm.features.nearbyshops.model.ShopData
import com.breezenationalplasticfsm.widgets.AppCustomEditText
import com.breezenationalplasticfsm.widgets.AppCustomTextView

/**
 * Created by Saheli on 12-03-2023 v 4.0.8 mantis 0025860.
 */
class ShopListDialog: DialogFragment() {

    private lateinit var header: AppCustomTextView
    private lateinit var close: ImageView
    private lateinit var rv_gender: RecyclerView
    private  var adapter: ShopListAdapter? = null
    private lateinit var mContext: Context
    private lateinit  var et_search: AppCustomEditText


    companion object{
        private lateinit var onSelectItem: (ShopData) -> Unit
        private var msalesmanList: ArrayList<ShopData>? = null
        private var headerStr = ""

        fun newInstance(header:String, gList: ArrayList<ShopData>, function: (ShopData) -> Unit): ShopListDialog {
            val dialogFragment = ShopListDialog()
            msalesmanList = gList
            onSelectItem = function
            headerStr = header
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

        val v = inflater.inflate(R.layout.dialog_salesman_list, container, false)

        isCancelable = false

        initView(v)
        initTextChangeListener()
        return v
    }

    private fun initView(v: View){
        header=v.findViewById(R.id.tv_dialog_list_header)
        close=v.findViewById(R.id.iv_dialog_gender_list_close_icon)
        rv_gender=v.findViewById(R.id.rv_dialog_list)
        rv_gender.layoutManager = LinearLayoutManager(mContext)
        et_search=v!!.findViewById(R.id.et_dialog_product_search)

        //header.text="Select Salesman"
        header.text=headerStr

        adapter=ShopListAdapter(mContext, msalesmanList!!,object: ShopListAdapter.OnClick {
            override fun OnClick(obj: ShopData) {
                dismiss()
                ShopListDialog.onSelectItem(obj)
            }
        })
        rv_gender.adapter=adapter

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