package com.breezefieldnationalplastic.app.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.breezefieldnationalplastic.app.AppConstant

@Entity(tableName = AppConstant.SCHEDULE_TEMPLATE)
data class ScheduleTemplateEntity (
    @PrimaryKey(autoGenerate = true) var sl_no: Int = 0,
    @ColumnInfo var template_id:String = "",
    @ColumnInfo var template_name:String = "",
    @ColumnInfo var template_desc:String = ""
)