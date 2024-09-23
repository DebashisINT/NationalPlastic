package com.breezefieldnationalplastic.features.orderITC

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.breezefieldnationalplastic.R
import com.breezefieldnationalplastic.app.domain.NewOrderProductDataEntity
import kotlinx.android.synthetic.main.row_ord_dtls.view.tv_row_ord_dtls_product_name
import kotlinx.android.synthetic.main.row_ord_dtls.view.tv_row_ord_dtls_qty
import kotlinx.android.synthetic.main.row_ord_dtls.view.tv_row_ord_dtls_rate
import kotlinx.android.synthetic.main.row_ord_dtls.view.tv_row_ord_dtls_total_price

class AdapterOrdDtls(var mContext: Context,var prodL:ArrayList<NewOrderProductDataEntity>):
    RecyclerView.Adapter<AdapterOrdDtls.OrdDtlsViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdDtlsViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.row_ord_dtls, parent, false)
        return OrdDtlsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return prodL.size
    }

    override fun onBindViewHolder(holder: OrdDtlsViewHolder, position: Int) {
        holder.bindItems(mContext,prodL)
    }

    inner class OrdDtlsViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        fun bindItems(context: Context, mlist:ArrayList<NewOrderProductDataEntity>){
            itemView.tv_row_ord_dtls_product_name.text = mlist.get(adapterPosition).product_name
            val ordQty = "<font color=" + mContext.resources.getColor(R.color.dark_gray) + ">Quantity: </font> <font color="+
                    mContext.resources.getColor(R.color.black) + ">" + mlist.get(adapterPosition).submitedQty + "</font>"
            itemView.tv_row_ord_dtls_qty.text = Html.fromHtml(ordQty)
            val ordRate = "<font color=" + mContext.resources.getColor(R.color.dark_gray) + ">Rate: </font> <font color="+
                    mContext.resources.getColor(R.color.black) + ">" + "₹ "+String.format("%.02f",mlist.get(adapterPosition).submitedSpecialRate.toDouble()) + "</font>"
            itemView.tv_row_ord_dtls_rate.text = Html.fromHtml(ordRate)
            val ordTotal = "<font color=" + mContext.resources.getColor(R.color.dark_gray) + ">Total Price: </font> <font color="+
                    mContext.resources.getColor(R.color.black) + ">" + "₹ "+String.format("%.02f", (mlist.get(adapterPosition).submitedQty.toInt() * mlist.get(adapterPosition).submitedSpecialRate.toDouble()).toDouble()) + "</font>"
            itemView.tv_row_ord_dtls_total_price.text = Html.fromHtml(ordTotal)
        }
    }

}