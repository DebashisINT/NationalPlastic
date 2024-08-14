package com.breezenationalplasticfsm.features.orderITC

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.breezedsm.app.domain.NewOrderDataEntity
import com.breezenationalplasticfsm.R
import com.breezenationalplasticfsm.app.AppDatabase
import com.breezenationalplasticfsm.app.domain.AddShopDBModelEntity
import com.breezenationalplasticfsm.app.domain.NewOrderProductDataEntity
import com.breezenationalplasticfsm.app.utils.AppUtils
import com.breezenationalplasticfsm.base.presentation.BaseFragment

class ViewOrdDtls: BaseFragment(), View.OnClickListener {
    private lateinit var mContext: Context
    lateinit var shopObj : AddShopDBModelEntity
    lateinit var ordDtlsObj : NewOrderDataEntity

    private lateinit var tv_shopName:TextView
    private lateinit var tv_ordID:TextView
    private lateinit var tv_ordDate:TextView
    private lateinit var tv_totalItemCount:TextView
    private lateinit var tv_totalOrderValue:TextView
    private lateinit var rv_ordDtls:RecyclerView

    private lateinit var adapterOrdDtls:AdapterOrdDtls

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    companion object{
        var order_id:String = ""
        fun getInstance(objects: Any): ViewOrdDtls {
            val viewOrdDtls = ViewOrdDtls()
            if (!TextUtils.isEmpty(objects.toString())) {
                order_id=objects.toString()
            }
            return viewOrdDtls
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.frag_ord_dtls, container, false)
        initView(view)
        return view
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun initView(view: View) {
        tv_shopName = view!!.findViewById(R.id.tv_frag_ord_dtls_shop_name)
        tv_ordID = view!!.findViewById(R.id.tv_frag_ord_dtls_ord_id)
        tv_ordDate = view!!.findViewById(R.id.tv_frag_ord_dtls_ord_date)
        rv_ordDtls = view!!.findViewById(R.id.rv_frag_ord_dtls)
        tv_totalItemCount = view!!.findViewById(R.id.tv_frag_ord_dtls_total_item)
        tv_totalOrderValue = view!!.findViewById(R.id.tv_frag_ord_dtls_total_value)

        try{
            ordDtlsObj = AppDatabase.getDBInstance()?.newOrderDataDao()?.getOrderByID(order_id)!!
            shopObj = AppDatabase.getDBInstance()?.addShopEntryDao()?.getShopByIdN(ordDtlsObj.shop_id)!!
            showData()
        }catch (ex:Exception){
            ex.printStackTrace()
        }

    }

    fun showData(){
        var productL = AppDatabase.getDBInstance()?.newOrderProductDataDao()?.getProductsOrder(order_id) as ArrayList<NewOrderProductDataEntity>
        tv_shopName.text = shopObj.shopName
        val text = "<font color=" + mContext.resources.getColor(R.color.dark_gray) + ">Order ID: </font> <font color="+
                mContext.resources.getColor(R.color.black) + ">" + "${ordDtlsObj.order_id}" + "</font>"
        tv_ordID.text = Html.fromHtml(text)
        tv_ordDate.text = AppUtils.convertToDateLikeOrderFormat(ordDtlsObj.order_date)
        val itemCnt = "<font color=" + mContext.resources.getColor(R.color.dark_gray) + ">Total item(s): </font> <font color="+
                mContext.resources.getColor(R.color.black) + ">" + productL!!.size + "</font>"
        tv_totalItemCount.text = Html.fromHtml(itemCnt)
        val ordValue = "<font color=" + mContext.resources.getColor(R.color.dark_gray) + ">Total Order Value(₹): </font> <font color="+
                mContext.resources.getColor(R.color.black) + ">" + String.format("%.02f",ordDtlsObj.order_total_amt.toDouble()) + "</font>"
        tv_totalOrderValue.text = Html.fromHtml(ordValue)

        adapterOrdDtls = AdapterOrdDtls(mContext,productL)
        rv_ordDtls.adapter = adapterOrdDtls
    }

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }

}