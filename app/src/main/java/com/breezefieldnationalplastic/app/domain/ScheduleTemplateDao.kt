package com.breezefieldnationalplastic.app.domain

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.breezefieldnationalplastic.app.AppConstant

@Dao
interface ScheduleTemplateDao {


    @Insert
    fun insert(vararg obj: ScheduleTemplateEntity)

    @Query("select * from " + AppConstant.SCHEDULE_TEMPLATE)
    fun getAll(): List<ScheduleTemplateEntity>

    @Query("select * from " + AppConstant.SCHEDULE_TEMPLATE+" where template_name !='Send Manually'")
    fun getAllExceptManually(): List<ScheduleTemplateEntity>

    @Query("Select * from schedule_template where template_name=:template_name COLLATE NOCASE ")
    fun getDuplicateTemplateData(template_name: String): List<ScheduleTemplateEntity>


    @Query("select * from " + AppConstant.SCHEDULE_TEMPLATE+" where template_id=:template_id")
    fun getByTemplate(template_id:String): ScheduleTemplateEntity

    @Query("DELETE FROM " + AppConstant.SCHEDULE_TEMPLATE)
    fun deleteAll()

    @Query("DELETE FROM " + AppConstant.SCHEDULE_TEMPLATE+" where template_id=:template_id")
    fun delete(template_id:String)
}