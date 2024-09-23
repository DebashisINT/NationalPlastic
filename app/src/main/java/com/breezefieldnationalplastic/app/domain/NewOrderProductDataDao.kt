package com.breezefieldnationalplastic.app.domain

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NewOrderProductDataDao {
    @Insert
    fun insert(vararg model: NewOrderProductDataEntity)

    @Query("Select * from new_order_product_data where order_id=:order_id ")
    fun getProductsOrder(order_id:String): List<NewOrderProductDataEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    abstract fun insertAll(kist: List<NewOrderProductDataEntity>)


}