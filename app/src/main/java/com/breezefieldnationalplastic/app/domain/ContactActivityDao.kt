package com.breezefieldnationalplastic.app.domain

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.breezefieldnationalplastic.app.AppConstant

@Dao
interface ContactActivityDao {
    @Insert
    fun insert(vararg obj: ContactActivityEntity)

    @Query("Select * from "+ AppConstant.CONTACT_ACTIVITY)
    fun getAll(): List<ContactActivityEntity>

    @Query("Select * from "+ AppConstant.CONTACT_ACTIVITY+" where isActivityDone = 0")
    fun getAllUnDone(): List<ContactActivityEntity>

    @Query("update "+ AppConstant.CONTACT_ACTIVITY+" set isActivityDone=:isActivityDone where shop_id=:shop_id and activity_date =:activity_date")
    fun updateIsActivityDone(isActivityDone:Boolean,shop_id:String,activity_date:String)

    @Query("Select * from "+ AppConstant.CONTACT_ACTIVITY+" where isActivityDone = 0 and activity_date =:today ")
    fun getAllUnDoneToday(today:String): List<ContactActivityEntity>
}