package com.nationalplasticfsm.features.viewAllOrder

import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.text.*
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.nationalplasticfsm.CustomStatic
import com.nationalplasticfsm.R
import com.nationalplasticfsm.app.AppDatabase
import com.nationalplasticfsm.app.Pref
import com.nationalplasticfsm.app.domain.ProductListEntity
import com.nationalplasticfsm.app.domain.ProductRateEntity
import com.nationalplasticfsm.app.utils.AppUtils
import com.nationalplasticfsm.app.utils.CustomSpecialTextWatcher
import com.nationalplasticfsm.app.utils.CustomSpecialTextWatcher2
import com.nationalplasticfsm.features.DecimalDigitsInputFilter
import com.nationalplasticfsm.features.dashboard.presentation.DashboardActivity
import com.nationalplasticfsm.features.login.model.productlistmodel.ProductRateDataModel
import kotlinx.android.synthetic.main.inflate_product_list.view.*
import kotlinx.android.synthetic.main.inflate_product_list.view.ll_stock
import kotlinx.android.synthetic.main.inflate_product_list.view.tv_brand
import kotlinx.android.synthetic.main.inflate_product_list.view.tv_product_list_log_type
import kotlinx.android.synthetic.main.inflate_product_list.view.tv_rate
import kotlinx.android.synthetic.main.inflate_product_list.view.tv_stock
import kotlinx.android.synthetic.main.inflate_product_list.view.tv_watt
import kotlinx.android.synthetic.main.inflate_product_new_list.view.*

/**
 * Created by Saikat on 08-11-2018.
 */
//Revision History
// mantis 	0025571
// 1.0  AppV 4.0.6  ProductListAdapter Saheli    06/01/2023 edt_rt_product_new_list off settext blank
// 2.0  AppV 4.0.6  ProductListAdapter Saheli    06/01/2023 0 discount reflect
// 3.0  AppV 4.0.6  ProductListAdapter  Saheli    06/01/2023 decimal input handle
// 4.0  AppV 4.0.6  ProductListAdapter  Saheli    06/01/2023 discount logic correction
// 5.0  AppV 4.0.6  ProductListAdapter  Saheli    20/01/2023 Scroll handle mantis 25596
// 6.0 ProductListAdapter AppV 4.0.7 saheli 10-02-2023 order rate issue mantis  25666
class ProductListAdapter(
    private val context: Context,
    private val workTypeList: ArrayList<ProductListEntity>?,
    private val productRateList: ArrayList<ProductRateDataModel>?,
    private val productRateListDb: ArrayList<ProductRateEntity>?,
    private val shopId: String,
    private val listener: OnProductClickListener,
    private val listenerDel: OnProductDelClickListener
) : RecyclerView.Adapter<ProductListAdapter.MyViewHolder>(), Filterable {


    private val layoutInflater: LayoutInflater

    private var productList: ArrayList<ProductListEntity>
    private var tempProductList: ArrayList<ProductListEntity>
    private var filteredProductList: ArrayList<ProductListEntity>

    init {
        layoutInflater = LayoutInflater.from(context)

        productList = ArrayList()
        tempProductList = ArrayList()
        filteredProductList = ArrayList()

        productList.addAll(workTypeList!!)
        tempProductList.addAll(workTypeList)

        CustomStatic.productQtyEdi = HashMap()
        CustomStatic.productRateEdi = HashMap()
        CustomStatic.productAddedID = ArrayList()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
//        val v = layoutInflater.inflate(R.layout.inflate_product_list, parent, false)
        val v = layoutInflater.inflate(R.layout.inflate_product_new_list, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindItems(
            context,
            productList,
            listener,
            listenerDel,
            productRateList,
            productRateListDb,
            shopId
        )
    }

    override fun getItemCount(): Int {
        return productList.size
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(context: Context, categoryList: ArrayList<ProductListEntity>?, listener: OnProductClickListener,listenerDel : OnProductDelClickListener,
                      productRateList: ArrayList<ProductRateDataModel>?, productRateListDb: ArrayList<ProductRateEntity>?,
                      shopId: String) {

            println(" tag_tag adapter pos : $adapterPosition")

            if (adapterPosition % 2 == 0)
                itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.report_screen_bg))
            else
                itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.white))

            try{
                itemView.tv_product_list_log_type.text = AppUtils.capitalizeCustom(categoryList?.get(adapterPosition)?.product_name!!.toLowerCase())
            }catch (ex:Exception){
                itemView.tv_product_list_log_type.text = ""
            }

            //itemView.tv_product_list_log_type.isSelected = true

            val builder = SpannableStringBuilder()
