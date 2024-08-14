package com.breezenationalplasticfsm.features.contacts

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
import com.breezenationalplasticfsm.app.domain.NewOrderProductEntity
import com.breezenationalplasticfsm.features.member.model.TeamListDataModel
import com.breezenationalplasticfsm.features.viewAllOrder.interf.ProductListNewOrderOnClick
import com.breezenationalplasticfsm.features.viewAllOrder.presentation.ProductListNewOrderAdapter
import com.breezenationalplasticfsm.features.viewAllOrder.presentation.ProductListNewOrderDialog
import com.breezenationalplasticfsm.widgets.AppCustomEditText
import com.breezenationalplasticfsm.widgets.AppCustomTextView

class TeamShowDialog: DialogFragment() {

    private lateinit var header: AppCustomTextView
    private lateinit var close: ImageView
    private lateinit var rv_team: RecyclerView
    private  var adapter: AdapterTeamShow? = null
    private lateinit  var et_search: AppCustomEditText
    private lateinit var mContext: Context

    companion object{
        private lateinit var onSelectItem: (TeamListDataModel) -> Unit
        private var mList: ArrayList<TeamListDataModel>? = null

        fun newInstance(pList: ArrayList<TeamListDataModel>, function: (TeamListDataModel) -> Unit): TeamShowDialog {
            val dialogFragment = TeamShowDialog()
            mList = pList
            onSelectItem = function
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
        val v = inflater.inflate(R.layout.dialog_team_list, container, false)
        isCancelable = false
        initView(v)
        initTextChangeListener()
        return v
    }

    private fun initView(v: View){
        header=v!!.findViewById(R.id.tv_dialog_team_list_header)
        close=v!!.findViewById(R.id.iv_dialog_team_list_close_icon)
        rv_team=v!!.findViewById(R.id.rv_dialog_team_list)
        et_search=v!!.findViewById(R.id.et_dialog_team_search)

        header.text="Select Member"
        rv_team.layoutManager = LinearLayoutManager(mContext)

        adapter= AdapterTeamShow(mContext,mList!! as ArrayList<TeamListDataModel>,object :
            AdapterTeamShow.onCLick {
            override fun onclick(obj: TeamListDataModel) {
                dismiss()
                onSelectItem(obj)
            }
        })
        rv_team.adapter=adapter

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