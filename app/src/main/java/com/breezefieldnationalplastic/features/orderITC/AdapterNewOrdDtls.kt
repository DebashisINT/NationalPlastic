package com.breezefieldnationalplastic.features.orderITC

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.breezedsm.app.domain.NewOrderDataEntity
import com.breezefieldnationalplastic.R
import com.breezefieldnationalplastic.app.AppDatabase
import com.breezefieldnationalplastic.app.domain.NewOrderProductDataEntity
import com.breezefieldnationalplastic.app.utils.AppUtils
import kotlinx.android.synthetic.main.row_ord_dtls_new.view.iv_row_ord_dtls_share
import kotlinx.android.synthetic.main.row_ord_dtls_new.view.iv_row_ord_dtls_sync
import kotlinx.android.synthetic.main.row_ord_dtls_new.view.iv_row_ord_dtls_view
import kotlinx.android.synthetic.main.row_ord_dtls_new.view.tv_row_ord_dtls_order_amt
import kotlinx.android.synthetic.main.row_ord_dtls_new.view.tv_row_ord_dtls_order_date
import kotlinx.android.synthetic.main.row_ord_dtls_new.view.tv_row_ord_dtls_order_id
import kotlinx.android.synthetic.main.row_ord_dtls_new.view.tv_row_ord_dtls_order_items
import kotlinx.android.synthetic.main.row_ord_dtls_new.view.tv_row_ord_dtls_order_shop_addr
import kotlinx.android.synthetic.main.row_ord_dtls_new.view.tv_row_ord_dtls_order_shop_name
import kotlinx.android.synthetic.main.row_order_list.view.iv_row_ord_sync
import timber.log.Timber

class AdapterNewOrdDtls(var mContext:Context, var ordL:ArrayList<NewOrderDataEntity>, var listner: AdapterNewOrdDtls.OnCLick):
    RecyclerView.Adapter<AdapterNewOrdDtls.NewOrdDtlsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewOrdDtlsViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.row_ord_dtls_new,parent,false)
        return NewOrdDtlsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return ordL.size
    }

    override fun onBindViewHolder(holder: NewOrdDtlsViewHolder, position: Int) {
        holder.bindItems(mContext,ordL,listner)
    }

    inner class NewOrdDtlsViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        fun bindItems(context: Context,mList:ArrayList<NewOrderDataEntity>,listner : AdapterNewOrdDtls.OnCLick){
            itemView.apply {
                var orderIdtext = "<font color=" + mContext.resources.getColor(R.color.dark_gray) + ">Order ID: </font> <font color="+
                    mContext.resources.getColor(R.color.black) + ">" + "${mList.get(adapterPosition).order_id}" + "</font>"
                var orderDatetext = "<font color=" + mContext.resources.getColor(R.color.dark_gray) + ">Date: </font> <font color="+
                        mContext.resources.getColor(R.color.black) + ">" + "${AppUtils.convertToDateLikeOrderFormat(mList.get(adapterPosition).order_date)}" + "</font>"
                var productL = AppDatabase.getDBInstance()!!.newOrderProductDataDao().getProductsOrder(mList.get(adapterPosition).order_id) as ArrayList<NewOrderProductDataEntity>

                var orderItemstext = "<font color=" + mContext.resources.getColor(R.color.dark_gray) + ">Order item(s): </font> <font color="+
                        mContext.resources.getColor(R.color.black) + ">" + "${productL.size}" + "</font>"
                var orderAmttext = "<font color=" + mContext.resources.getColor(R.color.dark_gray) + ">Order Amount: </font> <font color="+
                        mContext.resources.getColor(R.color.black) + ">" + "${String.format("%.02f",mList.get(adapterPosition).order_total_amt.toDouble())}" + "</font>"
                tv_row_ord_dtls_order_id.text = Html.fromHtml(orderIdtext)
                tv_row_ord_dtls_order_date.text = Html.fromHtml(orderDatetext)
                tv_row_ord_dtls_order_items.text =Html.fromHtml(orderItemstext)
                tv_row_ord_dtls_order_amt.text =Html.fromHtml(orderAmttext)

                try{
                    var shopObj = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopByIdN(mList.get(adapterPosition).shop_id)
                    tv_row_ord_dtls_order_shop_name.text = shopObj.shopName
                    tv_row_ord_dtls_order_shop_addr.text = shopObj.address
                }catch (ex:Exception){
                    ex.printStackTrace()
                    Timber.d("tag_ex_order ${ex.printStackTrace()} for ${mList.get(adapterPosition).shop_id}")
                }


                if(mList.get(adapterPosition).isUploaded){
                    iv_row_ord_dtls_sync.setImageResource(R.drawable.ic_registered_shop_sync)
                }else{
                    iv_row_ord_dtls_sync.setImageResource(R.drawable.ic_registered_shop_not_sync)
                }

                iv_row_ord_dtls_view.setOnClickListener {
                    listner.onViewCLick(mList.get(adapterPosition))
                }
                iv_row_ord_dtls_sync.setOnClickListener {
                    if(!mList.get(adapterPosition).isUploaded){
                        listner.onSyncCLick(ordL.get(adapterPosition))
                    }
                }
                iv_row_ord_dtls_share.setOnClickListener {
                    listner.onShareCLick(ordL.get(adapterPosition))
                }


            }
        }
    }

    interface OnCLick {
        fun onShareCLick(obj:NewOrderDataEntity)
        fun onViewCLick(obj:NewOrderDataEntity)
        fun onSyncCLick(obj:NewOrderDataEntity)
    }
}