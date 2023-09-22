package com.nationalplasticfsm.features.addAttendence

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
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nationalplasticfsm.R
import com.nationalplasticfsm.app.domain.NewOrderProductEntity
import com.nationalplasticfsm.app.utils.AppUtils
import com.nationalplasticfsm.features.addAttendence.model.AreaList
import com.nationalplasticfsm.features.addAttendence.model.ReimbListModel
import com.nationalplasticfsm.features.addAttendence.model.ReimbListOnClick
import com.nationalplasticfsm.features.viewAllOrder.presentation.ProductListNewOrderAdapter
import com.nationalplasticfsm.features.viewAllOrder.presentation.ProductListNewOrderDialog
import com.nationalplasticfsm.widgets.AppCustomEditText

class ReimbursementListCustomDialog:DialogFragment() {

    private lateinit var headerTv:TextView
    private lateinit var crossIV: ImageView
    private lateinit var searchET: AppCustomEditText
    private lateinit var rvList: RecyclerView
    private  var adapter: AreaListAdapter? = null
    private lateinit var mContext: Context

    companion object{
        private var mList: ArrayList<AreaList>? = null
        private lateinit var onSelectItem: (AreaList) -> Unit
        fun newInstance(pList: ArrayList<AreaList>, function: (AreaList) -> Unit): ReimbursementListCustomDialog {
            val dialogFragment = ReimbursementListCustomDialog()
            mList = pList
            onSelectItem = function
            return dialogFragment
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        dialog?.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCanceledOnTouchOutside(true)
        dialog?.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val v = inflater.inflate(R.layout.dialog_reimb_list_custom, container, false)
        isCancelable = false

        initView(v)
        initTextChangeListener()
        return v
    }

    private fun initView(v: View){
        headerTv = v.findViewById(R.id.tv_dialog_reimb_list_header)
        crossIV = v.findViewById(R.id.iv_dialog_reimb_list_close_icon)
        searchET = v.findViewById(R.id.et_dialog_reimb_search)
        rvList = v.findViewById(R.id.rv_dialog_reimb_list)
        rvList.layoutManager = LinearLayoutManager(mContext)

        headerTv.text = AppUtils.hiFirstNameText()

        adapter = AreaListAdapter(mContext,mList!!,object : AreaListAdapter.onCLick {
            override fun onTick(obj: AreaList) {
                onSelectItem(obj)
                dismiss()
            }
        })
        rvList.adapter = adapter
        crossIV.apply {
            visibility = View.VISIBLE
            setOnClickListener {
                dismiss()
            }
        }
    }

    private fun initTextChangeListener() {
        /*searchET.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                adapter!!.getFilter().filter(searchET.text.toString().trim())
            }
        })*/
    }

}