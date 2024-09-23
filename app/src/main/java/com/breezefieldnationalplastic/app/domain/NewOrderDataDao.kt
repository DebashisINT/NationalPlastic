package com.breezedsm.app.domain

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.breezefieldnationalplastic.app.AppConstant
import com.breezefieldnationalplastic.app.domain.NewOrderScrOrderEntity
import java.math.BigDecimal

@Dao
interface NewOrderDataDao {
    @Insert
    fun insert(vararg model: NewOrderDataEntity)

    @Query("Select * from new_order_data where order_date=:order_date ")
    fun getTodayOrder(order_date:String): List<NewOrderDataEntity>

    @Query("Select * from new_order_data where order_date=:order_date order by order_id desc")
    fun getTodayOrderOrderBy(order_date:String): List<NewOrderDataEntity>

    @Query("Select * from new_order_data order by order_date desc ,order_id desc")
    fun getAllOrderOrderBy(): List<NewOrderDataEntity>

    @Query("Select * from new_order_data")
    fun getAllOrder(): List<NewOrderDataEntity>

    @Query("Select * from new_order_data where isUploaded=:isUploaded ")
    fun getUnsyncList(isUploaded:Boolean): List<NewOrderDataEntity>

    @Query("Select * from new_order_data where shop_id=:shop_id order by order_date desc,order_id desc")
    fun getOrderByShop(shop_id:String): List<NewOrderDataEntity>

    @Query("Select * from new_order_data where order_id=:order_id")
    fun getOrderByID(order_id:String): NewOrderDataEntity

    @Query("select \n" +
            "case when sum(order_total_amt) IS NULL then '0.00' else sum(order_total_amt) END as total\n" +
            " from new_order_data where shop_id = :shop_id")
    fun getOrderAmtByShop(shop_id:String): Double

    @Query("select \n" +
            "case when sum(order_total_amt) IS NULL then '0.00' else sum(order_total_amt) END as total\n" +
            " from new_order_data where order_date = :order_date")
    fun getOrderSumByDate(order_date:String): Double

    @Query("update new_order_data set isUploaded=:isUploaded where order_id=:order_id ")
    fun updateIsUploaded(order_id:String,isUploaded:Boolean)

    @Query("Select *  from "+ AppConstant.NEW_ORDER_DATA+ " where order_date=:order_date " )
    fun getRateListByDate(order_date:String):List<NewOrderDataEntity>
}