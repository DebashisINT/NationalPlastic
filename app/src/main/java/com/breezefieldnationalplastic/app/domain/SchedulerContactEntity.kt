package com.breezefieldnationalplastic.app.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.breezefieldnationalplastic.app.AppConstant

@Entity(tableName = AppConstant.CRM_SCHEDULER_MASTER_CONTACTS)
data class SchedulerContactEntity (
    @PrimaryKey(autoGenerate = true) var sl_no: Int = 0,
    @ColumnInfo var scheduler_id:String = "",
    @ColumnInfo var select_contact_id:String = "",
    @ColumnInfo var select_contact:String = "",
    @ColumnInfo var select_contact_number:String = "",
)