package com.breezenationalplasticfsm.app.domain

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.breezenationalplasticfsm.app.AppConstant

/**
 * Created by Saikat on 01-Jun-20.
 */
@Dao
interface ShopTypeDao {

    @Query("SELECT * FROM " + AppConstant.SHOP_TYPE)
    fun getAll(): List<ShopTypeEntity>

    // 4.0 OwnPerformanceFragment AppV 4.1.3 Saheli   24/05/2023 0026221
    @Query("SELECT * FROM " + AppConstant.SHOP_TYPE + " order by upper(shoptype_name)")
    fun getOrderByalphabeticallyAll(): List<ShopTypeEntity?>?
    // 4.0 OwnPerformanceFragment AppV 4.1.3 Saheli   24/05/2023 0026221

    @Query("SELECT * FROM " + AppConstant.SHOP_TYPE + " where shoptype_id=:shoptype_id")
    fun getSingleType(shoptype_id: String): ShopTypeEntity


    @Insert
    fun insertAll(vararg shopType: ShopTypeEntity)

    @Query("DELETE FROM " + AppConstant.SHOP_TYPE)
    fun deleteAll()

    @Query("SELECT shoptype_name FROM " + AppConstant.SHOP_TYPE + " where shoptype_id=:shoptype_id")
    fun getShopNameById(shoptype_id: String): String

    @Query("select shoptype_name from shop_type_list where shoptype_id = (\n" +
            "select type from shop_detail where shop_id =:shop_id) ")
    fun getShopTypeNameById(shop_id: String): String

}