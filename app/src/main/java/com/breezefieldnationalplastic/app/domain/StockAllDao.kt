package com.breezefieldnationalplastic.app.domain

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StockAllDao {
    @Insert
    fun insert(vararg model: StockAllEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    abstract fun insertAll(kist: List<StockAllEntity>)

    @Query("delete from stock_all")
    fun deleteAll()

    @Query("select * from stock_all where stock_productid = :stock_productid and stock_shopcode = \n" +
            "(select assigned_to_dd_id from shop_detail where shop_id = :stock_shopcode)")
    fun getStockDtlsForDD(stock_productid:String,stock_shopcode:String): List<StockAllEntity>

    @Query("select * from stock_all where stock_productid = :stock_productid and stock_shopcode = :stock_shopcode ")
    fun getStockDtls(stock_productid:String,stock_shopcode:String): List<StockAllEntity>

    @Query("select * from stock_all where stock_shopcode = :stock_shopcode and stock_productid = :stock_productid")
    fun getParticularStock(stock_productid:String,stock_shopcode:String):StockAllEntity

    @Query("update stock_all set stock_productbalqty = :balStock where stock_shopcode = :stock_shopcode and stock_productid = :stock_productid")
    fun updateStock(balStock:String,stock_productid:String,stock_shopcode:String)
}