package com.breezenationalplasticfsm.app.domain

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.breezenationalplasticfsm.app.AppConstant
import com.breezenationalplasticfsm.features.contacts.ProductDtls

@Dao
interface OpportunityProductDao {

    @Insert
    fun insertAll(vararg activity: OpportunityProductEntity)

    @Insert
    fun insert(vararg model: OpportunityProductEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    abstract fun insertAll(kist: List<OpportunityProductEntity>)

    @Query("DELETE FROM CRM_Opportunity_Product where opportunity_id=:opportunity_id")
    fun deleteOprtntyById(opportunity_id: String?): Int

    @Query("SELECT * FROM " + AppConstant.CRM_Opportunity_Product +" where opportunity_id=:oppotunity_id")
    fun getOpportunityPrdctL(oppotunity_id:String): List<OpportunityProductEntity>

    @Query("SELECT * FROM " + AppConstant.CRM_Opportunity_Product +" where opportunity_id=:oppotunity_id and product_id!='0'")
    fun getOpportunityRealPrdctL(oppotunity_id:String): List<OpportunityProductEntity>

    @Query("select product_id,product_name,1 as isTick from CRM_Opportunity_Product where shop_id=:shop_id and opportunity_id=:oppotunity_id and product_id!='0' ")
    fun getOpportunitySelectedPrdctL(shop_id: String,oppotunity_id:String): List<ProductDtls>


}