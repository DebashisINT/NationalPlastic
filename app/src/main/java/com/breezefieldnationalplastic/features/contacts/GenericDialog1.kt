package com.breezefieldnationalplastic.features.contacts

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
import com.breezefieldnationalplastic.widgets.AppCustomEditText
import com.breezefieldnationalplastic.widgets.AppCustomTextView

class GenericDialog1: DialogFragment() {

    private lateinit var header: AppCustomTextView
    private lateinit var close: ImageView
    private lateinit var rv_list: RecyclerView
    private  var adapter: AdapterGenericDialog? = null
    private lateinit  var et_search: AppCustomEditText
    private lateinit var mContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    companion object{
        private lateinit var onSelectItem: (CustomData) -> Unit
        private lateinit var onSelectCross: () -> Unit
        private var mList: ArrayList<CustomData>? = null
        private var headerStr=""

        fun newInstance(headerText:String,pList: ArrayList<CustomData>, function: (CustomData) -> Unit , function1: () -> Unit): GenericDialog1 {
            val dialogFragment = GenericDialog1()
            mList = pList
            onSelectItem = function
            onSelectCross = function1
            headerStr=headerText
            return dialogFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        dialog?.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.setCancelable(true)
        dialog?.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val v = inflater.inflate(R.layout.dialog_generic, container, false)
      //  isCancelable = false
        initView(v)
        initTextChangeListener()
        return v
    }

    private fun initView(v: View){
        header=v!!.findViewById(R.id.tv_dialog_generic_list_header)
        close=v!!.findViewById(R.id.iv_dialog_generic_list_close_icon)
        rv_list=v!!.findViewById(R.id.rv_dialog_generic_list)
        et_search=v!!.findViewById(R.id.et_dialog_generic_search)

        header.text="Select $headerStr"
        rv_list.layoutManager = LinearLayoutManager(mContext)

        adapter= AdapterGenericDialog(mContext, mList!! as ArrayList<CustomData>,object :
            AdapterGenericDialog.onCLick {
            override fun onclick(obj: CustomData) {
                dismiss()
                onSelectItem(obj)
            }
        })
        rv_list.adapter=adapter

        close.apply {
            visibility = View.VISIBLE
            setOnClickListener {
                dismiss()
                onSelectCross()
            }
        }

        dialog!!.setOnDismissListener {
           onSelectCross()
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