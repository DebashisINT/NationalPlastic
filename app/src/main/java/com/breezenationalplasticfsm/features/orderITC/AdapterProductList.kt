package com.breezenationalplasticfsm.features.orderITC

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.breezenationalplasticfsm.R
import com.breezenationalplasticfsm.app.Pref
import com.breezenationalplasticfsm.app.utils.AppUtils
import com.breezenationalplasticfsm.app.utils.CustomSpecialTextWatcher1
import com.breezenationalplasticfsm.app.utils.ToasterMiddle
import com.breezenationalplasticfsm.features.dashboard.presentation.DashboardActivity
import kotlinx.android.synthetic.main.row_product_l.view.cv_row_ord_pro_list_shop_add_product
import kotlinx.android.synthetic.main.row_product_l.view.iv_row_ord_opti_product_list_add_img
import kotlinx.android.synthetic.main.row_product_l.view.ll_row_ord_opti_product_list_add_text_root
import kotlinx.android.synthetic.main.row_product_l.view.ll_row_ord_pro_list_mrp_root
import kotlinx.android.synthetic.main.row_product_l.view.tv_row_ord_opti_product_list_add_text
import kotlinx.android.synthetic.main.row_product_l.view.tv_row_ord_opti_product_list_brand
import kotlinx.android.synthetic.main.row_product_l.view.tv_row_ord_opti_product_list_catagory
import kotlinx.android.synthetic.main.row_product_l.view.tv_row_ord_opti_product_list_measure
import kotlinx.android.synthetic.main.row_product_l.view.tv_row_ord_opti_product_list_name
import kotlinx.android.synthetic.main.row_product_l.view.tv_row_ord_opti_product_list_qty
import kotlinx.android.synthetic.main.row_product_l.view.tv_row_ord_opti_product_list_rate
import kotlinx.android.synthetic.main.row_product_l.view.tv_row_ord_opti_product_list_uom
import kotlinx.android.synthetic.main.row_product_l.view.tv_row_ord_pro_list_item_price
import kotlinx.android.synthetic.main.row_product_l.view.tv_row_ord_pro_list_mrp

