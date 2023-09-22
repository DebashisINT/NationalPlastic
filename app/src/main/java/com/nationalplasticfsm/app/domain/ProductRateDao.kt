package com.nationalplasticfsm.app.domain

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nationalplasticfsm.app.AppConstant

/**
 * Created by Saikat on 11-May-20.
 */
@Dao
interface ProductRateDao {

    @Insert
    fun insert(vararg productRate: ProductRateEntity)

    @Query("DELETE FROM " + AppConstant.PRODUCT_RATE_TABLE)
    fun deleteAll()

    @Query("SELECT * FROM " + AppConstant.PRODUCT_RATE_TABLE)
    fun getAll(): List<ProductRateEntity>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    abstract fun insertAll(kist: List<ProductRateEntity>)


    @Query("SELECT * FROM " + AppConstant.PRODUCT_RATE_TABLE + " where product_id=:product_id")
    fun getProductRateByProductID(product_id:String): ProductRateEntity

    @Query("select id as id,id as product_id,'0' as rate1,'0' as rate2,'0' as rate3,'0' as rate4,'0' as rate5,\n" +
            "'0' as stock_amount,'Units' as stock_unit,'' as ideal_duration,0 as isStockShow,0 as isRateShow\n" +
            "from product_list")
    fun getAllBlank(): List<ProductRateEntity>

}