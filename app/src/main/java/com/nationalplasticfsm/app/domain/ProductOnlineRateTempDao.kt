package com.nationalplasticfsm.app.domain

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nationalplasticfsm.app.AppConstant

@Dao
interface ProductOnlineRateTempDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    abstract fun insertAll(kist: List<ProductOnlineRateTempEntity>)

    @Query("DELETE FROM " + AppConstant.PRODUCT_ONLINE_RATE_TEMP_TABLE)
    fun deleteAllProduct()

    @Query("SELECT * FROM " + AppConstant.PRODUCT_ONLINE_RATE_TEMP_TABLE + " where product_id=:product_id")
    fun getObjByProductID(product_id: String): List<ProductOnlineRateTempEntity>

    @Query("SELECT * FROM " + AppConstant.PRODUCT_ONLINE_RATE_TEMP_TABLE)
    fun getAll(): List<ProductOnlineRateTempEntity>

}