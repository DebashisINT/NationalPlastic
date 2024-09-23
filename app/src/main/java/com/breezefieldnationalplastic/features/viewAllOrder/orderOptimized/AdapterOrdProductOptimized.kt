package com.breezefieldnationalplastic.features.viewAllOrder.orderOptimized

import android.content.Context
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.breezefieldnationalplastic.R
import com.breezefieldnationalplastic.app.AppDatabase
import com.breezefieldnationalplastic.app.Pref
import com.breezefieldnationalplastic.app.utils.*
import com.breezefieldnationalplastic.features.DecimalDigitsInputFilter
import com.breezefieldnationalplastic.features.dashboard.presentation.DashboardActivity
import kotlinx.android.synthetic.main.customnotification.view.*
import kotlinx.android.synthetic.main.row_ord_opti_cart_list.view.*
import kotlinx.android.synthetic.main.row_ord_opti_product_list.view.*

//Rev 1.0  AdapterOrdProductOptimized Suman 20-06-2023 mantis 0026389
//  Rev 2.0  v 4.1.6 Tufan 22/08/2023 mantis 26649 Show distributor scheme with Product
class AdapterOrdProductOptimized(val mContext: Context,var proList : ArrayList<ProductQtyRateSubmit>,shop_id:String,var finalOrderDataList : ArrayList<FinalOrderData>, var listner :OnProductOptiOnClick):
    RecyclerView.Adapter<AdapterOrdProductOptimized.OrdProductOptimizedViewHolder>(){

    var shopID = ""
    init {
        shopID = shop_id
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdProductOptimizedViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.row_ord_opti_product_list,parent,false)
        return OrdProductOptimizedViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrdProductOptimizedViewHolder, position: Int) {
        holder.bindItems(mContext,proList,listner)
    }

    override fun getItemCount(): Int {
        return proList.size!!
    }

    inner class OrdProductOptimizedViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bindItems(context: Context,prooductList:ArrayList<ProductQtyRateSubmit>,listner :OnProductOptiOnClick){

            itemView.tv_row_ord_opti_product_list_brand.text = prooductList.get(adapterPosition).brand.toString()
            itemView.tv_row_ord_opti_product_list_measure.text = prooductList.get(adapterPosition).watt.toString()
            itemView.tv_row_ord_opti_product_list_catagory.text = prooductList.get(adapterPosition).category.toString()
            itemView.tv_row_ord_opti_product_list_name.text = prooductList.get(adapterPosition).product_name.toString()
            itemView.tv_row_ord_opti_product_list_rate.setText(prooductList.get(adapterPosition).rate.toString())

            // start 2.0 AdapterOrdProductOptimized v 4.1.6 stock optmization mantis 0026391 20-06-2023 saheli v 4.1.6
                if(Pref.savefromOrderOrStock){
                    itemView.ll_row_ord_pro_list_stock_root.visibility = View.GONE
                }else if(prooductList.get(adapterPosition).isStockShow){
                    itemView.tv_row_ord_pro_list_stock.setText("Stock : "+prooductList.get(adapterPosition).stock_amount+"("+ prooductList.get(adapterPosition).stock_unit+")")
                    itemView.ll_row_ord_pro_list_stock_root.visibility = View.VISIBLE
                }else{
                    itemView.ll_row_ord_pro_list_stock_root.visibility = View.GONE
                }
            // end 2.0 AdapterOrdProductOptimized   v 4.1.6 stock optmization mantis 0026391 20-06-2023 saheli v 4.1.6

            if(Pref.IsViewMRPInOrder){
                itemView.ll_row_ord_pro_list_mrp_root.visibility = View.VISIBLE
                itemView.tv_row_ord_pro_list_mrp.setText("₹ "+prooductList.get(adapterPosition).product_mrp_show)
            }else{
                itemView.ll_row_ord_pro_list_mrp_root.visibility = View.GONE
            }
            if(Pref.IsDiscountInOrder){
                itemView.ll_row_ord_pro_list_discount_root.visibility = View.VISIBLE
                itemView.tv_row_ord_pro_list_discount.setText(prooductList.get(adapterPosition).product_discount_show)
            }else{
                itemView.ll_row_ord_pro_list_discount_root.visibility = View.GONE
            }
            //Begin Rev 1.0  AdapterOrdProductOptimized Suman 20-06-2023 mantis 0026389
            if(Pref.IsDiscountEditableInOrder){
                itemView.tv_row_ord_pro_list_discount.isEnabled = true
            }else{
                itemView.tv_row_ord_pro_list_discount.isEnabled = false
            }
            //End of Rev 1.0  AdapterOrdProductOptimized Suman 20-06-2023 mantis 0026389
            if(Pref.IsViewMRPInOrder && Pref.IsDiscountInOrder){
                itemView.ll_row_ord_pro_list_mrp_discount_root.visibility = View.VISIBLE
                try{
                    var discPrice = prooductList.get(adapterPosition).product_mrp_show.toDouble() * (100-prooductList.get(adapterPosition).product_discount_show.toDouble()) / 100
                    itemView.tv_row_ord_opti_product_list_rate.setText(String.format("%.2f",discPrice))
                }catch (ex:Exception){
                    itemView.tv_row_ord_opti_product_list_rate.setText("0")
                }
            }else if(!Pref.IsViewMRPInOrder){
                itemView.ll_row_ord_pro_list_mrp_discount_root.visibility = View.GONE
            }
            if(Pref.isRateNotEditable){
                itemView.tv_row_ord_opti_product_list_rate.isEnabled = false
            }else{
                itemView.tv_row_ord_opti_product_list_rate.isEnabled = true
            }

            itemView.tv_row_ord_opti_product_list_qty.clearFocus()
            itemView.tv_row_ord_opti_product_list_rate.clearFocus()
            itemView.tv_row_ord_pro_list_discount.clearFocus()

            itemView.tv_row_ord_opti_product_list_qty.setError(null)
            itemView.tv_row_ord_opti_product_list_rate.setError(null)
            itemView.tv_row_ord_pro_list_discount.setError(null)

            if((finalOrderDataList.map { it.product_id } as ArrayList<String>).contains(prooductList.get(adapterPosition).product_id)){
                prooductList.get(adapterPosition).submitedQty = finalOrderDataList.filter { prooductList.get(adapterPosition).product_id.equals(it.product_id) }.first().qty
            }else{
                prooductList.get(adapterPosition).submitedQty = "-1"
            }

            if(prooductList.get(adapterPosition).submitedQty.equals("-1")){
                itemView.tv_row_ord_opti_product_list_qty.setText("")
                if(!Pref.IsViewMRPInOrder && !Pref.IsDiscountInOrder){
                    itemView.tv_row_ord_opti_product_list_rate.setText(prooductList.get(adapterPosition).rate)
                }
                itemView.tv_row_ord_opti_product_list_add_text.text = "Add"
                itemView.ll_row_ord_opti_product_list_add_text_root.background.setTint(mContext.getResources().getColor(R.color.color_custom_blue))
                itemView.iv_row_ord_opti_product_list_add_img.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.icon_shopping))
            }else{
                itemView.tv_row_ord_opti_product_list_qty.setText(prooductList.get(adapterPosition).submitedQty)
                itemView.tv_row_ord_opti_product_list_rate.setText(finalOrderDataList.filter { it.product_id.equals(prooductList.get(adapterPosition).product_id) }.first().rate.toString())
                itemView.tv_row_ord_opti_product_list_add_text.text = "Added"
                itemView.ll_row_ord_opti_product_list_add_text_root.background.setTint(mContext.getResources().getColor(R.color.color_custom_green))
                itemView.iv_row_ord_opti_product_list_add_img.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_tick_nw))
            }

            itemView.cv_row_ord_pro_list_shop_add_product.setOnClickListener {
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

                var changingRate = "0.0"
                var changingDisc = "0.0"
                try{
                    changingRate = if(itemView.tv_row_ord_opti_product_list_rate.text.toString().equals("")) "0.00" else itemView.tv_row_ord_opti_product_list_rate.text.toString()
                }catch (ex:Exception){
                    changingRate = "0.0"
                }
                if(Pref.IsViewMRPInOrder && Pref.IsDiscountInOrder && prooductList.get(adapterPosition).product_mrp_show.toDouble()!=0.0){
                    if(changingRate.toDouble()>prooductList.get(adapterPosition).product_mrp_show.toDouble()){
                        ToasterMiddle.msgShort(mContext,"Rate can't be greater than MRP.")
                        return@setOnClickListener
                    }
                    changingDisc = String.format("%.2f",(100-((changingRate.toString().toDouble()*100)/prooductList.get(adapterPosition).product_mrp_show.toDouble())))
                }
                println("ord_load_test list prepare begin")
                if((finalOrderDataList.filter { it.product_id.equals(prooductList.get(adapterPosition).product_id)} as ArrayList<FinalOrderData>).size>0 ){
                    finalOrderDataList.filter { it.product_id.equals(prooductList.get(adapterPosition).product_id)}.first().qty=itemView.tv_row_ord_opti_product_list_qty.text.toString()
                    finalOrderDataList.filter { it.product_id.equals(prooductList.get(adapterPosition).product_id)}.first().rate=if(itemView.tv_row_ord_opti_product_list_rate.text.toString().equals("")) "0.00" else itemView.tv_row_ord_opti_product_list_rate.text.toString()
                    if(Pref.IsViewMRPInOrder && Pref.IsDiscountInOrder){
                        finalOrderDataList.filter { it.product_id.equals(prooductList.get(adapterPosition).product_id)}.first().product_discount_show=changingDisc
                    }
                    ToasterMiddle.msgShort(mContext,"Product updated.")
                    //notifyDataSetChanged()
                }
                else{
                    var obj = FinalOrderData()
                    obj.apply {
                        product_id = prooductList.get(adapterPosition).product_id
                        product_name = prooductList.get(adapterPosition).product_name
                        qty = if(itemView.tv_row_ord_opti_product_list_qty.text.toString().equals("")) "0" else itemView.tv_row_ord_opti_product_list_qty.text.toString()
                        rate = if(itemView.tv_row_ord_opti_product_list_rate.text.toString().equals("")) "0.00" else itemView.tv_row_ord_opti_product_list_rate.text.toString()
                        brand_id = prooductList.get(adapterPosition).brand_id
                        brand = prooductList.get(adapterPosition).brand
                        category_id = prooductList.get(adapterPosition).category_id
                        category = prooductList.get(adapterPosition).category
                        watt_id = prooductList.get(adapterPosition).watt_id
                        watt = prooductList.get(adapterPosition).watt
                        product_mrp_show = prooductList.get(adapterPosition).product_mrp_show
                        product_discount_show = changingDisc
                       // Begin Rev 2.0  v 4.1.6 Tufan 22/08/2023 mantis 26649 Show distributor scheme with Product
                        Qty_per_Unit= prooductList.get(adapterPosition).Qty_per_Unit
                        Scheme_Qty= prooductList.get(adapterPosition).Scheme_Qty
                        Effective_Rate= prooductList.get(adapterPosition).Effective_Rate
                        // End  Rev 2.0  v 4.1.6 Tufan 22/08/2023 mantis 26649 Show distributor scheme with Product
                    }
                    finalOrderDataList.add(obj)
                    prooductList.get(adapterPosition).submitedQty = obj.qty

                    itemView.tv_row_ord_opti_product_list_add_text.text = "Added"
                    itemView.ll_row_ord_opti_product_list_add_text_root.background.setTint(mContext.getResources().getColor(R.color.color_custom_green))

                    ToasterMiddle.msgShort(mContext,prooductList.get(adapterPosition).product_name + " added.")
                    //notifyDataSetChanged()
                }
                println("ord_load_test list prepare end")
                println("ord_load_test sum counting.....")
                var sumAmt = finalOrderDataList.sumOf { it.rate.toDouble() * it.qty.toDouble() }
                println("ord_load_test sum counted onProductAddClick calling")
                listner.onProductAddClick(finalOrderDataList.size,sumAmt)
            }

            itemView.tv_row_ord_opti_product_list_qty.setOnFocusChangeListener(object :View.OnFocusChangeListener{
                override fun onFocusChange(v: View?, hasFocus: Boolean) {
                    println("ord_load_test  focus : $hasFocus")
                    if(hasFocus){
                        println("ord_load_test  focus : if $adapterPosition")

                        //new filter
                        itemView.tv_row_ord_opti_product_list_qty.setFilters(arrayOf<InputFilter>(DecimalDigitsInputFilterAdv(5, 3)))

                        /*itemView.tv_row_ord_opti_product_list_qty.addTextChangedListener(
                            CustomSpecialTextWatcher1(itemView.tv_row_ord_opti_product_list_qty, 5, 3, object : CustomSpecialTextWatcher1.GetCustomTextChangeListener {
                                override fun beforeTextChange(text: String) {
                                    println("ord_load_test  beforeTextChange $adapterPosition")
                                }

                                override fun customTextChange(text: String) {
                                    println("ord_load_test  customTextChange $adapterPosition")
                                }
                            })
                        )*/
                        println("ord_load_test  focus : if end $adapterPosition")
                    }else{
                        AppUtils.hideSoftKeyboard(mContext as DashboardActivity)
                        println("ord_load_test  focus : else $adapterPosition")
                    }
                }
            })

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

                        //new filter
                        itemView.tv_row_ord_opti_product_list_rate.setFilters(arrayOf<InputFilter>(DecimalDigitsInputFilterAdv(6, 2)))

                        /*itemView.tv_row_ord_opti_product_list_rate.addTextChangedListener(
                            CustomSpecialTextWatcher1(itemView.tv_row_ord_opti_product_list_rate, 6, 2, object : CustomSpecialTextWatcher1.GetCustomTextChangeListener {
                                override fun beforeTextChange(text: String) {

                                }

                                override fun customTextChange(text: String) {

                                }
                            })
                        )*/
                    }else{
                        AppUtils.hideSoftKeyboard(mContext as DashboardActivity)
                    }
                }
            })

            itemView.tv_row_ord_pro_list_discount.setOnFocusChangeListener(object :View.OnFocusChangeListener{
                override fun onFocusChange(v: View?, hasFocus: Boolean) {
                    if(hasFocus){
                        itemView.tv_row_ord_pro_list_discount.addTextChangedListener(
                            CustomSpecialTextWatcher1(itemView.tv_row_ord_pro_list_discount, 2, 2, object : CustomSpecialTextWatcher1.GetCustomTextChangeListener {
                                override fun beforeTextChange(text: String) {

                                }

                                override fun customTextChange(text: String) {
                                    try{
                                        var discPrice = prooductList.get(adapterPosition).product_mrp_show.toDouble() * (100-itemView.tv_row_ord_pro_list_discount.text.toString().toDouble()) / 100
                                        itemView.tv_row_ord_opti_product_list_rate.setText(String.format("%.2f",discPrice))
                                    }catch (ex:Exception){
                                        itemView.tv_row_ord_opti_product_list_rate.setText("0")
                                    }
                                }
                            })
                        )
                    }else{
                        AppUtils.hideSoftKeyboard(mContext as DashboardActivity)
                    }
                }
            })

            //Begin  Rev 2.0  v 4.1.6 Tufan 22/08/2023 mantis 26649 Show distributor scheme with Product
            val shopType = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopByIdN(shopID).type.toString()
            if(Pref.Show_distributor_scheme_with_Product && shopType =="4"){
                itemView.tv_row_ord_opti_product_list_qty_per_unit.text = "Qty per Unit\n"+prooductList.get(adapterPosition).Qty_per_Unit.toString()
                itemView.tv_row_ord_opti_product_list_scheme_qty.text = "Scheme Qty\n"+prooductList.get(adapterPosition).Scheme_Qty.toString()
                itemView.tv_row_ord_opti_product_list_effective_rate.text = "Effective Rate\n"+prooductList.get(adapterPosition).Effective_Rate.toString()
                itemView.ll_row_ord_opti_product_list_Show_distributor_scheme_with_Product.visibility = View.VISIBLE
            }
            else{
                itemView.ll_row_ord_opti_product_list_Show_distributor_scheme_with_Product.visibility = View.GONE
            }

//End  Rev 2.0  v 4.1.6 Tufan 22/08/2023 mantis 26649 Show distributor scheme with Product

        }
    }

    interface OnProductOptiOnClick {
        fun onProductAddClick(productCount:Int,sumAmt:Double)
    }

}