//            val str1 = SpannableString("Brand: ")
            val str1 = SpannableString("")
            str1.setSpan(ForegroundColorSpan(Color.GRAY), 0, str1.length, 0)
            builder.append(str1)

            try{
                val str2 = SpannableString(AppUtils.capitalizeCustom(categoryList?.get(adapterPosition)?.brand!!.toLowerCase()))
                builder.append(str2)
            }catch (ex:Exception){
                builder.append("")
            }

            itemView.tv_brand.setText(builder, TextView.BufferType.SPANNABLE)


            val builder1 = SpannableStringBuilder()
            val str1_ = SpannableString("")
            str1_.setSpan(ForegroundColorSpan(Color.GRAY), 0, str1_.length, 0)
            builder1.append(str1_)

            try{
                val str2_ = SpannableString(AppUtils.capitalizeCustom(categoryList?.get(adapterPosition)?.watt!!.toLowerCase()))
                builder1.append(str2_)
            }catch (ex:Exception){
                builder1.append("")
            }

            itemView.tv_watt.setText(builder1, TextView.BufferType.SPANNABLE)

            val builder11 = SpannableStringBuilder()
//            val str11 = SpannableString("Category: ")
            val str11 = SpannableString("")
            str11.setSpan(ForegroundColorSpan(Color.GRAY), 0, str1.length, 0)
            builder11.append(str11)

            try{
                val str21 = SpannableString(AppUtils.capitalizeCustom(categoryList?.get(adapterPosition)?.category!!.toLowerCase()))
                builder11.append(str21)
            }catch (ex:Exception){
                builder11.append("")
            }


            itemView.tv_cate.setText(builder11, TextView.BufferType.SPANNABLE)

            try {
                if (Pref.isRateOnline) {
                    if (productRateList != null) {
                        for (i in productRateList.indices) {
                            if (Pref.isRateNotEditable) {
                                if (productRateList[i].isRateShow)
                                    itemView.tv_rate.visibility = View.VISIBLE
                                else
                                    itemView.tv_rate.visibility = View.GONE
                            }
                            if (productRateList[i].isStockShow)
                                itemView.ll_stock.visibility = View.VISIBLE
                            else
                                itemView.ll_stock.visibility = View.GONE
                            if (productRateList[i].product_id.toInt() == categoryList?.get(adapterPosition)?.id) {
                                itemView.tv_rate.text = "Rate: \u20B9" + productRateList[i].rate
                                if (!TextUtils.isEmpty(productRateList[i].stock_amount)) {
                                    if (productRateList[i].stock_amount.toDouble() >= 0.00) {
                                        itemView.tv_stock.setTextColor(context.resources.getColor(android.R.color.black))
                                        itemView.tv_stock.text = productRateList[i].stock_amount + " (" + productRateList[i].stock_unit + ")"
                                    } else {
                                        if (productRateList[i].stock_amount.toDouble() < 0.00) {
                                            itemView.tv_stock.setTextColor(context.resources.getColor(android.R.color.holo_red_light))
                                            itemView.tv_stock.text = productRateList[i].stock_amount + " (" + productRateList[i].stock_unit + ")"
                                        } /*else
                                    itemView.tv_stock.text = "N.A."*/
                                    }
                                }
                                break
                            }
                        }
                    }
                    else{
                        /*db start to rate*/
                        if (productRateListDb != null) {
                            val shop = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopByIdN(shopId)
                            for (i in productRateListDb.indices) {
                                if (Pref.isRateNotEditable) {
                                    if (productRateListDb[i].isRateShow!!)
                                        itemView.tv_rate.visibility = View.VISIBLE
                                    else
                                        itemView.tv_rate.visibility = View.GONE
                                }
                                else{
                                    if (productRateListDb[i].isRateShow!!)
                                        if(!Pref.IsShowNewOrderCart)
                                        itemView.tv_rate.visibility = View.VISIBLE
                                    else
                                        itemView.tv_rate.visibility = View.GONE
                                }
                                if (productRateList != null && productRateList.size > 0) {
                                    if (productRateList[i].isStockShow)
                                        itemView.ll_stock.visibility = View.VISIBLE
                                    else
                                        itemView.ll_stock.visibility = View.GONE
                                }
                                if (productRateListDb[i].product_id?.toInt() == categoryList?.get(adapterPosition)?.id) {
                                    itemView.tv_rate.text = "Rate: \u20B9" + productRateListDb[i].rate1
//                                    when {
//                                        shop.type == "1" -> itemView.tv_rate.text = "Rate: \u20B9" + productRateListDb[i].rate1
//                                        shop.type == "2" -> itemView.tv_rate.text = "Rate: \u20B9" + productRateListDb[i].rate2
//                                        shop.type == "3" -> itemView.tv_rate.text = "Rate: \u20B9" + productRateListDb[i].rate3
//                                        shop.type == "4" -> itemView.tv_rate.text = "Rate: \u20B9" + productRateListDb[i].rate4
//                                        shop.type == "5" -> itemView.tv_rate.text = "Rate: \u20B9" + productRateListDb[i].rate5
//                                    }

                                    if (productRateList != null && productRateList.size > 0) {
                                        if (!TextUtils.isEmpty(productRateList[i].stock_amount)) {
                                            if (productRateList[i].stock_amount.toDouble() >= 0.00) {
                                                itemView.tv_stock.setTextColor(context.resources.getColor(android.R.color.black))
                                                itemView.tv_stock.text = productRateList?.get(i)?.stock_amount + " (" + productRateList?.get(i)?.stock_unit + ")"
                                            } else {
                                                if (productRateList[i].stock_amount.toDouble() < 0.00) {
                                                    itemView.tv_stock.setTextColor(context.resources.getColor(android.R.color.holo_red_light))
                                                    itemView.tv_stock.text = productRateList?.get(i)?.stock_amount + " (" + productRateList?.get(i)?.stock_unit + ")"
                                                }
                                            }
                                        }
                                    }
                                    break
                                }
                            }
                        }
                        /*db end to rate */
                    }


//                        itemView.tv_rate.text = "Rate: ₹0.00"
                }
                else {
                    if (productRateListDb != null) {
                        val shop = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopByIdN(shopId)
                        for (i in productRateListDb.indices) {
                            if (Pref.isRateNotEditable) {
                                if (productRateListDb[i].isRateShow!!)
                                    itemView.tv_rate.visibility = View.VISIBLE
                                else
                                    itemView.tv_rate.visibility = View.GONE
                            }
                            if (productRateList != null && productRateList.size > 0) {
                                if (productRateList[i].isStockShow)
                                    itemView.ll_stock.visibility = View.VISIBLE
                                else
                                    itemView.ll_stock.visibility = View.GONE
                            }
                            //itemView.edt_rt_product_new_list.setText(productRateListDb[i].rate1.toString())
                            if (productRateListDb[i].product_id?.toInt() == categoryList?.get(adapterPosition)?.id) {
                                when {
                                    shop.type == "1" -> itemView.tv_rate.text = "Rate: \u20B9" + productRateListDb[i].rate1
                                    shop.type == "2" -> itemView.tv_rate.text = "Rate: \u20B9" + productRateListDb[i].rate2
                                    shop.type == "3" -> itemView.tv_rate.text = "Rate: \u20B9" + productRateListDb[i].rate3
                                    shop.type == "4" -> itemView.tv_rate.text = "Rate: \u20B9" + productRateListDb[i].rate4
                                    shop.type == "5" -> itemView.tv_rate.text = "Rate: \u20B9" + productRateListDb[i].rate5
                                }
//                                if (!TextUtils.isEmpty(productRateListDb[i].stock_amount)) {
//                                    if (productRateListDb[i].stock_amount?.toDouble()!! >= 0.00) {
//                                        itemView.tv_stock.setTextColor(context.resources.getColor(android.R.color.black))
//                                        itemView.tv_stock.text = productRateListDb[i].stock_amount + " (" + productRateListDb[i].stock_unit + ")"
//                                    } else {
//                                        if (productRateListDb[i].stock_amount?.toDouble()!! < 0.00) {
//                                            itemView.tv_stock.setTextColor(context.resources.getColor(android.R.color.holo_red_light))
//                                            itemView.tv_stock.text = productRateListDb[i].stock_amount + " (" + productRateListDb[i].stock_unit + ")"
//                                        } /*else
//                                    itemView.tv_stock.text = "N.A."*/
//                                    }
//                                }
                                if (productRateList != null && productRateList.size > 0) {
                                    if (!TextUtils.isEmpty(productRateList[i].stock_amount)) {
                                        if (productRateList[i].stock_amount.toDouble() >= 0.00) {
                                            itemView.tv_stock.setTextColor(context.resources.getColor(android.R.color.black))
                                            itemView.tv_stock.text = productRateList?.get(i)?.stock_amount + " (" + productRateList?.get(i)?.stock_unit + ")"
                                        } else {
                                            if (productRateList[i].stock_amount.toDouble() < 0.00) {
                                                itemView.tv_stock.setTextColor(context.resources.getColor(android.R.color.holo_red_light))
                                                itemView.tv_stock.text = productRateList?.get(i)?.stock_amount + " (" + productRateList?.get(i)?.stock_unit + ")"
                                            } /*else
                                    itemView.tv_stock.text = "N.A."*/
                                        }
                                    }
                                }
                                break
                            }
                        }
                    } else {
                        itemView.tv_rate.text = "Rate: ₹0.00"
                        if (productRateList != null) {
                            for (i in productRateList.indices) {
                                if (productRateList[i].isStockShow)
                                    itemView.ll_stock.visibility = View.VISIBLE
                                else
                                    itemView.ll_stock.visibility = View.GONE
                                if (productRateList[i].product_id.toInt() == categoryList?.get(adapterPosition)?.id) {
                                    if (!TextUtils.isEmpty(productRateList[i].stock_amount)) {
                                        if (productRateList[i].stock_amount.toDouble() >= 0.00) {
                                            itemView.tv_stock.setTextColor(context.resources.getColor(android.R.color.black))
                                            itemView.tv_stock.text = productRateList[i].stock_amount + " (" + productRateList[i].stock_unit + ")"
                                        } else {
                                            if (productRateList[i].stock_amount.toDouble() < 0.00) {
                                                itemView.tv_stock.setTextColor(context.resources.getColor(android.R.color.holo_red_light))
                                                itemView.tv_stock.text = productRateList[i].stock_amount + " (" + productRateList[i].stock_unit + ")"
                                            } /*else
                                    itemView.tv_stock.text = "N.A."*/
                                        }
                                    }
                                    break
                                }
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                itemView.tv_rate.text = "Rate: ₹0.00"
            }
            itemView./*tv_add_to_cart.*/setOnClickListener {
                if(!Pref.IsShowNewOrderCart){
                    listener.onProductClick(categoryList?.get(adapterPosition), adapterPosition)
                }
            }


            //new rate qty work
            if(Pref.IsShowNewOrderCart){
                itemView.ll_qty_rt_product_new_list.visibility = View.VISIBLE
                if(Pref.isRateNotEditable){
                    itemView.edt_rt_product_new_list.isEnabled = false
                }else{
                    itemView.edt_rt_product_new_list.isEnabled = true
                }
                if (productRateList != null) {
                    for (i in productRateList.indices) {
                        if (productRateList[i]?.product_id.toInt() == categoryList?.get(adapterPosition)?.id) {
                            itemView.edt_rt_product_new_list.setText(productRateList[i].rate.toString())
                            break
                        }
                    }
                }else if(productRateListDb != null){
                    for (i in productRateListDb.indices) {
                        if (productRateListDb[i]?.product_id!!.toInt() == categoryList?.get(adapterPosition)?.id) {
                            itemView.edt_rt_product_new_list.setText(productRateListDb[i].rate1.toString())
                            break
                        }
                    }
                }
            }
            else{
                itemView.ll_qty_rt_product_new_list.visibility = View.GONE
            }
            // 6.0 ProductListAdapter AppV 4.0.7 saheli 10-02-2023 order rate issue mantis  25666
            Handler(Looper.getMainLooper()).postDelayed({
                itemView.iv_prod_new_list_tick.setOnClickListener {
                    if(CustomStatic.productAddedID.contains(categoryList?.get(adapterPosition)!!.id)){
                        (context as DashboardActivity).showSnackMessage("This product has already added to cart")
                        return@setOnClickListener
                    }
                    var strRate = itemView.edt_rt_product_new_list.text.toString().trim().toString()
                    if(strRate.equals("")){
                        (context as DashboardActivity).rateList.add("0.00")
                    }else{
                        (context as DashboardActivity).rateList.add(strRate)
                    }
                    var strQty = itemView.edt_qty_product_new_list.text.toString().trim().toString()
                    if(strQty.equals("") || strQty.equals("0") || strQty.equals("0.0") || strQty.equals("0.00")|| strQty.equals("0.000")){
                        (context as DashboardActivity).showSnackMessage("Enter valid Qty.")
                        return@setOnClickListener
//                            (context as DashboardActivity).qtyList.add("0")
                    }else{
                        (context as DashboardActivity).qtyList.add(strQty)
                    }
//                        listener.onProductClick(categoryList?.get(adapterPosition), adapterPosition)
                    CustomStatic.productAddedID.add(categoryList!!.get(adapterPosition).id.toInt())

                    if(!CustomStatic.productQtyEdi.containsKey(adapterPosition)){
                        CustomStatic.productQtyEdi.put(adapterPosition,strQty)
                    }else{
                        CustomStatic.productQtyEdi.replace(adapterPosition,strQty)
                    }
                    var keyLL = CustomStatic.productQtyEdi.keys.toList()
                    for(i in 0..keyLL.size-1){
                        println("tag_list_show qty hash : ${CustomStatic.productQtyEdi.get(keyLL[i])}")
                    }

                    if(!CustomStatic.productRateEdi.containsKey(adapterPosition)){
                        CustomStatic.productRateEdi.put(adapterPosition,strRate)
                    }else{
                        CustomStatic.productRateEdi.replace(adapterPosition,strRate)
                    }
                    var keyL = CustomStatic.productRateEdi.keys.toList()
                    for(i in 0..keyL.size-1){
                        println("tag_list_show rate hash : ${CustomStatic.productRateEdi.get(keyL[i])}")
                    }

                    listener.onProductClick(categoryList!!.get(adapterPosition), adapterPosition)
                }
            }, 2000)

                itemView.iv_prod_new_list_cross.setOnClickListener {
                    CustomStatic.productAddedID.remove(categoryList?.get(adapterPosition)!!.id)
                    print("prod_added ${CustomStatic.productAddedID.size}")
                    itemView.iv_prod_new_list_cross.visibility = View.GONE
                    itemView.iv_prod_new_list_cross1.visibility = View.GONE
                    itemView.iv_prod_new_list_tick.visibility = View.GONE
                    itemView.edt_qty_product_new_list.setText("")
//                    itemView.edt_rt_product_new_list.setText("") //1.0  AppV 4.0.6  ProductListAdapter edt_rt_product_new_list off settext blank
                    listenerDel.onProductDelClick(categoryList?.get(adapterPosition)!!)
                }



            //new code for edit fix
            itemView.iv_prod_new_list_tick.visibility = View.GONE
            itemView.iv_prod_new_list_cross.visibility = View.GONE
            itemView.iv_prod_new_list_cross1.visibility = View.GONE
            if(CustomStatic.productAddedID.contains(categoryList!!.get(adapterPosition).id)){
                itemView.iv_prod_new_list_cross.visibility = View.VISIBLE
                itemView.iv_prod_new_list_cross1.visibility = View.VISIBLE
            }else{
                itemView.iv_prod_new_list_cross.visibility = View.GONE
                itemView.iv_prod_new_list_cross1.visibility = View.GONE
            }
            if(CustomStatic.productQtyEdi.contains(adapterPosition)){
                itemView.edt_qty_product_new_list.setText(CustomStatic.productQtyEdi.get(adapterPosition))
            }else{
                itemView.edt_qty_product_new_list.setText("")
            }
            if(CustomStatic.productRateEdi.contains(adapterPosition)){
                println("tag_rate_set ${CustomStatic.productRateEdi.get(adapterPosition)}   $adapterPosition")
                itemView.edt_rt_product_new_list.setText(CustomStatic.productRateEdi.get(adapterPosition))
            }else{
                if(itemView.edt_rt_product_new_list.text.toString().length==0)
                    itemView.edt_rt_product_new_list.setText("")
            }
            //if(CustomStatic.productQtyEdi.contains(adapterPosition) || CustomStatic.productRateEdi.contains(adapterPosition)){
            if(CustomStatic.productQtyEdi.contains(adapterPosition)){
                itemView.iv_prod_new_list_tick.visibility = View.VISIBLE
                itemView.iv_prod_new_list_tick1.visibility = View.VISIBLE
            }else{
                itemView.iv_prod_new_list_tick.visibility = View.GONE
                itemView.iv_prod_new_list_tick1.visibility = View.GONE
            }
            itemView.edt_qty_product_new_list.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                }
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    println(" tag_tag adapter pos inside : $adapterPosition")
                    var strDyna :String = p0!!.toString()
                    if (strDyna!!.equals("") || strDyna.equals("0") || strDyna.equals("0.0") || strDyna.equals("0.00")|| strDyna.equals("0.000")){
                        itemView.iv_prod_new_list_tick.visibility = View.GONE
                        itemView.iv_prod_new_list_tick1.visibility = View.GONE
                        /*if(CustomStatic.productQtyEdi.containsKey(adapterPosition)){
                            CustomStatic.productQtyEdi.remove(adapterPosition)
                        }*/
                    }else{
                        /*if(!CustomStatic.productQtyEdi.containsKey(adapterPosition)){
                            CustomStatic.productQtyEdi.put(adapterPosition,strDyna)
                        }else{
                            CustomStatic.productQtyEdi.replace(adapterPosition,strDyna)
                        }
                        var keyL = CustomStatic.productQtyEdi.keys.toList()
                        for(i in 0..keyL.size-1){
                            println("tag_list_show qty hash : ${CustomStatic.productQtyEdi.get(keyL[i])}")
                        }*/
                        itemView.iv_prod_new_list_tick.visibility = View.VISIBLE
                        itemView.iv_prod_new_list_tick1.visibility = View.VISIBLE
                    }
                }
            })
            itemView.edt_rt_product_new_list.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                    var t = p0.toString()
                    println("adapter_check $t")
                }
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    var tt = p0.toString()
                    println("adapter_check $tt")
                }
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    println(" tag_tag adapter pos inside : $adapterPosition")
                    var strDyna :String = p0!!.toString()
                    println("adapter_check $strDyna")
                    if (strDyna!!.equals("") || strDyna.equals("0") || strDyna.equals("0.0") || strDyna.equals("0.00")|| strDyna.equals("0.000")){
                        //itemView.iv_prod_new_list_tick.visibility = View.GONE
                        /*if(CustomStatic.productRateEdi.containsKey(adapterPosition)){
                            CustomStatic.productRateEdi.remove(adapterPosition)
                        }*/
                    }else{
                        /*if(!CustomStatic.productRateEdi.containsKey(adapterPosition)){
                            CustomStatic.productRateEdi.put(adapterPosition,strDyna)
                        }else{
                            CustomStatic.productRateEdi.replace(adapterPosition,strDyna)
                        }
                        var keyL = CustomStatic.productRateEdi.keys.toList()
                        for(i in 0..keyL.size-1){
                            println("tag_list_show rate hash : ${CustomStatic.productRateEdi.get(keyL[i])}")
                        }*/
                        //itemView.iv_prod_new_list_tick.visibility = View.VISIBLE
                    }
                }
            })
            itemView.edt_qty_product_new_list.filters=(arrayOf<InputFilter>(DecimalDigitsInputFilter(5, 3)))
            itemView.edt_rt_product_new_list.filters=(arrayOf<InputFilter>(DecimalDigitsInputFilter(6, 2)))

