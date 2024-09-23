package com.breezefieldnationalplastic.features.stock

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.breezefieldnationalplastic.R
import com.breezefieldnationalplastic.app.domain.StockProductListEntity
import com.breezefieldnationalplastic.features.dashboard.presentation.DashboardActivity
import kotlinx.android.synthetic.main.inflate_cart.view.*

/**
 * Created by Saikat on 11-09-2019.
 */

class StockDetailsAdapter(private val context: Context, private val selectedProductList: List<StockProductListEntity>) :
        RecyclerView.Adapter<StockDetailsAdapter.MyViewHolder>() {

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    init {
        (context as DashboardActivity).totalPrice.clear()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = layoutInflater.inflate(R.layout.inflate_cart, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindItems(context, selectedProductList)
    }

    override fun getItemCount(): Int {
        return selectedProductList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(context: Context, categoryList: List<StockProductListEntity>) {

            /*if (adapterPosition % 2 == 0)
                itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.report_screen_bg))
            else
                itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.white))*/

            itemView.tv_particular_item.text = categoryList[adapterPosition].product_name
            /*itemView.tv_brand_item.text = "Rate : ₹ " + categoryList.get(adapterPosition).rate*/ //categoryList?.get(adapterPosition)?.brand

            itemView.tv_brand_item.text = "Rate: ₹ " + String.format("%.2f", categoryList[adapterPosition].rate?.toFloat())

            if (categoryList[adapterPosition].qty?.contains(".")!!)
                itemView.tv_category_item.text = "Quantity: " + categoryList[adapterPosition].qty?.toFloat()?.toInt() //categoryList?.get(adapterPosition)?.category
            else
                itemView.tv_category_item.text = "Quantity: " + categoryList[adapterPosition].qty

            try{
                val qtyRectify = String.format("%.3f", categoryList[adapterPosition].qty!!.toDouble()).toDouble()
                if (qtyRectify - qtyRectify.toInt() == 0.0) {
                    itemView.tv_category_item.text = "Quantity: " + qtyRectify.toInt().toString()
                } else {
                    itemView.tv_category_item.text = "Quantity: " + qtyRectify.toString()
                }
            }catch (ex:Exception){
                ex.printStackTrace()
            }



            try {
                //val totalPrice = DecimalFormat("##.##").format(categoryList[adapterPosition].total_price?.toDouble())
                val totalPrice = String.format("%.2f", categoryList[adapterPosition].total_price?.toFloat())
                itemView.tv_watt_item.text = "Total Price: ₹ $totalPrice"  //categoryList?.get(adapterPosition)?.watt
            } catch (e: Exception) {
                e.printStackTrace()
            }
            //(context as DashboardActivity).totalPrice.add((context as DashboardActivity).rateList[adapterPosition].toInt() * (context as DashboardActivity).qtyList[adapterPosition].toInt())
            //itemView.tv_edit_product.visibility = View.GONE
            itemView.ll_edit_delete.visibility = View.GONE
        }
    }
}
