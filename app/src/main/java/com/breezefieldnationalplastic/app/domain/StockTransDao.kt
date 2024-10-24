package com.breezefieldnationalplastic.app.domain

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface StockTransDao {
    @Insert
    fun insert(vararg model: StockTransEntity)

    @Query("Select * from stock_trans where isUploaded=:isUploaded ")
    fun getUnsyncData(isUploaded:Boolean): List<StockTransEntity>

    @Query("update stock_trans set isUploaded=:isUploaded where stock_shopcode=:stock_shopcode and stock_productid=:stock_productid and order_id=:order_id")
    fun updateSync(isUploaded:Boolean,stock_shopcode:String,stock_productid:String,order_id:String)
}