class AdapterProductList(val mContext: Context, var proList : ArrayList<ProductRateList>, shop_id:String, var finalOrderDataList : ArrayList<FinalProductRateSubmit>,
                         var listner: OnProductOptiOnClick):
 RecyclerView.Adapter<AdapterProductList.ProductListViewHolder>(){

    var shopID = ""
    init {
        shopID = shop_id
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductListViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.row_product_l,parent,false)
        return ProductListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return proList.size
    }

    override fun onBindViewHolder(holder: ProductListViewHolder, position: Int) {
        holder.bindItems(mContext,proList,listner)
    }

    inner class ProductListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bindItems(context: Context,prooductList:ArrayList<ProductRateList>,listner :OnProductOptiOnClick){
            itemView.tv_row_ord_opti_product_list_brand.text = prooductList.get(adapterPosition).brand_name
            itemView.tv_row_ord_opti_product_list_measure.text = prooductList.get(adapterPosition).watt_name
            itemView.tv_row_ord_opti_product_list_catagory.text = prooductList.get(adapterPosition).category_name
            itemView.tv_row_ord_opti_product_list_uom.text = prooductList.get(adapterPosition).UOM

            itemView.tv_row_ord_opti_product_list_name.text = prooductList.get(adapterPosition).product_name
            itemView.tv_row_ord_pro_list_mrp.setText(prooductList.get(adapterPosition).mrp)
            itemView.tv_row_ord_pro_list_item_price.setText(prooductList.get(adapterPosition).item_price)
            itemView.tv_row_ord_opti_product_list_rate.setText(prooductList.get(adapterPosition).specialRate)

            if(Pref.IsViewMRPInOrder){
                itemView.ll_row_ord_pro_list_mrp_root.visibility = View.VISIBLE
            }else{
                itemView.ll_row_ord_pro_list_mrp_root.visibility = View.GONE
            }
            if(Pref.isRateNotEditable){
                itemView.tv_row_ord_opti_product_list_rate.isEnabled = false
            }else{
                itemView.tv_row_ord_opti_product_list_rate.isEnabled = true
            }

            //if(prooductList.get(adapterPosition).submitedQty.equals("-1")){
            if(!finalOrderDataList.map { it.product_id }.contains(prooductList.get(adapterPosition).product_id)){
                itemView.tv_row_ord_opti_product_list_qty.setText("")
                itemView.tv_row_ord_opti_product_list_add_text.text = "Add"
                itemView.ll_row_ord_opti_product_list_add_text_root.background.setTint(mContext.getResources().getColor(R.color.color_custom_blue))
                itemView.iv_row_ord_opti_product_list_add_img.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.icon_shopping))
            }else{
                var ob = finalOrderDataList.filter{it.product_id.equals(prooductList.get(adapterPosition).product_id)}.first()
                itemView.tv_row_ord_opti_product_list_qty.setText(ob.submitedQty.toString())
                itemView.tv_row_ord_opti_product_list_rate.setText(ob.submitedRate.toString())
                itemView.tv_row_ord_opti_product_list_add_text.text = "Added"
                itemView.ll_row_ord_opti_product_list_add_text_root.background.setTint(mContext.getResources().getColor(R.color.color_custom_green))
                itemView.iv_row_ord_opti_product_list_add_img.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_tick_nw))
            }

            itemView.cv_row_ord_pro_list_shop_add_product.setOnClickListener {
                try {
                    if(itemView.tv_row_ord_opti_product_list_rate.text.toString().length==0){
                        itemView.tv_row_ord_opti_product_list_rate.setError("Please enter valid rate.")
                        ToasterMiddle.msgShort(mContext,"Please enter valid rate.")
                    }
                }catch (ex:Exception){
                    ex.printStackTrace()
                }
                if(itemView.tv_row_ord_opti_product_list_qty.text.toString().length==0 || itemView.tv_row_ord_opti_product_list_qty.text.toString().toDouble() == 0.0){
                    itemView.tv_row_ord_opti_product_list_qty.setError("Please enter valid quantity.")
                    ToasterMiddle.msgShort(mContext,"Please enter valid quantity.")
                    return@setOnClickListener
                }
                if(!Pref.IsAllowZeroRateOrder){
                    if(itemView.tv_row_ord_opti_product_list_rate.text.toString().length==0 || itemView.tv_row_ord_opti_product_list_rate.text.toString().toDouble() == 0.0){
                        itemView.tv_row_ord_opti_product_list_rate.setError("Please enter valid rate.")
                        ToasterMiddle.msgShort(mContext,"Please enter valid rate.")
                        return@setOnClickListener
                    }
                }

                if((finalOrderDataList.map { it.product_id } as ArrayList<String>).contains(prooductList.get(adapterPosition).product_id)){
                    var objDel = finalOrderDataList.filter { it.product_id.equals(prooductList.get(adapterPosition).product_id) }.first()
                    finalOrderDataList.remove(objDel)
                }

                var changingRate = "0.0"
                try{
                    changingRate = if(itemView.tv_row_ord_opti_product_list_rate.text.toString().equals("")) "0.00" else itemView.tv_row_ord_opti_product_list_rate.text.toString()
                }catch (ex:Exception){
                    changingRate = "0.0"
                }

                var addObj : FinalProductRateSubmit = FinalProductRateSubmit()
                addObj.apply {
                    product_id = prooductList.get(adapterPosition).product_id
                    product_name = prooductList.get(adapterPosition).product_name
                    submitedQty = itemView.tv_row_ord_opti_product_list_qty.text.toString()
                    submitedRate = changingRate
                    mrp=prooductList.get(adapterPosition).mrp
                    item_price=prooductList.get(adapterPosition).item_price
                }
                finalOrderDataList.add(addObj)
                //prooductList.get(adapterPosition).submitedQty = addObj.submitedQty

                itemView.tv_row_ord_opti_product_list_add_text.text = "Added"
                itemView.ll_row_ord_opti_product_list_add_text_root.background.setTint(mContext.getResources().getColor(R.color.color_custom_green))

                ToasterMiddle.msgShort(mContext,prooductList.get(adapterPosition).product_name + " added.")
                listner.onProductAddClick(finalOrderDataList.size,finalOrderDataList.sumOf { it.submitedRate.toDouble() * it.submitedQty.toInt() })
            }

            itemView.tv_row_ord_opti_product_list_qty.clearFocus()
            itemView.tv_row_ord_opti_product_list_rate.clearFocus()
            itemView.tv_row_ord_opti_product_list_qty.setError(null)
            itemView.tv_row_ord_opti_product_list_rate.setError(null)


            itemView.tv_row_ord_opti_product_list_rate.setOnFocusChangeListener(object :View.OnFocusChangeListener{
                override fun onFocusChange(v: View?, hasFocus: Boolean) {
                    if(hasFocus){
                        try{
                            var prevRateText = itemView.tv_row_ord_opti_product_list_rate.text.toString()
                            if(prevRateText.toDouble() == 0.0){
                                itemView.tv_row_ord_opti_product_list_rate.setText("")
                            }
                        }catch (ex:Exception){
                            ex.printStackTrace()
                        }

                        itemView.tv_row_ord_opti_product_list_rate.addTextChangedListener(
                            CustomSpecialTextWatcher1(itemView.tv_row_ord_opti_product_list_rate, 6, 2, object : CustomSpecialTextWatcher1.GetCustomTextChangeListener {
                                override fun beforeTextChange(text: String) {

                                }

                                override fun customTextChange(text: String) {

                                }
                            })
                        )
                    }else{
                        AppUtils.hideSoftKeyboard(mContext as DashboardActivity)
                    }
                }
            })
        }
    }

    interface OnProductOptiOnClick {
        fun onProductAddClick(productCount:Int,sumAmt:Double)
    }

}