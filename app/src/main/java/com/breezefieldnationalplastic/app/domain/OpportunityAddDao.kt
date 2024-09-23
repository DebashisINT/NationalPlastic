package com.breezefieldnationalplastic.app.domain

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.breezedsm.app.domain.NewOrderDataEntity
import com.breezefieldnationalplastic.app.AppConstant

@Dao
interface OpportunityAddDao {

    @Insert
    fun insert(vararg model: OpportunityAddEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    abstract fun insertAll(kist: List<OpportunityAddEntity>)

    @Query("SELECT * FROM CRM_Opportunity_Add where isDeleted=:isDeleted and shop_id=:shop_id")
    fun getAll(isDeleted:Boolean,shop_id:String): List<OpportunityAddEntity>

    @Query("SELECT * FROM CRM_Opportunity_Add ")
    fun getAllL(): List<OpportunityAddEntity>

    @Insert
    fun insertAll(vararg activity: OpportunityAddEntity)

    @Query("DELETE FROM CRM_Opportunity_Add where opportunity_id=:opportunity_id")
    fun deleteOpportunityById(opportunity_id: String?): Int

    @Query("update "+ AppConstant.CRM_Opportunity_Add+" set isDeleted=:isDeleted where opportunity_id=:opportunity_id")
    fun updateIsDeleted(isDeleted:Boolean,opportunity_id:String)


    @Query("SELECT * FROM " + AppConstant.CRM_Opportunity_Add +" where opportunity_id=:opportunity_id")
    fun getSingleOpportunityL(opportunity_id: String): OpportunityAddEntity

    @Query("update " + AppConstant.CRM_Opportunity_Add + " set opportunity_description=:opportunity_description,opportunity_amount=:opportunity_amount,opportunity_status_id=:opportunity_status_id,opportunity_status_name=:opportunity_status_name,opportunity_created_date=:opportunity_created_date,opportunity_created_time=:opportunity_created_time,opportunity_created_date_time=:opportunity_created_date_time,opportunity_edited_date_time=:opportunity_edited_date_time,isUpload=:isUploaded,isDeleted=:isDeleted,isEdited=:isEdited where opportunity_id=:opportunity_id")
    fun updateIsEdited(opportunity_description:String,opportunity_amount:String,opportunity_status_id:String,opportunity_status_name:String,opportunity_created_date:String,opportunity_created_time:String,opportunity_created_date_time:String,opportunity_edited_date_time:String, isUploaded: Boolean, isDeleted: Boolean,isEdited: Boolean, opportunity_id: String)


    @Query("Select * from CRM_Opportunity_Add where opportunity_id=:opportunity_id")
    fun getOpportunityByIdN(opportunity_id: String): OpportunityAddEntity

    @Query("update CRM_Opportunity_Add set isUpload=:isUpload where opportunity_id=:opportunity_id ")
    fun updateIsUploaded(opportunity_id:String,isUpload:Boolean)

    @Query("update CRM_Opportunity_Add set isEdited=:isEdited where opportunity_id=:opportunity_id")
    fun updateIsEditUploaded(isEdited: Boolean, opportunity_id: String?)

    @Query("Select * from CRM_Opportunity_Add where isDeleted=:isDeleted ")
    fun getUnsyncDeleteL(isDeleted:Boolean): List<OpportunityAddEntity>

    @Query("Select * from CRM_Opportunity_Add where isUpload=:isUpload ")
    fun getUnsyncAddOpptL(isUpload:Boolean): List<OpportunityAddEntity>

    @Query("Select * from CRM_Opportunity_Add where isEdited=:isEdited ")
    fun getUnsyncEditedL(isEdited:Boolean): List<OpportunityAddEntity>
}