//            itemView.ll_pro_new_hint_root.visibility = View.GONE
//            itemView.xet_test.setText("30.20")
            if(Pref.isRateNotEditable){
                itemView.iv_rate_close.visibility = View.GONE
            }
            else{
                itemView.iv_rate_close.visibility = View.VISIBLE
            }

            itemView.iv_rate_close.setOnClickListener {
                itemView.edt_rt_product_new_list.setText("")
            }

//            categoryList!!.get(0).product_mrp_show = "100"
//            categoryList!!.get(0).product_discount_show = "12"
//
//            categoryList!!.get(1).product_mrp_show = "50"

            if(Pref.IsViewMRPInOrder) {
                itemView.tv_mrp_product_new_list.text = "M.R.P "+categoryList!!.get(adapterPosition).product_mrp_show
            }else{
                itemView.tv_mrp_product_new_list.visibility = View.GONE
            }



            if(Pref.IsDiscountInOrder) {
                // 5.0  AppV 4.0.6  ProductListAdapter Scroll handle mantis 25596
//                itemView.edt_discount_product_new_list.setText(categoryList!!.get(adapterPosition).product_discount_show)

                itemView.edt_discount_product_new_list.text = "Discount : "+categoryList!!.get(adapterPosition).product_discount_show

                itemView.ll_pro_nw_list_disct_root.visibility = View.VISIBLE
            }else{
                itemView.edt_discount_product_new_list.visibility = View.GONE
                itemView.ll_pro_nw_list_disct_root.visibility = View.GONE
            }
            if(Pref.IsDiscountInOrder ){
            if( !categoryList!!.get(adapterPosition).product_discount_show.equals("")){
                var newRate=(categoryList!!.get(adapterPosition).product_mrp_show)!!.toDouble() * (categoryList!!.get(adapterPosition).product_discount_show)!!.toDouble()
                var newRatewithdis = newRate/100
                var oriPrice = (categoryList!!.get(adapterPosition).product_mrp_show)!!.toDouble() - newRatewithdis// 4.0  AppV 4.0.6  ProductListAdapter   discount logic correction
//                itemView.edt_rt_product_new_list.setText(oriPrice.toString())
                // mantis 25601
                itemView.edt_rt_product_new_list.setText(String.format("%.2f",oriPrice.toDouble()))

            }else{
                itemView.edt_rt_product_new_list.setText("")
            }}

            try{
                // 3.0  AppV 4.0.6  ProductListAdapter  decimal input handle old
                /*itemView.edt_discount_product_new_list.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(p0: Editable?) {
                    }
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    }
                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        var strDis :String = p0!!.toString()
                        if (strDis!!.equals("") || strDis.equals("0")){
                            return
                        }
                        else{
                           var mrpShow = categoryList!!.get(adapterPosition).product_mrp_show
                            if(mrpShow!!.isNotEmpty()|| !mrpShow.equals("")|| !mrpShow.equals("0")|| !mrpShow.equals("0.0")||!mrpShow.equals("0.00")){
                                var newRate=(categoryList!!.get(adapterPosition).product_mrp_show)!!.toString().trim().toDouble() * (itemView.edt_discount_product_new_list.text.toString().toDouble())
                                var newRatewithdis = newRate/100
                                var oriPrice = (categoryList!!.get(adapterPosition).product_mrp_show)!!.toDouble() - newRatewithdis
                                itemView.edt_rt_product_new_list.setText(oriPrice.toString())
                            }
                            else{
                                itemView.edt_rt_product_new_list.setText("")
                            }

                        }
                    }
                })*/

        // 3.0  AppV 4.0.6  ProductListAdapter  decimal input handle

                // 5.0  AppV 4.0.6  ProductListAdapter Scroll handle mantis 25596
               /* itemView.edt_discount_product_new_list.addTextChangedListener(
                    CustomSpecialTextWatcher2(itemView.edt_discount_product_new_list, 2, 2, object : CustomSpecialTextWatcher2.GetCustomTextChangeListener {
                        override fun beforeTextChange(text: String) {

                        }

                        override fun customTextChange(p0: String) {
                            var strDis :String = p0!!.toString()
//                            if (strDis!!.equals("") || strDis.equals("0")){
                                if (strDis!!.equals("")){   // 2.0  AppV 4.0.6  ProductListAdapter  0 discount reflect
                                return
                            }
                            else{
                                var mrpShow = categoryList!!.get(adapterPosition).product_mrp_show
                                if(mrpShow!!.isNotEmpty()|| !mrpShow.equals("")|| !mrpShow.equals("0")|| !mrpShow.equals("0.0")||!mrpShow.equals("0.00")){
                                    var newRate=(categoryList!!.get(adapterPosition).product_mrp_show)!!.toString().trim().toDouble() * (itemView.edt_discount_product_new_list.text.toString().toDouble())
                                    var newRatewithdis = newRate/100
                                    var oriPrice = (categoryList!!.get(adapterPosition).product_mrp_show)!!.toDouble() - newRatewithdis// 4.0  AppV 4.0.6  ProductListAdapter   discount logic correction
                                    itemView.edt_rt_product_new_list.setText(oriPrice.toString())
                                }
                                else{
                                    itemView.edt_rt_product_new_list.setText("")
                                }

                            }
                        }
                    })
                )*/

        // 3.0  AppV 4.0.6  ProductListAdapter  decimal input handle

            }catch (e: IllegalStateException) {
                e.printStackTrace()
            }
