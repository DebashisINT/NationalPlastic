package com.breezenationalplasticfsm.app.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.breezenationalplasticfsm.app.AppConstant

@Entity(tableName = AppConstant.CRM_STATUS_MASTER)
data class StatusMasterEntity (
    @PrimaryKey(autoGenerate = true) var sl_no: Int = 0,
    @ColumnInfo var status_id:Int = 0,
    @ColumnInfo var status_name:String = ""
)