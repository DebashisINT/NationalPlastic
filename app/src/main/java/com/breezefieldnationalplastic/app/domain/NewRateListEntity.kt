package com.breezedsm.app.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.breezefieldnationalplastic.app.AppConstant

@Entity(tableName = AppConstant.NEW_RATE_LIST)
data class NewRateListEntity (
    @PrimaryKey(autoGenerate = false) var product_id:String = "",
    @ColumnInfo var mrp:String = "",
    @ColumnInfo var item_price:String = "",
    @ColumnInfo var specialRate:String = ""
)