//            if(Pref.isRateOnline){
                if (Pref.isRateNotEditable) {
                    itemView.edt_rt_product_new_list.isEnabled = false
                }
                else{
                    itemView.edt_rt_product_new_list.isEnabled = true
                }

            if(!Pref.IsDiscountInOrder && !Pref.IsViewMRPInOrder){
                itemView.ll_pro_nw_list_mrp_disc_root.visibility = View.GONE
            }else{
                itemView.ll_pro_nw_list_mrp_disc_root.visibility = View.VISIBLE
            }

//            }else{
//                itemView.edt_rt_product_new_list.isEnabled = true
//            }






        }
    }

    fun refresh() {
        notifyDataSetChanged()
    }


    interface OnProductClickListener {
        fun onProductClick(brand: ProductListEntity?, adapterPosition: Int)
    }

    interface OnProductDelClickListener {
        fun onProductDelClick(position: ProductListEntity)
    }

    fun updateList(mProductList: ArrayList<ProductListEntity>) {
        productList.clear()
        productList.addAll(mProductList)
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return SearchFilter()
    }

    inner class SearchFilter : Filter() {
        override fun performFiltering(p0: CharSequence?): FilterResults {
            val results = FilterResults()

            filteredProductList.clear()

            tempProductList.indices
                .filter {
                    tempProductList[it].product_name?.toLowerCase()
                        ?.contains(p0?.toString()?.toLowerCase()!!)!! ||
                            tempProductList[it].brand?.toLowerCase()
                                ?.contains(p0?.toString()?.toLowerCase()!!)!! ||
                            tempProductList[it].category?.toLowerCase()
                                ?.contains(p0?.toString()?.toLowerCase()!!)!! ||
                            tempProductList[it].watt?.toLowerCase()
                                ?.contains(p0?.toString()?.toLowerCase()!!)!!

                }
                .forEach { filteredProductList.add(tempProductList[it]) }

            results.values = filteredProductList
            results.count = filteredProductList.size

            return results
        }

        override fun publishResults(p0: CharSequence?, results: FilterResults?) {

            try {
                filteredProductList = results?.values as ArrayList<ProductListEntity>
                productList.clear()
                val hashSet = HashSet<String>()
                if (filteredProductList != null) {

                    filteredProductList.indices
                        .filter { hashSet.add(filteredProductList[it].id.toString()) }
                        .forEach { productList.add(filteredProductList[it]) }

                    notifyDataSetChanged()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}