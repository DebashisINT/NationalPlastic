package com.breezenationalplasticfsm.features.lead.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.breezenationalplasticfsm.app.AppConstant
import com.breezenationalplasticfsm.app.domain.ProductRateEntity
import com.breezenationalplasticfsm.app.domain.ShopActivityEntity

@Dao
interface LeadActivityDao {

    @Insert
    fun insertAll(vararg obj: LeadActivityEntity)


    @Query("SELECT * FROM " + AppConstant.TBL_LEAD_ACTIVITY +" where activity_next_date=:activity_next_date")
    fun getAll(activity_next_date:String): List<LeadActivityEntity>

    @Query("update "+AppConstant.TBL_LEAD_ACTIVITY+" set activity_next_date=:activity_next_date where crm_id=:crm_id ")
    fun trash2(crm_id: String, activity_next_date: String)
}