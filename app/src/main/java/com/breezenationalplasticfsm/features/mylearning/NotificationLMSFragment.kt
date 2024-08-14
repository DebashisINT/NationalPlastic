package com.breezenationalplasticfsm.features.mylearning

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.breezenationalplasticfsm.R
import com.breezenationalplasticfsm.base.presentation.BaseFragment

class NotificationLMSFragment : BaseFragment(), View.OnClickListener {
    private lateinit var mContext: Context
    private lateinit var recyclerView: RecyclerView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext=context
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater!!.inflate(R.layout.fragment_notification_l_m_s, container, false)

        initView(view)
        return view
    }

    private fun initView(view: View) {
        recyclerView = view.findViewById(R.id.rv_notification)

        val headerItems = listOf(
            HeaderItem("Today", listOf(
                ValueItem("New sales video added","It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout.", R.drawable.bell),
                ValueItem("New sales PDF added","It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout.", R.drawable.bell)
            )),
            HeaderItem("Yesterday", listOf(
                ValueItem("New sales video added","It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout.", R.drawable.bell),
                ValueItem("New sales PDF added","It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout.", R.drawable.bell)
            ))
        )

        val headerAdapter = HeaderAdapterForLMSNotification(headerItems)
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        recyclerView.adapter = headerAdapter
    }

    companion object {
        fun getInstance(objects: Any): NotificationLMSFragment {
            val fragment = NotificationLMSFragment()
            return fragment
        }
    }

    override fun onClick(p0: View?) {

    }
}