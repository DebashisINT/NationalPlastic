package com.breezefieldnationalplastic.features.orderITC

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.breezefieldnationalplastic.R
import com.breezefieldnationalplastic.app.Pref
import com.breezefieldnationalplastic.app.utils.AppUtils
import com.breezefieldnationalplastic.app.utils.CustomSpecialTextWatcher1
import com.breezefieldnationalplastic.app.utils.ToasterMiddle
import com.breezefieldnationalplastic.features.dashboard.presentation.DashboardActivity
import com.breezefieldnationalplastic.widgets.AppCustomTextView
import kotlinx.android.synthetic.main.row_cart_l.view.iv_row_ord_opti_cart_del
import kotlinx.android.synthetic.main.row_cart_l.view.iv_row_ord_opti_cart_tick
import kotlinx.android.synthetic.main.row_cart_l.view.tv_row_ord_cart_list_item_price
import kotlinx.android.synthetic.main.row_cart_l.view.tv_row_ord_cart_list_mrp
import kotlinx.android.synthetic.main.row_cart_l.view.tv_row_ord_opti_cart_list_qty
import kotlinx.android.synthetic.main.row_cart_l.view.tv_row_ord_opti_cart_list_rate
import kotlinx.android.synthetic.main.row_cart_l.view.tv_row_ord_opti_cart_product_name
import kotlinx.android.synthetic.main.row_product_l.view.ll_row_ord_pro_list_mrp_root

