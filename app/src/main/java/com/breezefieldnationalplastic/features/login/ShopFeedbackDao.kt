package com.breezefieldnationalplastic.features.login

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.breezefieldnationalplastic.app.AppConstant
import com.breezefieldnationalplastic.app.domain.ProductListEntity

@Dao
interface ShopFeedbackDao {

    @Insert
    fun insert(vararg obj: ShopFeedbackEntity)

    @Query("DELETE FROM " + AppConstant.TBL_SHOP_FEEDBACK)
    fun deleteAll()

    @Query("select * FROM " + AppConstant.TBL_SHOP_FEEDBACK +" where shop_id=:shop_id order by date_time desc")
    fun getAllByShopID(shop_id:String):List<ShopFeedbackEntity>


    @Query("select * FROM " + AppConstant.TBL_SHOP_FEEDBACK )
    fun getAll():List<ShopFeedbackEntity>
}