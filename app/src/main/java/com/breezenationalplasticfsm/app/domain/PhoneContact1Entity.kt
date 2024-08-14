package com.breezenationalplasticfsm.app.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.breezenationalplasticfsm.app.AppConstant

@Entity(tableName = AppConstant.PHONE_CONTACT1)
data class PhoneContact1Entity (
    @PrimaryKey(autoGenerate = true) var sl_no: Int = 0,
    @ColumnInfo var contact_id:String = "",
    @ColumnInfo var contact_name:String = "",
    @ColumnInfo var contact_phone:String = "",
)