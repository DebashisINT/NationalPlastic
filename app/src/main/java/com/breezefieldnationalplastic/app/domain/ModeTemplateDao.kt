package com.breezefieldnationalplastic.app.domain

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.breezefieldnationalplastic.app.AppConstant

@Dao
interface ModeTemplateDao {


    @Insert
    fun insert(vararg obj: ModeTemplateEntity)

    @Query("select * from " + AppConstant.MODE_TEMPLATE)
    fun getAll(): List<ModeTemplateEntity>
    @Query("DELETE FROM " + AppConstant.MODE_TEMPLATE)
    fun deleteAll()
}