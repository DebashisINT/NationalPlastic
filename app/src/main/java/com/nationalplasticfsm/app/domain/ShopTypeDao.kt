package com.nationalplasticfsm.app.domain

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.nationalplasticfsm.app.AppConstant

/**
 * Created by Saikat on 01-Jun-20.
 */
@Dao
interface ShopTypeDao {

    @Query("SELECT * FROM " + AppConstant.SHOP_TYPE)
    fun getAll(): List<ShopTypeEntity>

    @Query("SELECT * FROM " + AppConstant.SHOP_TYPE + " where shoptype_id=:shoptype_id")
    fun getSingleType(shoptype_id: String): ShopTypeEntity


    @Insert
    fun insertAll(vararg shopType: ShopTypeEntity)

    @Query("DELETE FROM " + AppConstant.SHOP_TYPE)
    fun deleteAll()

    @Query("SELECT shoptype_name FROM " + AppConstant.SHOP_TYPE + " where shoptype_id=:shoptype_id")
    fun getShopNameById(shoptype_id: String): String
}