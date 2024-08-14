package com.breezenationalplasticfsm.app.domain

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.breezenationalplasticfsm.app.AppConstant

@Dao
interface ShopAudioDao {
    @Insert
    fun insert(vararg obj: ShopAudioEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    abstract fun insertAll(kist: List<ShopAudioEntity>)

    @Query("select * from shop_audio where isUploaded=:isUploaded")
    fun getUnsyncL(isUploaded: Boolean):List<ShopAudioEntity>

    @Query("select * from shop_audio")
    fun getAll():List<ShopAudioEntity>

    @Query("select a.sl_no,a.shop_id,a.audio_path,a.isUploaded,a.datetime,a.revisitYN from shop_audio as a INNER join  \n" +
            "shop_activity as b on a.shop_id = b.shopid\n" +
            "where strftime('%Y-%m-%d', b.visited_date) = :dt and b.isUploaded = 1 and a.isUploaded = 0")
    fun getUnsyncLForSyncedVisitRevisit(dt: String):List<ShopAudioEntity>

    @Query("update shop_audio set isUploaded=:isUploaded where shop_id=:shop_id and datetime=:datetime")
    fun updateIsUploaded(isUploaded: Boolean,shop_id: String,datetime:String)

    @Query("select * from shop_audio where strftime('%Y-%m-%d', datetime) =:date and shop_id=:shop_id")
    fun getShopExistanceWIthDate(shop_id: String,date:String) :List<ShopAudioEntity>
}