class AdapterCartList(val mContext: Context,var finalOrderDataList : ArrayList<FinalProductRateSubmit>,var listner: OnCartOptiOnClick)
    :RecyclerView.Adapter<AdapterCartList.CartListViewHolder>(){

    var isRateChanging = false
    var isQtyChanging = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartListViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.row_cart_l,parent,false)
        return CartListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return finalOrderDataList.size
    }

    override fun onBindViewHolder(holder: CartListViewHolder, position: Int) {
        holder.bindItems(mContext,finalOrderDataList,listner)
    }

    inner class CartListViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        fun bindItems(context: Context,cartList:ArrayList<FinalProductRateSubmit>,listner : AdapterCartList.OnCartOptiOnClick){

            itemView.tv_row_ord_opti_cart_product_name.text = cartList.get(adapterPosition).product_name
            itemView.tv_row_ord_cart_list_mrp.setText(cartList.get(adapterPosition).mrp)
            itemView.tv_row_ord_cart_list_item_price.setText(cartList.get(adapterPosition).item_price)
            itemView.tv_row_ord_opti_cart_list_qty.setText(cartList.get(adapterPosition).submitedQty)
            itemView.tv_row_ord_opti_cart_list_rate.setText(cartList.get(adapterPosition).submitedRate)

            if(Pref.IsViewMRPInOrder){
                itemView.ll_row_ord_pro_list_mrp_root.visibility = View.VISIBLE
            }else{
                itemView.ll_row_ord_pro_list_mrp_root.visibility = View.GONE
            }
            if(Pref.isRateNotEditable){
                itemView.tv_row_ord_opti_cart_list_rate.isEnabled = false
            }else{
                itemView.tv_row_ord_opti_cart_list_rate.isEnabled = true
            }

            CartListFrag.iseditCommit = true
            itemView.tv_row_ord_opti_cart_list_qty.clearFocus()
            itemView.tv_row_ord_opti_cart_list_rate.clearFocus()
            itemView.tv_row_ord_opti_cart_list_qty.setError(null)
            itemView.tv_row_ord_opti_cart_list_rate.setError(null)

            itemView.iv_row_ord_opti_cart_tick.visibility = View.GONE

            itemView.tv_row_ord_opti_cart_list_rate.setOnFocusChangeListener(object :View.OnFocusChangeListener{
                override fun onFocusChange(v: View?, hasFocus: Boolean) {
                    if(hasFocus){
                        itemView.tv_row_ord_opti_cart_list_rate.addTextChangedListener(
                            CustomSpecialTextWatcher1(itemView.tv_row_ord_opti_cart_list_rate, 6, 2, object : CustomSpecialTextWatcher1.GetCustomTextChangeListener {
                                override fun beforeTextChange(text: String) {

                                }

                                override fun customTextChange(text: String) {
                                    isRateChanging = true
                                    isQtyChanging = false
                                    CartListFrag.iseditCommit = false
                                    itemView.iv_row_ord_opti_cart_tick.visibility = View.VISIBLE
                                }
                            })
                        )
                    }else{
                        itemView.iv_row_ord_opti_cart_tick.visibility = View.GONE
                        CartListFrag.iseditCommit = true
                        AppUtils.hideSoftKeyboard(mContext as DashboardActivity)
                    }
                }
            })
            itemView.tv_row_ord_opti_cart_list_qty.setOnFocusChangeListener(object :View.OnFocusChangeListener{
                override fun onFocusChange(v: View?, hasFocus: Boolean) {
                    if(hasFocus){
                        itemView.tv_row_ord_opti_cart_list_qty.addTextChangedListener(
                            CustomSpecialTextWatcher1(itemView.tv_row_ord_opti_cart_list_qty, 5, 3, object : CustomSpecialTextWatcher1.GetCustomTextChangeListener {
                                override fun beforeTextChange(text: String) {

                                }
                                override fun customTextChange(text: String) {
                                    isRateChanging = false
                                    isQtyChanging = true
                                    CartListFrag.iseditCommit = false
                                    itemView.iv_row_ord_opti_cart_tick.visibility = View.VISIBLE
                                }
                            })
                        )
                    }else{
                        itemView.iv_row_ord_opti_cart_tick.visibility = View.GONE
                        CartListFrag.iseditCommit = true
                        AppUtils.hideSoftKeyboard(mContext as DashboardActivity)
                    }
                }
            })

            itemView.iv_row_ord_opti_cart_tick.setOnClickListener {
                try {
                    if(itemView.tv_row_ord_opti_cart_list_rate.text.toString().length==0){
                        itemView.tv_row_ord_opti_cart_list_rate.setError("Please enter valid rate.")
                        ToasterMiddle.msgShort(mContext,"Please enter valid rate.")
                    }
                }catch (ex:Exception){
                    ex.printStackTrace()
                }

                if(!Pref.IsAllowZeroRateOrder){
                    if(itemView.tv_row_ord_opti_cart_list_rate.text.toString().length==0 || itemView.tv_row_ord_opti_cart_list_rate.text.toString().toDouble() == 0.0){
                        itemView.tv_row_ord_opti_cart_list_rate.setError("Please enter valid rate.")
                        ToasterMiddle.msgShort(mContext,"Please enter valid rate.")
                        return@setOnClickListener
                    }
                }
                if(itemView.tv_row_ord_opti_cart_list_qty.text.toString().length==0 || itemView.tv_row_ord_opti_cart_list_qty.text.toString().toDouble() == 0.0){
                    itemView.tv_row_ord_opti_cart_list_qty.setError("Please enter valid quantity.")
                    ToasterMiddle.msgShort(mContext,"Please enter valid quantity.")
                    return@setOnClickListener
                }

                CartListFrag.iseditCommit = true
                AppUtils.hideSoftKeyboard(mContext as DashboardActivity)
                var changRateStr = ""
                var changQtyStr = ""
                try{
                    changRateStr = String.format("%.2f",itemView.tv_row_ord_opti_cart_list_rate.text.toString().toDouble())
                }catch (ex:Exception){
                    changRateStr = "0"
                }
                try{
                    changQtyStr = itemView.tv_row_ord_opti_cart_list_qty.text.toString()
                }catch (ex:Exception){
                    changQtyStr = "0"
                }
                if(isQtyChanging){
                    finalOrderDataList.get(adapterPosition).submitedQty = changQtyStr
                    listner.onRateQtyChange()
                    notifyDataSetChanged()
                }
                if(isRateChanging){
                    finalOrderDataList.get(adapterPosition).submitedRate = changRateStr
                    listner.onRateQtyChange()
                    notifyDataSetChanged()
                }
            }
            itemView.iv_row_ord_opti_cart_del.setOnClickListener {
                val simpleDialogg = Dialog(mContext)
                simpleDialogg.setCancelable(false)
                simpleDialogg.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                simpleDialogg.setContentView(R.layout.dialog_yes_no)
                val dialogHeader = simpleDialogg.findViewById(R.id.dialog_yes_no_headerTV) as AppCustomTextView
                val dialogBody = simpleDialogg.findViewById(R.id.dialog_cancel_order_header_TV) as AppCustomTextView
                dialogHeader.text=finalOrderDataList.get(adapterPosition).product_name
                dialogBody.text="Wish to Remove the Product from the Cart?"
                val dialogYes = simpleDialogg.findViewById(R.id.tv_dialog_yes_no_yes) as AppCustomTextView
                val dialogNo = simpleDialogg.findViewById(R.id.tv_dialog_yes_no_no) as AppCustomTextView
                dialogYes.setOnClickListener {
                    simpleDialogg.dismiss()
                    finalOrderDataList.removeAt(adapterPosition)
                    listner.onDelChangeClick(finalOrderDataList.size)
                    notifyDataSetChanged()
                }
                dialogNo.setOnClickListener {
                    simpleDialogg.dismiss()
                }
                simpleDialogg.show()
            }
        }
    }

    interface OnCartOptiOnClick {
        fun onDelChangeClick(cartSize:Int)
        fun onRateQtyChange()
    }
}