package com.breezefieldnationalplastic.app.domain

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.breezefieldnationalplastic.app.AppConstant

@Dao
interface VisitRevisitWhatsappStatusDao {

    @Insert
    fun insert(vararg obj: VisitRevisitWhatsappStatus)

    @Query("SELECT * FROM " + AppConstant.SHOP_VISIT_REVISIT_WHATSAPP_STATUS)
    fun getAll(): List<VisitRevisitWhatsappStatus?>?

    @Query("SELECT * FROM " + AppConstant.SHOP_VISIT_REVISIT_WHATSAPP_STATUS +" where shop_id=:shop_id and date=:date")
    fun getByShopIDDate(shop_id:String,date:String): VisitRevisitWhatsappStatus?

    @Query("update " + AppConstant.SHOP_VISIT_REVISIT_WHATSAPP_STATUS+" set isWhatsappSent=:isWhatsappSent,whatsappSentMsg=:whatsappSentMsg,transactionId=:transactionId " +
            "where sl_no=:sl_no")
    fun updateWhatsStatus(isWhatsappSent:Boolean,whatsappSentMsg:String,sl_no:Int,transactionId:String)

    @Query("SELECT * FROM " + AppConstant.SHOP_VISIT_REVISIT_WHATSAPP_STATUS+" where isUploaded = 0")
    fun getUnsyncList(): List<VisitRevisitWhatsappStatus?>?

    @Query("update " + AppConstant.SHOP_VISIT_REVISIT_WHATSAPP_STATUS+" set isUploaded=1")
    fun updateWhatsStatusUpload()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    abstract fun insertAll(kist: List<VisitRevisitWhatsappStatus>)
}