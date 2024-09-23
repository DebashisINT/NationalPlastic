package com.breezefieldnationalplastic.app.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.breezefieldnationalplastic.app.AppConstant

@Entity(tableName = AppConstant.PHONE_CONTACT)
data class PhoneContactEntity (
    @PrimaryKey(autoGenerate = true) var sl_no: Int = 0,
    @ColumnInfo var contact_id:String = "",
    @ColumnInfo var contact_name:String = "",
    @ColumnInfo var contact_phone:String = "",
)