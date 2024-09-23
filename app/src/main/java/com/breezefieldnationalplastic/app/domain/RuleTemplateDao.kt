package com.breezefieldnationalplastic.app.domain

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.breezefieldnationalplastic.app.AppConstant

@Dao
interface RuleTemplateDao {


    @Insert
    fun insert(vararg obj: RuleTemplateEntity)

    @Query("select * from " + AppConstant.RULE_TEMPLATE)
    fun getAll(): List<RuleTemplateEntity>
    @Query("DELETE FROM " + AppConstant.RULE_TEMPLATE)
    fun deleteAll()

}