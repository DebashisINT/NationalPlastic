package com.nationalplasticfsm.app.domain

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.nationalplasticfsm.app.AppConstant
// 1.0 ShopExtraContactDao AppV 4.0.6 suman 12-01-2023 multiple contact updation
@Dao
interface ShopExtraContactDao {

    @Insert
    fun insert(vararg obj: ShopExtraContactEntity)

    // 1.0 ShopExtraContactDao AppV 4.0.6 suman 12-01-2023 multiple contact updation
    @Query("select * from " + AppConstant.SHOP_EXTRA_CONTACT)
    fun getExtraContList() : List<ShopExtraContactEntity>

    @Query("select * from " + AppConstant.SHOP_EXTRA_CONTACT + " where shop_id=:shop_id")
    fun getExtraContListByShopID( shop_id: String) : List<ShopExtraContactEntity>

    @Query("select * from " + AppConstant.SHOP_EXTRA_CONTACT + " where shop_id=:shop_id and contact_serial=:contact_serial")
    fun getExtraContListByShopIDContactSl(shop_id: String,contact_serial: String) : List<ShopExtraContactEntity>

    // 1.0 ShopExtraContactDao AppV 4.0.6 suman 12-01-2023 multiple contact updation
    @Query("update " + AppConstant.SHOP_EXTRA_CONTACT + " set isUploaded=:isUploaded where shop_id=:shop_id and contact_serial=:contact_serial")
    fun updateIsUploaded(isUploaded: Boolean, shop_id: String,contact_serial: String)

    // 1.0 ShopExtraContactDao AppV 4.0.6 suman 12-01-2023 multiple contact updation
    @Query("select DISTINCT shop_id from " + AppConstant.SHOP_EXTRA_CONTACT + " where isUploaded=:isUploaded")
    fun getExtraContListShopIDUnsync( isUploaded: Boolean) : List<String>

}