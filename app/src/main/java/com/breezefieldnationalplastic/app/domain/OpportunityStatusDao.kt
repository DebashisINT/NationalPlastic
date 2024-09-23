package com.breezefieldnationalplastic.app.domain

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.breezedsm.app.domain.NewProductListEntity
import com.breezefieldnationalplastic.app.AppConstant

@Dao
interface OpportunityStatusDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    abstract fun insertAll(kist: List<OpportunityStatusEntity>)

    @Insert
    fun insert(vararg model: OpportunityStatusEntity)

    @Query("SELECT * FROM " + AppConstant.CRM_Opportunity_Status)
    fun getAll(): List<OpportunityStatusEntity>

    @Query("delete FROM " + AppConstant.CRM_Opportunity_Status)
    fun deleteAll()
}